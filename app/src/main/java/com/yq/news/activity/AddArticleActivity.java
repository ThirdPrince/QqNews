package com.yq.news.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Camera;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.yq.news.R;
import com.yq.news.adapter.GridImageAdapter;
import com.yq.news.view.AutoExpandGridView;
import com.yq.news.view.FullyGridLayoutManager;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 新建文章详情
 */
public class AddArticleActivity extends BaseActivity {


    public  static  void startActivity(Activity activity)
    {
        Intent intent = new Intent(activity,AddArticleActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 底部选择照片
     */
    private ImageView foot_grid_view_image_select ;

    private RecyclerView recyclerView;

    private GridImageAdapter adapter;

    private int chooseMode = PictureMimeType.ofImage();

    private int themeId;

    private List<LocalMedia> selectList = new ArrayList<>();

    private static final int RC_SD_PERM = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);
        themeId  = R.style.picture_default_style;
        initToolBar();
        initView();
    }

    private void initView()
    {
        toolbar_title.setText("新建文章");
        back_lay.setVisibility(View.VISIBLE);
        foot_grid_view_image_select = (ImageView)findViewById(R.id.foot_grid_view_image_select);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        back_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        foot_grid_view_image_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyPermissions.requestPermissions(
                        AddArticleActivity.this,
                        getString(R.string.rationale_sd),
                        RC_SD_PERM,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,  Manifest.permission.CAMERA);
            }
        });
        FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new GridImageAdapter(this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(9);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((position, v) -> {
            LocalMedia media = selectList.get(position);
            String pictureType = media.getPictureType();
            int mediaType = PictureMimeType.pictureToVideo(pictureType);
            switch (mediaType) {
                case 1:
                    // 预览图片
                    PictureSelector.create(AddArticleActivity.this).themeStyle(themeId).openExternalPreview(position, selectList);
                    break;
                default:
                    break;
            }
        });
    }


    @AfterPermissionGranted(RC_SD_PERM)
    private void addPhoto()
    {
        PictureSelector.create(this)
                .openGallery(chooseMode)
                .theme(themeId)
                .maxSelectNum(9)
                .minSelectNum(1)
                .selectionMode( PictureConfig.MULTIPLE )
                .previewImage(true)
                .previewVideo(false)
                .enablePreviewAudio(false) // 是否可播放音频
                .isCamera(true)
                .enableCrop(false)
                .compress(true)
                .glideOverride(160, 160)
                .previewEggs(true)
                .hideBottomControls( true)
                .isGif(false)
                .freeStyleCropEnabled(false)
                .showCropGrid(false)
                .openClickSound(false)
                .selectionMedia(selectList)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            PictureSelector.create(AddArticleActivity.this)
                    .openGallery(chooseMode)
                    .theme(themeId)
                    .maxSelectNum(9)
                    .minSelectNum(1)
                    .selectionMode( PictureConfig.MULTIPLE )
                    .previewImage(true)
                    .previewVideo(false)
                    .enablePreviewAudio(false) // 是否可播放音频
                    .isCamera(true)
                    .enableCrop(false)
                    .compress(true)
                    .glideOverride(160, 160)
                    .previewEggs(true)
                    .hideBottomControls( true)
                    .isGif(false)
                    .freeStyleCropEnabled(false)
                    .showCropGrid(false)
                    .openClickSound(false)
                    .selectionMedia(selectList)
                    .forResult(PictureConfig.CHOOSE_REQUEST);

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择
                    selectList = PictureSelector.obtainMultipleResult(data);
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
