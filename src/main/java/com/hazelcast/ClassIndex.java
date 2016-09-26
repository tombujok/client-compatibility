package com.hazelcast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassIndex {

    Map<String, JavaClass> classes = new HashMap<>();

    public ClassIndex(List<JavaClass> classes) {
        for (JavaClass clazz : classes) {
            this.classes.put(clazz.name, clazz);
        }
    }

}
