package dev.czho;

import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.feature.command.Command;
import org.rusherhack.core.command.annotations.CommandExecutor;


public class HideModuleCommand extends Command {

    public HideModuleCommand() {
        super("hidemodule", "Manages hidden module list modules");
    }

    @CommandExecutor(subCommand = "add")
    @CommandExecutor.Argument("string")
    private String addModule(String string) {
        if (RusherHackAPI.getHudManager().getFeature("UnifiedModuleList").isPresent()) {
            UnifiedModuleListHudElement hudElement = (UnifiedModuleListHudElement) RusherHackAPI.getHudManager().getFeature("UnifiedModuleList").get();

            if (!hudElement.isModuleLoaded(string.toLowerCase())) return "No module with name matches " + string;

            if (UnifiedModuleListHudElement.hiddenModules.contains(string.toLowerCase())) return string + " is already in list";
            UnifiedModuleListHudElement.hiddenModules.add(string.toLowerCase());

            hudElement.save();
            return "Added " + string + " to hidden module list";
        } else return "Hud element is not present";
    }

    @CommandExecutor(subCommand = "list")
    private String getModules() {
        if (RusherHackAPI.getHudManager().getFeature("UnifiedModuleList").isPresent()) {
            StringBuilder modules = new StringBuilder();
            for (String hiddenModule : UnifiedModuleListHudElement.hiddenModules) {
                modules.append(hiddenModule).append(", ");
            }
            return modules.toString();
        } else return "Hud element is not present";
    }

    @CommandExecutor(subCommand = "remove")
    @CommandExecutor.Argument("string")
    private String removeModule(String string) {
        if (RusherHackAPI.getHudManager().getFeature("UnifiedModuleList").isPresent()) {
            if (UnifiedModuleListHudElement.hiddenModules.remove(string.toLowerCase())) {return "Removed " + string + " from hidden modules";
            } else return string + " is not a loaded module";
        } else return "Hud element is not present";
    }
}
