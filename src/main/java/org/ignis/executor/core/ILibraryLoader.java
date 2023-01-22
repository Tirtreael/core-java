package org.ignis.executor.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ignis.executor.api.Pair;
import org.ignis.executor.api.function.IFunction;
import org.ignis.executor.api.function.IFunction0;
import org.ignis.executor.api.function.IFunction2;
import org.ignis.rpc.IEncoded;
import org.ignis.rpc.ISource;

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

    private final IPropertyParser properties;
    private final Map<String, IFunction> functionsMap;


    ILibraryLoader(IPropertyParser properties) {
        this.properties = properties;
        this.functionsMap = new HashMap<>();
    }

    public static IFunction0 loadISource0(ISource src) {
        return loadISource(src, IFunction0.class);
    }

    public static IFunction loadISource(ISource src) {
        return loadISource(src, IFunction.class);
    }

    public static IFunction loadISourceIndividual(ISource src) {
        try {
            return loadISourceIndividual(src, IFunction.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static IFunction2 loadISource2(ISource src) {
        return loadISource(src, IFunction2.class);
    }

    public static <T> T loadISource(ISource src, Class<T> superClazz) {
        String fName = src.getObj().getName();
        LOGGER.info("Loaded function: " + fName);
        return loadFunction(fName, superClazz);
    }

    public static <T> IFunction loadISourceIndividual(ISource src, Class<T> superClazz) throws Exception {
        String fName = src.getObj().getName();
        LOGGER.info("Loaded function: " + fName);
        return loadFunction2(fName);
    }

    public static IFunction loadFunction(String src) {
        try {
            return Class.forName(src).asSubclass(IFunction.class).getConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T loadFunction(String src, Class<T> superClazz) {
        try {
            return Class.forName(src).asSubclass(superClazz).getConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ISource createSource(String src) {
        IEncoded iEncoded = new IEncoded(IEncoded._Fields.NAME, src);

        return new ISource(iEncoded, null);
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

    public static IFunction loadFunction2(String src) throws Exception {
        String[] srcStringArr = src.split(":");
        String fileName = srcStringArr[0];
        String functionName = srcStringArr[1];

        File file = new File(fileName);
        URLClassLoader child = new URLClassLoader(
                new URL[]{file.toURI().toURL()},
                ILibraryLoader.class.getClassLoader()
        );
        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> e = jarFile.entries();
//
//        Map.Entry<String, IFunction> functionEntry = null;

        while (e.hasMoreElements()) {
            JarEntry jarEntry = e.nextElement();
            if (jarEntry.getName().endsWith(".class") && jarEntry.getName().equals(functionName.replace(".", "/") + ".class")) {
                String className = jarEntry.getName()
                        .replace("/", ".")
                        .replace(".class", "");
                Class<?> clazz = Class.forName(functionName, true, child);
                Class<? extends IFunction> runClass = clazz.asSubclass(IFunction.class);
                Constructor<? extends IFunction> ctor;
                ctor = runClass.getConstructor();
                return ctor.newInstance();
            }
        }
        return null;
    }

    public void loadSource() {
    }
}
