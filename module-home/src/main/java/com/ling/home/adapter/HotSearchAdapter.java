package com.ling.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ling.home.R;
import com.ling.home.bean.HotSearchEntity;

import org.jetbrains.annotations.NotNull;


public class HotSearchAdapter extends BaseQuickAdapter<HotSearchEntity, BaseViewHolder> {

    public HotSearchAdapter() {
        super(R.layout.hotkeywords_item);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder bindingHolder, HotSearchEntity hotSearchEntity) {
        bindingHolder.setText(R.id.tv_label,hotSearchEntity.getName());
    }
}
