package com.ling.officialaccount.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.ling.common.bean.ProjectTabBean;
import com.ling.officialaccount.R;
import com.ling.officialaccount.databinding.ItemProjectCategoryBinding;

import org.jetbrains.annotations.NotNull;

/**
 * Created by zjp on 2020/08/21 11:16
 */
public class CategoryAdapter extends BaseQuickAdapter<ProjectTabBean, BaseDataBindingHolder<ItemProjectCategoryBinding>> {

    public CategoryAdapter() {
        super(R.layout.item_project_category);
    }

    @Override
    protected void convert(@NotNull BaseDataBindingHolder<ItemProjectCategoryBinding> holder, ProjectTabBean projectTabBean) {
        ItemProjectCategoryBinding dataBinding = holder.getDataBinding();
        if (dataBinding != null) {
            dataBinding.setCategory(projectTabBean);
            dataBinding.executePendingBindings();
        }
    }
}
