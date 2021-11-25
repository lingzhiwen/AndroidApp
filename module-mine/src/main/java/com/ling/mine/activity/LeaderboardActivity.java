package com.ling.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ling.base.activity.LBaseActivity;
import com.ling.common.bean.UserInfo;
import com.ling.common.bean.page.PageInfo;
import com.ling.common.view.CommonHeadTitle;
import com.ling.mine.R;
import com.ling.mine.adapter.LeaderboardAdapter;
import com.ling.mine.viewmodel.MineViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

/**
 * Created by zjp on 2020/7/15 22:15.
 */
public class LeaderboardActivity extends LBaseActivity<MineViewModel> implements OnRefreshLoadMoreListener {
    private PageInfo pageInfo;
    private LeaderboardAdapter leaderboardAdapter;
    private UserInfo userInfo;
    private RecyclerView recy;
    private SmartRefreshLayout refresh;
    private View cl_item_rank;
    private TextView tv_rank;
    private TextView tv_name;
    private TextView tv_integral;
    private View clContent;

    public static void start(Context context, UserInfo userInfo) {
        Intent intent = new Intent(context, LeaderboardActivity.class);
        intent.putExtra("userInfo", userInfo);
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
        return R.layout.activity_leader_board;
    }

    @Override
    protected void initView() {
        super.initView();
        CommonHeadTitle titleview = findViewById(R.id.titleview);
        recy = findViewById(R.id.recy);
        refresh = findViewById(R.id.refresh);
        cl_item_rank = findViewById(R.id.cl_item_rank);
        tv_rank = findViewById(R.id.tv_rank);
        tv_name = findViewById(R.id.tv_name);
        tv_integral = findViewById(R.id.tv_integral);
        clContent = findViewById(R.id.cl_content);

        titleview.setTitle("积分排行榜");
        pageInfo = new PageInfo();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            titleview.setElevation(10f);
        }

        Intent intent = getIntent();
        if (intent != null) {
            userInfo = intent.getParcelableExtra("userInfo");
        }
        setLoadSir(clContent);
        loadData();

        recy.setLayoutManager(new LinearLayoutManager(this));
        recy.setAdapter(leaderboardAdapter = new LeaderboardAdapter());
        refresh.setOnRefreshLoadMoreListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        cl_item_rank.setVisibility(userInfo != null ? View.VISIBLE : View.GONE);
        if (userInfo != null) {
            tv_rank.setText(userInfo.getRank());
            tv_name.setText(userInfo.getUsername());
            tv_integral.setText(userInfo.getCoinCount());
        }
        mViewModel.leaderBoardLiveData.observe(this, leaderboards -> {
            if (refresh.getState().isOpening) {
                refresh.finishRefresh();
                refresh.finishLoadMore();
            }
            if (leaderboards != null && leaderboards.size() > 0) {
                if (pageInfo.isFirstPage()) {
                    showContent();
                    leaderboardAdapter.setList(leaderboards);
                } else {
                    leaderboardAdapter.addData(leaderboards);
                }
                pageInfo.nextPage();
            } else {
                if (pageInfo.isFirstPage()) {
                    showEmpty();
                } else {
                    leaderboardAdapter.addData(leaderboards);
                    refresh.finishLoadMoreWithNoMoreData();
                }
            }
        });

        leaderboardAdapter.setOnItemClickListener((adapter, view, position) -> UserCenterActivity.start(LeaderboardActivity.this, leaderboardAdapter.getData().get(position).getUserId()));
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

    private void loadData() {
        mViewModel.getRankList(pageInfo.page);
    }
}
