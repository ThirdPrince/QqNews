package com.yq.util;

import com.yq.news.model.CityInfo;
import com.yq.news.model.DepartmentBean;
import com.yq.news.treeview.bean.Dir;

import java.util.ArrayList;
import java.util.List;

import tellh.com.recyclertreeview_lib.TreeNode;

public class NodeUtils {

    public static List<TreeNode> buildTreeByRecursive(List<DepartmentBean> treeNodes) {

        List<TreeNode> rootNodes = new ArrayList<>();

        for (DepartmentBean departmentBean : treeNodes) {
            TreeNode<Dir> childNode = new TreeNode<>(new Dir(departmentBean.getName(),departmentBean.getId()+""));
            rootNodes.add(childNode);
            buildChildTreeByRecursive(departmentBean,childNode);
        }

        return rootNodes;
    }


    /**
     * 地区转 tree
     * @param treeNodes
     * @return
     */
    public static List<TreeNode> buildTreeByRecursiveCity(List<CityInfo> treeNodes) {

        List<TreeNode> rootNodes = new ArrayList<>();

        for (CityInfo cityInfo : treeNodes) {
            TreeNode<Dir> childNode = new TreeNode<>(new Dir(cityInfo.getName(),cityInfo.getCode()+""));
            rootNodes.add(childNode);
            buildChildTreeByRecursiveCity(cityInfo,childNode);
        }


        return rootNodes;
    }


    public static void buildChildTreeByRecursive(DepartmentBean departmentBean, TreeNode<Dir> childNode) {

        List<DepartmentBean> child = departmentBean.getChildren();
        if (child.size() > 0) {
            for (DepartmentBean bean : child) {
                TreeNode<Dir> childNode2 = new TreeNode<>(new Dir(bean.getName(),departmentBean.getId()+""));
                childNode.addChild(childNode2);
                buildChildTreeByRecursive(bean,childNode2);
            }

        }
    }

    public static void buildChildTreeByRecursiveCity(CityInfo cityInfo, TreeNode<Dir> childNode) {

        List<CityInfo> child = cityInfo.getChildren();
        if (child.size() > 0) {
            for (CityInfo bean : child) {
                TreeNode<Dir> childNode2 = new TreeNode<>(new Dir(bean.getName(),cityInfo.getCode()+""));
                childNode.addChild(childNode2);
                buildChildTreeByRecursiveCity(bean,childNode2);
            }

        }
    }
}
