package com.ling.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;

import com.blankj.utilcode.util.ScreenUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ling.base.activity.LBaseActivity;
import com.ling.common.storage.MmkvHelper;
import com.ling.common.view.CommonHeadTitle;
import com.ling.mine.R;
import com.ling.mine.viewmodel.MineViewModel;

public class AddArticleActivity extends LBaseActivity<MineViewModel> {

    private AppCompatEditText etShareTitle;
    private AppCompatEditText etShareUrl;

    @Override
    protected void initImmersionBar() {
        super.initImmersionBar();
        mImmersionBar
                .statusBarDarkFont(true)
                .init();
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, AddArticleActivity.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_addarticle;
    }

    @Override
    protected void initView() {
        super.initView();
        CommonHeadTitle titleview = findViewById(R.id.titleview);
        TextView shareUsername = findViewById(R.id.share_username);
        etShareTitle = findViewById(R.id.et_share_title);
        etShareUrl =findViewById(R.id.et_share_url);
        titleview.setTitle("添加文章");
        titleview.getIvRight().setImageResource(R.mipmap.ic_guize);
        titleview.setIvRightVisible(View.VISIBLE);
        shareUsername.setText(MmkvHelper.getInstance().getUserInfo().getUsername());
        titleview.getIvRight().setOnClickListener(view -> {

            View dialogView = LayoutInflater.from(AddArticleActivity.this).inflate(R.layout.bottom_sheet_dialog_warm_tip, null);
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(AddArticleActivity.this, R.style.BottomSheetDialog);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.setCancelable(true);
            bottomSheetDialog.setCanceledOnTouchOutside(true);
            ViewGroup parent = (ViewGroup) dialogView.getParent();
            BottomSheetBehavior<ViewGroup> behavior = BottomSheetBehavior.from((ViewGroup) parent);
            // 设置bottomSheet的折叠高度
            // 比如设置成屏幕的1/2（不包括状态栏）
            behavior.setPeekHeight(ScreenUtils.getScreenHeight() / 2);
            behavior.setHideable(true);

            ViewGroup.LayoutParams layoutParams = parent.getLayoutParams();
            layoutParams.height = ScreenUtils.getScreenHeight() / 6 * 5;
            parent.setLayoutParams(layoutParams);

            bottomSheetDialog.show();
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mViewModel.mShareArticleMutable.observe(this, baseResponse -> finish());
    }

    public void share(View view) {

        new AlertDialog.Builder(AddArticleActivity.this)
                .setTitle(R.string.tips)
                .setMessage(R.string.share_tips)
                .setPositiveButton(R.string.sure, (dialogInterface, i) -> mViewModel.shareArticle(etShareTitle.getText().toString(), etShareUrl.getText().toString()))
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
