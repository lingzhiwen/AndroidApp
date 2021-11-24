package com.ling.home.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Outline;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.appbar.AppBarLayout;
import com.ling.aop.checklogin.annotation.CheckLogin;
import com.ling.base.event.IEventBus;
import com.ling.base.event.SettingEvent;
import com.ling.base.fragment.LBaseFragment;
import com.ling.base.router.RouterFragmentPath;
import com.ling.common.adapter.ArticleListAdapter;
import com.ling.common.bean.ArticleEntity;
import com.ling.common.bean.page.PageInfo;
import com.ling.common.storage.MmkvHelper;
import com.ling.common.ui.WebViewActivity;
import com.ling.common.utils.CustomItemDecoration;
import com.ling.home.R;
import com.ling.home.activity.SearchActivity;
import com.ling.home.adapter.HomeHeadBannerAdapter;
import com.ling.home.viewmodel.HomeViewModel;
import com.ling.network.constant.C;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.youth.banner.Banner;
import com.youth.banner.config.BannerConfig;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.util.BannerUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

@Route(path = RouterFragmentPath.Home.PAGER_HOME)
public class HomeFragment extends LBaseFragment<HomeViewModel> implements IEventBus {

    private ArticleListAdapter articleListAdapter;
    private PageInfo pageInfo;
    private boolean isLoading = true;

    /**
     * 点击收藏后将点击事件上锁,等接口有相应结果再解锁
     * 避免重复点击产生的bug
     */
    private boolean lockCollectClick = true;

