package com.ling.square.fragment;

import android.app.AlertDialog;

import androidx.recyclerview.widget.RecyclerView;

import com.ling.base.fragment.BaseLazyFragment;
import com.ling.common.bean.ProjectTabBean;
import com.ling.square.R;
import com.ling.square.activity.SystemActivity;
import com.ling.square.adapter.SystemListAdapter;
import com.ling.square.databinding.FragmentListSystemBinding;
import com.ling.square.viewmodel.SquareViewModel;

/**
 * Created by zjp on 2020/08/20 14:15
 */
public class SystemFragment extends BaseLazyFragment<SquareViewModel>
        implements SystemListAdapter.OnItemClickListener {

    private SystemListAdapter systemListAdapter;
    private boolean isLoading;
    private RecyclerView recy;

    public static SystemFragment newInstance() {
        return new SystemFragment();
    }

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar
                .statusBarDarkFont(true)
                .init();
    }

    @Override
    protected void initView() {
        super.initView();
        recy = getView().findViewById(R.id.recy);
        recy.setAdapter(systemListAdapter = new SystemListAdapter());
        systemListAdapter.setOnItemClickListener(this::onClick);
    }

    @Override
    protected void initData() {
        super.initData();
        mViewModel.mSystemListMutable.observe(this, projectTabBeans -> {
            if (isLoading)
                showContent();
            if (projectTabBeans != null && projectTabBeans.size() > 0) {
                systemListAdapter.setList(projectTabBeans);
            }
            isLoading = !isLoading;
        });
    }

    @Override
    protected void lazyLoadData() {
        isLoading = true;
        showLoading();
        onRetryBtnClick();
    }

    @Override
    protected void onRetryBtnClick() {
        super.onRetryBtnClick();
        mViewModel.getSystemList();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_list_system;
    }

    @Override
    public void onClick(ProjectTabBean projTabBean) {
        SystemActivity.start(getActivity(), projTabBean.getName(),projTabBean.getCourseId());
    }
}
