package com.yyz.ard.cactus.uiaf;


import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.yyz.ard.cactus.R;
import com.yyz.ard.cactus.uiaf.joggle.AFindViewById;
import com.yyz.hover.Hover;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import util.SpeedReflex;
import util.StringUtils;

/**
 * 解析实体自动为控件赋值
 * Created by No.9 on 7/8/2017.
 *
 * @author yyz
 * @date 7/8/2017.
 */
public class ViewAssignment {

    private static Class mRSClass;

    public static void setRStringClass(Class rsClass) {
        ViewAssignment.mRSClass = rsClass;
    }

    /**
     * 自动查找控件并且设置值
     *
     * @param objView Activity or ViewControlLayer or Window or Fragment
     * @param data    任意数据类型
     */
    public static void setViewData(@NonNull Object objView, @NonNull Object data) {
        if (!checkObject(objView) || data == null) {
            throw new RuntimeException("object or data is null or object is not Activity ,ViewControlLayer ,Window,Fragment");
        }
        Class cls = data.getClass();
        if (cls.isAnnotationPresent(AFindViewById.class)) {
            AFindViewById clxAnnotation = (AFindViewById) cls.getAnnotation(AFindViewById.class);
            String className = clxAnnotation.className();
            if (className != null) {
                try {
                    Class exec = Class.forName(className);
                    Constructor constructor = exec.getConstructors()[0];
                    constructor.setAccessible(true);
                    constructor.newInstance(objView, data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                try {
                    if (field.isAnnotationPresent(AFindViewById.class)) {
                        field.setAccessible(true);
                        AFindViewById annotation = field.getAnnotation(AFindViewById.class);
                        Object value = field.get(data);

                        String className = annotation.className();
                        if (StringUtils.isNotEmpty(className)) {
                            try {
                                Class exec = Class.forName(className);
                                Constructor constructor = exec.getConstructors()[0];
                                constructor.setAccessible(true);
                                constructor.newInstance(objView, data);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            String[] methodArray = annotation.method();
                            int[] ridArray = annotation.rid();

                            for (int index = 0; index < ridArray.length; index++) {
                                View view = getView(ridArray[index], objView);
                                String method ;
                                if (index > methodArray.length - 1) {
                                    method = methodArray[0];
                                } else {
                                    method = methodArray[index];
                                }
                                invoke(view, method, value);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 检测该实体是否非法
     *
     * @param object
     * @return
     */
    private static boolean checkObject(Object object) {
        return object instanceof Activity || object instanceof View || object instanceof Window ||
                object instanceof android.support.v4.app.Fragment || object instanceof android.app.Fragment;
    }


    /**
     * 获取view里所有的控件
     *
     * @param layoutId layout 资源id
     * @return
     */
    public static View[] findViewAllChild(Context context, @LayoutRes int layoutId) {
        View layout = View.inflate(context, layoutId, null);
        ViewGroup viewGroup = (ViewGroup) layout;
        return findViewAllChild(viewGroup);
    }

    /**
     * 获取view里所有的控件
     *
     * @param viewGroup
     * @return
     */
    public static View[] findViewAllChild(ViewGroup viewGroup) {
        if (viewGroup == null) {
            return null;
        }
        int count = viewGroup.getChildCount();
        View[] children = new View[count];
        for (int index = 0; index < count; index++) {
            children[index] = viewGroup.getChildAt(index);
        }
        return children;
    }


    public static void setViewData(Activity activity, @IdRes int id, String methodName, Object value) {
        View view = activity.findViewById(id);
        invoke(view, methodName, value);
    }

    public static void setViewData(View view, @IdRes int id, String methodName, Object value) {
        View v = view.findViewById(id);
        invoke(v, methodName, value);
    }

    public static void setViewData(Window window, @IdRes int id, String methodName, Object value) {
        View view = window.findViewById(id);
        invoke(view, methodName, value);
    }

    public static int getRid(String viewName) {
        try {
            Field field = R.id.class.getDeclaredField(viewName);
            return (int) field.get(R.id.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean isRidString(@StringRes int ridStr) {
        try {
            if (mRSClass != null) {
                Field[] fields = mRSClass.getFields();
                for (Field field : fields) {
                    int rid = (int) field.get(R.string.class);
                    if (rid == ridStr) {
                        return true;
                    }
                }

            }
            Field[] fields = R.string.class.getFields();
            for (Field field : fields) {
                int rid = (int) field.get(R.string.class);
                if (rid == ridStr) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static View getView(int rid, Object object) {
        View view = null;
        if (object instanceof Activity) {
            view = ((Activity) object).findViewById(rid);
        } else if (object instanceof View) {
            view = ((View) object).findViewById(rid);
        } else if (object instanceof Window) {
            view = ((Window) object).findViewById(rid);
        } else if (object instanceof android.support.v4.app.Fragment || object instanceof android.app.Fragment) {
            Class clx = object.getClass();
            try {
                Method method = clx.getMethod("getView");
                View layout = (View) method.invoke(object);
                view = layout.findViewById(rid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    private static void invoke(View view, String methodName, Object value) {
        if (view == null || methodName == null || value == null) {
            return;
        }
        SpeedReflex cache = SpeedReflex.getCache();
        Class clx = cache.getClass(view);
        Class valueClx = cache.getClass(value);
        try {
            if (view instanceof ListView || view instanceof RecyclerView) {
                Object adapter;
                if (view instanceof ListView) {
                    Object objectAdapter = ((ListView) view).getAdapter();
                    if (objectAdapter instanceof HeaderViewListAdapter) {
                        HeaderViewListAdapter headerViewListAdapter = (HeaderViewListAdapter) objectAdapter;
                        adapter = headerViewListAdapter.getWrappedAdapter();
                    } else {
                        adapter = objectAdapter;
                    }
                } else {
                    adapter = ((RecyclerView) view).getAdapter();
                }

                Class adapterClass = cache.getClass(adapter);
                methodName = StringUtils.isEmpty(methodName) ? "addAllData" : methodName;
                Method method;
                String[] parameter = methodName.split(":");
                if (parameter.length > 1) {
                    method = cache.getMethod(adapterClass, parameter[0], int.class);
                    method.setAccessible(true);
                    method.invoke(adapter, parameter[1]);
                } else {
                    method = cache.getMethod(adapterClass, methodName, valueClx);
                }

                method.setAccessible(true);
                method.invoke(adapter, value);
            } else if (view instanceof ImageView) {
                if (value instanceof byte[]) {
                    Hover.getInstance().loadImage((byte[]) value, (ImageView) view);
                } else if (value instanceof String) {
                    Hover.getInstance().loadImage((String) value, (ImageView) view);
                }
            } else {
                methodName = methodName == null ? "setText" : methodName;
                Method method = null;
                if (value instanceof Integer && isRidString((Integer) value)) {
                    method = cache.getMethod(clx, methodName, int.class);
                }
                if (method == null) {
                    method = cache.getMethod(clx, methodName, CharSequence.class);
                    value = value == null ? "" : String.valueOf(value);
                }
                method.setAccessible(true);
                method.invoke(view, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object classNameToClassValue(String typeSimpleName, String value) {
        Object object = null;
        if (Integer.class.getName().contains(typeSimpleName) || int.class.getName().contains(typeSimpleName)) {
            object = Integer.parseInt(value);
        } else if (Double.class.getName().contains(typeSimpleName) || double.class.getName().contains(typeSimpleName)) {
            object = Double.parseDouble(value);
        } else if (Long.class.getName().contains(typeSimpleName) || long.class.getName().contains(typeSimpleName)) {
            object = Long.parseLong(value);
        } else if (Boolean.class.getName().contains(typeSimpleName) || boolean.class.getName().contains(typeSimpleName)) {
            object = Boolean.parseBoolean(value);
        } else if (Float.class.getName().contains(typeSimpleName) || float.class.getName().contains(typeSimpleName)) {
            object = Float.parseFloat(value);
        } else if (String.class.getName().contains(typeSimpleName)) {
            object = value;
        }
        return object;
    }

    private static Class classNameToClass(String typeSimpleName) {
        Class object = null;
        if (Integer.class.getName().contains(typeSimpleName)) {
            object = Integer.class;
        } else if (int.class.getName().contains(typeSimpleName)) {
            object = int.class;
        } else if (Double.class.getName().contains(typeSimpleName)) {
            object = Double.class;
        } else if (double.class.getName().contains(typeSimpleName)) {
            object = double.class;
        } else if (Long.class.getName().contains(typeSimpleName)) {
            object = Long.class;
        } else if (long.class.getName().contains(typeSimpleName)) {
            object = long.class;
        } else if (Boolean.class.getName().contains(typeSimpleName)) {
            object = Boolean.class;
        } else if (boolean.class.getName().contains(typeSimpleName)) {
            object = boolean.class;
        } else if (Float.class.getName().contains(typeSimpleName)) {
            object = Float.class;
        } else if (float.class.getName().contains(typeSimpleName)) {
            object = float.class;
        } else if (String.class.getName().contains(typeSimpleName)) {
            object = String.class;
        }
        return object;
    }
}
