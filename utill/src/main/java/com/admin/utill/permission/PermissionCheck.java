package com.admin.utill.permission;

import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * @author Lv
 * Created at 2019/6/15
 */
@SuppressWarnings("AlibabaAbstractClassShouldStartWithAbstractNaming")
public abstract class PermissionCheck extends AppCompatActivity {

    private ICheckPermission mICheckPermission = null;

   public interface ICheckPermission {
        void onAllow();

        void onReject();
    }

    public void checkPermission(String[] permission, ICheckPermission iCheckPermission) {
        if (Build.VERSION.SDK_INT < 23 || permission.length == 0) {
            if (iCheckPermission != null) {
                iCheckPermission.onAllow();
            }
        } else {
            if (iCheckPermission != null) {
                mICheckPermission = iCheckPermission;
                ActivityCompat.requestPermissions(this, permission, 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mICheckPermission != null && requestCode == 0) {
            for (int grantResult : grantResults) {
                //判断权限是否被允许，只要又一次拒绝就算失败
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    mICheckPermission.onReject();
                }
            }
            mICheckPermission.onAllow();
        }
    }

}
