package com.yq.news.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yq.news.R;
import com.yq.news.itf.OnRadioBtnClick;
import com.yq.news.model.CityInfo;
import com.yq.news.model.DepartmentBean;
import com.yq.news.model.IndustryBean;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.yq.news.treeview.bean.Dir;
import com.yq.news.treeview.viewbinder.DirectoryNodeBinder;
import com.yq.news.treeview.viewbinder.FileNodeBinder;
import com.yq.util.NodeUtils;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;

public class CityActivity extends BaseActivity {

    private RecyclerView rv;

    private TreeViewAdapter adapter;

    private List<CityInfo> list ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        list = new ArrayList<>();
        initView();

        OkHttpManager.getInstance().getArea(new NetCallBack() {
            @Override
            public void success(String response) {
                JSONObject jsonObject = null;
                String data = "";
                try {
                    jsonObject = new JSONObject(response);
                    data = jsonObject.getString("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List<CityInfo> industryBeans = new Gson().fromJson(data, new TypeToken<List<CityInfo>>() {
                }.getType());
                list.clear();
                list.addAll(industryBeans);
                List<CityInfo> trees = new ArrayList<>();
                for (CityInfo cityInfo : industryBeans) {

                    /**
                     *  这里的6 是中国下面的根节点，中国不现实
                     */
                    if (cityInfo.getPid() == 6) {
                        trees.add(cityInfo);
                    }
                    for (CityInfo child : industryBeans) {
                        if (child.getPid() == cityInfo.getId()) {
                            if (cityInfo.getChildren().size() == 0) {
                            }
                            cityInfo.add(child);
                        }
                    }
                }


                List<TreeNode> rootNodes = new ArrayList<>();
                rootNodes = NodeUtils.buildTreeByRecursiveCity(trees);
                DirectoryNodeBinder directoryNodeBinder = new DirectoryNodeBinder();
                adapter = new TreeViewAdapter(rootNodes, Arrays.asList(new FileNodeBinder(), directoryNodeBinder));
                directoryNodeBinder.setOnRadioBtnClick(new OnRadioBtnClick() {
                    @Override
                    public void onClick(TreeNode node) {
                        Dir dir = (Dir) node.getContent();
                        Intent intent = new Intent();
                        intent.putExtra("city",dir.dirName);
                        intent.putExtra("id",dir.id);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                });
                rv.setLayoutManager(new LinearLayoutManager(CityActivity.this));
                adapter = new TreeViewAdapter(rootNodes, Arrays.asList(new FileNodeBinder(), directoryNodeBinder));
                adapter.setOnTreeNodeListener(new TreeViewAdapter.OnTreeNodeListener() {
                    @Override
                    public boolean onClick(TreeNode node, RecyclerView.ViewHolder holder) {
                        if (!node.isLeaf()) {
                            //Update and toggle the node.
                            onToggle(!node.isExpand(), holder);
//                    if (!node.isExpand())
//                        adapter.collapseBrotherNode(node);
                        }else
                        {
                            Dir dir = (Dir) node.getContent();

                            Intent intent = new Intent();
                            intent.putExtra("city",dir.dirName);
                            intent.putExtra("id",dir.id);
                            setResult(RESULT_OK,intent);
                            finish();
                        }
                        return false;
                    }

                    @Override
                    public void onToggle(boolean isExpand, RecyclerView.ViewHolder holder) {
                        DirectoryNodeBinder.ViewHolder dirViewHolder = (DirectoryNodeBinder.ViewHolder) holder;
                        final ImageView ivArrow = dirViewHolder.getIvArrow();
                        int rotateDegree = isExpand ? 90 : -90;
                        ivArrow.animate().rotationBy(rotateDegree)
                                .start();
                    }
                });
                rv.setAdapter(adapter);


                }


            @Override
            public void failed(String msg) {

            }
        });
    }

    private void initView()
    {
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("选择地区");
        back_lay = (RelativeLayout) findViewById(R.id.back_lay);
        back_lay.setVisibility(View.VISIBLE);
        back_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rv = (RecyclerView) findViewById(R.id.rv);
    }
}
