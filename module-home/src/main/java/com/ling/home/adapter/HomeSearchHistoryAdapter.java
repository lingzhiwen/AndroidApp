package com.ling.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ling.home.R;
import com.ling.home.databinding.AdapterSearchHistoryItemBinding;

import org.jetbrains.annotations.NotNull;

/**
 * Created by zjp on 2020/5/29 17:20
 */
public class HomeSearchHistoryAdapter extends BaseQuickAdapter<String, BaseViewHolder>  {

    public HomeSearchHistoryAdapter() {
        super(R.layout.adapter_search_history_item);
        addChildClickViewIds(R.id.clear_keywords_ib);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder bindingHolder, String string) {
        bindingHolder.setText(R.id.tv_history_keywords,string);
    }
}
