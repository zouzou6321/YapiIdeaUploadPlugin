package com.qbb.action;

import com.google.common.base.Strings;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.qbb.build.BuildJsonForDubbo;
import com.qbb.build.BuildJsonForYapi;
import com.qbb.config.YapiProjectConfig;
import com.qbb.config.YapiProjectConfigUtils;
import com.qbb.constant.YapiConstant;
import com.qbb.dto.YapiApiDTO;
import com.qbb.dto.YapiDubboDTO;
import com.qbb.dto.YapiResponse;
import com.qbb.dto.YapiSaveParam;
import com.qbb.upload.UploadYapi;
import com.qbb.util.NotificationUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.xml.sax.SAXException;

/**
 * @description: 入口
 * @author: chengsheng@qbb6.com
 * @date: 2019/5/15
 */
public class YapiUploadAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(CommonDataKeys.PROJECT);
        if (project == null) {
            return;
        }
        VirtualFile file = event.getData(CommonDataKeys.VIRTUAL_FILE);
        if (file == null) {
            return;
        }
        // 查找配置文件
        Module module = ModuleUtil.findModuleForFile(file, project);
        VirtualFile yapiConfigFile = YapiProjectConfigUtils.findConfigFile(project, module);
        if (yapiConfigFile == null || !yapiConfigFile.exists()) {
            NotificationUtils.notifyError("Not found config file yapi.xml.");
            return;
        }
        // 获取配置
        YapiProjectConfig config = null;
        try {
            String projectConfig = new String(yapiConfigFile.contentsToByteArray(), StandardCharsets.UTF_8);
            config = YapiProjectConfigUtils.readFromXml(projectConfig, module != null ? module.getName() : null);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            NotificationUtils.notifyError(String.format("Config file error: %s", e.getMessage()));
            return;
        }
        // 配置校验
        if (!config.isValidate()) {
            NotificationUtils.notifyError(
                    "Yapi config required, please check config,[projectToken,projectId,yapiUrl,projectType]");
            return;
        }
        // 判断项目类型
        if (config.isDubboProject()) {
            // 获得dubbo需上传的接口列表 参数对象
            ArrayList<YapiDubboDTO> yapiDubboDTOs = new BuildJsonForDubbo().actionPerformedList(event);
            if (yapiDubboDTOs != null) {
                for (YapiDubboDTO yapiDubboDTO : yapiDubboDTOs) {
                    YapiSaveParam yapiSaveParam = new YapiSaveParam(config.getProjectToken(), yapiDubboDTO.getTitle(), yapiDubboDTO.getPath(), yapiDubboDTO.getParams(), yapiDubboDTO.getResponse(), Integer.valueOf(config.getProjectId()), config.getYapiUrl(), yapiDubboDTO.getDesc());
                    yapiSaveParam.setStatus(yapiDubboDTO.getStatus());
                    if (!Strings.isNullOrEmpty(yapiDubboDTO.getMenu())) {
                        yapiSaveParam.setMenu(yapiDubboDTO.getMenu());
                    } else {
                        yapiSaveParam.setMenu(YapiConstant.menu);
                    }
                    try {
                        // 上传
                        YapiResponse yapiResponse = new UploadYapi()
                                .uploadSave(yapiSaveParam, null, project.getBasePath(), project);
                        if (yapiResponse.getErrcode() != 0) {
                            NotificationUtils.notifyError("Sorry ,upload api error cause:" + yapiResponse.getErrmsg());
                        } else {
                            String url = config.resolveCatUrl(yapiResponse.getCatId());
                            NotificationUtils.notifyInfo(String.format("Success ,url: <a href='%s'>%s</a>", url, url));
                        }
                    } catch (Exception e) {
                        NotificationUtils
                                .notifyError("Sorry ,upload api error cause:" + ExceptionUtils.getStackTrace(e));
                    }
                }
            }
        } else if (config.isApiProject()) {
            //获得api 需上传的接口列表 参数对象
            ArrayList<YapiApiDTO> yapiApiDTOS = new BuildJsonForYapi()
                    .actionPerformedList(event, config.getAttachUpload(), config.getReturnClass());
            if (yapiApiDTOS != null) {
                for (YapiApiDTO yapiApiDTO : yapiApiDTOS) {
                    YapiSaveParam yapiSaveParam = new YapiSaveParam(config.getProjectToken(), yapiApiDTO.getTitle(), yapiApiDTO.getPath(), yapiApiDTO.getParams(), yapiApiDTO.getRequestBody(), yapiApiDTO.getResponse(), Integer.valueOf(config.getProjectId()), config.getYapiUrl(), true, yapiApiDTO.getMethod(), yapiApiDTO.getDesc(), yapiApiDTO.getHeader());
                    yapiSaveParam.setReq_body_form(yapiApiDTO.getReq_body_form());
                    yapiSaveParam.setReq_body_type(yapiApiDTO.getReq_body_type());
                    yapiSaveParam.setReq_params(yapiApiDTO.getReq_params());
                    yapiSaveParam.setStatus(yapiApiDTO.getStatus());
                    yapiSaveParam.setTag(yapiApiDTO.getTag());
                    if (!Strings.isNullOrEmpty(yapiApiDTO.getMenu())) {
                        yapiSaveParam.setMenu(yapiApiDTO.getMenu());
                    } else {
                        yapiSaveParam.setMenu(YapiConstant.menu);
                    }
                    try {
                        // 上传
                        YapiResponse yapiResponse = new UploadYapi()
                                .uploadSave(yapiSaveParam, config.getAttachUpload(), project.getBasePath(), project);
                        if (yapiResponse.getErrcode() != 0) {
                            NotificationUtils.notifyError("Sorry ,upload api error cause:" + yapiResponse.getErrmsg());
                        } else {
                            String url = config.resolveCatUrl(yapiResponse.getCatId());
                            NotificationUtils.notifyInfo(String.format("Success ,url: <a href='%s'>%s</a>", url, url));
                        }
                    } catch (Exception e) {
                        NotificationUtils
                                .notifyError("Sorry ,upload api error cause:" + ExceptionUtils.getStackTrace(e));
                    }
                }
            }
        }
    }
}
