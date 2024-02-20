package dev.czho;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.plugin.Plugin;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UnifiedModuleListPlugin extends Plugin {
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static final String configFile = RusherHackAPI.getConfigPath() + "/unifiedmodulelist.json";
    @Override
    public void onLoad() {
        this.getLogger().info("Loaded Unified Module list plugin");

        final UnifiedModuleListHudElement unifiedModuleListHudElement = new UnifiedModuleListHudElement();
        RusherHackAPI.getHudManager().registerFeature(unifiedModuleListHudElement);

        unifiedModuleListHudElement.load();

        final HideModuleCommand hideModuleCommand = new HideModuleCommand();
        RusherHackAPI.getCommandManager().registerFeature(hideModuleCommand);

        final ListModulesCommand listModulesCommand = new ListModulesCommand();
        RusherHackAPI.getCommandManager().registerFeature(listModulesCommand);
    }

    @Override
    public void onUnload() {
        this.getLogger().info("Unloaded Unified Module list plugin");
    }

    static void saveConfig(List<String> list) {
        try (Writer writer = new FileWriter(configFile)) {
            gson.toJson(list, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static List<String> loadConfig() {
        List<String> loadedList = new ArrayList<>();

        try (Reader reader = new FileReader(configFile)) {
            Type listType = new TypeToken<ArrayList<String>>(){}.getType();
            loadedList.addAll(gson.fromJson(reader, listType));
        } catch (FileNotFoundException e) {
            saveConfig(new ArrayList<>());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return loadedList;
    }
}