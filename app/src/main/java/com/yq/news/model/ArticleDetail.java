package com.yq.news.model;

import java.util.List;


/**
 * 信息详情
 */
public class ArticleDetail {


    /**
     * id : 6
     * title : 大撒发今南京烽火
     * content : 市共和国很多个接口返回接口和高考的家伙可靠的更广阔的和副科级还款卡公司电话客家话可科技南京烽火很困惑客户看
     * createtime : 1571646857
     * userid : 2
     * classid : 3
     * viewnum : 0
     * pointsnum : 0
     * collectionnum : 0
     * state : 0
     * classname : 财经
     * username : test1
     * count : 0
     * remark : null
     * imglist : [{"id":3,"articleid":6,"orders":1,"imgurl":"/home/NetPF/GUI/file/inforeport/Tulips.jpg","createtime":1571810960}]
     */

    private int id;
    private String title;
    private String content;
    private int createtime;
    private int userid;
    private int classid;
    private int viewnum;
    private int pointsnum;
    private int collectionnum;



    private boolean iscollected ;

    private boolean ispointed ;
    private int state;
    private String classname;
    private String username;
    private int count;
    private Object remark;
    private List<ImglistBean> imglist;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }

    public int getViewnum() {
        return viewnum;
    }

    public void setViewnum(int viewnum) {
        this.viewnum = viewnum;
    }

    public int getPointsnum() {
        return pointsnum;
    }

    public void setPointsnum(int pointsnum) {
        this.pointsnum = pointsnum;
    }

    public boolean isIspointed() {
        return ispointed;
    }

    public void setIspointed(boolean ispointed) {
        this.ispointed = ispointed;
    }

    public int getCollectionnum() {
        return collectionnum;
    }

    public void setCollectionnum(int collectionnum) {
        this.collectionnum = collectionnum;
    }

    public boolean isIscollected() {
        return iscollected;
    }

    public void setIscollected(boolean iscollected) {
        this.iscollected = iscollected;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public List<ImglistBean> getImglist() {
        return imglist;
    }

    public void setImglist(List<ImglistBean> imglist) {
        this.imglist = imglist;
    }

    public static class ImglistBean {
        /**
         * id : 3
         * articleid : 6
         * orders : 1
         * imgurl : /home/NetPF/GUI/file/inforeport/Tulips.jpg
         * createtime : 1571810960
         */

        private int id;
        private int articleid;
        private int orders;
        private String imgurl;
        private int createtime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getArticleid() {
            return articleid;
        }

        public void setArticleid(int articleid) {
            this.articleid = articleid;
        }

        public int getOrders() {
            return orders;
        }

        public void setOrders(int orders) {
            this.orders = orders;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public int getCreatetime() {
            return createtime;
        }

        public void setCreatetime(int createtime) {
            this.createtime = createtime;
        }
    }
}
