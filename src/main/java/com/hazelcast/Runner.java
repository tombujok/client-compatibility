package com.hazelcast;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Runner {


    public static void main(String[] args) {

        List<JavaClass> files = ClassFinder.findFiles("/repos/hz-clean/", "java");
        ClassIndex index = new ClassIndex(files);

        List<JavaClass> dsNotIds = new ArrayList<>();
        for (JavaClass file : files) {
            if (file.ds && !file.ids) {
                dsNotIds.add(file);
            }
        }

        List<JavaClass> client = ClassFinder.findFiles("/repos/hz-clean/hazelcast-client/", "java");
        // System.err.println(client.size());

        List<JavaClass> result = new ArrayList<>();
        Set<JavaClass> processed = new HashSet<>();
        for (JavaClass clientClz : client) {
            findAllImportsTree(index, clientClz, result, processed, 0);
        }

//        System.err.println(result.size());


        Set<String> all = new TreeSet<>();
        for (JavaClass clazz : result) {
            if (clazz.ds) {
                all.add(clazz.name);
            }
        }

        for (String s : all) {
            System.out.println(s);
        }
    }


    public static void findAllImportsTree(ClassIndex index, JavaClass clientClazz,
                                          List<JavaClass> result, Set<JavaClass> processed, int level) {
        String serPostfix = clientClazz.ids ? "IDS" : (clientClazz.ds ? "DS" : "");
        if (processed.contains(clientClazz)) {
            for (int i = 0; i < level; i++) {
                System.out.print("\t");
            }
            System.out.println(clientClazz.name + " " + serPostfix + " (...)");
            return;
        }
        processed.add(clientClazz);

        for (int i = 0; i < level; i++) {
            System.out.print("\t");
        }
        System.out.println(clientClazz.name + " " + serPostfix);

        for (String imp : clientClazz.imports) {
            if (imp.startsWith("com.hazelcast")) {
                JavaClass impClazz = index.classes.get(imp);
                if (impClazz == null) {
                    // System.err.println("Could not find in index " + imp);
                    continue;
                }
                result.add(impClazz);
                int myLevel = level + 1;
                findAllImportsTree(index, impClazz, result, processed, myLevel);
            }
        }

    }

}
