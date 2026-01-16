package com.example.javamod01;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class AutoClicker {
    public static boolean isEnabled = false;
    private static long lastClickTime = 0;
    private static final long CLICK_DELAY = 20; // 20ms delay between clicks (50 clicks per second)
    private static int clickCount = 0;
    private static KeyBinding toggleKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoClicker.class);

    public static void init() {
        try {
            LOGGER.info("Initializing AutoClicker");
            
            // Register key binding
            toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.javamod01.autoclicker.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                "category.javamod01.clicker"
            ));

            // Register key input event
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (client.currentScreen == null && client.player != null) {
                    if (toggleKey.wasPressed()) {
                        isEnabled = !isEnabled;
                        client.player.sendMessage(Text.of("AutoClicker: " + (isEnabled ? "Enabled" : "Disabled")), false);
                        LOGGER.info("AutoClicker toggled to: {}", isEnabled);
                    }
                }
            });

            // Register separate tick event for autoclicking
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (client.currentScreen == null && client.player != null && client.world != null) {
                    if (isEnabled) {
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - lastClickTime >= CLICK_DELAY) {
                            // Get the block the player is looking at
                            if (client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.BLOCK) {
                                BlockHitResult hitResult = (BlockHitResult) client.crosshairTarget;
                                // Perform the block interaction
                                client.interactionManager.interactBlock(
                                    client.player,
                                    Hand.MAIN_HAND,
                                    hitResult
                                );
                                
                                // Trigger the animation
                                client.player.swingHand(Hand.MAIN_HAND);
                                LOGGER.debug("AutoClicker clicked block");
                            }
                            lastClickTime = currentTime;
                            clickCount++;
                            LOGGER.debug("AutoClicker click count: {}", clickCount);
                        }
                    }
                }
            });
            
            LOGGER.info("AutoClicker initialized successfully");
        } catch (Exception e) {
            LOGGER.error("Error initializing AutoClicker", e);
            throw e;
        }
    }

    public static boolean isEnabled() {
        return isEnabled;
    }

    public static int getClickCount() {
        return clickCount;
    }

    public static void resetClickCount() {
        clickCount = 0;
    }

    public static void render(DrawContext context, int scaledWidth, int scaledHeight) {
        String statusText = "AutoClicker: " + (isEnabled ? "On" : "Off") + " (50 CPS)";
        int statusX = scaledWidth - MinecraftClient.getInstance().textRenderer.getWidth(statusText) - 5;
        int statusY = 5;
        int color = isEnabled ? 0x00FF00 : 0xFF0000;
        
        context.drawTextWithShadow(
            MinecraftClient.getInstance().textRenderer,
            statusText,
            statusX,
            statusY,
            color
        );
        
        if (isEnabled) {
            String clickText = "Clicks: " + clickCount;
            context.drawTextWithShadow(
                MinecraftClient.getInstance().textRenderer,
                clickText,
                statusX,
                statusY + 10,
                0x00FF00
            );
        }
    }
}
