package com.ling.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gyf.immersionbar.ImmersionBar;
import com.ling.base.activity.LBaseActivity;
import com.ling.common.adapter.ArticleListAdapter;
import com.ling.common.bean.ArticleEntity;
import com.ling.common.bean.UserInfo;
import com.ling.common.bean.page.PageInfo;
import com.ling.common.utils.CustomItemDecoration;
import com.ling.common.view.CommonHeadTitle;
import com.ling.mine.R;
import com.ling.mine.viewmodel.MineViewModel;
import com.ling.network.constant.C;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;

public class UserCenterActivity extends LBaseActivity<MineViewModel> implements OnLoadMoreListener {

    private String userId;
    private PageInfo pageInfo;
    private ArticleListAdapter articleListAdapter;
    private int currentPosition = 0;
    /**
     * 点击收藏后将点击事件上锁,等接口有相应结果再解锁
     * 避免重复点击产生的bug
     */
    private boolean lockCollectClick = true;
    private RecyclerView recy;
    private SmartRefreshLayout smart;
    private CommonHeadTitle titleview;
    private MotionLayout co;
    private View ivBackLeft;
    private TextView tv_name;
    private TextView tv_id;
    private TextView tv_rank;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar
                .statusBarColorInt(getResources().getColor(R.color.colorPrimary))
                .statusBarDarkFont(false)
                .init();
    }

    public static void start(Context context, String userId) {
        Intent intent = new Intent(context, UserCenterActivity.class);
        intent.putExtra(C.USERID, userId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_usercenter1;
    }

    @Override
    protected void initView() {
        super.initView();
        titleview = findViewById(R.id.titleview);
        recy = findViewById(R.id.recy);
        smart = findViewById(R.id.smart);
        co = findViewById(R.id.co);
        tv_name = findViewById(R.id.tv_name);
        tv_id = findViewById(R.id.tv_id);
        tv_rank = findViewById(R.id.tv_rank);
        ivBackLeft = findViewById(R.id.iv_back_left);
        Intent intent = getIntent();
        if (null != intent) {
            userId = intent.getStringExtra(C.USERID);
        }
        pageInfo = new PageInfo();
        recy.setLayoutManager(new LinearLayoutManager(this));
        recy.addItemDecoration(new CustomItemDecoration(this,
                CustomItemDecoration.ItemDecorationDirection.VERTICAL_LIST, R.drawable.linear_split_line));
        recy.setAdapter(articleListAdapter = new ArticleListAdapter(null));
        co.setPadding(0, ImmersionBar.getStatusBarHeight(this), 0, 0);
        setLoadSir(co);
        loadData();
    }

    @Override
    protected void initData() {
        super.initData();

        mViewModel.userCenterLiveData.observe(this, userCenter -> {
            if (userCenter != null) {
                ArticleEntity shareArticles = userCenter.getShareArticles();
                List<ArticleEntity.DatasBean> datas = shareArticles.getDatas();
                pageInfo.nextPage();
                if (shareArticles.getCurPage() == 1) {
                    UserInfo coinInfo = userCenter.getCoinInfo();
                    tv_name.setText(coinInfo.getUsername());
                    tv_id.setText(coinInfo.getUserId());
                    tv_rank.setText(coinInfo.getRank()+"积分:"+coinInfo.getCoinCount());
                    if (datas != null && datas.size() > 0) {
                        showContent();
                        articleListAdapter.setList(datas);
                    } else {
                        showEmpty();
                    }
                } else {
                    articleListAdapter.addData(datas);
                }

                if (shareArticles.isOver()) {
                    smart.finishLoadMoreWithNoMoreData();
                }
                smart.finishRefresh(true);
                smart.finishLoadMore(true);
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

        mViewModel.mCollectMutable.observe(this, baseResponse -> {
            lockCollectClick = true;
            if (baseResponse.getErrorCode() == 0) {
                if (currentPosition < articleListAdapter.getData().size()) {
                    articleListAdapter.getData().get(currentPosition).setCollect(true);
                    articleListAdapter.notifyItemChanged(currentPosition, C.REFRESH_COLLECT);
                }
            }
        });

        ivBackLeft.setOnClickListener(view -> finish());
        smart.setOnLoadMoreListener(this);
        articleListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.iv_collect) {
                collectArticle(position);
            }
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadData();
    }

    private void loadData() {
        mViewModel.getUserCenter(Integer.parseInt(userId), pageInfo.page);
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

}