package me.djdisaster.skDisaster.utils;

import ch.njol.skript.Skript;
import me.djdisaster.skDisaster.SkDisaster;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.codehaus.janino.Compiler;
import org.codehaus.janino.SimpleCompiler;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class JavaCompilerManager {


    public static CompiledJavaClass compile(String fileName, String fileContent) throws Exception {
        SimpleCompiler compiler = new SimpleCompiler();
        compiler.setParentClassLoader(Compiler.class.getClassLoader());
        compiler.cook(fileContent);
        Class<?> clazz = compiler.getClassLoader().loadClass(fileName);
        return new CompiledJavaClass(clazz);
    }


}
