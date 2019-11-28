package com.github.sylvainmaillard.gredis;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public class ServicesLocator {

    private ServicesLocator() {}

    private static Map<Class<?>, Object> SERVICES = new ConcurrentHashMap<>();

    public static <T> T loadDependency(Class<T> classOfDependency) {
        return classOfDependency.cast(SERVICES.computeIfAbsent(classOfDependency, aClass -> ServiceLoader.load(classOfDependency).findFirst().orElseThrow()));
    }
}
