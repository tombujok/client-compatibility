package com.hazelcast;

import java.util.ArrayList;
import java.util.List;

public class ImportFinder {

    public static List<String> findImports(JavaClass clazz) {
        List<String> imports = new ArrayList<>();
        String content = clazz.getContent();
        for (String line : content.split("\\n")) {
            if (line.startsWith("import ")) {
                String imp = line.replace("import ", "");
                imp = imp.replace(";", "");
                imports.add(imp);
            }
        }
        return imports;
    }

}
