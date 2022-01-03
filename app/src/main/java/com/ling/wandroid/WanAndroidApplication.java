package com.ling.wandroid;

import com.ling.base.application.BaseApplication;
import com.ling.base.config.ModuleLifecycleConfig;
import com.ling.wandroid.BuildConfig;

/**
 * Created by ling on 2020/4/30 16:33
 */
public class WanAndroidApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        setDebug(BuildConfig.DEBUG);
        // 初始化需要初始化的组件
        ModuleLifecycleConfig.getInstance().initModuleAhead(this);
    }
}
