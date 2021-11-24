package com.ling.login.activity;

import android.view.View;
import android.widget.TextView;

import androidx.navigation.fragment.NavHostFragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.ToastUtils;
import com.ling.base.activity.LBaseActivity;
import com.ling.base.router.RouterActivityPath;
import com.ling.base.viewmodel.BaseViewModel;
import com.ling.common.view.CommonHeadTitle;
import com.ling.login.R;
import com.ling.login.fragment.LoginFragment;

/**
 * Created by zjp on 2020/5/16 23:24.
 */
@Route(path = RouterActivityPath.Login.PAGER_LOGIN)
public class LoginActivity extends LBaseActivity<BaseViewModel> {

    public TextView tvRight;
    public View backView;
    private View ivWbLogin;
    private View ivQqLogin;
    private View ivWxLogin;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        super.initView();
        CommonHeadTitle headTitle = findViewById(R.id.head_title);
        ivWbLogin = findViewById(R.id.iv_wb_login);
        ivQqLogin = findViewById(R.id.iv_qq_login);
        ivWxLogin = findViewById(R.id.iv_wx_login);
        tvRight = headTitle.getTvRight();
        backView = headTitle.getBackView();
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("注册");

        ivWxLogin.setOnClickListener(view -> ToastUtils.showShort("该功能暂未开发"));
        ivQqLogin.setOnClickListener(view -> ToastUtils.showShort("该功能暂未开发"));
        ivWbLogin.setOnClickListener(view -> ToastUtils.showShort("该功能暂未开发"));
    }

    @Override
    public boolean onSupportNavigateUp() {
        LoginFragment loginFragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        return NavHostFragment.findNavController(loginFragment).navigateUp();
    }
}
