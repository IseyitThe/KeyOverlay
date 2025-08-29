package me.seyit.config;

public class KeyOverlayManager {
    private static KeyOverlayConfig config;
    
    public static KeyOverlayConfig getConfig() {
        if (config == null) {
            config = KeyOverlayConfig.load();
        }
        return config;
    }
    
    public static void setConfig(KeyOverlayConfig newConfig) {
        config = newConfig;
    }
}