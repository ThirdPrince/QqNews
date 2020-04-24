package com.yq.news.model;

import java.io.Serializable;

public class IndustryBean implements Serializable {


    /**
     * id : 100000
     * name : 公安
     */

    private int id;
    private String name;

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
}
