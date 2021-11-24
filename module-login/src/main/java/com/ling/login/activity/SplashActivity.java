package com.ling.login.activity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.ling.base.activity.LBaseActivity;
import com.ling.base.router.RouterActivityPath;
import com.ling.common.storage.MmkvHelper;
import com.ling.login.R;
import com.ling.login.viewmodel.LoginViewModel;
import com.ling.network.constant.C;

import me.wangyuwei.particleview.ParticleView;

public class SplashActivity extends LBaseActivity<LoginViewModel> {

    private ParticleView particleview;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        super.initView();
        particleview = findViewById(R.id.particleview);
        mViewModel.getProjectTab();
        particleview.startAnim();
    }

    @Override
    protected void initData() {
        super.initData();

        mViewModel.mProjectListMutable.observe(this, projectTabBeans -> {
            if (null != projectTabBeans && projectTabBeans.size() > 0) {

                particleview.setOnParticleAnimListener(() -> {
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
