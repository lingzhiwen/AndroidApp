package com.ling.square.api;

import com.ling.common.bean.ArticleEntity;
import com.ling.common.bean.ProjectTabBean;
import com.ling.network.bean.BaseResponse;
import com.ling.square.bean.NaviBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ling on 2020/08/20 14:19
 */
public interface SquareService {

    // 体系
    @GET("tree/json")
    Observable<BaseResponse<List<ProjectTabBean>>> getSystemList();

    //获取项目list
    @GET("project/list/{pageNum}/json")
    Observable<BaseResponse<ArticleEntity>> getProjectList(@Path("pageNum") int pageNum, @Query("cid") int cid);

    //收藏
    @POST("/lg/collect/{id}/json")
    Observable<BaseResponse> collect(@Path("id") int id);

    //取消收藏
    @POST("lg/uncollect_originId/{id}/json")
    Observable<BaseResponse> unCollect(@Path("id") int id);

    //导航
    @GET("navi/json")
    Observable<BaseResponse<List<NaviBean>>> getNavigation();

}
