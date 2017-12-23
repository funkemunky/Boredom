package me.vertises.aztec.tablist.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;

public final class Reflection {
    private static String OBC_PREFIX = Bukkit.getServer().getClass().getPackage().getName();
    private static String NMS_PREFIX = OBC_PREFIX.replace("org.bukkit.craftbukkit", "net.minecraft.server");
    private static String VERSION = OBC_PREFIX.replace("org.bukkit.craftbukkit", "").replace(".", "");
    private static Pattern MATCH_VARIABLE = Pattern.compile("\\{([^\\}]+)\\}");

    private Reflection() {
    }

    public static <T> FieldAccessor<T> getField(Class<?> target, String name, Class<T> fieldType) {
        return Reflection.getField(target, name, fieldType, 0);
    }

    public static <T> FieldAccessor<T> getField(String className, String name, Class<T> fieldType) {
        return Reflection.getField(Reflection.getClass(className), name, fieldType, 0);
    }

    public static <T> FieldAccessor<T> getField(Class<?> target, Class<T> fieldType, int index) {
        return Reflection.getField(target, null, fieldType, index);
    }

    public static <T> FieldAccessor<T> getField(String className, Class<T> fieldType, int index) {
        return Reflection.getField(Reflection.getClass(className), fieldType, index);
    }

    private static <T> FieldAccessor<T> getField(Class<?> target, String name, Class<T> fieldType, int index) {
        Field[] arrfield = target.getDeclaredFields();
        int n = arrfield.length;
        int n2 = 0;
        while (n2 < n) {
            Field field = arrfield[n2];
            if ((name == null || field.getName().equals(name)) && fieldType.isAssignableFrom(field.getType()) && index-- <= 0) {
                field.setAccessible(true);
                return new FieldAccessor<T>(){

                    @Override
                    public T get(Object target) {
                        try {
                            return this.get(target);
                        }
                        catch (Exception e) {
                            throw new RuntimeException("Cannot access reflection.", e);
                        }
                    }

                    @Override
                    public void set(Object target, Object value) {
                        try {
                            this.set(target, value);
                        }
                        catch (Exception e) {
                            throw new RuntimeException("Cannot access reflection.", e);
                        }
                    }

                    @Override
                    public boolean hasField(Object target) {
                        return this.getClass().getDeclaringClass().isAssignableFrom(target.getClass());
                    }
                };
            }
            ++n2;
        }
        if (target.getSuperclass() != null) {
            return Reflection.getField(target.getSuperclass(), name, fieldType, index);
        }
        throw new IllegalArgumentException("Cannot find field with type " + fieldType);
    }

    public static /* varargs */ MethodInvoker getMethod(String className, String methodName, Class<?> ... params) {
        return Reflection.getTypedMethod(Reflection.getClass(className), methodName, null, params);
    }

    public static /* varargs */ MethodInvoker getMethod(Class<?> clazz, String methodName, Class<?> ... params) {
        return Reflection.getTypedMethod(clazz, methodName, null, params);
    }

    public static /* varargs */ MethodInvoker getTypedMethod(Class<?> clazz, String methodName, Class<?> returnType, Class<?> ... params) {
        Method[] arrmethod = clazz.getDeclaredMethods();
        int n = arrmethod.length;
        int n2 = 0;
        while (n2 < n) {
            Method method = arrmethod[n2];
            if ((methodName == null || method.getName().equals(methodName)) && (returnType == null || method.getReturnType().equals(returnType)) && Arrays.equals(method.getParameterTypes(), params)) {
                method.setAccessible(true);
                return new MethodInvoker(){

                    @Override
                    public /* varargs */ Object invoke(Object target, Object ... arguments) {
                        try {
                            return this.invoke(target, arguments);
                        }
                        catch (Exception e) {
                            throw new RuntimeException("Cannot invoke method " + this.getClass(), e);
                        }
                    }
                };
            }
            ++n2;
        }
        if (clazz.getSuperclass() != null) {
            return Reflection.getMethod(clazz.getSuperclass(), methodName, params);
        }
        throw new IllegalStateException(String.format("Unable to find method %s (%s).", methodName, Arrays.asList(params)));
    }

    public static /* varargs */ ConstructorInvoker getConstructor(String className, Class<?> ... params) {
        return Reflection.getConstructor(Reflection.getClass(className), params);
    }

    public static /* varargs */ ConstructorInvoker getConstructor(Class<?> clazz, Class<?> ... params) {
        Constructor<?>[] arrconstructor = clazz.getDeclaredConstructors();
        int n = arrconstructor.length;
        int n2 = 0;
        while (n2 < n) {
            Constructor<?> constructor = arrconstructor[n2];
            if (Arrays.equals(constructor.getParameterTypes(), params)) {
                constructor.setAccessible(true);
                return new ConstructorInvoker(){

                    @Override
                    public Object invoke(Object ... arguments) {
                        try {
                            return this.getClass().isInstance(arguments);
                        }
                        catch (Exception e) {
                            throw new RuntimeException("Cannot invoke constructor " + this, e);
                        }
                    }
                };
            }
            ++n2;
        }
        throw new IllegalStateException(String.format("Unable to find constructor for %s (%s).", clazz, Arrays.asList(params)));
    }

    public static Class<Object> getUntypedClass(String lookupName) {
        Class<Object> clazz = Reflection.getClass(lookupName);
        return clazz;
    }

    public static Class<Object> getClass(String lookupName) {
        return Reflection.getCanonicalClass(Reflection.expandVariables(lookupName));
    }

    public static Class<?> getMinecraftClass(String name) {
        return Reflection.getCanonicalClass(String.valueOf(NMS_PREFIX) + "." + name);
    }

    public static Class<?> getCraftBukkitClass(String name) {
        return Reflection.getCanonicalClass(String.valueOf(OBC_PREFIX) + "." + name);
    }

    @SuppressWarnings("unchecked")
	private static Class<Object> getCanonicalClass(String canonicalName) {
        try {
            return (Class<Object>) Class.forName(canonicalName);
        }
        catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Cannot find " + canonicalName, e);
        }
    }

    private static String expandVariables(String name) {
        StringBuffer output = new StringBuffer();
        Matcher matcher = MATCH_VARIABLE.matcher(name);
        while (matcher.find()) {
            String variable = matcher.group(1);
            String replacement = "";
            if ("nms".equalsIgnoreCase(variable)) {
                replacement = NMS_PREFIX;
            } else if ("obc".equalsIgnoreCase(variable)) {
                replacement = OBC_PREFIX;
            } else if ("version".equalsIgnoreCase(variable)) {
                replacement = VERSION;
            } else {
                throw new IllegalArgumentException("Unknown variable: " + variable);
            }
            if (replacement.length() > 0 && matcher.end() < name.length() && name.charAt(matcher.end()) != '.') {
                replacement = String.valueOf(replacement) + ".";
            }
            matcher.appendReplacement(output, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(output);
        return output.toString();
    }

    public static interface ConstructorInvoker {
        public /* varargs */ Object invoke(Object ... var1);
    }

    public static interface FieldAccessor<T> {
        public T get(Object var1);

        public void set(Object var1, Object var2);

        public boolean hasField(Object var1);
    }

    public static interface MethodInvoker {
        public /* varargs */ Object invoke(Object var1, Object ... var2);
    }

}

