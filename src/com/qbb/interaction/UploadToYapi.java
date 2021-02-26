package com.qbb.interaction;

import com.google.common.base.Strings;
import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.qbb.build.BuildJsonForDubbo;
import com.qbb.build.BuildJsonForYapi;
import com.qbb.constant.YapiConstant;
import com.qbb.dto.*;
import com.qbb.upload.UploadYapi;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * @description: 入口
 * @author: chengsheng@qbb6.com
 * @date: 2019/5/15
 */
public class UploadToYapi extends AnAction {

    private static NotificationGroup notificationGroup;

    static {
        notificationGroup = new NotificationGroup("YapiIdeaUploadPlugin.NotificationGroup", NotificationDisplayType.BALLOON, true);
    }


    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor editor = (Editor) e.getDataContext().getData(CommonDataKeys.EDITOR);
        Project project = editor.getProject();
        VirtualFile file = e.getData(CommonDataKeys.VIRTUAL_FILE);
        Module module = null;
        if (project != null && file != null) {
            module = ModuleUtil.findModuleForFile(file, project);
        }
        VirtualFile yapiConfigFile = resolveYapiConfigFile(project, module);
        if (yapiConfigFile == null || !yapiConfigFile.exists()) {
            Notification error = notificationGroup.createNotification("Yapi not found config file yapi.xml", NotificationType.ERROR);
            Notifications.Bus.notify(error, project);
            return;
        }
        // 获取配置
        YapiProjectConfig config = null;
        try {
            String projectConfig = new String(yapiConfigFile.contentsToByteArray(), StandardCharsets.UTF_8);
            config = YapiProjectConfig.readFromXml(projectConfig, module != null ? module.getName() : null);
        } catch (Exception e2) {
            Notification error = notificationGroup.createNotification("Yapi config error:" + e2.getMessage(), NotificationType.ERROR);
            Notifications.Bus.notify(error, project);
            return;
        }
        // 配置校验
        if (!config.isValidate()) {
            Notification error = notificationGroup.createNotification("Yapi config required, please check config,[projectToken,projectId,yapiUrl,projectType]", NotificationType.ERROR);
            Notifications.Bus.notify(error, project);
            return;
        }
        // 判断项目类型
        if (config.isDubboProject()) {
            // 获得dubbo需上传的接口列表 参数对象
            ArrayList<YapiDubboDTO> yapiDubboDTOs = new BuildJsonForDubbo().actionPerformedList(e);
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
                        YapiResponse yapiResponse = new UploadYapi().uploadSave(yapiSaveParam, null, project.getBasePath(), project);
                        if (yapiResponse.getErrcode() != 0) {
                            Notification error = notificationGroup.createNotification("Sorry ,upload api error cause:" + yapiResponse.getErrmsg(), NotificationType.ERROR);
                            Notifications.Bus.notify(error, project);
                        } else {
                            String url = config.resolveCatUrl(yapiResponse.getCatId());
                            this.setClipboard(url);
                            Notification error = notificationGroup.createNotification("Success ,url: " + url, NotificationType.INFORMATION);
                            Notifications.Bus.notify(error, project);
                        }
                    } catch (Exception e1) {
                        Notification error = notificationGroup.createNotification("Sorry ,upload api error cause:" + e1, NotificationType.ERROR);
                        Notifications.Bus.notify(error, project);
                    }
                }
            }
        } else if (config.isApiProject()) {
            //获得api 需上传的接口列表 参数对象
            ArrayList<YapiApiDTO> yapiApiDTOS = new BuildJsonForYapi().actionPerformedList(e, config.getAttachUpload(), config.getReturnClass());
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
                        YapiResponse yapiResponse = new UploadYapi().uploadSave(yapiSaveParam, config.getAttachUpload(), project.getBasePath(), project);
                        if (yapiResponse.getErrcode() != 0) {
                            Notification error = notificationGroup.createNotification("Sorry ,upload api error cause:" + yapiResponse.getErrmsg(), NotificationType.ERROR);
                            Notifications.Bus.notify(error, project);
                        } else {
                            String url = config.resolveCatUrl(yapiResponse.getCatId());
                            this.setClipboard(url);
                            Notification error = notificationGroup.createNotification("Success ,url:  " + url, NotificationType.INFORMATION);
                            Notifications.Bus.notify(error, project);
                        }
                    } catch (Exception e1) {
                        Notification error = notificationGroup.createNotification("Sorry ,upload api error cause:" + e1, NotificationType.ERROR);
                        Notifications.Bus.notify(error, project);
                    }
                }
            }
        }
    }

    @Nullable
    private VirtualFile resolveYapiConfigFile(Project project, Module module) {
        VirtualFile yapiConfigFile = null;
        if (module != null) {
            VirtualFile[] moduleContentRoots = ModuleRootManager.getInstance(module).getContentRoots();
            if (moduleContentRoots.length > 0) {
                yapiConfigFile = moduleContentRoots[0].findFileByRelativePath("src/main/resources/yapi.xml");
                if (yapiConfigFile == null) {
                    yapiConfigFile = moduleContentRoots[0].findFileByRelativePath("yapi.xml");
                }
            }
        }
        if (yapiConfigFile == null || !yapiConfigFile.exists()) {
            yapiConfigFile = project.getBaseDir().findFileByRelativePath("src/main/resources/yapi.xml");
            if (yapiConfigFile == null || !yapiConfigFile.exists()) {
                yapiConfigFile = project.getBaseDir().findFileByRelativePath("yapi.xml");
            }
        }
        return yapiConfigFile;
    }

    /**
     * @description: 设置到剪切板
     * @param: [content]
     * @return: void
     * @author: chengsheng@qbb6.com
     * @date: 2019/7/3
     */
    private void setClipboard(String content) {
        //获取系统剪切板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        //构建String数据类型
        StringSelection selection = new StringSelection(content);
        //添加文本到系统剪切板
        clipboard.setContents(selection, null);
    }
}
