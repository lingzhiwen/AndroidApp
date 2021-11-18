package com.ling.mine.fragment;

import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ling.aop.checklogin.annotation.CheckLogin;
import com.ling.base.fragment.BaseFragment;
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
import com.ling.mine.databinding.FragmentMineFragmentBinding;
import com.ling.mine.viewmodel.MineViewModel;

@Route(path = RouterFragmentPath.Mine.PAGER_MINE)
public class MineFragment extends BaseFragment<FragmentMineFragmentBinding, MineViewModel> {

    private UserInfo mUserInfo;

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
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) mViewDataBinding.cl.getLayoutParams();
        mViewDataBinding.waveview.setOnWaveAnimationListener(y -> {
            lp.setMargins(0, 0, 0, (int) y + 90);
            mViewDataBinding.cl.setLayoutParams(lp);
        });
    }

    @Override
    protected void initData() {
        super.initData();

        mViewModel.mIntegralLiveData.observe(this, userInfo -> {
            mViewDataBinding.tvId.setText("ID." + userInfo.getUserId());
            mViewDataBinding.tvLevel.setText("lv." + userInfo.getLevel());
            mViewDataBinding.tvIntergralVal.setText("当前积分：" + userInfo.getCoinCount());
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
            mViewDataBinding.tvName.setText("未登录");
            mViewDataBinding.tvId.setVisibility(View.GONE);
            mViewDataBinding.tvLevel.setVisibility(View.GONE);
        } else {
            mViewDataBinding.tvName.setText(mUserInfo.getUsername());
            mViewDataBinding.tvId.setVisibility(View.VISIBLE);
            mViewDataBinding.tvLevel.setVisibility(View.VISIBLE);
            loadUserInfo();
        }

        mViewDataBinding.setEventlistener(new EventListener(mUserInfo));
    }

    private void loadUserInfo() {
        mViewModel.getIntegral();
    }

    public class EventListener {

        private UserInfo userInfo;

        public EventListener(UserInfo userInfo) {
            this.userInfo = userInfo;
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
            LeaderboardActivity.start(getActivity(), userInfo);
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
}
