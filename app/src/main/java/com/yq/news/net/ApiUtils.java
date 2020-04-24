package com.yq.news.net;

import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;

public class ApiUtils {


    //10.1.126.242:8080 测试
    public static String ADDRESS_HEADER = "http://";

    public static String ADDRESS_IP = "10.1.126.243";
           // "10.1.126.243";

    public static String ADDRESS_PORT = "8236";

    public static String BASE_ADDRESS = setAdd();
           // "https://hunan.fhyqw.com";
            //setAdd();
            //"http://10.1.126.241:8080" ;测试
            //"http://10.1.126.243:8080";
            //"http://106.75.116.130:28888";
            //"http://10.1.126.243:8080";

    /**
     * IP地址保存后重新赋值
     */
    public static String  setAdd()
    {
        String ip = SPUtils.getInstance().getString("ip");
        if(!TextUtils.isEmpty(ip))
        {
            BASE_ADDRESS = ADDRESS_HEADER + ip+":"+SPUtils.getInstance().getString("port");
        }else
        {
            BASE_ADDRESS = ADDRESS_HEADER + ADDRESS_IP +":"+ADDRESS_PORT ;
        }

        return BASE_ADDRESS ;
    }
    public static String BASE_URL = "https://wwts.jscert.org.cn:8236";

             //https://wwts.jscert.org.cn:8236/
            //"https://hunan.fhyqw.com";
            //BASE_ADDRESS +"/report";
           // +"/report";

    public static String LOGIN_URL = "http://47.98.142.126:8080";  //47.98.142.126:8080
}
