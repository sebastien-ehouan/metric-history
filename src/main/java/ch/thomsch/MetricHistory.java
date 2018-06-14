package ch.thomsch;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.thomsch.converter.SourceMeterConverter;
import ch.thomsch.export.Reporter;
import ch.thomsch.filter.FileFilter;
import ch.thomsch.loader.CommitReader;
import ch.thomsch.loader.ZafeirisRefactoringMiner;
import ch.thomsch.metric.Collector;
import ch.thomsch.metric.MetricDump;
import ch.thomsch.metric.SourceMeter;
import ch.thomsch.model.Raw;
import ch.thomsch.versioncontrol.GitRepository;
import ch.thomsch.versioncontrol.Repository;

import static ch.thomsch.model.Raw.getFormat;

/**
 * @author Thomsch
 */
public class MetricHistory {
    private static final Logger logger = LoggerFactory.getLogger(MetricHistory.class);

    private final Collector collector;
    private final Reporter reporter;
    private final CommitReader commitReader;

    private final Map<String, MetricDump> cache;
    private final FileFilter filter;

    public MetricHistory(Collector collector, Reporter reporter, CommitReader reader) {
        this.collector = collector;
        this.reporter = reporter;
        this.commitReader = reader;

        cache = new HashMap<>();
        filter = FileFilter.production();
    }

    /**
     * Collects the metrics before and after for each of the revisions found in the file <code>revisionFile</code>.
     * @param revisionFile Path to the CSV file containing the revisions
     * @param repository The repository containing the revisions.
     * @param outputFile Path to the file where the results will be printed
     */
    public void collect(String revisionFile, Repository repository, String outputFile) {
        final long beginning = System.nanoTime();

        final List<String> revisions = commitReader.load(revisionFile);
        logger.info("Read {} distinct revisions", revisions.size());

        try {
            reporter.initialize(outputFile);
            reporter.printMetaInformation();
        } catch (IOException e) {
            logger.error("Cannot initialize element:", e);
            return;
        }

        int i = 0;
        for (String revision : revisions) {
            try {
                logger.info("Processing {} ({})", revision, ++i);

                final String parent = repository.getParent(revision);

                final MetricDump before = collectCachedMetrics(repository, parent);
                final MetricDump current = collectCachedMetrics(repository, revision);

                reporter.report(revision, parent, before, current);
            } catch (IOException e) {
                logger.error("Cannot write results for revision {}:", revision, e);
            } catch (GitAPIException e) {
                logger.error("Checkout failure: ", e);
            }
        }

        try {
            reporter.finish();
            repository.close();
        } catch (IOException e) {
            logger.error("Cannot close output file:", e);
        } catch (Exception e) {
            logger.error("Failed to properly close the repository", e);
        }

        cache.clear();
        final long elapsed = System.nanoTime() - beginning;
        logger.info("Task completed in {}", Duration.ofNanos(elapsed));
    }

    private MetricDump collectCachedMetrics(Repository repository, String revision) throws
            GitAPIException {
        final MetricDump cachedMetrics = cache.get(revision);
        if (cachedMetrics != null) {
            return cachedMetrics;
        }

        repository.checkout(revision);
        final MetricDump metrics = collector.collect(repository.getDirectory(), revision, filter);
        cache.put(revision, metrics);
        return metrics;
    }

    public static void main(String[] args) {
        if (args[0].equalsIgnoreCase("convert")) {
            processConvertCommand(args);
        } else if (args[0].equalsIgnoreCase("ancestry")) {
            processAncestryCommand(args);
        } else if (args[0].equalsIgnoreCase("diff")) {
            processDiffCommand(args);
        } else {
            processCollectCommand(args);
        }
    }

    /**
     * Verify the number of arguments.
     * @param args the container of arguments
     * @param expected the number of arguments expected
     * @throws IllegalArgumentException if the number of arguments doesn't match the actual number of arguments
     */
    private static void verifyArguments(String[] args, int expected) {
        if (args.length != expected) {
            throw new IllegalArgumentException("Incorrect number of arguments (" + args.length + ") expected " +
                    expected);
        }
    }

    private static void processDiffCommand(String[] args) {
        verifyArguments(args, 4);

        String ancestryFile = normalizePath(args[1]);
        String rawFile = normalizePath(args[2]);
        String outputFile = normalizePath(args[3]);

        Ancestry ancestry = new Ancestry(null, null);
        try {
            ancestry.loadFromDisk(ancestryFile);
        } catch (IOException e) {
            logger.error("I/O error while reading ancestry file");
            return;
        }

        CSVParser parser;
        try {
            parser = new CSVParser(new FileReader(rawFile), getFormat().withSkipHeaderRecord());
        } catch (IOException e) {
            logger.error("I/O error while reading raw file: {}" + e.getMessage());
            return;
        }
        Raw model = Raw.load(parser);

        Difference difference = new Difference();
        try (CSVPrinter writer = new CSVPrinter(new FileWriter(outputFile), getFormat())) {
            difference.export(ancestry, model, writer);
        } catch (IOException e) {
            logger.error("I/O error with file {}", outputFile, e);
        }
    }

    private static void processAncestryCommand(String[] args) {
        verifyArguments(args, 4);

        String revisionFile = normalizePath(args[1]);
        String repository = normalizePath(args[2]);
        String outputFile = normalizePath(args[3]);

        Ancestry ancestry = null;
        try {
            ancestry = new Ancestry(GitRepository.get(repository), new ZafeirisRefactoringMiner());
        } catch (IOException e) {
            throw new IllegalArgumentException("This repository doesn't have version control: " + repository);
        }
        ancestry.load(revisionFile);

        try (CSVPrinter writer = ancestry.getPrinter(outputFile)) {
            ancestry.export(writer);
        } catch (IOException e) {
            logger.error("I/O error with file {}", outputFile, e);
        }

    }

    private static void processCollectCommand(String[] args) {
        verifyArguments(args, 6);

        String revisionFile = normalizePath(args[0]);
        String executable = normalizePath(args[1]);
        String project = normalizePath(args[2]);

        String repository = args[3];
        if (repository.equalsIgnoreCase("same")) {
            repository = project;
        } else {
            repository = normalizePath(repository);
        }

        String executableOutput = normalizePath(args[4]);
        String projectName = args[5];

        try {
            Collector collector = new SourceMeter(executable, executableOutput, projectName, project);
            MetricHistory metricHistory = new MetricHistory(collector, new Reporter(), new ZafeirisRefactoringMiner());

            metricHistory.collect(revisionFile, GitRepository.get(repository), "./output.csv");
        } catch (IOException e) {
            logger.error("Resource access problem", e);
        } catch (Exception e) {
            logger.error("Something went wrong", e);
        }
    }

    static void processConvertCommand(String[] args) {
        verifyArguments(args,3);

        String inputFolder = normalizePath(args[1]);
        String outputFile = normalizePath(args[2]);

        SourceMeterConverter.convert(inputFolder, outputFile);
    }

    private static String normalizePath(String arg) {
        return FilenameUtils.normalize(new File(arg).getAbsolutePath());
    }
}
