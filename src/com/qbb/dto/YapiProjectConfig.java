package com.qbb.dto;

import com.qbb.constant.ProjectTypeConstant;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Yapi项目配置.
 */
public class YapiProjectConfig {

    String yapiUrl;

    String projectToken;

    String projectId;

    String projectType;

    String returnClass;

    String attachUpload;

    public static YapiProjectConfig readFromProperties(String props) throws IOException {
        Properties properties = new Properties();
        properties.load(new StringReader(props));

        YapiProjectConfig config = new YapiProjectConfig();
        config.yapiUrl = StringUtils.trim(properties.getProperty("yapiUrl"));
        config.projectToken = StringUtils.trim(properties.getProperty("projectToken"));
        config.projectId = StringUtils.trim(properties.getProperty("projectId"));
        config.projectType = StringUtils.trim(properties.getProperty("projectType"));
        config.returnClass = StringUtils.trim(properties.getProperty("returnClass"));
        config.attachUpload = StringUtils.trim(properties.getProperty("attachUpload"));
        return config;
    }

    public static YapiProjectConfig readFromXml(String xml) throws ParserConfigurationException, IOException, SAXException {
        return readFromXml(xml, null);
    }

    public static YapiProjectConfig readFromXml(String xml, String moduleName) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        Element root = doc.getDocumentElement();
        String rootName = root.getNodeName();
        if("project".equals(rootName)) {
            return readXmlYapiProjectConfigByOldVersion(doc);
        } else {
            NodeList nodes = root.getChildNodes();
            YapiProjectConfig rootConfig = readXmlYapiProjectConfigByNodeList(nodes);

            if (StringUtils.isNotEmpty(moduleName)) {
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    if (!"project".equals(node.getNodeName())) {
                        continue;
                    }
                    NamedNodeMap attributes = node.getAttributes();
                    String projectTagName = attributes.getNamedItem("name").getNodeValue();
                    if (moduleName.equalsIgnoreCase(projectTagName)) {
                        YapiProjectConfig moduleConfig = readXmlYapiProjectConfigByNodeList(node.getChildNodes());
                        mergeToFirst(rootConfig, moduleConfig);
                        break;
                    }
                }
            }
            return rootConfig;
        }
    }

    private static YapiProjectConfig readXmlYapiProjectConfigByOldVersion(Document doc) {
        YapiProjectConfig config = new YapiProjectConfig();
        NodeList nodes = doc.getElementsByTagName("option");
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String attributeName = node.getAttributes().getNamedItem("name").getNodeValue();
            switch (attributeName) {
                case "yapiUrl":
                    config.yapiUrl = node.getTextContent().trim();
                    break;
                case "projectToken":
                    config.projectToken = node.getTextContent().trim();
                    break;
                case "projectId":
                    config.projectId = node.getTextContent().trim();
                    break;
                case "projectType":
                    config.projectType = node.getTextContent().trim();
                    break;
                case "returnClass":
                    config.returnClass = node.getTextContent().trim();
                    break;
                case "attachUpload":
                    config.attachUpload = node.getTextContent().trim();
                    break;
            }
        }
        return config;
    }


    @NotNull
    private static YapiProjectConfig readXmlYapiProjectConfigByNodeList(NodeList nodes) {
        YapiProjectConfig config = new YapiProjectConfig();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            switch (node.getNodeName()) {
                case "yapiUrl":
                    config.yapiUrl = node.getTextContent().trim();
                    break;
                case "projectToken":
                    config.projectToken = node.getTextContent().trim();
                    break;
                case "projectId":
                    config.projectId = node.getTextContent().trim();
                    break;
                case "projectType":
                    config.projectType = node.getTextContent().trim();
                    break;
                case "returnClass":
                    config.returnClass = node.getTextContent().trim();
                    break;
                case "attachUpload":
                    config.attachUpload = node.getTextContent().trim();
                    break;
            }
        }
        return config;
    }

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

    /**
     * 配置合并.
     */
    public static void mergeToFirst(YapiProjectConfig a, YapiProjectConfig b) {
        if (b != null) {
            if (StringUtils.isNotEmpty(b.yapiUrl)) {
                a.yapiUrl = b.yapiUrl;
            }
            if (StringUtils.isNotEmpty(b.projectToken)) {
                a.projectToken = b.projectToken;
            }
            if (StringUtils.isNotEmpty(b.projectId)) {
                a.projectId = b.projectId;
            }
            if (StringUtils.isNotEmpty(b.projectType)) {
                a.projectType = b.projectType;
            }
            if (StringUtils.isNotEmpty(b.returnClass)) {
                a.returnClass = b.returnClass;
            }
            if (StringUtils.isNotEmpty(b.attachUpload)) {
                a.attachUpload = b.attachUpload;
            }
        }

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
