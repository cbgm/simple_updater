package de.chris.usbupdater.util;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SingleInstanceApplicationUtil {
      private static Logger LOGGER = LoggerFactory.getLogger(SingleInstanceApplicationUtil.class);

      private SingleInstanceApplicationUtil() {
      }

      /**
      * Creates a temporary file and locks it until the application is shut down.
      * Returns true, if the file could be locked --> no other instance is running
      * Returns false, it the file could not be locked --> there is another instance running
      *
      * @param lockFile the temporary file to create and lock
      * @return true if no other instance is running, false otherwise
      */

      public static boolean lockInstance(final String lockFile) {
          try {
              final File file = new File(lockFile);
              final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
              final FileLock fileLock = randomAccessFile.getChannel().tryLock();
              if (fileLock != null) {
                  Runtime.getRuntime().addShutdownHook(new Thread() {
                      public void run() {
                          try {
                              fileLock.release();
                              randomAccessFile.close();
                              file.delete();
                          } catch (Exception e) {
                              LOGGER.error("Unable to remove lock file: " + lockFile, e);
                          }
                      }
                  });
                  return true;
              }
          } catch (Exception e) {
            LOGGER.error("Unable to create and/or lock file: " + lockFile, e);
          }
          return false;
      }
}