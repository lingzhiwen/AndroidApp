package com.ling.aop.permission.aspect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ling.aop.permission.annotation.CheckPermission;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by ling on 2020/08/18 17:12
 */
@Aspect
public class CheckPermissionAspect {

    private static final String TAG = CheckPermissionAspect.class.getSimpleName();

    @Pointcut("execution(@com.ling.aop.permission.annotation.CheckPermission * *(..)) && @annotation(checkPermission)")
    public void requestPermission(CheckPermission checkPermission) {

    }

    @SuppressLint("WrongConstant")
    @Around("requestPermission(checkPermission)")
    public void aroundRequestPermission(final ProceedingJoinPoint joinPoint, CheckPermission checkPermission) throws Throwable {

        final Object object = joinPoint.getThis();
        Context context = null;
        if (object instanceof Context) {
            context = (Context) object;
        } else if (object instanceof Fragment) {
            context = ((Fragment) object).getActivity();
        } else if (object instanceof AppCompatActivity) {
            context = (AppCompatActivity) object;
        }

        if (null == context || null == checkPermission) {
            Log.e(TAG, "PermissionAspect needPermission is null or context is null");
            return;
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] permissionArray = checkPermission.permissions();

        for (String permissionName : permissionArray) {
            Log.i(TAG, "AOP checked class =" + object.getClass().getSimpleName()
                    + " method=" + signature.getName()
                    + "  requestPermission=" + permissionName);
        }

        PermissionUtils.permission(permissionArray)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        try {
                            joinPoint.proceed(); //??????????????????????????????
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                    @Override
                    public void onDenied() {  //?????????????????????????????????
                        ToastUtils.showShort("????????????????????????");
                    }
                }).request();
    }
}
