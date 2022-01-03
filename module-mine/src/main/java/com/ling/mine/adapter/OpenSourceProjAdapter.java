package com.ling.mine.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ling.mine.R;
import com.ling.mine.bean.OpenSourceProj;

import org.jetbrains.annotations.NotNull;

/**
 * Created by ling on 2020/08/03 16:41
 */
public class OpenSourceProjAdapter extends BaseQuickAdapter<OpenSourceProj, BaseViewHolder> {

    public OpenSourceProjAdapter() {
        super(R.layout.adapter_open_source_proj);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder bindingHolder, OpenSourceProj openSourceProj) {

        bindingHolder.setText(R.id.tv_project, openSourceProj.getAuthor())
                .setText(R.id.tv_description, openSourceProj.getContent());
    }
}
