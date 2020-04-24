package com.yq.news.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 地区
 */
public class CityInfo {


    /**
     * id : 7
     * name : 上海
     * code : 310200
     * pid : 6
     */

    private int id;
    private String name;
    private int code;
    private int pid;
    protected List<CityInfo> children = new ArrayList<>();

    public List<CityInfo> getChildren() {
        return children;
    }
    public void add(CityInfo bean) {
        children.add(bean);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
