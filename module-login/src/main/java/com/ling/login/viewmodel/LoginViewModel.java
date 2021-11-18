package com.ling.login.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ToastUtils;
import com.ling.base.viewmodel.BaseViewModel;
import com.ling.common.bean.ProjectTabBean;
import com.ling.common.bean.UserInfo;
import com.ling.login.api.LoginService;
import com.ling.network.bean.BaseResponse;
import com.ling.network.https.RetrofitHelper;
import com.ling.network.observer.NetCallback;
import com.ling.network.observer.NetHelperObserver;
import com.ling.network.scheduler.IoMainScheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zjp on 2020/5/28 14:32
 */
public class LoginViewModel extends BaseViewModel {

    public MutableLiveData<UserInfo> registerLiveData= new MutableLiveData<>();
    public MutableLiveData<UserInfo> loginLiveData= new MutableLiveData<>();
    public MutableLiveData<List<ProjectTabBean>> mProjectListMutable = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public void register(String username, String pwd) {

        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", pwd);
        map.put("repassword", pwd);
        RetrofitHelper.getInstance().create(LoginService.class)
                .register(map)
                .compose(new IoMainScheduler<>())
                .doOnSubscribe(this)
                .subscribe(new NetHelperObserver<>(new NetCallback<BaseResponse<UserInfo>>() {
                    @Override
                    public void success(BaseResponse<UserInfo> response) {
                        registerLiveData.postValue(response.getData());
                    }

                    @Override
                    public void error(String msg) {
                        ToastUtils.showShort(msg);
                    }
                }));
    }

    public void login(String username, String pwd) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", pwd);
        RetrofitHelper.getInstance().create(LoginService.class)
                .login(map)
                .compose(new IoMainScheduler<>())
                .doOnSubscribe(this)
                .subscribe(new NetHelperObserver<>(new NetCallback<BaseResponse<UserInfo>>() {
                    @Override
                    public void success(BaseResponse<UserInfo> response) {
                        loginLiveData.postValue(response.getData());
                    }

                    @Override
                    public void error(String msg) {
                        ToastUtils.showShort(msg);
                    }
                }));
    }

    public void getProjectTab() {
        RetrofitHelper.getInstance().create(LoginService.class)
                .getProjectTab()
                .compose(new IoMainScheduler<>())
                .doOnSubscribe(this)  //  请求与ViewModel周期同步
                .subscribe(new NetHelperObserver<>(new NetCallback<BaseResponse<List<ProjectTabBean>>>() {
                    @Override
                    public void success(BaseResponse<List<ProjectTabBean>> response) {
                        if (response.getData() != null && response.getData().size() > 0) {
                            mProjectListMutable.postValue(response.getData());
                        } else {

                        }
                    }

                    @Override
                    public void error(String msg) {

                    }
                }));
    }
}
