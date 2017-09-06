package com.fastaccess.permission.base.callback;

import android.support.annotation.NonNull;

import java.util.List;

public interface OnPermissionCallback {

    void onPermissionGranted(@NonNull String[] permissionName);

    void onPermissionDeclined(@NonNull String[] permissionName);

    void onPermissionPreGranted(@NonNull String permissionsName);

    void onPermissionNeedExplanation(@NonNull String permissionName);

    void onPermissionReallyDeclined(@NonNull String permissionName);
    void onPermissionReallyDeclined(@NonNull List<String> permissionName);

    void onNoPermissionNeeded();
}
