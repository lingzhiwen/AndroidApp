package com.ling.officialaccount.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ling.common.bean.ArticleEntity;
import com.ling.officialaccount.R;

import org.jetbrains.annotations.NotNull;

/**
 * Created by zjp on 2020/8/23 10:09.
 */
public class OfficialAccountListAdapter extends BaseQuickAdapter<ArticleEntity.DatasBean, BaseViewHolder> {

    public OfficialAccountListAdapter() {
        super(R.layout.article_txt_item);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, ArticleEntity.DatasBean datasBean) {
        baseViewHolder.setText(R.id.tv_author, datasBean.getAuthor())
                .setText(R.id.tv_date, datasBean.getNiceDate())
                .setText(R.id.tv_title, datasBean.getTitle());

    }
}
