package dev.xdark.fabricclasscache;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

final class ClassStorage {

  private final Map<String, byte[]> contentMap = new HashMap<>();
  private final Path path;
  private boolean closed;

  private ClassStorage(Path path) {
    this.path = path;
  }

  void readFromDisk() throws IOException {
    try (var in = new ObjectInputStream(Files.newInputStream(path))) {
      contentMap.putAll((Map<String, byte[]>) in.readObject());
    } catch (ClassNotFoundException ex) {
      throw new RuntimeException(ex);
    }
  }

  void writeToDisk() throws IOException {
    try (var out =
        new ObjectOutputStream(
            new BufferedOutputStream(
                Files.newOutputStream(
                    path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)))) {
      out.writeObject(contentMap);
    }
  }

  void writeClassBytes(String className, byte[] classBytes) {
    if (closed) return;
    contentMap.put(className, classBytes);
  }

  byte[] readClassBytes(String className) {
    if (closed) return null;
    return contentMap.remove(className);
  }

  void close() {
    closed = true;
  }

  boolean isClosed() {
    return closed;
  }

  static ClassStorage open(Path path) {
    return new ClassStorage(path);
  }
}
