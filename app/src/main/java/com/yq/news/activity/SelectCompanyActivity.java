package com.yq.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yq.news.R;
import com.yq.news.itf.OnRadioBtnClick;
import com.yq.news.model.DepartmentBean;
import com.yq.news.net.NetCallBack;
import com.yq.news.net.OkHttpManager;
import com.yq.news.treeview.bean.Dir;
import com.yq.news.treeview.viewbinder.DirectoryNodeBinder;
import com.yq.news.treeview.viewbinder.FileNodeBinder;
import com.yq.util.NodeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;

/**
 * 选择单位
 */
public class SelectCompanyActivity extends BaseActivity {

    private static final String TAG = "SelectCompanyActivity";

    private TextView toolbar_title ;

    private RelativeLayout back_lay ;

    private RecyclerView rv;

    private TreeViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_company);
        initView();
        initData();
    }

    private void initView()
    {
         initToolBar();
        toolbar_title.setText("选择单位");
        back_lay.setVisibility(View.VISIBLE);
        back_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rv = (RecyclerView) findViewById(R.id.rv);
    }

    private void initData() {

        OkHttpManager.getInstance().getDeptList("", new NetCallBack() {
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
                     List<DepartmentBean> departmentBeans = new Gson().fromJson(data, new TypeToken<List<DepartmentBean>>() {
                     }.getType());

                    List<DepartmentBean> trees = new ArrayList<>();

                    for (DepartmentBean departmentBean : departmentBeans) {

                              if (departmentBean.getPid()==0) {
                                      trees.add(departmentBean);
                                 }
                                  for (DepartmentBean child : departmentBeans) {
                                            if (child.getPid() == departmentBean.getId()) {
                                                     if (departmentBean.getChildren().size() == 0) {
                                                        }
                                                      departmentBean.add(child);
                                                 }
                                        }
                    }
                    Log.e(TAG,"tree:"+trees);
                    List<TreeNode> rootNodes = new ArrayList<>();
                    rootNodes = NodeUtils.buildTreeByRecursive(trees);

                    rv.setLayoutManager(new LinearLayoutManager(SelectCompanyActivity.this));
                    DirectoryNodeBinder directoryNodeBinder = new DirectoryNodeBinder();
                    adapter = new TreeViewAdapter(rootNodes, Arrays.asList(new FileNodeBinder(), directoryNodeBinder));
                   directoryNodeBinder.setOnRadioBtnClick(new OnRadioBtnClick() {
                    @Override
                    public void onClick(TreeNode node) {
                        Dir dir = (Dir) node.getContent();
                        Intent intent = new Intent();
                        intent.putExtra("dept",dir.dirName);
                        intent.putExtra("id",dir.id);
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                });
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
                                intent.putExtra("dept",dir.dirName);
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
     /*   List<TreeNode> nodes = new ArrayList<>();
        TreeNode<Dir> app = new TreeNode<>(new Dir("app"));
        nodes.add(app);
        app.addChild(
                new TreeNode<>(new Dir("manifests"))
                        .addChild(new TreeNode<>(new File("AndroidManifest.xml")))
        );
        app.addChild(
                new TreeNode<>(new Dir("java")).addChild(
                        new TreeNode<>(new Dir("tellh")).addChild(
                                new TreeNode<>(new Dir("com")).addChild(
                                        new TreeNode<>(new Dir("recyclertreeview"))
                                                .addChild(new TreeNode<>(new File("Dir")))
                                                .addChild(new TreeNode<>(new File("DirectoryNodeBinder")))
                                                .addChild(new TreeNode<>(new File("File")))
                                                .addChild(new TreeNode<>(new File("FileNodeBinder")))
                                                .addChild(new TreeNode<>(new File("TreeViewBinder")))
                                )
                        )
                )
        );
        TreeNode<Dir> res = new TreeNode<>(new Dir("res"));
        nodes.add(res);
        res.addChild(
                new TreeNode<>(new Dir("layout")) // lock this TreeNode
                        .addChild(new TreeNode<>(new File("activity_main.xml")))
                        .addChild(new TreeNode<>(new File("item_dir.xml")))
                        .addChild(new TreeNode<>(new File("item_file.xml")))
        );
        res.addChild(
                new TreeNode<>(new Dir("mipmap"))
                        .addChild(new TreeNode<>(new File("ic_launcher.png")))
        );*/


    }
}
