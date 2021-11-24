package com.ling.square.activity;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ling.base.activity.LBaseActivity;
import com.ling.base.databinding.BaseRefreshLayoutBinding;
import com.ling.common.view.CommonHeadTitle;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.ling.common.adapter.ArticleListAdapter;
import com.ling.common.bean.ArticleEntity;
import com.ling.common.bean.page.PageInfo;
import com.ling.common.utils.CustomItemDecoration;
import com.ling.network.constant.C;
import com.ling.square.R;
import com.ling.square.viewmodel.SquareViewModel;

import java.util.List;

/**
 * Created by zjp on 2020/08/20 16:18
 */
public class SystemActivity extends LBaseActivity<SquareViewModel>
        implements OnRefreshLoadMoreListener {

    private PageInfo pageInfo;
    private ArticleListAdapter articleListAdapter;
    private int cid;
    //记录当前点击收藏的position
    private int currentPosition = 0;

    /**
     * 点击收藏后将点击事件上锁,等接口有相应结果再解锁
     * 避免重复点击产生的bug
     */
    private boolean lockCollectClick = true;
    private BaseRefreshLayoutBinding includeRefresh;

    public static void start(Context context, String title, int cid) {
        Intent intent = new Intent(context, SystemActivity.class);
        intent.putExtra(C.TITLE, title);
        intent.putExtra(C.CID, cid);
        context.startActivity(intent);
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
        return R.layout.activity_ststem;
    }

    @Override
    protected void initView() {
        super.initView();
        Intent intent = getIntent();
        CommonHeadTitle headTitle = findViewById(R.id.title);
        if (intent != null) {
            String title = intent.getStringExtra(C.TITLE);
            cid = intent.getIntExtra(C.CID, 0);
            headTitle.setTitle(title);
        }

        pageInfo = new PageInfo();
        setLoadSir(includeRefresh.refresh);
        loadData();

        includeRefresh.recy.setLayoutManager(new LinearLayoutManager(this));
        includeRefresh.recy.addItemDecoration(new CustomItemDecoration(this,
                CustomItemDecoration.ItemDecorationDirection.VERTICAL_LIST, R.drawable.linear_split_line));
        includeRefresh.recy.setAdapter(articleListAdapter = new ArticleListAdapter(null));
        includeRefresh.refresh.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        super.initData();

        mViewModel.mArticleMuTable.observe(this, articleEntity -> {
            if (includeRefresh.refresh.getState().isOpening) {
                includeRefresh.refresh.finishRefresh();
                includeRefresh.refresh.finishLoadMore();
            }

            List<ArticleEntity.DatasBean> dataList = articleEntity.getDatas();

//            if (dataList != null && dataList.size() > 0) {
//                for (ArticleEntity.DatasBean articleBean : dataList) {
//                    articleBean.setCollect(true);
//                }
//            }

            pageInfo.nextZeroPage();
            if (articleEntity.getCurPage() == 0) {
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
                includeRefresh.refresh.finishLoadMoreWithNoMoreData();
            }
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

        articleListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.iv_collect) {
                collectArticle(position);
            }
        });
    }

    private void collectArticle(int position) {
        if (position < articleListAdapter.getData().size() && lockCollectClick) {
            lockCollectClick = false;
            currentPosition = position;
            if (articleListAdapter.getData().get(position).isCollect()) {
                mViewModel.unCollect(articleListAdapter.getData().get(position).getId());
            } else {
                mViewModel.collect(articleListAdapter.getData().get(position).getId());
            }
        }
    }

    private void loadData() {
        mViewModel.getProjectList(pageInfo.mPage, cid);
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
