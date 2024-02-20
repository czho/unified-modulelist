package dev.czho;

import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.feature.command.Command;
import org.rusherhack.core.command.annotations.CommandExecutor;


public class ListModulesCommand extends Command {

    public ListModulesCommand() {
        super("listmodules", "description");
    }


    @CommandExecutor
    private String allModules() {
        if (RusherHackAPI.getHudManager().getFeature("UnifiedModuleList").isPresent()) {
            UnifiedModuleListHudElement hudElement = (UnifiedModuleListHudElement) RusherHackAPI.getHudManager().getFeature("UnifiedModuleList").get();
            StringBuilder modules = new StringBuilder();

            for (UnifiedModuleListHudElement.ModuleHolder module : hudElement.modules) {
                modules.append(module.getId()).append(", ");
            }
            return modules.toString();

        } else return "Hud element is not present";
    }
}
