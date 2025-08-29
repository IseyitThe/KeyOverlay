package me.seyit;

import me.seyit.config.KeyOverlayManager;
import net.fabricmc.api.ClientModInitializer;

public class KeyOverlay implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        KeyOverlayManager.getConfig();
    }
}