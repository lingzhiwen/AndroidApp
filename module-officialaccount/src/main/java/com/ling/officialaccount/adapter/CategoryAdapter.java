package com.ling.officialaccount.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ling.common.bean.ProjectTabBean;
import com.ling.officialaccount.R;
import com.ling.officialaccount.databinding.ItemProjectCategoryBinding;

import org.jetbrains.annotations.NotNull;

/**
 * Created by ling on 2020/08/21 11:16
 */
public class CategoryAdapter extends BaseQuickAdapter<ProjectTabBean, BaseViewHolder> {

    public CategoryAdapter() {
        super(R.layout.item_project_category);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, ProjectTabBean projectTabBean) {
        TextView textView = holder.getView(R.id.txt_category);
        textView.setText(projectTabBean.getName());
    }
}
