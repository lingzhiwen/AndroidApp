package com.ling.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.ling.aop.permission.annotation.CheckPermission;
import com.ling.base.activity.LBaseActivity;
import com.ling.common.view.CommonHeadTitle;
import com.ling.mine.R;
import com.ling.mine.viewmodel.MineViewModel;

public class AboutMeActivity extends LBaseActivity<MineViewModel> {

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar
                .statusBarDarkFont(true)
                .init();
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, AboutMeActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_me;
    }

    @Override
    protected void initView() {
        super.initView();
        TextView ivAlipay = findViewById(R.id.iv_alipay);
        TextView ivWx = findViewById(R.id.iv_wx);
        CommonHeadTitle titleview = findViewById(R.id.titleview);
        titleview.setTitle("关于作者");
        ivAlipay.setOnLongClickListener(view -> {
            saveAliQrCode();
            return false;
        });

        ivWx.setOnLongClickListener(view -> {
            saveWxQrCode();
            return false;
        });
    }

    @Override
    protected void initData() {
        super.initData();


    }

    @CheckPermission(permissions = {
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"})
    private void saveAliQrCode() {
    }

    @CheckPermission(permissions = {
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"})
    private void saveWxQrCode() {
    }


}
