package dev.xdark.fabricclasscache;

import net.fabricmc.loader.api.Tweaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class ClassTweaker implements Tweaker {

  private static final Logger LOGGER = LogManager.getLogger("FabricCDS");
  private ClassStorage storage;
  private boolean readMode;

  @Override
  public void initialize() {
    var path = Paths.get("minecraft.cds");
    var readMode = this.readMode = Files.isRegularFile(path);
    var storage = this.storage = ClassStorage.open(path);
    if (readMode) {
      try {
        storage.readFromDisk();
      } catch (IOException ex) {
        LOGGER.error("Could not read class storage:", ex);
        storage.close();
      }
    }
    if (!storage.isClosed()) {
      Runtime.getRuntime()
          .addShutdownHook(
              new Thread(
                  () -> {
                    storage.close();
                    if (!readMode) {
                      try {
                        storage.writeToDisk();
                      } catch (IOException ex) {
                        LOGGER.error("Could not dump storage to disk:", ex);
                      }
                    }
                  }));
    }
  }

  @Override
  public byte[] getClassBytes(String s) {
    return readMode ? storage.readClassBytes(s) : null;
  }

  @Override
  public void postApply(String s, byte[] bytes) {
    if (!readMode) {
      storage.writeClassBytes(s, bytes);
    }
  }
}
