package com.example.javamod01;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.javamod01.menu.MenuSystem;

public class HUDOverlay {
    private static final Logger LOGGER = LoggerFactory.getLogger(HUDOverlay.class);
    public static void init() {
        try {
            LOGGER.info("Initializing HUDOverlay");
            
            HudRenderCallback.EVENT.register((context, tickDelta) -> {
                try {
                    MinecraftClient client = MinecraftClient.getInstance();
                    if (client == null) {
                        LOGGER.warn("MinecraftClient is null in HUD rendering!");
                        return;
                    }
                    
                    if (client.currentScreen == null) {
                        int scaledWidth = client.getWindow().getScaledWidth();
                        int scaledHeight = client.getWindow().getScaledHeight();
                        
                        // Render AutoClicker status
                        AutoClicker.render(context, scaledWidth, scaledHeight);
                        
                        // Only render menu when it's open
                        if (MenuSystem.isOpen()) {
                            MenuSystem.render(context, scaledWidth, scaledHeight);
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Error in HUD rendering", e);
                }
            });
            
            LOGGER.info("HUDOverlay initialized successfully");
        } catch (Exception e) {
            LOGGER.error("Error initializing HUDOverlay", e);
            throw e;
        }
    }
}
