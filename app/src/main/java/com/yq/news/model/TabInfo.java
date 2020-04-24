package com.yq.news.model;

import java.util.List;

/**
 *
 */
public class TabInfo {


    /**
     * status : 200
     * msg : 请求成功
     * data : [{"id":1,"name":"科技","createtime":1571646857,"userid":1,"subscribe":null},{"id":3,"name":"财经","createtime":1571646857,"userid":1,"subscribe":null},{"id":2,"name":"新闻","createtime":1571646857,"userid":1,"subscribe":null},{"id":4,"name":"娱乐","createtime":1571646857,"userid":1,"subscribe":null},{"id":5,"name":"测试","createtime":1571646857,"userid":1,"subscribe":null},{"id":6,"name":"测试2","createtime":1571646857,"userid":1,"subscribe":null},{"id":7,"name":"测试3","createtime":1571646857,"userid":1,"subscribe":null},{"id":8,"name":"测试4","createtime":1571646857,"userid":1,"subscribe":null}]
     */

    private int status;
    private String msg;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * name : 科技
         * createtime : 1571646857
         * userid : 1
         * subscribe : null
         */

        private int id;
        private String name;
        private int createtime;
        private int userid;
        private Object subscribe;

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

        public int getCreatetime() {
            return createtime;
        }

        public void setCreatetime(int createtime) {
            this.createtime = createtime;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public Object getSubscribe() {
            return subscribe;
        }

        public void setSubscribe(Object subscribe) {
            this.subscribe = subscribe;
        }
    }
}
