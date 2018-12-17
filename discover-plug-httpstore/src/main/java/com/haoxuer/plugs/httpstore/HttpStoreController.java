package com.haoxuer.plugs.httpstore;


import javax.annotation.Resource;

import com.haoxuer.discover.plug.api.IPlugin;
import com.haoxuer.discover.plug.data.entity.PluginConfig;
import com.haoxuer.discover.plug.data.plugs.base.PlugTemplateController;
import com.haoxuer.discover.plug.data.service.PluginConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/plugin_storage/httpstore")
public class HttpStoreController extends PlugTemplateController {
  
  @Resource(name = "httpStorePlugin")
  private HttpStorePlugin httpStorePlugin;
  
  @Resource(name = "pluginConfigServiceImpl")
  private PluginConfigService pluginConfigService;
  
  @Override
  public PluginConfig getPluginConfig() {
    return httpStorePlugin.getPluginConfig();
  }
  
  @Override
  public IPlugin getPlug() {
    return httpStorePlugin;
  }
  
  @Override
  public PluginConfigService getPluginConfigService() {
    return pluginConfigService;
  }
  
  @Override
  public String getView() {
    return "/admin/plugin_storage";
  }
  
  @Override
  public String getSettingView() {
    return "httpstore";
  }
}
