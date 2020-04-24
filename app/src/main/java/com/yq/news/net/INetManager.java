package com.yq.news.net;

import java.io.File;

/**
 * 获取更新信息
 */
public interface INetManager {

    void get(String url, NetCallBack callBack);
    void download(String url, File targetFile, INetDownloadCallBack callBack, Object tag);
    void cancel(Object tag);
}
