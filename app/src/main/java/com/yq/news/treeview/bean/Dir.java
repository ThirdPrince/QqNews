package com.yq.news.treeview.bean;

import com.yq.news.R;

import tellh.com.recyclertreeview_lib.LayoutItemType;

/**
 * Created by tlh on 2016/10/1 :)
 */

public class Dir implements LayoutItemType {
    public String dirName;

    public  String id ;

    public Dir(String dirName,String id) {
        this.dirName = dirName;
        this.id= id;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_dir;
    }
}
