package ch.thomsch.versioncontrol;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class GitVcs implements VCS {

    private static final Logger logger = LoggerFactory.getLogger(GitVcs.class);

    private final Repository repository;
    private String saved;

    GitVcs(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void checkout(String revision) throws GitAPIException {
        final CheckoutCommand command = new Git(repository).checkout().setName(revision).setForce(true);
        command.call();
    }

    @Override
    public void clean() {
        try {
            final Status status = new Git(repository).status().call();

            if(status.getUntracked().size() > 0) {
                new Git(repository).clean().setCleanDirectories(true).call();
            }

            if(status.getUncommittedChanges().size() > 0 || status.getConflicting().size() > 0) {
                new Git(repository).reset().setMode(ResetCommand.ResetType.HARD).call();
            }

        } catch (NoWorkTreeException e) {
            logger.error("Cannot clean a bare working directory:", e);
        } catch (GitAPIException e) {
            unexpectedGitError(e);
        }
    }

    @Override
    public void saveVersion() {
        try {
            saved = repository.getBranch();

            if (saved == null) {
                logger.warn("No reference was saved!");
            }
        } catch (IOException e) {
            logger.error("An error occurred when trying to retrieve the current branch checked out", e);
        }
    }

    @Override
    public void restoreVersion() {
        if(saved == null) {
            logger.warn("No reference was saved. Ignoring.");
            return;
        }

        try {
            clean();
            checkout(saved);
        } catch (GitAPIException e) {
            unexpectedGitError(e);
        }
    }

    @Override
    public void getChangedFiles(String revision, Collection<File> beforeFiles, Collection<File> afterFiles)
            throws IOException {
        final Git git = new Git(repository);
        final ObjectReader reader = repository.newObjectReader();

        final ObjectId revisionId = repository.resolve(revision);

        try (RevWalk walk = new RevWalk(repository)) {
            final RevCommit commit = walk.parseCommit(revisionId);

            final CanonicalTreeParser oldTree = new CanonicalTreeParser();
            final CanonicalTreeParser newTree = new CanonicalTreeParser();
            newTree.reset(reader, commit.getTree());

            walk.markStart(commit.getParent(0));

            oldTree.reset(reader, commit.getParent(0).getTree());


            final List<DiffEntry> diffEntries = git.diff().setNewTree(newTree).setOldTree(oldTree).call();

            for (DiffEntry diffEntry : diffEntries) {
                if (diffEntry.getChangeType() != DiffEntry.ChangeType.ADD) {
                    beforeFiles.add(convertPathToFile(diffEntry.getOldPath()));
                }

                if (diffEntry.getChangeType() != DiffEntry.ChangeType.DELETE) {
                    afterFiles.add(convertPathToFile(diffEntry.getNewPath()));
                }
            }

        } catch (GitAPIException e) {
            logger.error("Unable to retrieve changed files for revision {}", revision, e);
        }
    }

    /**
     * Converts a {@link DiffEntry} path to the corresponding absolute {@link File}.
     *
     * @param path the path
     * @return a new instance of the file
     */
    private File convertPathToFile(String path) {
        final String concat = FilenameUtils.concat(getDirectory(), path);
        return new File(concat);
    }

    @Override
    public String getDirectory() {
        return FilenameUtils.normalize(repository.getDirectory().getParentFile().getAbsolutePath());
    }

    @Override
    public String getParent(String revision) throws IOException {
        final ObjectId revisionId = repository.resolve(revision);
        try(RevWalk walk = new RevWalk(repository)){
            final RevCommit commit = walk.parseCommit(revisionId);

            if(commit.getParentCount() == 0) {
                return null;
            }

            final RevCommit parentRevision = commit.getParent(0);
            return parentRevision.getName();
        }
    }

    @Override
    public void close() {
        repository.close();
    }

    private void unexpectedGitError(GitAPIException e) {
        logger.error("An unexpected error occurred in git:", e);
    }
}