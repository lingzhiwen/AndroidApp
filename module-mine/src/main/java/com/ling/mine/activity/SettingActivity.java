package com.ling.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.ling.base.activity.LBaseActivity;
import com.ling.base.event.SettingEvent;
import com.ling.common.BuildConfig;
import com.ling.common.storage.MmkvHelper;
import com.ling.common.view.CommonHeadTitle;
import com.ling.mine.R;
import com.ling.mine.viewmodel.MineViewModel;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ling on 2020/7/7 21:46.
 */
public class SettingActivity extends LBaseActivity<MineViewModel> {
    private CommonHeadTitle titleview;
    private TextView tvCurrentVersionVal;
    private SwitchMaterial swShowTop;
    private TextView tvCacheVal;
    private Context mContext;

    public static void start(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected void initView() {
        super.initView();
        titleview = findViewById(R.id.titleview);
        swShowTop = findViewById(R.id.sw_show_top);
        tvCurrentVersionVal = findViewById(R.id.tv_current_version_val);
        tvCacheVal = findViewById(R.id.tv_cache_val);
        findViewById(R.id.cl_clear_cache).setOnClickListener(v -> clickClearCache());
        findViewById(R.id.cl_current_version).setOnClickListener(v -> ToastUtils.showShort("已经是最新版本"));
        findViewById(R.id.cl_about_me).setOnClickListener(v -> clickAboutMe());
        findViewById(R.id.cl_copyright).setOnClickListener(v -> clickCopyright());
        findViewById(R.id.btn_login_out).setOnClickListener(v -> clickLoginout());
        titleview.setTitle("设置");
        boolean hideTopArticle = MmkvHelper.getInstance().getshowTopArticle();
        swShowTop.setChecked(hideTopArticle);

        tvCurrentVersionVal.setText("v" + BuildConfig.BUILD_TYPE);
        mViewModel.getCacheSize();
    }

    @Override
    protected void initData() {
        super.initData();
        mContext = this;
        mViewModel.mCacheSizeLiveData.observe(this, s -> tvCacheVal.setText(s));
        mViewModel.loginoutLiveData.observe(this, baseResponse -> {
            if (baseResponse.getErrorCode() == 0) {
                MmkvHelper.getInstance().logout();
                finish();
            }
        });
        swShowTop.setOnCheckedChangeListener((compoundButton, isChecked) -> MmkvHelper.getInstance().showTopArticle(isChecked));
    }


    public void clickClearCache() {
        new AlertDialog.Builder(mContext)
                .setTitle(R.string.tips)
                .setMessage(R.string.clear_cache)
                .setPositiveButton(R.string.sure, (dialogInterface, i) -> mViewModel.clearCache())
                .setNegativeButton(R.string.cancel, null)
                .show();

    }

    public void clickCopyright() {
        new AlertDialog.Builder(mContext)
                .setTitle(R.string.tips)
                .setMessage(R.string.copyright_tips)
                .setPositiveButton(R.string.sure, null)
                .show();
    }

    public void clickAboutMe() {
        //mContext.startActivity(new Intent(mContext, AboutAppActivity.class));
    }

    public void clickLoginout() {
        new AlertDialog.Builder(mContext)
                .setTitle(R.string.tips)
                .setMessage(R.string.login_out_tips)
                .setPositiveButton(R.string.sure, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    mViewModel.loginout();
                }).setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss()).show();
    }

    /**
     * 用户操作的时候不发送广播，当关闭设置页时候发送广播
     */
    @Override
    protected void onStop() {
        super.onStop();
        boolean showTopArticle = MmkvHelper.getInstance().getshowTopArticle();
        SettingEvent settingEvent = new SettingEvent();
        settingEvent.setShowTopArticle(showTopArticle);
        EventBus.getDefault().post(settingEvent);
    }
}
