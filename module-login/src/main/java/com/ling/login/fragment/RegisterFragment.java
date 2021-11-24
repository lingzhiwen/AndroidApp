package com.ling.login.fragment;

import android.app.MediaRouteButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.ling.base.fragment.BaseFragment;
import com.ling.base.fragment.LBaseFragment;
import com.ling.common.storage.MmkvHelper;
import com.ling.common.textwatcher.SimpleTextWatcher;
import com.ling.login.R;
import com.ling.login.activity.LoginActivity;
import com.ling.login.databinding.FragmentRegisterBinding;
import com.ling.login.viewmodel.LoginViewModel;

/**
 * Created by zjp on 2020/5/18 17:30
 */
public class RegisterFragment extends LBaseFragment<LoginViewModel> {

    private LoginActivity activity;

    //密码是否显示明文
    private boolean isPasswordShow = false;
    private TextInputEditText etUsername;
    private TextInputEditText etPwd;
    private ImageView ivClearName;
    private ImageView ivClearPwd;
    private ImageView ivPwdPrivate;
    private View btnRegister;
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
        return R.layout.fragment_register;
    }

    @Override
    protected void initView() {
        super.initView();
        activity = (LoginActivity) getActivity();
        etUsername = activity.findViewById(R.id.et_username);
        ivClearName = activity.findViewById(R.id.iv_clear_name);
        etPwd = activity.findViewById(R.id.et_pwd);
        ivClearPwd = activity.findViewById(R.id.iv_clear_pwd);
        ivPwdPrivate = activity.findViewById(R.id.iv_pwd_private);
        btnRegister = activity.findViewById(R.id.btn_register);
        clLoading = activity.findViewById(R.id.cl_loading);
        activity.backView.setBackgroundResource(R.drawable.back_left_black);
        activity.backView.setOnClickListener(v -> Navigation.findNavController(getView()).navigateUp());

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

        btnRegister.setOnClickListener(v -> {
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
            btnRegister.setVisibility(View.GONE);
            clLoading.setVisibility(View.VISIBLE);
            KeyboardUtils.hideSoftInput(getActivity());
            mViewModel.register(userName, pwd);
        });
    }

    @Override
    protected void initData() {
        super.initData();

        mViewModel.registerLiveData.observe(this, userInfo -> {
            if (userInfo != null) {
                MmkvHelper.getInstance().saveUserInfo(userInfo);
                getActivity().finish();
            } else {
                btnRegister.setVisibility(View.VISIBLE);
                clLoading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.tvRight.setVisibility(View.GONE);
    }
}
