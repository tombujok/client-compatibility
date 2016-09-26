package com.hazelcast;

import com.hazelcast.nio.serialization.DataSerializable;
import com.hazelcast.nio.serialization.IdentifiedDataSerializable;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class JavaClass {

    Class clazz;
    File file;
    boolean ds;
    boolean ids;
    List<String> imports;
    String name;

    public JavaClass(File file) {
        String path = file.getAbsolutePath();
        int index = path.indexOf("src/main/java/");
        if (index < 0) {
            throw new RuntimeException("Illegal index for " + file.getAbsolutePath());
        }
        path = path.substring(index + "src/main/java/".length());
        path = path.replace(".java", "");
        path = path.replace("/", ".");

        this.file = file;
        try {
            this.clazz = Class.forName(path);
            this.ds = DataSerializable.class.isAssignableFrom(this.clazz);
            this.ids = IdentifiedDataSerializable.class.isAssignableFrom(this.clazz);
            this.name = clazz.getName();
        } catch (Throwable e) {
            System.err.println("Could not instantiate: " + path);
            // throw new RuntimeException(e);
        }
        this.imports = ImportFinder.findImports(this);
    }

    public String getContent() {
        try {
            return IOUtils.toString(new FileReader(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "JavaClass{" +
                "clazz=" + clazz +
                ", file=" + file +
                ", ds=" + ds +
                ", ids=" + ids +
                ", imports=" + imports +
                '}';
    }
}
