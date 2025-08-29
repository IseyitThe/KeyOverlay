package me.seyit.mixin;

import me.seyit.config.KeyOverlayConfig;
import me.seyit.config.KeyOverlayManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    
    @Shadow @Final private MinecraftClient client;
    
    private static final int SLOT_SIZE = 20;
    private static final int HOTBAR_WIDTH = 182;
    private static final int HOTBAR_HEIGHT = 22;
    
    @Inject(method = "renderHotbar", at = @At("TAIL"))
    private void renderKeybindOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        if (this.client.player == null) return;
        
        KeyOverlayConfig config = KeyOverlayManager.getConfig();
        
        int scaledWidth = this.client.getWindow().getScaledWidth();
        int scaledHeight = this.client.getWindow().getScaledHeight();
        
        int hotbarX = (scaledWidth - HOTBAR_WIDTH) / 2;
        int hotbarY = scaledHeight - HOTBAR_HEIGHT - 1;
        
        PlayerEntity player = this.client.player;
        int selectedSlot = player.getInventory().getSelectedSlot();
        
        for (int i = 0; i < 9; i++) {
            if (config.hideOnActiveSlot && i == selectedSlot) {
                continue;
            }
            
            KeyBinding keyBinding = this.client.options.hotbarKeys[i];
            String keyText = getKeyDisplayString(keyBinding);
            
            if (keyText.isEmpty()) continue;
            
            int slotX = hotbarX + 3 + i * 20;
            int slotY = hotbarY + 3;
            
            renderKeybindText(context, keyText, slotX, slotY, config);
        }
    }
    
    private String getKeyDisplayString(KeyBinding keyBinding) {
        if (keyBinding.isUnbound()) {
            return "";
        }
        
        String keyName = keyBinding.getBoundKeyLocalizedText().getString();
        
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
    
    private void renderKeybindText(DrawContext drawContext, String text, int slotX, int slotY, KeyOverlayConfig config) {
        TextRenderer textRenderer = this.client.textRenderer;
        
        int textWidth = (int)(textRenderer.getWidth(text) * config.textScale);
        int textHeight = (int)(textRenderer.fontHeight * config.textScale);
        
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
            drawContext.fill(textX - 1, textY - 1, textX + textWidth + 1, textY + textHeight + 1, bgColor);
        }
        
        int color = 0xFF000000 | (config.textColor & 0xFFFFFF);
        
        if (config.textScale != 1.0f) {
            drawContext.getMatrices().pushMatrix();
            drawContext.getMatrices().translate(textX, textY);
            drawContext.getMatrices().scale(config.textScale, config.textScale);
            drawContext.drawText(textRenderer, text, 0, 0, color, false);
            drawContext.getMatrices().popMatrix();
        } else {
            drawContext.drawText(textRenderer, text, textX, textY, color, false);
        }
    }
}