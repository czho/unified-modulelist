package dev.czho;

import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.plugin.Plugin;


public class UnifiedModuleListPlugin extends Plugin {
    @Override
    public void onLoad() {
        this.getLogger().info("Loaded Unified Module list plugin");

        final UnifiedModuleListHudElement unifiedModuleListHudElement = new UnifiedModuleListHudElement();
        RusherHackAPI.getHudManager().registerFeature(unifiedModuleListHudElement);
        //unifiedModuleListHudElement.loadModules();
    }

    @Override
    public void onUnload() {
        this.getLogger().info("Unloaded Unified Module list plugin");
    }
}