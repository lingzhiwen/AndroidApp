package com.ling.mine.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ling.common.bean.UserInfo;
import com.ling.mine.R;

import org.jetbrains.annotations.NotNull;

/**
 * Created by zjp on 2020/08/19 11:05
 */
public class MyIntergralAdapter extends BaseQuickAdapter<UserInfo, BaseViewHolder> {

    public MyIntergralAdapter() {
        super(R.layout.adapter_item_my_intergral);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, UserInfo userInfo) {
        holder.setText(R.id.tv_sign_in_points, userInfo.getCoinCount())
                .setText(R.id.tv_date, userInfo.getDate())
                .setText(R.id.tv_point_nums, userInfo.getRank());
    }
}
