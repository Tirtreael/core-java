package org.ignis.executor.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ignis.executor.api.function.IFunction;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ILibraryLoader {

    private static final Logger LOGGER = LogManager.getLogger();

    private IPropertyParser properties;
    private Map<String, IFunction> functionsMap;


    ILibraryLoader(IPropertyParser properties) {
        this.properties = properties;
        this.functionsMap = new HashMap<>();
    }

    public void loadFunction() {

    }

    public <C> C LoadClass(String directory, String classpath, Class<C> parentClass) throws ClassNotFoundException {
        File pluginsDir = new File(System.getProperty("user.dir") + directory);
        System.out.println(pluginsDir.getAbsolutePath());
        for (File jar : Objects.requireNonNull(pluginsDir.listFiles())) {
            try {
                ClassLoader loader = new URLClassLoader(
                        new URL[] { new URL(jar.getAbsolutePath()) },
                        getClass().getClassLoader()
                );
                Class<?> clazz = Class.forName(classpath, true, loader);
                Class<? extends C> newClass = clazz.asSubclass(parentClass);
                Constructor<? extends C> constructor = newClass.getConstructor();
                return constructor.newInstance();

            } catch (ClassNotFoundException e) {
                LOGGER.error(e.getMessage());
            } catch (MalformedURLException | NoSuchMethodException | InstantiationException
                    | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        throw new ClassNotFoundException("Class " + classpath
                + " wasn't found in directory " + System.getProperty("user.dir") + directory);
    }

    public void loadSource() {

    }
}
