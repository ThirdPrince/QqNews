package com.yq.news.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.CacheDiskUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chaychan.uikit.UIUtils;
import com.github.anzewei.parallaxbacklayout.ParallaxHelper;
import com.github.anzewei.parallaxbacklayout.widget.ParallaxBackLayout;
import com.google.gson.Gson;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebViewClient;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.r0adkll.slidr.Slidr;
import com.wx.goodview.GoodView;
import com.yq.news.MainActivity;
import com.yq.news.R;
import com.yq.news.adapter.ArticleImageAdapter;
import com.yq.news.itf.JsCallImage;
import com.yq.news.model.ArticleDetail;
import com.yq.news.model.H5ImgList;
import com.yq.news.model.LoginInfo;
import com.yq.news.net.ApiUtils;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.yq.news.view.WaterMarkBg;
import com.yq.util.Constant;
import com.yq.util.RelativeDateFormat;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.github.anzewei.parallaxbacklayout.ViewDragHelper.EDGE_LEFT;
import static com.github.anzewei.parallaxbacklayout.widget.ParallaxBackLayout.EDGE_MODE_DEFAULT;
import static com.github.anzewei.parallaxbacklayout.widget.ParallaxBackLayout.EDGE_MODE_FULL;
import static com.github.anzewei.parallaxbacklayout.widget.ParallaxBackLayout.LAYOUT_COVER;
import static com.yq.util.Constant.TEXT_FONT_BIG_SIZE;
import static com.yq.util.Constant.TEXT_FONT_SIZE;
import static com.yq.util.Constant.TEXT_FONT_SMALL_SIZE;
import static com.yq.util.Constant.TEXT_FONT_STAND_SIZE;
import static com.yq.util.Constant.TEXT_FONT_SUPER_BIG_SIZE;

/**
 * 信息详情
 */
public class ArticleDetailActivity extends BaseActivity {


    private static final String TAG = "ArticleDetailActivity";

    private String id = "";

    private   ArticleDetail detail ;

    /**
     *
     */
    private  TextView title_tv ;

    private TextView content_tv ;

    /**
     *  作     */
    private TextView author_tv ;

    /**
     *
     */
    private TextView time_tv ;

    /**
     * 收藏
     */
    private TextView collection_tv ;

    /**
     * 点赞
     */

    private TextView encourage_tv ;

    private GoodView goodView ;

    private int themeId;

    private List<LocalMedia> selectList = new ArrayList<>();

    private RecyclerView rcy_view ;

    private ArticleImageAdapter  articleImageAdapter ;

    protected AgentWeb mAgentWeb;

    private RelativeLayout root_lay ;

    private LinearLayout container ;

    /**
     * rey header
     */
    private LinearLayout mHeaderGroup ;


    private HeaderAndFooterWrapper headerAndFooterWrapper ;

    private String webUrl  = ApiUtils.BASE_URL +"/api/mobile/article/detail.html?token=";

    private H5ImgList h5ImgList ;

    private    WebView webView ;

    private WebSettings webSettings ;

    private final int COLLECTION_WHAT = 1024;

    private final int ZAN_WHAT = 1025;

    /**
     * 用来控制字体大小
     */
    int fontSize = 1;

    private Handler handler = new Handler(Looper.getMainLooper())
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case COLLECTION_WHAT:
                    if(detail != null)
                    {
                        if(detail.isIscollected())
                        {
                            operateArticle("6");
                            goodView.setText("-1");
                            setDrawableLeft(R.drawable.wsc, collection_tv);
                        }else
                        {
                            operateArticle("2");
                            goodView.setText("+1");
                            setDrawableLeft(R.drawable.ysc, collection_tv);
                        }
                        goodView.show(collection_tv);
                    }
                    break;

