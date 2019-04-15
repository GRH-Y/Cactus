package com.yyz.ard.cactus.uiaf.joggle;


import android.support.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * view 注解
 * Created by Administrator on 2017/6/16.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AFindViewById {

    @IdRes int[] rid();//控件的资源id

    String[] method() default "setText";//要对应上id[]数量

    /**
     * //定义一个类来处理view
     * 类的构造方法标准格式：类名（Object object, Object data）
     * object 可以为view , activity,Window
     * data 可以为任何数据类型
     *
     * @return
     */
    String className() default "";
}
