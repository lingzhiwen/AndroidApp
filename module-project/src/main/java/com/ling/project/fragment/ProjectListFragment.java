package com.ling.project.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.ling.aop.checklogin.annotation.CheckLogin;
import com.ling.base.fragment.BaseLazyFragment;
import com.ling.common.adapter.ArticleListAdapter;
import com.ling.common.bean.ArticleEntity;
import com.ling.common.bean.page.PageInfo;
import com.ling.common.ui.WebViewActivity;
import com.ling.common.utils.CustomItemDecoration;
import com.ling.network.constant.C;
import com.ling.project.R;
import com.ling.project.viewmodel.ProjectViewModel;


/**
 * Created by ling on 2020/7/1 10:17
 */
public class ProjectListFragment extends BaseLazyFragment<ProjectViewModel>
        implements OnRefreshLoadMoreListener {

    private int id;
    private PageInfo pageInfo;
    private ArticleListAdapter articleListAdapter;
    private boolean isLoading;

    /**
     * 点击收藏后将点击事件上锁,等接口有相应结果再解锁
     * 避免重复点击产生的bug
     */
    private boolean lockCollectClick = true;

    //记录当前点击收藏的position
    private int currentPosition = 0;
    private RecyclerView recy;
    private RefreshLayout refresh;

    public static ProjectListFragment newInstance(int id) {
        ProjectListFragment projectListFragment = new ProjectListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        projectListFragment.setArguments(bundle);
        return projectListFragment;
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_project_list;
    }

    @Override
    protected void initView() {
        super.initView();
        recy = getView().findViewById(R.id.recy);
        refresh = getView().findViewById(R.id.refresh);
        Bundle bundle = getArguments();
        if (null != bundle) {
            id = bundle.getInt("id", 0);
        }
        recy.setLayoutManager(new LinearLayoutManager(getActivity()));
        recy.addItemDecoration(new CustomItemDecoration(getActivity(),
                CustomItemDecoration.ItemDecorationDirection.VERTICAL_LIST, R.drawable.linear_split_line));
        articleListAdapter = new ArticleListAdapter(null);
        recy.setAdapter(articleListAdapter);
    }

    @Override
    protected void initData() {
        super.initData();

        mViewModel.mArticleListMutable.observe(this, datasBeans -> {
            if (refresh.getState().isOpening) {
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }
            if (isLoading)
                showContent();
            if (datasBeans != null && datasBeans.size() > 0) {
                if (pageInfo.isFirstPage()) {
                    articleListAdapter.setList(datasBeans);
                } else {
                    articleListAdapter.addData(datasBeans);
                }
//                pageInfo.nextPage();
            } else {
                if (pageInfo.isFirstPage()) {
                    showEmpty();
                } else {
                    refresh.finishLoadMoreWithNoMoreData();
                }
            }
            isLoading = false;
        });

        mViewModel.mCollectMutable.observe(this, baseResponse -> {
            lockCollectClick = true;
            if (baseResponse.getErrorCode() == 0) {
                if (currentPosition < articleListAdapter.getData().size()) {
                    articleListAdapter.getData().get(currentPosition).setCollect(true);
                    articleListAdapter.notifyItemChanged(currentPosition, C.REFRESH_COLLECT);
                }
            }
        });

        mViewModel.mUnCollectMutable.observe(this, baseResponse -> {
            lockCollectClick = true;
            if (baseResponse.getErrorCode() == 0) {
                if (currentPosition < articleListAdapter.getData().size()) {
                    articleListAdapter.getData().get(currentPosition).setCollect(false);
                    articleListAdapter.notifyItemChanged(currentPosition, C.REFRESH_COLLECT);
                }
            }
        });
    }

    @Override
    protected void lazyLoadData() {
        pageInfo = new PageInfo();
        isLoading = true;
        refresh.setOnRefreshLoadMoreListener(this);
        articleListAdapter.setOnItemClickListener((adapter, view, position) -> {
            ArticleEntity.DatasBean datasBean = articleListAdapter.getData().get(position);
            WebViewActivity.start(getActivity(), datasBean.getTitle(), datasBean.getLink());
        });
        onRetryBtnClick();

        articleListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.iv_collect) {
                collectArticle(position);
            }
        });
    }

    @Override
    protected void onRetryBtnClick() {
        super.onRetryBtnClick();
        mViewModel.getProjectList(pageInfo.page, id);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageInfo.nextPage();
        onRetryBtnClick();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageInfo.reset();
        onRetryBtnClick();
    }

    @CheckLogin
    private void collectArticle(int position) {
        if (position < articleListAdapter.getData().size() && lockCollectClick) {
            lockCollectClick = false;
            currentPosition = position;
            if (articleListAdapter.getData().get(position).isCollect()) {
                mViewModel.uncollect(articleListAdapter.getData().get(position).getId());
            } else {
                mViewModel.collect(articleListAdapter.getData().get(position).getId());
            }
        }
    }
}
