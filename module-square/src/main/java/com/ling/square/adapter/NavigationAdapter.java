package com.ling.square.adapter;

import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;
import com.ling.common.bean.ArticleEntity;
import com.ling.square.R;
import com.ling.square.bean.NaviBean;
import com.ling.square.databinding.AdapterItemNavigationBinding;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by ling on 2020/8/20 22:06.
 */
public class NavigationAdapter extends BaseQuickAdapter<NaviBean, BaseViewHolder> {

    private LayoutInflater layoutInflater = null;
    private Queue<AppCompatTextView> mFlexItemTextViewCaches = new LinkedList<>();
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public NavigationAdapter() {
        super(R.layout.adapter_item_navigation);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder bindingHolder, NaviBean naviBean) {
            List<ArticleEntity.DatasBean> articles = naviBean.getArticles();
            FlexboxLayout flexLayout = bindingHolder.getView(R.id.flex_layout);
            for (int i = 0; i < articles.size(); i++) {
                ArticleEntity.DatasBean datasBean = articles.get(i);
                AppCompatTextView labelTv = createOrGetCacheTv(flexLayout);
                labelTv.setText(datasBean.getTitle());
                labelTv.setOnClickListener(v -> {
                    if (onItemClickListener != null)
                        onItemClickListener.onClick(datasBean);
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
        void onClick(ArticleEntity.DatasBean datasBean);
    }
}
