package me.seyit.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class KeyOverlayConfig {
    
    public enum Position {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }
    
    public Position position = Position.TOP_RIGHT;
    public int textColor = 0xFFFFFF;
    public int offsetX = 0;
    public int offsetY = 0;
    public float textScale = 1.0f;
    public boolean showBackground = false;
    public int backgroundColor = 0x000000;
    public int backgroundOpacity = 128;
    public boolean hideOnActiveSlot = false;
    
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "keyoverlay.json");
    
    public static KeyOverlayConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                return GSON.fromJson(reader, KeyOverlayConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new KeyOverlayConfig();
    }
    
    public void save() {
        try {
            CONFIG_FILE.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}