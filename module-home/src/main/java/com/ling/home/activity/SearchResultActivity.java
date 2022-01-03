package com.ling.home.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gyf.immersionbar.ImmersionBar;
import com.ling.base.activity.LBaseActivity;
import com.ling.common.adapter.ArticleListAdapter;
import com.ling.common.bean.ArticleEntity;
import com.ling.common.bean.page.PageInfo;
import com.ling.common.ui.WebViewActivity;
import com.ling.common.utils.CustomItemDecoration;
import com.ling.home.R;
import com.ling.home.viewmodel.SearchViewModel;
import com.ling.network.constant.C;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

/**
 * Created by ling on 2020/6/4 10:34
 */
public class SearchResultActivity extends LBaseActivity<SearchViewModel> {

    private String keyword;
    private PageInfo pageInfo;

    private ArticleListAdapter articleListAdapter;
    private boolean isLoading = true;
    private AppCompatEditText searchEt;
    private View viewStatus;
    private SmartRefreshLayout refresh;
    private RecyclerView recy;
    private Button tvCancel;


    public static void start(Activity activity, String keyword, ActivityOptionsCompat optionsCompat) {
        Intent intent = new Intent(activity, SearchResultActivity.class);
        intent.putExtra(C.KEYWORD, keyword);
        ActivityCompat.startActivityForResult(activity, intent, C.SEARCH_REQUEST, optionsCompat.toBundle());
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
        return R.layout.activity_searchresult;
    }

    @Override
    protected void initView() {
        super.initView();
        searchEt = findViewById(R.id.search_et);
        viewStatus = findViewById(R.id.view_status);
        refresh = findViewById(R.id.refresh);
        recy = findViewById(R.id.recy);
        tvCancel = findViewById(R.id.tv_cancel);
        ViewGroup.LayoutParams layoutParams = viewStatus.getLayoutParams();
        layoutParams.height = ImmersionBar.getStatusBarHeight(this);
        viewStatus.setLayoutParams(layoutParams);
        setLoadSir((View)refresh);
        pageInfo = new PageInfo();
        Intent intent = getIntent();
        if (intent != null) {
            keyword = intent.getStringExtra(C.KEYWORD);
            if (!TextUtils.isEmpty(keyword)) {
                searchEt.setText(keyword);
                searchEt.setFocusable(false);
                searchEt.setSelection(searchEt.length());
            }
        }

        recy.setLayoutManager(new LinearLayoutManager(this));
        recy.addItemDecoration(new CustomItemDecoration(this,
                CustomItemDecoration.ItemDecorationDirection.VERTICAL_LIST, R.drawable.linear_split_line));
        articleListAdapter = new ArticleListAdapter(null);
        recy.setAdapter(articleListAdapter);

        loadData(keyword);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initData() {
        super.initData();
        mViewModel.mArticleSearch.observe(this, articleEntity -> {

            if (refresh.getState().isOpening) {
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }
            if (isLoading)
                showContent();
            List<ArticleEntity.DatasBean> datas = articleEntity.getDatas();
            if (datas != null && datas.size() > 0) {
                if (pageInfo.isZeroPage()) {
                    articleListAdapter.setList(datas);
                } else {
                    articleListAdapter.addData(datas);
                }
                pageInfo.nextZeroPage();
            } else {
                if (pageInfo.isZeroPage()) {
                    showEmpty();
                } else {
                    articleListAdapter.addData(datas);
                    refresh.finishLoadMoreWithNoMoreData();
                }
            }
            isLoading = false;
        });

        articleListAdapter.setOnItemClickListener((adapter, view, position) -> {
            ArticleEntity.DatasBean datasBean = articleListAdapter.getData().get(position);
            WebViewActivity.start(this, datasBean.getTitle(), datasBean.getLink());
        });

        refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadData(keyword);
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageInfo.resetZero();
                loadData(keyword);
            }
        });

        searchEt.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                Intent intent = new Intent();
                intent.putExtra(C.KEYWORD, searchEt.getText().toString());
                setResult(RESULT_OK, intent);
                finishAfterTransition();
            }
        });

        searchEt.setOnTouchListener((view, motionEvent) -> {
            if (searchEt == null) return false;
            searchEt.setFocusable(true);
            searchEt.setFocusableInTouchMode(true);
            searchEt.requestFocus();
            return false;
        });

        tvCancel.setOnClickListener(v -> finishAfterTransition());
    }

    private void loadData(String keyword) {
        mViewModel.search(pageInfo.mPage, keyword);
    }

}
