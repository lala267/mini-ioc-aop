package com.juanjuan.miniioc.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class ClassScanner {

    public static Set<Class<?>> scanPackage(String basePackage) {
        Set<Class<?>> classes = new HashSet<>();
        String path = basePackage.replace(".", "/");

        try {
            Enumeration<URL> resources =
                    Thread.currentThread().getContextClassLoader().getResources(path);

            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                File dir = new File(filePath);
                findClasses(dir, basePackage, classes);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to scan package: " + basePackage, e);
        }

        return classes;
    }

    private static void findClasses(File directory, String packageName, Set<Class<?>> classes) {
        if (!directory.exists() || !directory.isDirectory()) {
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                findClasses(file, packageName + "." + file.getName(), classes);
            } else if (file.getName().endsWith(".class")) {
                String className =
                        packageName + "." + file.getName().replace(".class", "");
                try {
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException ignored) {
                }
            }
        }
    }
}
