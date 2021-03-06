package com.ling.login.api;

import com.ling.common.bean.ProjectTabBean;
import com.ling.common.bean.UserInfo;
import com.ling.network.bean.BaseResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by ling on 2020/5/28 14:34
 */
public interface LoginService {

    //注册
    @FormUrlEncoded
    @POST("user/register")
    Observable<BaseResponse<UserInfo>> register(@FieldMap Map<String, Object> map);

    //登录
    @FormUrlEncoded
    @POST("user/login")
    Observable<BaseResponse<UserInfo>> login(@FieldMap Map<String, Object> map);

    //获取项目tab
    @GET("/project/tree/json")
    Observable<BaseResponse<List<ProjectTabBean>>> getProjectTab();
}
