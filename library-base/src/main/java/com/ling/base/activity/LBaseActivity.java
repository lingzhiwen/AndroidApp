package com.ling.base.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.blankj.utilcode.util.ToastUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.ling.base.event.IEventBus;
import com.ling.base.interf.IBaseView;
import com.ling.base.loadsir.EmptyCallback;
import com.ling.base.loadsir.ErrorCallback;
import com.ling.base.loadsir.LoadingCallback;
import com.ling.base.viewmodel.BaseViewModel;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by ling on 2020/4/30 16:12
 */
public abstract class LBaseActivity<VM extends BaseViewModel>
        extends AppCompatActivity implements IBaseView {


    protected VM mViewModel;

    protected ImmersionBar mImmersionBar;

    protected LoadService mLoadService;

    private boolean isShowedContent = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImmersionBar();
        setContentView(getLayoutId());
        initViewModel();
        //让ViewModel拥有View的生命周期感应
        getLifecycle().addObserver(mViewModel);
        initView();
        initData();
        if (this instanceof IEventBus)
            EventBus.getDefault().register(this);
    }

    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.init();
    }

    @LayoutRes
    protected abstract int getLayoutId();

    protected void initView() {

    }

    protected void initData() {

    }

    private void initViewModel() {
        if (mViewModel == null) {
            Class modelClass= BaseViewModel.class;
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                Type[] types = ((ParameterizedType) type).getActualTypeArguments();
                if (types.length > 0) {
                    modelClass = (Class) types[0];
                }
            }
//            mViewModel = new (VM) ViewModelProviders.of(this).get(modelClass);
            //mViewModel = (VM) new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(modelClass);
            mViewModel = (VM) new ViewModelProvider(this).get(modelClass);
        }
    }

    /**
     * 注册LoadSir
     *
     * @param view 替换视图
     */
    public void setLoadSir(View view) {
        if (mLoadService == null) {
            mLoadService = LoadSir.getDefault()
                    .register(view, (Callback.OnReloadListener) v -> onRetryBtnClick());
        }
        showLoading();
    }

    @Override
    public void showContent() {
        if (null != mLoadService) {
            isShowedContent = true;
            mLoadService.showSuccess();
        }
    }

    @Override
    public void showLoading() {
        if (null != mLoadService) {
            mLoadService.showCallback(LoadingCallback.class);
        }
    }

    @Override
    public void showEmpty() {
        if (null != mLoadService) {
            mLoadService.showCallback(EmptyCallback.class);
        }
    }

    @Override
    public void showFailure(@Nullable String message) {
        if (null != mLoadService) {
            if (!isShowedContent) {
                mLoadService.showCallback(ErrorCallback.class);
            } else {
                ToastUtils.showShort(message);
            }
        }
    }

    /**
     * 失败重试,重新加载事件
     */
    protected void onRetryBtnClick() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this instanceof IEventBus)
            EventBus.getDefault().unregister(this);
    }
}
