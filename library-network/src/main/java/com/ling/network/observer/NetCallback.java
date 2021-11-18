package com.ling.network.observer;


import com.ling.network.bean.BaseResponse;

public interface NetCallback<T extends BaseResponse> {

    void success(T response);

    void error(String msg);
}
