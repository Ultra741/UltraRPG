package me.ultradev.ultrarpg.api.util;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.Set;

public class ReflectionUtil {

    private ReflectionUtil() {}

    public static <T> Set<Class<? extends T>> findClasses(final String packageName, final Class<T> type) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .filterInputsBy(new FilterBuilder().includePackage(packageName))
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(new SubTypesScanner(false)));
        return reflections.getSubTypesOf(type);
    }


}
