package com.example.javamod01;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

public class AutoClicker {
    private static final Logger LOGGER = ModLogger.LOGGER;
    public static boolean isEnabled = false;
    private static long lastClickTime = 0;
    private static final long CLICK_DELAY = 20; // 20ms delay between clicks (50 clicks per second)
    private static KeyBinding TOGGLE_KEY;
    private static int clickCount = 0;

    public static void init() {
        TOGGLE_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.javamod01.autoclicker.toggle",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "category.javamod01"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.currentScreen == null && client.player != null) {
                if (TOGGLE_KEY.wasPressed()) {
                    isEnabled = !isEnabled;
                    client.player.sendMessage(Text.of("AutoClicker: " + (isEnabled ? "Enabled" : "Disabled")), false);
                }

                if (isEnabled) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastClickTime >= CLICK_DELAY) {
                        // Get the block the player is looking at
                        MinecraftClient mc = MinecraftClient.getInstance();
                        if (mc.player != null && mc.world != null) {
                            // Get the block hit result from the player's crosshair
                            BlockHitResult hitResult = (BlockHitResult) mc.crosshairTarget;
                            if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
                                // Perform the block interaction
                                mc.interactionManager.interactBlock(
                                    mc.player,
                                    Hand.MAIN_HAND,
                                    hitResult
                                );
                                
                                // Trigger the animation
                                mc.player.swingHand(Hand.MAIN_HAND);
                            }
                        }
                        lastClickTime = currentTime;
                        clickCount++;
                    }
                }
            }
        });
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
        if (isEnabled) {
            String text = "AutoClicker: On (50 CPS)";
            context.drawTextWithShadow(
                MinecraftClient.getInstance().textRenderer,
                text,
                scaledWidth - MinecraftClient.getInstance().textRenderer.getWidth(text) - 5,
                5,
                0x00FF00
            );
        }
    }
}
