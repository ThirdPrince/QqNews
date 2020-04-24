package com.yq.news.model;


import java.util.List;

/**
 * H5  imageList
 * @author dhl
 */
public class H5ImgList {


    /**
     * status : 200
     * msg : 请求成功
     * data : ["/file/ueditor/upload/image/20191108/1573193855493083618.jpg","/file/ueditor/upload/image/20191108/1573193876433011895.png","/file/ueditor/upload/image/20191108/1573193896157072976.jpg"]
     */

    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
