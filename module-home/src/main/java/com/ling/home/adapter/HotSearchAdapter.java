package com.ling.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.ling.home.R;
import com.ling.home.bean.HotSearchEntity;
import com.ling.home.databinding.HotkeywordsItemBinding;

import org.jetbrains.annotations.NotNull;


/**
 * Created by zjp on 2020/6/1 13:22
 */
public class HotSearchAdapter extends BaseQuickAdapter<HotSearchEntity, BaseDataBindingHolder<HotkeywordsItemBinding>> {

    public HotSearchAdapter() {
        super(R.layout.hotkeywords_item);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<HotkeywordsItemBinding> bindingHolder, HotSearchEntity hotSearchEntity) {
        HotkeywordsItemBinding dataBinding = bindingHolder.getDataBinding();
        if (dataBinding != null) {
            dataBinding.setHotkey(hotSearchEntity);
            dataBinding.executePendingBindings();
        }
    }
}