    //记录当前点击收藏的position
    private int currentPosition = 0;
    private View rootview;
    private RecyclerView recy;
    private AppBarLayout appBar;
    private View nestscrollview;
    private View ivSearch;
    private View cl;
    private SmartRefreshLayout refresh;
    private Banner banner;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initImmersionBar();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.home_fragment_home;
    }

    @SuppressLint("NewApi")
    @Override
    protected void initView() {
        super.initView();
        rootview = getView().findViewById(R.id.rootview);
        recy = getView().findViewById(R.id.recy);
        appBar = getView().findViewById(R.id.appBar);
        nestscrollview = getView().findViewById(R.id.nestscrollview);
        ivSearch = getView().findViewById(R.id.ivSearch);
        cl = getView().findViewById(R.id.cl);
        refresh = getView().findViewById(R.id.refresh);
        banner = getView().findViewById(R.id.banner);
        pageInfo = new PageInfo();
        setLoadSir(rootview);
        recy.setLayoutManager(new LinearLayoutManager(getActivity()));
        recy.addItemDecoration(new CustomItemDecoration(getActivity(),
                CustomItemDecoration.ItemDecorationDirection.VERTICAL_LIST, R.drawable.linear_split_line));
        articleListAdapter = new ArticleListAdapter(null);
        recy.setAdapter(articleListAdapter);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            cl.setElevation(10f);
//            llRadius.setElevation(20f);
//            recy.setNestedScrollingEnabled(false);
//        }

        //解决swiperefresh与scrollview滑动冲突问题
        appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            /**
             *   verticalOffset == 0 时候也就是Appbarlayout完全展开的时候，事件交给swiperefresh
             */
//            swipe.setEnabled(verticalOffset == 0);
        });

        nestscrollview.getViewTreeObserver().addOnScrollChangedListener(() -> {
            /**
             * 原理如上
             */
//            swipe.setEnabled(nestscrollview.getScrollY() == 0);
        });

        nestscrollview.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            float alpha = 0f;
            if (scrollY > 0) {
                ivSearch.setEnabled(true);
                alpha = (float) scrollY / (float) 300;
            } else {
                ivSearch.setEnabled(false);
            }
            cl.setAlpha(alpha);
        });

        refresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                pageInfo.resetZero();
                loadData();
            }
        });

        loadData();

        ivSearch.setOnClickListener(v -> {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), ivSearch, "search");
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            ActivityCompat.startActivity(getActivity(), intent, optionsCompat.toBundle());
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mViewModel.mBannerListMutable.observe(this, bannerEntities -> {
            if (refresh.getState().isOpening) {
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }

            if (bannerEntities != null && bannerEntities.size() > 0) {
                banner.setAdapter(new HomeHeadBannerAdapter(bannerEntities))
                        .setIndicator(new CircleIndicator(getActivity()))
                        .setIndicatorGravity(IndicatorConfig.Direction.RIGHT)
                        .setIndicatorMargins(new IndicatorConfig.Margins(0, 0,
                                BannerConfig.INDICATOR_SELECTED_WIDTH, (int) BannerUtils.dp2px(40)))
                        .setOnBannerListener((data, position) -> {
                            WebViewActivity.start(getActivity(), bannerEntities.get(position).getTitle(), bannerEntities.get(position).getUrl());
                        });
            }
        });

        mViewModel.mArticleListMutable.observe(this, datasBeans -> {
            if (refresh.getState().isOpening) {
                refresh.finishRefresh();
            }

            if (isLoading)
                showContent();
            articleListAdapter.setList(datasBeans);
            pageInfo.nextZeroPage();
            isLoading = false;
        });

        //加载更多
        mViewModel.mArticleMutable.observe(this, articleEntity -> {
            if (refresh.getState().isOpening)
                refresh.finishLoadMore();
            if (null != articleEntity) {
                List<ArticleEntity.DatasBean> entityDatas = articleEntity.getDatas();
                if (null != entityDatas && entityDatas.size() > 0) {
                    if (!MmkvHelper.getInstance().getshowTopArticle() && pageInfo.isZeroPage()) {
                        showContent();
                        articleListAdapter.setList(entityDatas);
                    } else {
                        articleListAdapter.addData(entityDatas);
                    }
                    pageInfo.nextZeroPage();
                }
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

        articleListAdapter.setOnItemClickListener((adapter, view, position) -> {
            ArticleEntity.DatasBean datasBean = articleListAdapter.getData().get(position);
            WebViewActivity.start(getActivity(), datasBean.getTitle(), datasBean.getLink());
        });

        articleListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.iv_collect) {
                collectArticle(position);
            }
        });
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

    private void loadData() {
        if (pageInfo.isZeroPage()) {
            mViewModel.getBanner();
            if (MmkvHelper.getInstance().getshowTopArticle()) {
                mViewModel.getArticleMultiList(pageInfo.mPage);
            } else {  //隐藏置顶文章
                mViewModel.getArticleList(pageInfo.mPage);
            }
        } else {
            mViewModel.getArticleList(pageInfo.mPage);
        }
    }

    @Override
    protected void onRetryBtnClick() {

    }

    @Subscribe
    public void onEvent(SettingEvent settingEvent) {
        if (isDetached()) {
            return;
        }

        if (settingEvent != null) {

            if (settingEvent.isShowTopArticle()) { //置顶
                pageInfo.resetZero();
                if (MmkvHelper.getInstance().getshowTopArticle()) {
                    mViewModel.getArticleMultiList(pageInfo.mPage);
                } else {  //隐藏
//                removeTopItems();
                    mViewModel.getArticleList(pageInfo.mPage);
                }
            }
        }
    }

    private void removeTopItems() {
        List<ArticleEntity.DatasBean> data = articleListAdapter.getData();
//        for (int i = 0; i < data.size(); i++) {
//            ArticleEntity.DatasBean datasBean = data.get(i);
//            if (datasBean.getItemType() == C.ARTICLE_ITEM) {
//                if (datasBean.getType() == 1) { //说明是置顶文章,此时移除置顶文章
//                    removeIndex(data, i);
//                }
//            }
//        }

//        int from = -1;
//        int count = 0;
//        for (int i = 0; i < data.size(); i++) {
//            ArticleEntity.DatasBean datasBean = data.get(i);
//            if (from < 0) {
//                if (datasBean.getItemType() == C.ARTICLE_ITEM) {
//                    if (datasBean.isTop()) {
//                        from = i;
//                    }
//                }
//            }
//
//            if (from >= 0) {
//                if (datasBean.getItemType() == C.ARTICLE_ITEM_PIC) {
//                    if (!datasBean.isTop()) {
//                        break;
//                    }
//                }
//                count++;
//            }
//        }
//        if (from >= 0) {
//            for (int i = 0; i < count; i++) {
//                articleListAdapter.removeAt(from);
//            }
//        }
    }

//    private void removeIndex(List<ArticleEntity.DatasBean> data, int position) {
//        data.remove(position);
//        articleListAdapter.notifyItemRemoved(position);
//        articleListAdapter.notifyItemRangeChanged(position, data.size());
//    }
}
