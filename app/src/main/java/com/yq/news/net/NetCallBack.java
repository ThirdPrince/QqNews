package com.yq.news.net;

/**
 * 网络请求数据返回
 * @author dhl
 */
public interface NetCallBack {

    void success(String response);
    void failed(String msg);
}
