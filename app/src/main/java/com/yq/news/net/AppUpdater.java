package com.yq.news.net;



public class AppUpdater {
    private static AppUpdater  mInstance ;

    private INetManager mNetManager =  OkHttpManager.getInstance();

    public void setNetManager(INetManager mNetManager)
    {
        this.mNetManager = mNetManager ;
    }
    public INetManager getNetManager()
    {
        return mNetManager ;
    }
    private AppUpdater()
    {

    }
     public static AppUpdater getInstance()
     {
         if(mInstance == null)
         {
             synchronized (AppUpdater.class)
             {
                 if(mInstance == null)
                 {
                     mInstance = new AppUpdater();
                 }
             }
         }

         return mInstance ;
     }

}
