package com.yyz.ard.cactus.uiaf.joggle;

/**
 * @className: IPermissionListener
 * @classDescription: 申请权限的回调接口
 * @Author: yyz
 */
public interface IPermissionListener {

    void onGranted(String... permissions);

    void onDenied(String... deniedPermissions);

    void onALLGranted();
}
