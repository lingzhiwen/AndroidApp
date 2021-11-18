package com.ling.login.activity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ling.base.activity.BaseActivity;
import com.ling.base.router.RouterActivityPath;
import com.ling.common.storage.MmkvHelper;
import com.ling.login.R;
import com.ling.login.databinding.ActivitySplashBinding;
import com.ling.login.viewmodel.LoginViewModel;
import com.ling.network.constant.C;

public class SplashActivity extends BaseActivity<ActivitySplashBinding, LoginViewModel> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        super.initView();

        mViewModel.getProjectTab();
        mViewDataBinding.particleview.startAnim();
    }

    @Override
    protected void initData() {
        super.initData();

        mViewModel.mProjectListMutable.observe(this, projectTabBeans -> {
            if (null != projectTabBeans && projectTabBeans.size() > 0) {

                mViewDataBinding.particleview.setOnParticleAnimListener(() -> {
//                        UserInfo userInfo = MmkvHelper.getInstance().getUserInfo();
//                        if (userInfo == null) {
//                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                        } else {
                    MmkvHelper.getInstance().saveList(C.PROJECT_TAB, projectTabBeans);
                    ARouter.getInstance().build(RouterActivityPath.Main.PAGER_MAIN).navigation();
//                        }
                    finish();
                });
            }
        });
    }
}
