package me.seyit.mixin;

import me.seyit.config.KeyOverlayConfig;
import me.seyit.config.KeyOverlayManager;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class InGameHudMixin {
    
    @Shadow @Final private Minecraft minecraft;
    
    private static final int SLOT_SIZE = 20;
    private static final int HOTBAR_WIDTH = 182;
    private static final int HOTBAR_HEIGHT = 22;
    
    @Inject(method = "extractItemHotbar", at = @At("TAIL"))
    private void renderKeybindOverlay(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (this.minecraft.player == null) return;
        
        KeyOverlayConfig config = KeyOverlayManager.getConfig();
        
        int scaledWidth = graphics.guiWidth();
        int scaledHeight = graphics.guiHeight();
        
        int hotbarX = (scaledWidth - HOTBAR_WIDTH) / 2;
        int hotbarY = scaledHeight - HOTBAR_HEIGHT - 1;
        
        Player player = this.minecraft.player;
        int selectedSlot = player.getInventory().getSelectedSlot();
        
        for (int i = 0; i < 9; i++) {
            if (config.hideOnActiveSlot && i == selectedSlot) {
                continue;
            }
            
            KeyMapping keyBinding = this.minecraft.options.keyHotbarSlots[i];
            String keyText = getKeyDisplayString(keyBinding);
            
            if (keyText.isEmpty()) continue;
            
            int slotX = hotbarX + 3 + i * 20;
            int slotY = hotbarY + 3;
            
            renderKeybindText(graphics, keyText, slotX, slotY, config);
        }
    }
    
    private String getKeyDisplayString(KeyMapping keyBinding) {
        if (keyBinding.isUnbound()) {
            return "";
        }
        
        String keyName = keyBinding.getTranslatedKeyMessage().getString();
        
        if (keyName.toLowerCase().contains("space")) return "SPC";
        if (keyName.toLowerCase().contains("shift")) {
            if (keyName.toLowerCase().contains("left")) return "LSH";
            if (keyName.toLowerCase().contains("right")) return "RSH";
            return "SHF";
        }
        if (keyName.toLowerCase().contains("control") || keyName.toLowerCase().contains("ctrl")) {
            if (keyName.toLowerCase().contains("left")) return "LCT";
            if (keyName.toLowerCase().contains("right")) return "RCT";
            return "CTL";
        }
        if (keyName.toLowerCase().contains("alt")) {
            if (keyName.toLowerCase().contains("left")) return "LAL";
            if (keyName.toLowerCase().contains("right")) return "RAL";
            return "ALT";
        }
        if (keyName.toLowerCase().contains("enter")) return "ENT";
        if (keyName.toLowerCase().contains("escape")) return "ESC";
        if (keyName.toLowerCase().contains("tab")) return "TAB";
        if (keyName.toLowerCase().contains("caps")) return "CAP";
        if (keyName.toLowerCase().contains("backspace")) return "BSP";
        
        if (keyName.matches(".*[0-9].*")) {
            String number = keyName.replaceAll("[^0-9]", "");
            if (!number.isEmpty()) return number;
        }
        
        if (keyName.length() <= 3) {
            return keyName.toUpperCase();
        }
        
        return keyName.substring(0, 3).toUpperCase();
    }
    
    private void renderKeybindText(GuiGraphicsExtractor graphics, String text, int slotX, int slotY, KeyOverlayConfig config) {
        Font font = this.minecraft.font;
        
        int textWidth = (int) (font.width(text) * config.textScale);
        int textHeight = (int) (font.lineHeight * config.textScale);
        
        int textX, textY;
        
        switch (config.position) {
            case TOP_LEFT:
                textX = slotX + config.offsetX;
                textY = slotY + config.offsetY;
                break;
            case TOP_RIGHT:
                textX = slotX + SLOT_SIZE - textWidth + config.offsetX;
                textY = slotY + config.offsetY;
                break;
            case BOTTOM_LEFT:
                textX = slotX + config.offsetX;
                textY = slotY + SLOT_SIZE - textHeight + config.offsetY;
                break;
            case BOTTOM_RIGHT:
            default:
                textX = slotX + SLOT_SIZE - textWidth + config.offsetX;
                textY = slotY + SLOT_SIZE - textHeight + config.offsetY;
                break;
        }
        
        if (config.showBackground) {
            int bgColor = (config.backgroundOpacity << 24) | (config.backgroundColor & 0xFFFFFF);
            graphics.fill(textX - 1, textY - 1, textX + textWidth + 1, textY + textHeight + 1, bgColor);
        }
        
        int color = 0xFF000000 | (config.textColor & 0xFFFFFF);
        
        if (config.textScale != 1.0f) {
            graphics.pose().pushMatrix();
            graphics.pose().translate(textX, textY);
            graphics.pose().scale(config.textScale, config.textScale);
            graphics.text(font, text, 0, 0, color, false);
            graphics.pose().popMatrix();
        } else {
            graphics.text(font, text, textX, textY, color, false);
        }
    }
}
