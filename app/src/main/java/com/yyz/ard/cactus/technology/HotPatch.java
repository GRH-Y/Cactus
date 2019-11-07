package com.yyz.ard.cactus.technology;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;
import storage.FileHelper;

/**
 * 热补丁
 * @className: HotPatch
 * @classDescription:
 * @author: yyz
 * @createTime: 2018/12/5
 */
public class HotPatch {
    private DexClassLoader dexClassLoader;

    public HotPatch(Context context, String jarPath) {
        File file = new File(jarPath);
        if (file.exists()) {
            String path = context.getExternalCacheDir().getAbsolutePath();
            dexClassLoader = new DexClassLoader(jarPath, path, null, context.getClassLoader());
        } else {
            throw new IllegalStateException(" jarPath is not exists !!! ");
        }
    }

    public void addPatch(Context context, String jarPath) {
        String path = context.getExternalCacheDir().getAbsolutePath();
        byte[] jarData = FileHelper.readFile(jarPath);
        String[] array = jarPath.split(File.separator);
        FileHelper.writeFile(path + array[array.length - 1], jarData);
    }

    public Class findClass(String className) {
        try {
            return dexClassLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object loadClass(String className) {
        Class clx = findClass(className);
        if (clx != null) {
            try {
                return clx.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Object invokeMethod(Class<?> clx, Object object, String methodName, Object... args) {
        Object result = null;
        try {
            Class<?>[] parameterTypes = getParameterTypes(args);
            Method method = clx.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            result = method.invoke(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static Class<?>[] getParameterTypes(Object... initArgs) {
        Class<?>[] parameterTypes = null;
        if (initArgs != null) {
            parameterTypes = new Class<?>[initArgs.length];
            for (int index = 0; index < initArgs.length; index++) {
                parameterTypes[index] = initArgs[index].getClass();
            }
        }
        return parameterTypes;
    }


}
