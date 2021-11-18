package com.ling.square.fragment;

import android.os.Build;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ling.base.fragment.BaseFragment;
import com.ling.base.router.RouterFragmentPath;
import com.ling.base.viewmodel.BaseViewModel;
import com.ling.common.adapter.TabNavigatorAdapter;
import com.ling.common.adapter.TabPagerAdapter;
import com.ling.common.callback.OnTabClickListener;
import com.ling.common.callback.TabPagerListener;
import com.ling.square.R;
import com.ling.square.databinding.FragmentSquareFragmentBinding;

import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.Arrays;

@Route(path = RouterFragmentPath.Square.PAGER_SQUARE)
public class SquareFragment extends BaseFragment<FragmentSquareFragmentBinding, BaseViewModel>
        implements TabPagerListener, OnTabClickListener {

    private TabPagerAdapter tabPagerAdapter;

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
        return R.layout.fragment_square_fragment;
    }

    @Override
    protected void initView() {
        super.initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mViewDataBinding.fl.setElevation(10f);
        }

        String[] stringArray = getResources().getStringArray(R.array.square_title);
        tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager(), stringArray.length);
        tabPagerAdapter.setListener(this::getFragment);
        mViewDataBinding.vp.setAdapter(tabPagerAdapter);
        mViewDataBinding.vp.setOffscreenPageLimit(stringArray.length);

        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        TabNavigatorAdapter tabNavigatorAdapter = new TabNavigatorAdapter(Arrays.asList(stringArray));
        tabNavigatorAdapter.setOnTabClickListener(this::onTabClick);
        commonNavigator.setAdapter(tabNavigatorAdapter);
        mViewDataBinding.magicInticator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mViewDataBinding.magicInticator, mViewDataBinding.vp);
    }

    @Override
    public Fragment getFragment(int position) {
        switch (position) {
            case 0:
                return SystemFragment.newInstance();
            default:
                return NavigationFragment.newInstance();
        }
    }

    @Override
    public void onTabClick(View view, int index) {
        mViewDataBinding.vp.setCurrentItem(index);
    }
}
