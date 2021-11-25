package com.ling.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ling.base.activity.LBaseActivity;
import com.ling.common.adapter.ArticleListAdapter;
import com.ling.common.bean.ArticleEntity;
import com.ling.common.bean.page.PageInfo;
import com.ling.common.utils.CustomItemDecoration;
import com.ling.common.view.CommonHeadTitle;
import com.ling.mine.R;
import com.ling.mine.viewmodel.MineViewModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

public class MyShareActivity extends LBaseActivity<MineViewModel>
        implements OnRefreshLoadMoreListener {
    private ArticleListAdapter articleListAdapter;
    private PageInfo pageInfo;
    private RecyclerView recy;
    private RefreshLayout refresh;
    private CommonHeadTitle titleview;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MyShareActivity.class));
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_myshare;
    }

    @Override
    protected void initView() {
        super.initView();
        titleview = findViewById(R.id.titleview);
        recy = findViewById(R.id.recy);
        refresh = findViewById(R.id.refresh);

        titleview.setTitle("我的分享");
        titleview.getIvRight().setImageResource(R.mipmap.add);
        titleview.setIvRightVisible(View.VISIBLE);
        recy.addItemDecoration(new CustomItemDecoration(this,
                CustomItemDecoration.ItemDecorationDirection.VERTICAL_LIST, R.drawable.linear_split_line));
        recy.setAdapter(articleListAdapter = new ArticleListAdapter(null));
        refresh.setOnRefreshLoadMoreListener(this);

        getData();

        titleview.getIvRight().setOnClickListener(view -> AddArticleActivity.start(MyShareActivity.this));
    }

    @Override
    protected void initData() {
        super.initData();

        mViewModel.userCenterLiveData.observe(this, userCenter -> {
            if (refresh.getState().isOpening) {
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }
            pageInfo.nextPage();

            ArticleEntity shareArticles = userCenter.getShareArticles();
            List<ArticleEntity.DatasBean> dataList = shareArticles.getDatas();

            if (dataList != null && dataList.size() > 0) {
                for (ArticleEntity.DatasBean articleBean : dataList) {
                    articleBean.setCollect(true);
                }
            }

            if (shareArticles.getCurPage() == 1) {
                if (dataList != null && dataList.size() > 0) {
                    showContent();
                    articleListAdapter.setList(dataList);
                } else {
                    showEmpty();
                }
            } else {
                articleListAdapter.addData(dataList);
            }
            if (shareArticles.isOver()) {
                refresh.finishLoadMoreWithNoMoreData();
            }
        });
    }

    private void loadData() {
        mViewModel.myShare(pageInfo.page);
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

    @Override
    protected void onRetryBtnClick() {
        super.onRetryBtnClick();
        getData();
    }

    private void getData() {
        pageInfo = new PageInfo();
        setLoadSir((View) refresh);
        loadData();
    }
}
