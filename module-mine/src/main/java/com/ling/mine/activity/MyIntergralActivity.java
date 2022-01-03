package com.ling.mine.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.ling.base.activity.LBaseActivity;
import com.ling.common.bean.UserInfo;
import com.ling.common.bean.page.PageInfo;
import com.ling.common.storage.MmkvHelper;
import com.ling.common.ui.WebViewActivity;
import com.ling.common.view.CommonHeadTitle;
import com.ling.mine.R;
import com.ling.mine.adapter.MyIntergralAdapter;
import com.ling.mine.viewmodel.MineViewModel;
import com.ling.network.constant.C;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.List;

/**
 * Created by ling on 2020/07/15 17:07
 */
public class MyIntergralActivity extends LBaseActivity<MineViewModel>
        implements OnLoadMoreListener {

    private PageInfo pageInfo;
    private MyIntergralAdapter myIntergralAdapter;
    private RecyclerView rvIntergralList;
    private View rootview;
    private RefreshLayout smartRefresh;
    private AppCompatTextView tvIntegralAnim;
    private CommonHeadTitle titleview;

    public static void start(Context context) {
        context.startActivity(new Intent(context, MyIntergralActivity.class));
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar.statusBarDarkFont(true).init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_intergral;
    }

    @Override
    protected void initView() {
        super.initView();
        titleview = findViewById(R.id.titleview);
        rvIntergralList = findViewById(R.id.rv_intergral_List);
        rootview = findViewById(R.id.rootview);
        smartRefresh = findViewById(R.id.smartRefresh);
        tvIntegralAnim = findViewById(R.id.tvIntegralAnim);

        titleview.setTitle("我的积分");
        titleview.getIvRight().setImageResource(R.mipmap.ic_guize);
        titleview.setIvRightVisible(View.VISIBLE);
        pageInfo = new PageInfo();
        rvIntergralList.setAdapter(myIntergralAdapter = new MyIntergralAdapter());
        setLoadSir(rootview);
        loadData();
    }

    @Override
    protected void initData() {
        super.initData();

        mViewModel.mLeaderBoardMuTable.observe(this, leaderboard -> {
            if (null != leaderboard) {
                List<UserInfo> userInfos = leaderboard.getDatas();
                pageInfo.nextPage();
                if (leaderboard.getCurPage() == 1) {
                    startAnim();//设置"我的积分"动画

                    if (userInfos != null && userInfos.size() > 0) {
                        showContent();
                        myIntergralAdapter.setList(userInfos);
                    } else {
                        showEmpty();
                    }
                } else {
                    myIntergralAdapter.addData(userInfos);
                }

                if (leaderboard.isOver()) {
                    smartRefresh.finishLoadMoreWithNoMoreData();
                }
                smartRefresh.finishLoadMore(true);
            }
        });

        smartRefresh.setOnLoadMoreListener(this);
        titleview.getIvRight().setOnClickListener(view -> {
            WebViewActivity.start(this, "积分规则", C.INTERGRAL_URL);
        });
    }

    private void loadData() {
        mViewModel.getIntegralRecord(pageInfo.page);
    }

    private void startAnim() {
        String coinCount = MmkvHelper.getInstance().getUserInfo().getCoinCount();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, Integer.parseInt(coinCount));
        //播放时长
        valueAnimator.setDuration(C.DURATION);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            //获取改变后的值
            int currentValue = (int) valueAnimator1.getAnimatedValue();
            tvIntegralAnim.setText(currentValue + "");
        });
        valueAnimator.start();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadData();
    }
}
