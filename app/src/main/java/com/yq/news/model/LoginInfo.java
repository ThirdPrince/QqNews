package com.yq.news.model;


import java.io.Serializable;

/**
 * 登录信息
 */
public class LoginInfo implements Serializable {


    /**
     * status : 200
     * msg : 请求成功
     * data : {"id":2,"account":"test1","telphone":"13911111111","email":"test@163.com","state":1,"remark":null,"deptid":2,
     * "deptname":null,"roleid":2,"rolename":null,"realname":"test1","applytime":1569744311,"equipmentcount":2,"equipmentlist":null}
     */

    private int status;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * id : 2
         * account : test1
         * telphone : 13911111111
         * email : test@163.com
         * state : 1
         * remark : null
         * deptid : 2
         * deptname : null
         * roleid : 2
         * rolename : null
         * realname : test1
         * applytime : 1569744311
         * equipmentcount : 2
         * equipmentlist : null
         */

        private int id;
        private String account;
        private String telphone;
        private String email;
        private int state;
        private Object remark;
        private int deptid;
        private Object deptname;
        private int roleid;
        private Object rolename;
        private String realname;
        private int applytime;
        private int equipmentcount;
        private Object equipmentlist;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getTelphone() {
            return telphone;
        }

        public void setTelphone(String telphone) {
            this.telphone = telphone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public int getDeptid() {
            return deptid;
        }

        public void setDeptid(int deptid) {
            this.deptid = deptid;
        }

        public Object getDeptname() {
            return deptname;
        }

        public void setDeptname(Object deptname) {
            this.deptname = deptname;
        }

        public int getRoleid() {
            return roleid;
        }

        public void setRoleid(int roleid) {
            this.roleid = roleid;
        }

        public Object getRolename() {
            return rolename;
        }

        public void setRolename(Object rolename) {
            this.rolename = rolename;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public int getApplytime() {
            return applytime;
        }

        public void setApplytime(int applytime) {
            this.applytime = applytime;
        }

        public int getEquipmentcount() {
            return equipmentcount;
        }

        public void setEquipmentcount(int equipmentcount) {
            this.equipmentcount = equipmentcount;
        }

        public Object getEquipmentlist() {
            return equipmentlist;
        }

        public void setEquipmentlist(Object equipmentlist) {
            this.equipmentlist = equipmentlist;
        }
    }
}
