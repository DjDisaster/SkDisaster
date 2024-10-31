package me.djdisaster.testAddon.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ch.njol.skript.Skript;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class JavaCompilerManager {

    private static List<File> libsCache = getLibs();

    public static CompiledJavaClass compile(String fileName, String fileContent) throws Exception {
        return new CompiledJavaClass(compile(fileName, fileContent, null));
    }

    public static Class<?> compile(String fileName, String fileContent, Object importDir) throws Exception {
        List<File> libs = new ArrayList<>(libsCache);

        if (importDir != null && !importDir.equals("")) {
            File importDirFile;
            if (importDir instanceof File) {
                importDirFile = (File) importDir;
            } else {
                importDirFile = new File(Bukkit.getWorldContainer(), importDir.toString());
            }
            importDirFile.mkdirs();
            libs.addAll(listFiles(importDirFile));
        }

        File tmpJavaFile = new File(getTmpJava(), fileName + ".java");
        if (!tmpJavaFile.exists()) {
            tmpJavaFile.getParentFile().mkdirs();
            tmpJavaFile.createNewFile();
        }

        try (FileWriter writer = new FileWriter(tmpJavaFile)) {
            writer.write(fileContent);
        }

        List<String> options = new ArrayList<>();
        options.add("-classpath");

        List<String> classpathEntries = new ArrayList<>();
        for (File lib : libs) {
            classpathEntries.add(Paths.get(lib.getAbsolutePath()).toAbsolutePath().normalize().toString());
        }
        options.add(String.join(File.pathSeparator, classpathEntries));

        JavaCompiler jc = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fm = jc.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> compilationUnits = fm.getJavaFileObjectsFromFiles(Collections.singletonList(tmpJavaFile));

        Boolean result = jc.getTask(null, fm, null, options, null, compilationUnits).call();
        if (!result) {
            throw new Exception("Compilation failed.");
        }

        Method getClassLoaderMethod = JavaPlugin.class.getDeclaredMethod("getClassLoader");
        getClassLoaderMethod.setAccessible(true);
        ClassLoader parentClassLoader = (ClassLoader) getClassLoaderMethod.invoke(Skript.getInstance());

        URLClassLoader classLoader = new URLClassLoader(new URL[]{getTmpJava().toURI().toURL()}, parentClassLoader);
        return classLoader.loadClass(fileName);
    }

    private static File getTmpJava() {
        return new File(Bukkit.getWorldContainer(), "tmp/java/");
    }

    private static List<File> listFiles(File file) {
        List<File> files = new ArrayList<>();
        File[] entries = file.listFiles();
        if (entries != null) {
            for (File entry : entries) {
                if (entry.isDirectory()) {
                    files.addAll(listFiles(entry));
                } else {
                    files.add(entry);
                }
            }
        }
        return files;
    }

    private static List<File> getLibs() {
        List<File> files = new ArrayList<>();
        File[] candidates = Bukkit.getWorldContainer().listFiles((dir, name) -> name.endsWith(".jar"));
        String software = "";
        if (candidates != null) {
            try {
                for (File candidate : candidates) {
                    try (ZipFile zip = new ZipFile(candidate)) {
                        Enumeration<? extends ZipEntry> entries = zip.entries();
                        while (entries.hasMoreElements()) {
                            ZipEntry entry = entries.nextElement();
                            String entryName = entry.getName();
                            if (entryName.startsWith("org/bukkit")) {
                                files.add(candidate);
                                software = "spigot";
                                break;
                            } else if (entryName.startsWith("paperclip")) {
                                software = "paper";
                            }
                            if ("paper".equals(software) && "META-INF/patches.list".equals(entryName)) {
                                try (BufferedReader in = new BufferedReader(new InputStreamReader(zip.getInputStream(entry)))) {
                                    String[] versions = in.lines().findFirst().orElse("").split("\t");
                                    if (versions.length > 0) {
                                        File versionFile = new File(new File(Bukkit.getWorldContainer(), "versions"), versions[versions.length - 1]);
                                        files.add(versionFile);
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (software.isEmpty()) {
            Bukkit.getLogger().warning("Unknown server software probably gonna cause issues") ;
        } else {
            files.addAll(listFiles(new File(Bukkit.getWorldContainer(), "plugins")));
        }

        File libDir = new File(Bukkit.getWorldContainer(), "libraries");
        if (libDir.exists()) {
            files.addAll(listFiles(libDir));
        }

        return files;
    }
}
