package com.ling.square.fragment;

import android.app.AlertDialog;

import androidx.recyclerview.widget.RecyclerView;

import com.ling.base.fragment.BaseLazyFragment;
import com.ling.common.bean.ArticleEntity;
import com.ling.square.R;
import com.ling.square.activity.SystemActivity;
import com.ling.square.adapter.NavigationAdapter;
import com.ling.square.databinding.FragmentListSystemBinding;
import com.ling.square.viewmodel.SquareViewModel;

/**
 * Created by zjp on 2020/08/20 14:47
 */
public class NavigationFragment extends BaseLazyFragment<SquareViewModel>
implements NavigationAdapter.OnItemClickListener {

    private NavigationAdapter navigationAdapter;
    private boolean isLoading;
    private RecyclerView recy;

    public static NavigationFragment newInstance() {
        return new NavigationFragment();
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected void initView() {
        super.initView();
        recy = getView().findViewById(R.id.recy);
        recy.setAdapter(navigationAdapter = new NavigationAdapter());
        navigationAdapter.setOnItemClickListener(this::onClick);
    }

    @Override
    protected void initData() {
        super.initData();
        mViewModel.mArticleListMuTable.observe(this, articleEntities -> {
            if (isLoading)
                showContent();
            if (articleEntities != null && articleEntities.size() > 0) {
                navigationAdapter.setList(articleEntities);
            }
            isLoading = !isLoading;
        });
    }

    @Override
    protected void lazyLoadData() {
        isLoading = true;
        showLoading();
        onRetryBtnClick();
    }

    @Override
    protected void onRetryBtnClick() {
        super.onRetryBtnClick();
        mViewModel.getNavigation();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_list_system;
    }

    @Override
    public void onClick(ArticleEntity.DatasBean datasBean) {
        SystemActivity.start(getActivity(), datasBean.getTitle(),datasBean.getCourseId());
    }
}
