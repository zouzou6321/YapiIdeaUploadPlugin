package com.qbb.config;

import com.qbb.constant.ProjectTypeConstant;
import org.apache.commons.lang3.StringUtils;

/**
 * Yapi项目配置.
 */
public class YapiProjectConfig {

    /**
     * 服务地址
     */
    private String yapiUrl;

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 项目token
     */
    private String projectToken;

    /**
     * 项目类型
     */
    private String projectType;

    /**
     * 响应结果包装类.
     */
    private String returnClass;

    /**
     * 附加上传地址.
     */
    private String attachUpload;

    /**
     * 配置是否有效.
     */
    public boolean isValidate() {
        return StringUtils.isNotEmpty(projectToken)
                && StringUtils.isNotEmpty(projectId)
                && StringUtils.isNotEmpty(yapiUrl)
                && StringUtils.isNotEmpty(projectType);
    }

    public boolean isDubboProject() {
        return ProjectTypeConstant.dubbo.equals(projectType);
    }

    public boolean isApiProject() {
        return ProjectTypeConstant.api.equals(projectType);
    }

    /**
     * 计算上传后访问地址.
     */
    public String resolveCatUrl(String catId) {
        return yapiUrl + "/project/" + projectId + "/interface/api/cat_" + catId;
    }

    //------------------ generated ------------------//

    public String getYapiUrl() {
        return yapiUrl;
    }

    public void setYapiUrl(String yapiUrl) {
        this.yapiUrl = yapiUrl;
    }

    public String getProjectToken() {
        return projectToken;
    }

    public void setProjectToken(String projectToken) {
        this.projectToken = projectToken;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getReturnClass() {
        return returnClass;
    }

    public void setReturnClass(String returnClass) {
        this.returnClass = returnClass;
    }

    public String getAttachUpload() {
        return attachUpload;
    }

    public void setAttachUpload(String attachUpload) {
        this.attachUpload = attachUpload;
    }

    @Override
    public String toString() {
        return "YapiProjectConfig{" +
                "yapiUrl='" + yapiUrl + '\'' +
                ", projectToken='" + projectToken + '\'' +
                ", projectId='" + projectId + '\'' +
                ", projectType='" + projectType + '\'' +
                ", returnClass='" + returnClass + '\'' +
                ", attachUpload='" + attachUpload + '\'' +
                '}';
    }
}
