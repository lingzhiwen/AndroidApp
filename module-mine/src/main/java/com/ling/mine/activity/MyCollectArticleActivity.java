package com.ling.mine.activity;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ling.base.activity.LBaseActivity;
import com.ling.common.adapter.ArticleListAdapter;
import com.ling.common.bean.ArticleEntity;
import com.ling.common.bean.page.PageInfo;
import com.ling.common.utils.CustomItemDecoration;
import com.ling.common.view.CommonHeadTitle;
import com.ling.mine.R;
import com.ling.mine.viewmodel.MineViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

public class MyCollectArticleActivity extends LBaseActivity<MineViewModel>
        implements OnRefreshLoadMoreListener {

    private PageInfo pageInfo;
    private ArticleListAdapter articleListAdapter;

    //记录当前点击收藏的position
    private int currentPosition = 0;
    private RecyclerView recy;
    private SmartRefreshLayout refresh;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MyCollectArticleActivity.class));
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarDarkFont(true).init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mycollect_article;
    }

    @Override
    protected void initView() {
        super.initView();
        CommonHeadTitle titleview = findViewById(R.id.titleview);
        recy = findViewById(R.id.recy);
        refresh = findViewById(R.id.refresh);
        titleview.setTitle("我的收藏");
        pageInfo = new PageInfo();
        setLoadSir(refresh);
        loadData();

        recy.setLayoutManager(new LinearLayoutManager(this));
        recy.addItemDecoration(new CustomItemDecoration(this,
                CustomItemDecoration.ItemDecorationDirection.VERTICAL_LIST, R.drawable.linear_split_line));
        recy.setAdapter(articleListAdapter = new ArticleListAdapter(null));
        refresh.setOnRefreshLoadMoreListener(this);
    }

    private void loadData() {
        mViewModel.getCollectArticleList(pageInfo.mPage);
    }

    @Override
    protected void initData() {
        super.initData();
        mViewModel.articleLiveData.observe(this, articleEntity -> {
            if (refresh.getState().isOpening) {
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }

            List<ArticleEntity.DatasBean> dataList = articleEntity.getDatas();

            if (dataList != null && dataList.size() > 0) {
                for (ArticleEntity.DatasBean articleBean : dataList) {
                    articleBean.setCollect(true);
                }
            }

            pageInfo.nextZeroPage();
            if (articleEntity.getCurPage() == 1) {
                if (dataList != null && dataList.size() > 0) {
                    showContent();
                    articleListAdapter.setList(dataList);
                } else {
                    showEmpty();
                }
            } else {
                articleListAdapter.addData(dataList);
            }
            if (articleEntity.isOver()) {
                refresh.finishLoadMoreWithNoMoreData();
            }
        });

        mViewModel.mUnCollectMutable.observe(this, baseResponse -> {
            if (baseResponse.getErrorCode() == 0) {
                if (currentPosition < articleListAdapter.getData().size()) {
                    articleListAdapter.cancelCollect(currentPosition);
                }
            }
        });

        articleListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.iv_collect) {
                collectArticle(position);
            }
        });
    }

    private void collectArticle(int position) {
        if (position < articleListAdapter.getData().size()) {
            currentPosition = position;
            mViewModel.unCollect(articleListAdapter.getData().get(position).getId());
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageInfo.reset();
        loadData();
    }
}
