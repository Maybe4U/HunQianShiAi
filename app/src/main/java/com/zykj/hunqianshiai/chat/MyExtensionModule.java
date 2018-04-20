package com.zykj.hunqianshiai.chat;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.ImagePlugin;
import io.rong.imkit.widget.provider.FilePlugin;
import io.rong.imlib.model.Conversation;

/**
 * Created by xu on 2018/1/18.
 */

public class MyExtensionModule extends DefaultExtensionModule {
    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
//        List<IPluginModule> pluginModules =  super.getPluginModules(conversationType);
        List<IPluginModule> pluginModuleList = new ArrayList<>();
        pluginModuleList.add(new ImagePlugin());
//        pluginModuleList.add(new PaiShePlugin());
        IPluginModule file = new FilePlugin();
        return pluginModuleList;
    }
}
