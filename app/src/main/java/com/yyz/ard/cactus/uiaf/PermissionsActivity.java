package com.yyz.ard.cactus.uiaf;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.yyz.ard.cactus.uiaf.joggle.IPermissionListener;

import java.util.ArrayList;
import java.util.List;

public class PermissionsActivity extends BaseActivity implements IPermissionListener {

    protected final int requestPermissionCode = 0x123;

    /**
     * 申请运行时权限
     *
     * @param permissions - 权限集合
     */
    protected void requestRuntimePermission(String... permissions) {
        if (permissions == null || permissions.length == 0 || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        // 检查权限
        List<String> permitList = new ArrayList<>();
        List<String> noPermitList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 请求权限
                noPermitList.add(permission);
            } else {
                //已授权
                permitList.add(permission);
            }
        }
        if (permitList.size() > 0) {
            String[] array = new String[permitList.size()];
            array = permitList.toArray(array);
            onGranted(array);
        }
        if (noPermitList.size() > 0) {
            String[] array = new String[noPermitList.size()];
            array = noPermitList.toArray(array);
            ActivityCompat.requestPermissions(this, array, requestPermissionCode);
        } else {
            onALLGranted();
        }
    }

    /**
     * 申请权限结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestPermissionCode) {
            if (grantResults.length > 0) {
                List<String> permitList = new ArrayList<>();
                List<String> noPermitList = new ArrayList<>();
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        noPermitList.add(permissions[i]);
                    } else {
                        // 用户已授权
                        permitList.add(permissions[i]);
                    }
                }
                if (permitList.size() > 0) {
                    String[] array = new String[permitList.size()];
                    array = permitList.toArray(array);
                    onGranted(array);
                }
                if (noPermitList.size() > 0) {
                    String[] array = new String[noPermitList.size()];
                    array = noPermitList.toArray(array);
                    onDenied(array);
                } else {
                    onALLGranted();
                }
            }
        }
    }

    @Override
    public void onGranted(String... permissions) {

    }

    @Override
    public void onDenied(String... deniedPermissions) {

    }

    @Override
    public void onALLGranted() {

    }
}
