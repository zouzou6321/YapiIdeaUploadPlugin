package com.qbb.vo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Copyright: Copyright (c) 2019  ALL RIGHTS RESERVED.
 * @Company: 成都国盛天丰技术有限责任公司
 * @Author: 程立涛
 * @CreateDate: 2019/9/29 15:56
 * @UpdateDate: 2019/9/29 15:56
 * @UpdateRemark: init
 * @Version: 1.0
 * @menu 接口描述
 */
public class InterfaceVo implements Serializable {
    /**
     * edit_uid : 0
     * status : undone
     * api_opened : false
     * index : 0
     * tag : []
     * _id : 1105
     * title : /stu/list
     * catid : 292
     * path : /stu/list
     * method : POST
     * project_id : 115
     * uid : 63
     * add_time : 1569397582
     * up_time : 1569399892
     */

    @SerializedName("edit_uid")
    private int editUid;
    @SerializedName("status")
    private String status;
    @SerializedName("api_opened")
    private boolean apiOpened;
    @SerializedName("index")
    private int index;
    @SerializedName("_id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("catid")
    private int catid;
    @SerializedName("path")
    private String path;
    @SerializedName("method")
    private String method;
    @SerializedName("project_id")
    private int projectId;
    @SerializedName("uid")
    private int uid;
    @SerializedName("add_time")
    private int addTime;
    @SerializedName("up_time")
    private int upTime;
    @SerializedName("tag")
    private List<?> tag;

    public int getEditUid() {
        return editUid;
    }

    public void setEditUid(int editUid) {
        this.editUid = editUid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isApiOpened() {
        return apiOpened;
    }

    public void setApiOpened(boolean apiOpened) {
        this.apiOpened = apiOpened;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

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

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getAddTime() {
        return addTime;
    }

    public void setAddTime(int addTime) {
        this.addTime = addTime;
    }

    public int getUpTime() {
        return upTime;
    }

    public void setUpTime(int upTime) {
        this.upTime = upTime;
    }

    public List<?> getTag() {
        return tag;
    }

    public void setTag(List<?> tag) {
        this.tag = tag;
    }
}
