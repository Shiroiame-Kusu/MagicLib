package top.hendrixshen.magiclib.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtil {
    public static @NotNull Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static @NotNull Class<?> getInnerClass(@NotNull Class<?> outerClass, String innerClassName) {
        for (Class<?> cls : outerClass.getDeclaredClasses()) {
            if (cls.getName().replace(String.format("%s$", outerClass.getName()), "").equals(innerClassName)) {
                return cls;
            }
        }
        throw new RuntimeException();
    }

    public static @NotNull Object newInstance(String className, int index, Object... parameters) {
        try {
            return Class.forName(className).getDeclaredConstructors()[index].newInstance(parameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static @NotNull Object newInstance(String className, Class<?>[] parameterTypes, Object... parameters) {
        try {
            return Class.forName(className).getDeclaredConstructor(parameterTypes).newInstance(parameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setFieldValue(String className, String fieldName, Object instance, Object value) {
        try {
            Field field = Class.forName(className).getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getFieldValue(String className, String fieldName, Object instance) {
        try {
            Field field = Class.forName(className).getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(instance);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object invoke(String className, String methodName, Object instance, Object... parameters) {
        return ReflectUtil.invoke(ReflectUtil.getClass(className), methodName, instance, parameters);
    }

    public static Object invoke(String className, String methodName, Object instance, Class<?>[] type, Object... parameters) {
        return ReflectUtil.invoke(ReflectUtil.getClass(className), methodName, instance, type,parameters);
    }

    public static Object invoke(@NotNull Class<?> cls, String methodName, Object instance, Object... parameters) {
        return ReflectUtil.invoke(cls, methodName, instance, null, parameters);
    }

    public static Object invoke(@NotNull Class<?> cls, String methodName, Object instance, Class<?>[] type, Object... parameters) {
        try {
            Method method = cls.getMethod(methodName, type);
            method.setAccessible(true);
            return method.invoke(instance, parameters);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object invokeDeclared(String className, String methodName, Object instance, Object... parameters) {
        return ReflectUtil.invokeDeclared(ReflectUtil.getClass(className), methodName, instance, parameters);
    }

    public static Object invokeDeclared(String className, String methodName, Object instance, Class<?>[] type, Object... parameters) {
        return ReflectUtil.invokeDeclared(ReflectUtil.getClass(className), methodName, instance, type, parameters);
    }

    public static Object invokeDeclared(@NotNull Class<?> cls, String methodName, Object instance, Object... parameters) {
        return ReflectUtil.invokeDeclared(cls, methodName, instance,null, parameters);
    }

    public static Object invokeDeclared(@NotNull Class<?> cls, String methodName, Object instance, Class<?>[] type, Object... parameters) {
        try {
            Method method = cls.getDeclaredMethod(methodName, type);
            method.setAccessible(true);
            return method.invoke(instance, parameters);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}