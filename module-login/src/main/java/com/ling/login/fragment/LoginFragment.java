package com.ling.login.fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ImageView;

import androidx.navigation.Navigation;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.ling.base.fragment.LBaseFragment;
import com.ling.common.storage.MmkvHelper;
import com.ling.common.textwatcher.SimpleTextWatcher;
import com.ling.login.R;
import com.ling.login.activity.LoginActivity;
import com.ling.login.viewmodel.LoginViewModel;

/**
 * Created by ling on 2020/5/18 17:30
 */
public class LoginFragment extends LBaseFragment<LoginViewModel> {

    private LoginActivity activity;
    //密码是否显示明文
    private boolean isPasswordShow = false;
    private View btnLogin;
    private TextInputEditText etUsername;
    private TextInputEditText etPwd;
    private ImageView ivClearName;
    private ImageView ivClearPwd;
    private ImageView ivPwdPrivate;
    private View clLoading;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView() {
        super.initView();
        etUsername = getActivity().findViewById(R.id.et_username);
        ivClearName = getActivity().findViewById(R.id.iv_clear_name);
        etPwd = getActivity().findViewById(R.id.et_pwd);
        ivClearPwd = getActivity().findViewById(R.id.iv_clear_pwd);
        ivPwdPrivate = getActivity().findViewById(R.id.iv_pwd_private);
        btnLogin = getActivity().findViewById(R.id.btn_login);
        clLoading = getActivity().findViewById(R.id.cl_loading);
        activity = (LoginActivity) getActivity();
        activity.tvRight.setOnClickListener(v -> {
            Navigation.findNavController(btnLogin).navigate(R.id.action_fragment_register);
        });
        activity.backView.setBackgroundResource(R.drawable.close);
        activity.backView.setOnClickListener(v -> {
            getActivity().finish();
        });

        etUsername.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                ivClearName.setVisibility(TextUtils.isEmpty(s.toString()) ? View.GONE : View.VISIBLE);
            }
        });

        etPwd.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                ivClearPwd.setVisibility(TextUtils.isEmpty(s.toString()) ? View.GONE : View.VISIBLE);
                ivPwdPrivate.setVisibility(TextUtils.isEmpty(s.toString()) ? View.GONE : View.VISIBLE);
            }
        });

        ivPwdPrivate.setOnClickListener(v -> {
            if (isPasswordShow) {
                etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            ivPwdPrivate.setImageResource(isPasswordShow ? R.mipmap.pwd_close : R.mipmap.pwd_open);
            etPwd.setSelection(etPwd.getText().length());
            isPasswordShow = !isPasswordShow;
            etPwd.postInvalidate();
        });

        ivClearName.setOnClickListener(v -> etUsername.setText(""));
        ivClearPwd.setOnClickListener(v -> etPwd.setText(""));

        btnLogin.setOnClickListener(v -> {
            String userName = etUsername.getText().toString();
            if (userName.length() == 0) {
                ToastUtils.showShort("请输入用户名");
                return;
            }
            String pwd = etPwd.getText().toString();
            if (pwd.length() == 0) {
                ToastUtils.showShort("请输入密码");
                return;
            }
            btnLogin.setVisibility(View.GONE);
            clLoading.setVisibility(View.VISIBLE);
            KeyboardUtils.hideSoftInput(getActivity());
            mViewModel.login(userName, pwd);
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mViewModel.loginLiveData.observe(this, userInfo -> {
            if (null != userInfo) {
                MmkvHelper.getInstance().saveUserInfo(userInfo);
                getActivity().finish();
            } else {
                btnLogin.setVisibility(View.VISIBLE);
                clLoading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (activity.tvRight.getVisibility() == View.GONE) {
            activity.tvRight.setVisibility(View.VISIBLE);
        }
    }
}
