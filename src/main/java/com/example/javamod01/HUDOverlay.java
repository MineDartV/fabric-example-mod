package com.example.javamod01;

import net.minecraft.client.gui.DrawContext;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class HUDOverlay {
    public static void init() {
        HudRenderCallback.EVENT.register(((context, tickDelta) -> {
            int scaledWidth = context.getScaledWindowWidth();
            int scaledHeight = context.getScaledWindowHeight();
            
            // Render AutoClicker status
            AutoClicker.render(context, scaledWidth, scaledHeight);
        }));
    }
}
