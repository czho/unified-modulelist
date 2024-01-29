package dev.czho;

import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.network.chat.Component;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.events.client.EventUpdate;
import org.rusherhack.client.api.events.world.EventLoadWorld;
import org.rusherhack.client.api.feature.hud.ListHudElement;
import org.rusherhack.client.api.feature.module.IModule;
import org.rusherhack.client.api.feature.module.ToggleableModule;
import org.rusherhack.core.event.subscribe.Subscribe;
import org.rusherhack.core.setting.BooleanSetting;

import java.util.ArrayList;
import java.util.List;

public class UnifiedModuleListHudElement extends ListHudElement {
    final BooleanSetting lowercase = new BooleanSetting("Lowercase", false);
    List<ModuleHolder> modules = new ArrayList<>();

    public UnifiedModuleListHudElement() {
        super("UnifiedModuleList");
        registerSettings(lowercase);
    }

    public void loadModules() {
        modules.clear();
        for (IModule feature : RusherHackAPI.getModuleManager().getFeatures()) {
            if (feature instanceof ToggleableModule module) {
                modules.add(new ModuleHolder(module));
            }
        }

        for (Module module : Modules.get().getList()) {
            modules.add(new ModuleHolder(module));
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        loadModules();
    }

    @Subscribe
    public void onLoadWorld(EventLoadWorld event) {
        loadModules();
    }

    @Subscribe
    public void onTick(EventUpdate event) {
        //search thru modules list to see if any modules that are enabled, are not in the list, and then add them to the list
        for (ModuleHolder module : modules) {
            if (module.isEnabled() && module.isVisible()) {
                boolean foundModule = false;
                for (ListItem member : getMembers()) {
                    if (member instanceof ModuleListItem moduleListItem) {
                        if (moduleListItem.module.equals(module)) {
                            foundModule = true;
                        }
                    }
                }
                if (!foundModule) add(new ModuleListItem(module, this));
            }
        }
    }

    static class ModuleHolder {
        public Module meteorModule;
        public ToggleableModule rusherModule;
        public ModuleType moduleType;

        public ModuleHolder(Module meteorModule) {
            this.meteorModule = meteorModule;
            moduleType = ModuleType.METEOR;
        }

        public ModuleHolder(ToggleableModule rusherModule) {
            this.rusherModule = rusherModule;
            moduleType = ModuleType.RUSHER;
        }

        public boolean isEnabled() {
            if (moduleType == ModuleType.METEOR) {
                return meteorModule.isActive();
            } else if (moduleType == ModuleType.RUSHER) {
                return rusherModule.isToggled();
            }
            //unreachable
            throw new RuntimeException("Type not supported");
        }

        public String getName() {
            if (moduleType == ModuleType.METEOR) {
                return meteorModule.title;
            } else if (moduleType == ModuleType.RUSHER) {
                return rusherModule.getDisplayName();
            }
            //unreachable
            throw new RuntimeException("Type not supported");
        }

        public String getMetadata() {
            if (moduleType == ModuleType.METEOR) {
                return meteorModule.getInfoString();
            } else if (moduleType == ModuleType.RUSHER) {
                if (rusherModule.getMetadata().isEmpty()) return null;
                else return rusherModule.getMetadata();
            }
            //unreachable
            throw new RuntimeException("Type not supported");
        }

        public boolean isVisible() {
            if (moduleType == ModuleType.METEOR) {
                //todo: make this return actual value and not just true every time
                //meteor client doesn't let me access this without mixins or anything like that because hidden modules are stored in a setting in ActiveModulesHud, there is no way accessing this object easily from what i can see
                return true;
            } else if (moduleType == ModuleType.RUSHER) {
                return !rusherModule.isHidden();
            }
            //unreachable
            throw new RuntimeException("Type not supported");
        }

        public boolean equals(ModuleHolder moduleHolder) {
            if (moduleType == ModuleType.METEOR) {
                return meteorModule == moduleHolder.meteorModule;
            } else if (moduleType == ModuleType.RUSHER) {
                return rusherModule == moduleHolder.rusherModule;
            }
            //unreachable
            throw new RuntimeException("Type not supported");
        }

        public enum ModuleType {
            RUSHER,
            METEOR
        }
    }

    class ModuleListItem extends ListHudElement.ListItem {
        public ModuleHolder module;

        public ModuleListItem(ModuleHolder module, ListHudElement parent) {
            super(parent);

            this.module = module;
        }
        @Override
        public Component getText() {
            if (module.getMetadata() != null) {
                if (lowercase.getValue()) {
                    return Component.literal((module.getName() + " [" + module.getMetadata() + "]").toLowerCase());
                } else {
                    return Component.literal(module.getName() + " [" + module.getMetadata() + "]");
                }
            } else {
                if (lowercase.getValue()) {
                    return Component.literal((module.getName()).toLowerCase());
                } else {
                    return Component.literal(module.getName());
                }
            }
        }

        @Override
        public boolean shouldRemove() {
            return !module.isEnabled() || !module.isVisible();
        }
    }
}


