package com.quhaodian.plugs.httpstore;


import com.quhaodian.plug.api.IPlugin;
import com.quhaodian.plug.data.entity.PluginConfig;
import com.quhaodian.plug.data.plugs.base.PlugTemplateController;
import com.quhaodian.plug.data.service.PluginConfigService;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/storage_plugin/httpstore")
public class HttpStoreController extends PlugTemplateController {
  
  @Resource(name = "httpStorePlugin")
  private HttpStorePlugin diskFilePlugin;
  
  @Resource(name = "pluginConfigServiceImpl")
  private PluginConfigService pluginConfigService;
  
  @Override
  public PluginConfig getPluginConfig() {
    return diskFilePlugin.getPluginConfig();
  }
  
  @Override
  public IPlugin getPlug() {
    return diskFilePlugin;
  }
  
  @Override
  public PluginConfigService getPluginConfigService() {
    return pluginConfigService;
  }
  
  @Override
  public String getView() {
    return "/admin/storage_plugin";
  }
  
  @Override
  public String getSettingView() {
    return "httpstore";
  }
}
