package com.ling.home.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.util.BannerUtils;
import com.ling.common.utils.GlideUtil;
import com.ling.home.R;
import com.ling.home.bean.BannerEntity;

import java.util.List;

public class HomeHeadBannerAdapter extends BannerAdapter<BannerEntity, HomeHeadBannerAdapter.ImageTitleHolder> {

    public HomeHeadBannerAdapter(List<BannerEntity> bannerEntities) {
        super(bannerEntities);
    }

    @Override
    public ImageTitleHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new ImageTitleHolder(BannerUtils.getView(parent, R.layout.home_head_banner));
    }

    @Override
    public void onBindView(ImageTitleHolder holder, BannerEntity bannerEntity, int position, int size) {
        GlideUtil.getInstance().loadImage(holder.mIvCover, bannerEntity.getImagePath());
        holder.mTvBannerTitle.setText(bannerEntity.getTitle());
    }

    class ImageTitleHolder extends RecyclerView.ViewHolder {

        public AppCompatImageView mIvCover;
        public AppCompatTextView mTvBannerTitle;

        public ImageTitleHolder(@NonNull View view) {
            super(view);
            mIvCover = view.findViewById(R.id.iv_cover);
            mTvBannerTitle = view.findViewById(R.id.tv_banner_title);
        }
    }
}
