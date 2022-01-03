package com.ling.officialaccount.api;

import com.ling.common.bean.ArticleEntity;
import com.ling.common.bean.ProjectTabBean;
import com.ling.network.bean.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ling on 2020/08/21 11:32
 */
public interface OfficialAccountService {

    //获取公众号作者 tab
    @GET("wxarticle/chapters/json")
    Observable<BaseResponse<List<ProjectTabBean>>> getAuthorTabList();

    //获取公众号列表
    @GET("wxarticle/list/{id}/{pageNum}/json")
    Observable<BaseResponse<ArticleEntity>> getAuthorArticleList(@Path("id") int id,
                                                                 @Path("pageNum") int pageNum);
}
