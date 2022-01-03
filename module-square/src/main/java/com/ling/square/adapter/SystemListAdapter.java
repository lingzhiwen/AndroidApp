package com.ling.square.adapter;

import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;
import com.ling.common.bean.ProjectTabBean;
import com.ling.square.R;
import com.ling.square.databinding.AdapterItemSystemBinding;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by ling on 2020/08/20 14:28
 */
public class SystemListAdapter extends BaseQuickAdapter<ProjectTabBean, BaseViewHolder> {

    private LayoutInflater layoutInflater = null;
    private Queue<AppCompatTextView> mFlexItemTextViewCaches = new LinkedList<>();

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SystemListAdapter() {
        super(R.layout.adapter_item_system);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder bindingHolder, ProjectTabBean projectTabBean) {
            List<ProjectTabBean> children = projectTabBean.getChildren();
            FlexboxLayout flexLayout = bindingHolder.getView(R.id.flex_layout);
            for (int i = 0; i < children.size(); i++) {
                ProjectTabBean ptb = children.get(i);
                AppCompatTextView labelTv = createOrGetCacheTv(flexLayout);
                labelTv.setText(ptb.getName());
                labelTv.setOnClickListener(v -> {
                    if (onItemClickListener != null)
                        onItemClickListener.onClick(ptb);
                });
                flexLayout.addView(labelTv);
            }
    }

    private AppCompatTextView createOrGetCacheTv(FlexboxLayout flexboxLayout) {
        AppCompatTextView tv = mFlexItemTextViewCaches.poll();
        if (tv != null) {
            return tv;
        }
        return findLabel(flexboxLayout);
    }

    private AppCompatTextView findLabel(FlexboxLayout flexboxLayout) {
        if (layoutInflater == null)
            layoutInflater = LayoutInflater.from(flexboxLayout.getContext());
        return (AppCompatTextView) layoutInflater.inflate(R.layout.flextlayout_item_label, flexboxLayout, false);
    }

    public interface OnItemClickListener {
        void onClick(ProjectTabBean projTabBean);
    }
}
