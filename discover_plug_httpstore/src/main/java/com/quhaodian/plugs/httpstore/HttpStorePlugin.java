package com.quhaodian.plugs.httpstore;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.quhaodian.plug.api.StoragePlugin;
import com.quhaodian.plug.data.entity.PluginConfig;
import com.quhaodian.plug.data.vo.FileInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.springframework.stereotype.Component;

@Component("httpStorePlugin")
public class HttpStorePlugin extends StoragePlugin {
  
  
  @Override
  public String getName() {
    return "http文件上传插件";
  }
  
  @Override
  public String getVersion() {
    return "1.0";
  }
  
  @Override
  public String getAuthor() {
    return "ada.young";
  }
  
  @Override
  public String getSiteUrl() {
    return "";
  }
  
  @Override
  public String getInstallUrl() {
    return "admin/storage_plugin/httpstore/install.htm";
  }
  
  @Override
  public String getUninstallUrl() {
    return "admin/storage_plugin/httpstore/uninstall.htm";
  }
  
  @Override
  public String getSettingUrl() {
    return "admin/storage_plugin/httpstore/setting.htm";
  }
  
  private String remoteUrl;
  
  @Override
  public void upload(String path, File file, String contentType) {
    remoteUrl="";
    PluginConfig pluginConfig = getPluginConfig();
    if (pluginConfig != null) {
      String url = pluginConfig.getAttribute("url");
      HttpRequest httpRequest = HttpRequest
          .post(url)
          .form("file", file);
      
      HttpResponse httpResponse = httpRequest.send();
      if (httpResponse != null && httpResponse.statusCode() == 200) {
        httpResponse.charset("utf-8");
        String body = httpResponse.bodyText();
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(body);
        if (element==null){
          return;
        }
        remoteUrl=element.getAsJsonObject().get("url").getAsString();
      }
    }
  }
  
  @Override
  public String getUrl(String path) {
    return remoteUrl;
  }
  
  @Override
  public List<FileInfo> browser(String path) {
    List<FileInfo> fileInfos = new ArrayList<FileInfo>();
    PluginConfig pluginConfig = getPluginConfig();
    if (pluginConfig != null) {
      String disk = pluginConfig.getAttribute("disk");
      File directory = new File(disk + path);
      if (directory.exists() && directory.isDirectory()) {
        for (File file : directory.listFiles()) {
          FileInfo fileInfo = new FileInfo();
          fileInfo.setName(file.getName());
          fileInfo.setUrl(path + file.getName());
          fileInfo.setIsDirectory(file.isDirectory());
          fileInfo.setSize(file.length());
          fileInfo.setLastModified(new Date(file.lastModified()));
          fileInfos.add(fileInfo);
        }
      }
    }
    
    return fileInfos;
  }
  
}