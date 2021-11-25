package com.ling.mine.fragment;

import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ling.aop.checklogin.annotation.CheckLogin;
import com.ling.base.fragment.LBaseFragment;
import com.ling.base.router.RouterFragmentPath;
import com.ling.common.bean.UserInfo;
import com.ling.common.storage.MmkvHelper;
import com.ling.mine.R;
import com.ling.mine.activity.AboutMeActivity;
import com.ling.mine.activity.LeaderboardActivity;
import com.ling.mine.activity.MyCollectArticleActivity;
import com.ling.mine.activity.MyIntergralActivity;
import com.ling.mine.activity.MyShareActivity;
import com.ling.mine.activity.OpenSourceProjActivity;
import com.ling.mine.activity.SettingActivity;
import com.ling.mine.viewmodel.MineViewModel;
import com.ling.mine.widget.WaveView;

@Route(path = RouterFragmentPath.Mine.PAGER_MINE)
public class MineFragment extends LBaseFragment<MineViewModel> {
    private UserInfo mUserInfo;
    private TextView tvId;
    private TextView tvLevel;
    private TextView tvIntergralVal;
    private TextView tvName;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar
                .statusBarDarkFont(false)
                .init();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine_fragment;
    }

    @Override
    protected void initView() {
        super.initView();
        ConstraintLayout cl = getView().findViewById(R.id.cl);
        WaveView waveview = getView().findViewById(R.id.waveview);
        tvId = getView().findViewById(R.id.tv_id);
        tvLevel = getView().findViewById(R.id.tv_level);
        tvIntergralVal = getView().findViewById(R.id.tv_intergral_val);
        tvName = getView().findViewById(R.id.tv_name);
        getView().findViewById(R.id.iv_set).setOnClickListener(v -> clickSet());
        tvName.setOnClickListener(v -> clickGoLogin());
        getView().findViewById(R.id.tv_leaderboard).setOnClickListener(v -> clickIntergral());
        getView().findViewById(R.id.cl_intergral).setOnClickListener(v -> clickMyIntergral());
        getView().findViewById(R.id.cl_collect).setOnClickListener(v -> clickMyCollect());
        getView().findViewById(R.id.cl_share).setOnClickListener(v -> clickMyShare());
        getView().findViewById(R.id.cl_article).setOnClickListener(v -> clickMyArticle());
        getView().findViewById(R.id.cl_read_history).setOnClickListener(v -> clickReadHistory());
        getView().findViewById(R.id.cl_about_author).setOnClickListener(v -> clickAboutAuthor());
        getView().findViewById(R.id.cl_open_proj).setOnClickListener(v -> clickOpenSource());

        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) cl.getLayoutParams();
        waveview.setOnWaveAnimationListener(y -> {
            lp.setMargins(0, 0, 0, (int) y + 90);
            cl.setLayoutParams(lp);
        });
    }

    @Override
    protected void initData() {
        super.initData();

        mViewModel.mIntegralLiveData.observe(this, userInfo -> {
            tvId.setText("ID." + userInfo.getUserId());
            tvLevel.setText("lv." + userInfo.getLevel());
            tvIntergralVal.setText("当前积分：" + userInfo.getCoinCount());
            mUserInfo.setLevel(userInfo.getLevel());
            mUserInfo.setCoinCount(userInfo.getCoinCount());
            MmkvHelper.getInstance().saveUserInfo(mUserInfo);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mUserInfo = MmkvHelper.getInstance().getUserInfo();

        if (mUserInfo == null) {
            tvName.setText("未登录");
            tvId.setVisibility(View.GONE);
            tvLevel.setVisibility(View.GONE);
        } else {
            tvName.setText(mUserInfo.getUsername());
            tvId.setVisibility(View.VISIBLE);
            tvLevel.setVisibility(View.VISIBLE);
            loadUserInfo();
        }

    }

    private void loadUserInfo() {
        mViewModel.getIntegral();
    }




    @CheckLogin()
    public void clickSet() {
        SettingActivity.start(getActivity());
    }

    @CheckLogin()
    public void clickGoLogin() {

    }

    @CheckLogin()
    public void clickIntergral() {
        LeaderboardActivity.start(getActivity(), mUserInfo);
    }

    @CheckLogin()
    public void clickMyIntergral() {
        MyIntergralActivity.start(getActivity());
    }

    @CheckLogin()
    public void clickMyCollect() {
        MyCollectArticleActivity.start(getActivity());
    }

    @CheckLogin()
    public void clickMyShare() {
        MyShareActivity.start(getActivity());
    }

    @CheckLogin()
    public void clickMyArticle() {

    }

    @CheckLogin()
    public void clickReadHistory() {

    }

    public void clickOpenSource() {
        OpenSourceProjActivity.start(getActivity());
    }

    @CheckLogin()
    public void clickAboutAuthor() {
        AboutMeActivity.start(getActivity());
    }
}
