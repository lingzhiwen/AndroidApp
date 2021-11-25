package com.ling.project.fragment;


import android.os.Build;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.ling.base.fragment.LBaseFragment;
import com.ling.base.router.RouterFragmentPath;
import com.ling.common.adapter.TabNavigatorAdapter;
import com.ling.common.adapter.TabPagerAdapter;
import com.ling.common.bean.ProjectTabBean;
import com.ling.common.callback.OnTabClickListener;
import com.ling.common.callback.TabPagerListener;
import com.ling.common.storage.MmkvHelper;
import com.ling.network.constant.C;
import com.ling.project.R;
import com.ling.project.viewmodel.ProjectViewModel;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;

@Route(path = RouterFragmentPath.Project.PAGER_PROJECT)
public class ProjectFragment extends LBaseFragment<ProjectViewModel>
        implements TabPagerListener, OnTabClickListener {

    private List<ProjectTabBean> tabBeanList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private TabPagerAdapter tabPagerAdapter;
    private View fl;
    private ViewPager vp;
    private MagicIndicator magicInticator;

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
        return R.layout.fragment_project_fragment;
    }

    @Override
    protected void initView() {
        super.initView();
        fl = getView().findViewById(R.id.fl);
        vp = getView().findViewById(R.id.vp);
        magicInticator = getView().findViewById(R.id.magic_inticator);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fl.setElevation(10f);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        List<ProjectTabBean> mProjTabList = MmkvHelper.getInstance().getTList(C.PROJECT_TAB, ProjectTabBean.class);
        if (null != mProjTabList && mProjTabList.size() > 0) {
            tabBeanList.clear();
            tabBeanList.addAll(mProjTabList);
            for (int i = 0; i < mProjTabList.size(); i++) {
                titleList.add(mProjTabList.get(i).getName());
            }
            initFragment();
        }
    }

    private void initFragment() {
        tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager(), tabBeanList.size());
        tabPagerAdapter.setListener(this::getFragment);
        vp.setAdapter(tabPagerAdapter);
        vp.setOffscreenPageLimit(tabBeanList.size() );

        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        TabNavigatorAdapter tabNavigatorAdapter = new TabNavigatorAdapter(titleList);
        tabNavigatorAdapter.setOnTabClickListener(this::onTabClick);
        commonNavigator.setAdapter(tabNavigatorAdapter);
        magicInticator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicInticator, vp);

        //        Observable.fromIterable(null).map(SelectHomeListFragment::newInstance).toList()
//                .map(fragments -> new FragmentAdapter(getSupportFragmentManager()).newInstance(getSupportFragmentManager(), fragments, mCategotyTxt))
//                .subscribe(mFragmentAdapter -> binding.viewpager.setAdapter(mFragmentAdapter));

    }

    @Override
    public Fragment getFragment(int position) {
        return ProjectListFragment.newInstance(tabBeanList.get(position).getId());
    }

    @Override
    public void onTabClick(View view, int index) {
        vp.setCurrentItem(index);
    }
}
