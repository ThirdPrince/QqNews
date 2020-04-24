package com.yq.news.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yq.news.R;
import com.yq.news.model.ArticleDetail;


/**
 * 接收华为的推送定制页面
 */
public class DeepLinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link);
        Intent intent = getIntent();
        if(intent != null)
        {
            String id = intent.getStringExtra("id");
            ArticleDetailActivity.startActivity(this,id);

        }
        finish();

    }
}
