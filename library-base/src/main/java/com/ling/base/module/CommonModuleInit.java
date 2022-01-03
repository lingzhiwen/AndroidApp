package com.ling.base.module;


import com.alibaba.android.arouter.launcher.ARouter;
import com.kingja.loadsir.core.LoadSir;
import com.tencent.mmkv.MMKV;
import com.ling.base.application.BaseApplication;
import com.ling.base.loadsir.EmptyCallback;
import com.ling.base.loadsir.ErrorCallback;
import com.ling.base.loadsir.LoadingCallback;

/**
 * Created by ling on 2020/5/9 16:37
 */
public class CommonModuleInit implements IModuleInit {

    @Override
    public boolean onInitAhead(BaseApplication application) {

        MMKV.initialize(application);

        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new EmptyCallback())
                .setDefaultCallback(LoadingCallback.class)
                .commit();
        
        if (application.isDebug()) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(application);

        return false;
    }

    @Override
    public boolean onInitLow(BaseApplication application) {
        return false;
    }

}
