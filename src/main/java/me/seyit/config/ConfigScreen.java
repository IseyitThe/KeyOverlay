package me.seyit.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ConfigScreen {
    
    public static Screen createConfigScreen(Screen parent) {
        KeyOverlayConfig config = KeyOverlayManager.getConfig();
        
        ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Component.literal("Key Overlay Config"));
        
        ConfigCategory general = builder.getOrCreateCategory(Component.literal("General"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        
        general.addEntry(entryBuilder.startEnumSelector(Component.literal("Position"), KeyOverlayConfig.Position.class, config.position)
            .setDefaultValue(KeyOverlayConfig.Position.TOP_LEFT)
            .setSaveConsumer(newValue -> config.position = newValue)
            .build());
        
        general.addEntry(entryBuilder.startColorField(Component.literal("Text Color"), config.textColor)
            .setDefaultValue(0xFFFFFF)
            .setSaveConsumer(newValue -> config.textColor = newValue)
            .build());
        
        general.addEntry(entryBuilder.startFloatField(Component.literal("Text Scale"), config.textScale)
            .setDefaultValue(0.9f)
            .setMin(0.5f)
            .setMax(3.0f)
            .setSaveConsumer(newValue -> config.textScale = newValue)
            .build());
        
        general.addEntry(entryBuilder.startIntSlider(Component.literal("X Offset"), config.offsetX, -10, 10)
            .setDefaultValue(0)
            .setSaveConsumer(newValue -> config.offsetX = newValue)
            .build());
        
        general.addEntry(entryBuilder.startIntSlider(Component.literal("Y Offset"), config.offsetY, -10, 10)
            .setDefaultValue(0)
            .setSaveConsumer(newValue -> config.offsetY = newValue)
            .build());
        
        general.addEntry(entryBuilder.startBooleanToggle(Component.literal("Show Background"), config.showBackground)
            .setDefaultValue(false)
            .setSaveConsumer(newValue -> config.showBackground = newValue)
            .build());
        
        general.addEntry(entryBuilder.startColorField(Component.literal("Background Color"), config.backgroundColor)
            .setDefaultValue(0x00002f)
            .setSaveConsumer(newValue -> config.backgroundColor = newValue)
            .build());
        
        general.addEntry(entryBuilder.startIntSlider(Component.literal("Background Opacity"), config.backgroundOpacity, 0, 255)
            .setDefaultValue(128)
            .setSaveConsumer(newValue -> config.backgroundOpacity = newValue)
            .build());
        
        general.addEntry(entryBuilder.startBooleanToggle(Component.literal("Hide On Active Slot"), config.hideOnActiveSlot)
            .setDefaultValue(false)
            .setTooltip(Component.literal("Hide keybind on currently selected hotbar slot"))
            .setSaveConsumer(newValue -> config.hideOnActiveSlot = newValue)
            .build());
        
        builder.setSavingRunnable(() -> {
            config.save();
            KeyOverlayManager.setConfig(config);
        });
        
        return builder.build();
    }
}
