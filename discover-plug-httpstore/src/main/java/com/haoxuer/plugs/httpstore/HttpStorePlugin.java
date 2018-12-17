package com.haoxuer.plugs.httpstore;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.haoxuer.discover.plug.api.StoragePlugin;
import com.haoxuer.discover.plug.data.entity.PluginConfig;
import com.haoxuer.discover.plug.data.vo.FileInfo;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.apache.commons.io.FileUtils;
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
  public String viewName() {
    return "httpstore";
  }

  private String remoteUrl;
  
  @Override
  public void upload(String path, File file, String contentType) {
    remoteUrl = "";
    PluginConfig pluginConfig = getPluginConfig();
    if (pluginConfig != null) {
      File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + path);
      if (!tempFile.getParentFile().exists()) {
        tempFile.getParentFile().mkdirs();
      }
      try {
        FileUtils.moveFile(file, tempFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
      
      String url = pluginConfig.getAttribute("url");
      HttpRequest httpRequest = HttpRequest
          .post(url)
          .form("file", tempFile);
      
      HttpResponse httpResponse = httpRequest.send();
      if (httpResponse != null && httpResponse.statusCode() == 200) {
        httpResponse.charset("utf-8");
        String body = httpResponse.bodyText();
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(body);
        if (element == null) {
          return;
        }
        remoteUrl = element.getAsJsonObject().get("url").getAsString();
        FileUtils.deleteQuietly(tempFile);
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