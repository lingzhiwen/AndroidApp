package com.ling.base.loadsir;

import com.kingja.loadsir.callback.Callback;
import com.ling.base.R;

/**
 * Created by ling on 2020/5/15 13:32
 */
public class EmptyCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.base_layout_empty;
    }
}
