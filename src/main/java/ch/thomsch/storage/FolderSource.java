package ch.thomsch.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import ch.thomsch.model.ClassStore;

/**
 * Reads the contents of a directory. Each file in the folder represents a revision.
 */
public class FolderSource extends MeasureRepository {

    private static final Logger logger = LoggerFactory.getLogger(FolderSource.class);
    private final File directory;

    FolderSource(File directory) {
        this.directory = directory;
    }

    @Override
    public ClassStore get(String ... versions) {
        final ClassStore model = new ClassStore();

        for (String version : versions) {
            final File file = new File(directory, version + ".csv");

            try {
                Stores.loadClasses(file.getPath(), model);
            } catch (IOException e) {
                logger.error("Unable to read {}", file.getPath());
            }
        }
        return model;
    }
}
