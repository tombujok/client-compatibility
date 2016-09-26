package com.hazelcast;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class ClassFinder {

    public static List<JavaClass> findFiles(String pathString, String extension) {
        Path path = Paths.get(pathString);

        List<JavaClass> files = new ArrayList<>();
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path currentPath, BasicFileAttributes attrs) throws IOException {
                    File file = currentPath.toFile();
                    if (!attrs.isDirectory() && file.getName().endsWith(extension)) {
                        if (file.getAbsolutePath().contains("src/main/java")
                                && !file.getName().contains("package-info")
                                && !file.getName().contains("DependencyFinder")) {
                            files.add(new JavaClass(file));
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return files;

    }


}
