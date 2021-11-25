package com.ling.mine.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.ling.base.BuildConfig;
import com.ling.base.activity.LBaseActivity;
import com.ling.base.viewmodel.BaseViewModel;
import com.ling.common.ui.WebViewActivity;
import com.ling.common.view.CommonHeadTitle;
import com.ling.mine.R;
import com.ling.network.constant.C;

public class AboutAppActivity extends LBaseActivity<BaseViewModel> {

    private TextView tvWeb;
    private TextView tvAbout;
    private TextView tvGithub;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar
                .statusBarDarkFont(true)
                .init();
    }

    public static void start(Context context){
        context.startActivity(new Intent(context,AboutAppActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_app;
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void initView() {
        super.initView();
        CommonHeadTitle titleview = findViewById(R.id.titleview);
        TextView tvVersionCode = findViewById(R.id.tv_version_code);
        View llWeb = findViewById(R.id.ll_web);
        TextView tvWeb = findViewById(R.id.tv_web);
        View llAbout = findViewById(R.id.ll_about);
        TextView tvAbout = findViewById(R.id.tv_about);
        View llGithub = findViewById(R.id.ll_github);
        TextView tvGithub = findViewById(R.id.tv_github);
        titleview.setTitle("关于我们");

        tvVersionCode.setText(String.format("V%s(%d)", BuildConfig.BUILD_TYPE, BuildConfig.BUILD_TYPE));
        llWeb.setOnClickListener(v -> WebViewActivity.start(AboutAppActivity.this, tvWeb.getText().toString(), C.BASE_URL));
        llAbout.setOnClickListener(v -> WebViewActivity.start(AboutAppActivity.this, tvAbout.getText().toString(), C.URL_ABOUT));
        llGithub.setOnClickListener(v -> WebViewActivity.start(AboutAppActivity.this, tvGithub.getText().toString(), C.SOURCE_URL));
    }

    @Override
    protected void initData() {
        super.initData();
    }
}
