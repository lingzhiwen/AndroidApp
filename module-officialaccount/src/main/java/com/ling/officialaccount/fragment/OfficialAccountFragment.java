package com.ling.officialaccount.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.gyf.immersionbar.ImmersionBar;
import com.ling.base.fragment.LBaseFragment;
import com.ling.base.router.RouterFragmentPath;
import com.ling.common.bean.ArticleEntity;
import com.ling.common.bean.ProjectTabBean;
import com.ling.common.bean.page.PageInfo;
import com.ling.common.ui.WebViewActivity;
import com.ling.common.utils.CustomItemDecoration;
import com.ling.officialaccount.R;
import com.ling.officialaccount.adapter.CategoryAdapter;
import com.ling.officialaccount.adapter.OfficialAccountListAdapter;
import com.ling.officialaccount.viewmodel.OfficialAccountViewModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

@Route(path = RouterFragmentPath.OfficialAccount.PAGER_OFFICIALACCOUNT)
public class OfficialAccountFragment extends LBaseFragment<OfficialAccountViewModel> {

    private CategoryAdapter categoryAdapter;
    private OfficialAccountListAdapter officialAccountListAdapter;
    private PageInfo pageInfo;
    private int id = 0;
    private List<ArticleEntity.DatasBean> shareArticles = new ArrayList<>();
    private View viewStatus;
    private TextView tvTitle;
    private RefreshLayout refresh;
    private RecyclerView recy;
    private RecyclerView recyCategory;
    private OfficialAccountViewModel vm;

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
        return R.layout.fragment_official_account_fragment;
    }

    @Override
    protected void initView() {
        super.initView();
        viewStatus = getView().findViewById(R.id.view_status);
        tvTitle = getView().findViewById(R.id.tv_title);
        refresh = getView().findViewById(R.id.refresh);
        recy = getView().findViewById(R.id.recy);
        recyCategory = getView().findViewById(R.id.recy_category);
        ViewGroup.LayoutParams layoutParams = viewStatus.getLayoutParams();
        layoutParams.height = ImmersionBar.getStatusBarHeight(getActivity());
        viewStatus.setLayoutParams(layoutParams);
        tvTitle.setText("公众号");
        setVm(mViewModel);
        initCategory();
        initArticleList();
        pageInfo = new PageInfo();
        mViewModel.getAuthorTabList();
//
    }

    @Override
    protected void initData() {
        super.initData();
//        mViewModel.mObjMutable.observe(this, objects -> {
//            if (objects == null || objects.size() == 0) {
//                return;
//            }
//            if (objects.get(0) instanceof ProjectTabBean) {
//
//
//            } else if (objects.get(1) instanceof ArticleEntity) {
//            }
//        });

        mViewModel.mProjectListMutable.observe(this, projectTabBeans -> {
            if (null != projectTabBeans && projectTabBeans.size() > 0) {
                projectTabBeans.get(0).setSelect(true);
                categoryAdapter.setList(projectTabBeans);
                loadOfficialAccountList(projectTabBeans.get(0).getId());
            }
        });

        mViewModel.mArticleMutable.observe(this, articleEntity -> {
            officialAccountListAdapter.getData().clear();
            if (null != articleEntity) {
                List<ArticleEntity.DatasBean> shareArticles = articleEntity.getDatas();
                pageInfo.nextPage();
                if (articleEntity.getCurPage() == 1) {
                    recy.smoothScrollToPosition(0);
                    showContent();
                    if (shareArticles != null && shareArticles.size() > 0) {
                        showContent();
                        officialAccountListAdapter.setList(shareArticles);
                    } else {
                        showEmpty();
                    }
                } else {
                    officialAccountListAdapter.addData(shareArticles);
                }

                if (articleEntity.isOver()) {
                    refresh.finishLoadMoreWithNoMoreData();
                }
                refresh.finishRefresh(true);
                refresh.finishLoadMore(true);
            }
        });

        categoryAdapter.setOnItemClickListener((adapter, view, position) -> {
            List<ProjectTabBean> adapterData = categoryAdapter.getData();
            for (ProjectTabBean ptb : adapterData) {
                ptb.setSelect(false);
            }
            ProjectTabBean projectTabBean = adapterData.get(position);
            projectTabBean.setSelect(true);
            categoryAdapter.notifyDataSetChanged();
            loadOfficialAccountList(projectTabBean.getId());
        });
    }

    private void loadOfficialAccountList(int id) {
        pageInfo.reset();
        mViewModel.getAuthorArticleList(id, pageInfo.page);
    }

    private void initCategory() {
        recyCategory.setAdapter(categoryAdapter = new CategoryAdapter());
    }

    private void initArticleList() {
        recy.addItemDecoration(new CustomItemDecoration(getActivity(),
                CustomItemDecoration.ItemDecorationDirection.VERTICAL_LIST, R.drawable.linear_split_line));
        recy.setAdapter(officialAccountListAdapter = new OfficialAccountListAdapter());

        officialAccountListAdapter.setOnItemClickListener((adapter, view, position) -> {
            ArticleEntity.DatasBean datasBean = officialAccountListAdapter.getData().get(position);
            WebViewActivity.start(getActivity(), datasBean.getTitle(), datasBean.getLink());
        });
    }

    public void setVm(OfficialAccountViewModel vm) {
        this.vm = vm;
    }

    public OfficialAccountViewModel getVm() {
        return vm;
    }
}
