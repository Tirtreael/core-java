package org.ignis.executor.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ignis.executor.api.function.IFunction;
import org.ignis.executor.api.function.IFunction2;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ILibraryLoader {

    private static final Logger LOGGER = LogManager.getLogger();

    private IPropertyParser properties;
    private Map<String, IFunction> functionsMap;


    ILibraryLoader(IPropertyParser properties) {
        this.properties = properties;
        this.functionsMap = new HashMap<>();
    }

    public IFunction loadFunction(String src) {
        try {
            return Class.forName(src).asSubclass(IFunction.class).getConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T loadFunction(String src, Class<T> superClazz) {
        try {
            return Class.forName(src).asSubclass(superClazz).getConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    /*
    * Loads functions inside a jar
    */
    public Map<String, IFunction> loadLibrary(String src) throws Exception {
        File file = new File(src);
        URLClassLoader child = new URLClassLoader(
                new URL[]{file.toURI().toURL()},
                this.getClass().getClassLoader()
        );
        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> e = jarFile.entries();
        while (e.hasMoreElements()) {
            JarEntry jarEntry = e.nextElement();
            if (jarEntry.getName().endsWith(".class")) {
                String className = jarEntry.getName()
                        .replace("/", ".")
                        .replace(".class", "");
                Class<?> clazz = Class.forName(className, true, child);
                Class<? extends IFunction> runClass = clazz.asSubclass(IFunction.class);
                Constructor<? extends IFunction> ctor;
                ctor = runClass.getConstructor();
                this.functionsMap.put(runClass.getName(), ctor.newInstance());
            }
        }
        return this.functionsMap;
    }

    public void loadSource() {
    }
}
