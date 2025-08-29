package me.seyit.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigScreen {
    
    public static Screen createConfigScreen(Screen parent) {
        KeyOverlayConfig config = KeyOverlayManager.getConfig();
        
        ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Text.literal("Key Overlay Config"));
        
        ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        
        general.addEntry(entryBuilder.startEnumSelector(Text.literal("Position"), KeyOverlayConfig.Position.class, config.position)
            .setDefaultValue(KeyOverlayConfig.Position.TOP_LEFT)
            .setSaveConsumer(newValue -> config.position = newValue)
            .build());
        
        general.addEntry(entryBuilder.startColorField(Text.literal("Text Color"), config.textColor)
            .setDefaultValue(0xFFFFFF)
            .setSaveConsumer(newValue -> config.textColor = newValue)
            .build());
        
        general.addEntry(entryBuilder.startFloatField(Text.literal("Text Scale"), config.textScale)
            .setDefaultValue(0.9f)
            .setMin(0.5f)
            .setMax(3.0f)
            .setSaveConsumer(newValue -> config.textScale = newValue)
            .build());
        
        general.addEntry(entryBuilder.startIntSlider(Text.literal("X Offset"), config.offsetX, -10, 10)
            .setDefaultValue(0)
            .setSaveConsumer(newValue -> config.offsetX = newValue)
            .build());
        
        general.addEntry(entryBuilder.startIntSlider(Text.literal("Y Offset"), config.offsetY, -10, 10)
            .setDefaultValue(0)
            .setSaveConsumer(newValue -> config.offsetY = newValue)
            .build());
        
        general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Show Background"), config.showBackground)
            .setDefaultValue(false)
            .setSaveConsumer(newValue -> config.showBackground = newValue)
            .build());
        
        general.addEntry(entryBuilder.startColorField(Text.literal("Background Color"), config.backgroundColor)
            .setDefaultValue(0x00002f)
            .setSaveConsumer(newValue -> config.backgroundColor = newValue)
            .build());
        
        general.addEntry(entryBuilder.startIntSlider(Text.literal("Background Opacity"), config.backgroundOpacity, 0, 255)
            .setDefaultValue(128)
            .setSaveConsumer(newValue -> config.backgroundOpacity = newValue)
            .build());
        
        general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Hide On Active Slot"), config.hideOnActiveSlot)
            .setDefaultValue(false)
            .setTooltip(Text.literal("Hide keybind on currently selected hotbar slot"))
            .setSaveConsumer(newValue -> config.hideOnActiveSlot = newValue)
            .build());
        
        builder.setSavingRunnable(() -> {
            config.save();
            KeyOverlayManager.setConfig(config);
        });
        
        return builder.build();
    }
}