                case ZAN_WHAT :
                    if(detail != null)
                    {
                        if(detail.isIspointed())
                        {
                            operateArticle("5");
                            goodView.setText("-1");
                            setDrawableLeft(R.drawable.wdz, encourage_tv);
                        }else
                        {
                            operateArticle("1");
                            goodView.setText("+1");
                            setDrawableLeft(R.drawable.ydz, encourage_tv);
                        }
                        goodView.show(encourage_tv);
                    }
                    break;
            }
        }
    };
    public  static void startActivity(Activity activity ,String id )
    {
        Intent intent = new Intent(activity,ArticleDetailActivity.class);
        intent.putExtra("id",id);
        activity.startActivity(intent);
    }
    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        themeId = R.style.picture_default_style;
        if (enableSlideClose()) {
            ParallaxBackLayout layout = ParallaxHelper.getParallaxBackLayout(this, true);
            layout.setEdgeMode(EDGE_MODE_FULL);//全屏滑动
            layout.setEdgeMode(EDGE_MODE_DEFAULT);//边缘滑动
            layout.setEdgeFlag(getEdgeDirection());
            layout.setLayoutType(getSlideLayoutType(),null);

            layout.setSlideCallback(new ParallaxBackLayout.ParallaxSlideCallback() {
                @Override
                public void onStateChanged(int state) {
                    //收起软键盘
                   // UIUtils.hideInput(getWindow().getDecorView());
                }

                @Override
                public void onPositionChanged(float percent) {

                }
            });
        }


        // Slidr.attach(this);
        //StatusBarUtil.setColorForSwipeBack(this,getResources().getColor(R.color.white),200);
        initToolBar();
        initView();
        initData();
        //getData(false);

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        webSettings.setSupportZoom(true);



        if (webSettings.getTextSize() == WebSettings.TextSize.SMALLEST) {
            fontSize = 1;
        } else if (webSettings.getTextSize() == WebSettings.TextSize.SMALLER) {
            fontSize = 2;
        } else if (webSettings.getTextSize() == WebSettings.TextSize.NORMAL) {
            fontSize = 3;
        } else if (webSettings.getTextSize() == WebSettings.TextSize.LARGER) {
            fontSize = 4;
        } else if (webSettings.getTextSize() == WebSettings.TextSize.LARGEST) {
            fontSize = 5;
        }

        int text_size = SPUtils.getInstance().getInt(TEXT_FONT_SIZE);
        switch (text_size)
        {
            case TEXT_FONT_SMALL_SIZE:
                webSettings.setTextZoom(90);
                //webSettings.setTextSize(WebSettings.TextSize.SMALLEST);
                break;

            case TEXT_FONT_STAND_SIZE:
                //webSettings.setTextSize(WebSettings.TextSize.SMALLER);
                break;

            case TEXT_FONT_BIG_SIZE:
                webSettings.setTextZoom(115);
                //webSettings.setTextSize(WebSettings.TextSize.NORMAL);
                break;

            case TEXT_FONT_SUPER_BIG_SIZE:
                webSettings.setTextZoom(130);
                //webSettings.setTextSize(WebSettings.TextSize.LARGER);
                break;
        }
        // 随便找了个带图片的网站
        webView.loadUrl(webUrl);
        Log.e(TAG,"webUrl:"+webUrl);
        // 添加js交互接口类，并起别名 imagelistner
        webView.addJavascriptInterface(new JavascriptInterface(this), "imagelistner");
        webView.setWebViewClient(new MyWebViewClient());
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
       /* LoginInfo loginInfo = (LoginInfo) CacheDiskUtils.getInstance().getSerializable(Constant.LOGIN_CACHE_KEY);
        if(loginInfo != null && loginInfo.getData()!= null) {
            labels.add(loginInfo.getData().getRealname() + " " + loginInfo.getData().getTelphone());
            root_lay.setBackgroundDrawable(new WaterMarkBg(this, labels, -30, 13));
        }*/
      /*  mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(container, new LinearLayout.LayoutParams(-1, -1))
                .closeIndicator()//
               .setWebViewClient(new MyWebViewClient())
                .createAgentWeb()
                .ready()
                .go(webUrl);

        mAgentWeb.getJsInterfaceHolder().addJavaObject("android",new JavascriptInterface(this));*/


    }
    public boolean enableSlideClose() {
        return true;
    }

    /**
     * 默认为左滑，子类可重写返回对应的方向
     * @return
     */
    public int getEdgeDirection(){
        return EDGE_LEFT;
    }

    /**
     * 默认为覆盖滑动关闭效果，子类可重写
     * @return
     */
    public int getSlideLayoutType() {
        return LAYOUT_COVER;
    }
    // js通信接口
    public class JavascriptInterface {

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }
        @android.webkit.JavascriptInterface
        public void openImage(String img) {
            selectList.clear();
            int position = 0;
            if(h5ImgList != null && h5ImgList.getData().size()>0)
            {
                for(int i = 0;i< h5ImgList.getData().size();i++) {
                    LocalMedia localMedia = new LocalMedia(h5ImgList.getData().get(i));
                    if(img.equals(h5ImgList.getData().get(i)))
                    {
                        position = i;
                    }
                    selectList.add(localMedia);
                }
            }else
            {
                LocalMedia localMedia = new LocalMedia(img);
                selectList.add(localMedia);
            }



            PictureSelector.create(ArticleDetailActivity.this).themeStyle(themeId).openExternalPreview(position, selectList);
        }
    }

    // 监听
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            //view.getSettings().setJavaScriptEnabled(true);

            super.onPageFinished(view, url);
            // html加载完成之后，添加监听图片的点击js函数
            setWebImageClick(view);

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            super.onReceivedError(view, errorCode, description, failingUrl);

        }
    }
    private  void setWebImageClick(WebView view) {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，在还是执行的时候调用本地接口传递url过去
        //mAgentWeb.getWebCreator().getWebView()
      webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +

                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }


    private void initView()
    {
        root_lay = (RelativeLayout) findViewById(R.id.root_lay);
        container = (LinearLayout) findViewById(R.id.container);
        webView = (WebView) findViewById(R.id.web_view);
        collection_tv = (TextView) findViewById(R.id.collection_tv);
        encourage_tv = (TextView) findViewById(R.id.encourage_tv);
        goodView = new GoodView(this);
        rcy_view = (RecyclerView) findViewById(R.id.rcy_view);
        rcy_view.setLayoutManager(new LinearLayoutManager(this));
        mHeaderGroup = ((LinearLayout) LayoutInflater.from(this).inflate(R.layout.arcticle_info_title, null));
        title_tv = mHeaderGroup.findViewById(R.id.title_tv);
        author_tv = mHeaderGroup.findViewById(R.id.author_tv);
        time_tv = mHeaderGroup.findViewById(R.id.time_tv);
        content_tv = mHeaderGroup.findViewById(R.id.content_tv);
        toolbar_title.setText("");
        back_lay.setVisibility(View.VISIBLE);
        back_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        collection_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeMessages(COLLECTION_WHAT);
                handler.sendEmptyMessageDelayed(COLLECTION_WHAT,200);


            }
        });
        encourage_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handler.removeMessages(ZAN_WHAT);
                handler.sendEmptyMessageDelayed(ZAN_WHAT,200);

            }
        });
    }

    private void initData()
    {
        Intent intent = getIntent();
        if(intent != null)
        {
            id = intent.getStringExtra("id");
            String tokenSpu = SPUtils.getInstance().getString("token");
            String uIdSpu = SPUtils.getInstance().getString("uid");
            webUrl = webUrl+tokenSpu+"&userid="+uIdSpu+"&id="+id;
            operateArticle("3");
        }


    }

    private void setDrawableLeft(int id, TextView tv){
        Drawable drawableLeft = getResources().getDrawable(id);
        tv.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
        tv.setCompoundDrawablePadding(16);
    }

    /**
     * 跟新数据
     * @param isRefresh = ture  收藏点赞更新
     */
    private void getData(boolean isRefresh)
    {

        OkHttpManager.getInstance().getArticleDatailPic(id, new NetCallBack() {
            @Override
            public void success(String response) {

                h5ImgList = new Gson().fromJson(response,H5ImgList.class);

            }

            @Override
            public void failed(String msg) {

            }
        });
        OkHttpManager.getInstance().getArticleDetailList(id, new NetCallBack() {
            @Override
            public void success(String response) {
                JSONObject jsonObject = null;
                String rsp = "";
                try {
                    jsonObject = new JSONObject(response);
                    rsp = jsonObject.getString("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                 detail =  new Gson().fromJson(rsp,ArticleDetail.class);
                 initData(isRefresh);
                   // Glide.with(ArticleDetailActivity.this).load(ApiUtils.BASE_ADDRESS+detail.getImglist().get(0).getImgurl()).into(image);
                }


            @Override
            public void failed(String msg) {

            }
        });
    }

    private void initData(boolean isRefresh) {

       /* if (isRefresh)
        {
            collection_tv.setText(detail.getCollectionnum() + "");
            encourage_tv.setText(detail.getPointsnum() + "");
            return;
        }*/
        if (detail != null) {
          /*  title_tv.setText(detail.getTitle());
            author_tv.setText(detail.getUsername());
            time_tv.setText(RelativeDateFormat.timeStamp2Date(detail.getCreatetime()));
            content_tv.setText(Html.fromHtml(detail.getContent()));*/
            collection_tv.setText(detail.getCollectionnum() + "");
            encourage_tv.setText(detail.getPointsnum() + "");
            setDrawableLeft(detail.isIscollected() ? R.drawable.ysc : R.drawable.wsc, collection_tv);
            setDrawableLeft(detail.isIspointed() ? R.drawable.ydz : R.drawable.wdz, encourage_tv);
        }
    }
    /**
     * 操作收藏
     * @param type
     */
    private void operateArticle(String type)
    {
        OkHttpManager.getInstance().operateArticle(id, type, new NetCallBack() {
            @Override
            public void success(String response) {

                getData(true);
            }

            @Override
            public void failed(String msg) {

                ToastUtils.showShort(msg);
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //moveTaskToBack(true);
        back();

    }

    private void back()
    {
        if(ActivityUtils.isActivityExistsInStack(MainActivity.class))
        {
            finish();
        }else {
            ActivityUtils.finishAllActivities();
            Intent intent = new Intent(); //创建Intent对象
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName componentName1 = new ComponentName("com.yq.news","com.yq.news.activity.SplashActivity");
            intent.setComponent(componentName1);//调用Intent的setComponent()方法实现传递
            startActivity(intent);//显示启动Activity
            finish();
        }
    }
}
