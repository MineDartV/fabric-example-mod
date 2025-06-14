package com.example.autoblockmod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class AutoBlockConfig {
    private static boolean placeOnAir = false;
    private static boolean isEnabled = true;
    private static boolean placeBelow = false;

    public static void init() {
        // Initialize default values
    }

    public static boolean isPlaceOnAir() {
        return placeOnAir;
    }

    public static void setPlaceOnAir(boolean placeOnAir) {
        AutoBlockConfig.placeOnAir = placeOnAir;
        Text message = Text.of("AutoBlock: Place on air " + (placeOnAir ? "enabled" : "disabled"));
        if (MinecraftClient.getInstance() != null && MinecraftClient.getInstance().inGameHud != null) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(message);
        }
    }

    public static boolean isEnabled() {
        return isEnabled;
    }

    public static void setEnabled(boolean enabled) {
        isEnabled = enabled;
        Text message = Text.of("AutoBlock: " + (enabled ? "enabled" : "disabled"));
        if (MinecraftClient.getInstance() != null && MinecraftClient.getInstance().inGameHud != null) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(message);
        }
    }

    public static boolean isPlaceBelow() {
        return placeBelow;
    }

    public static void setPlaceBelow(boolean placeBelow) {
        AutoBlockConfig.placeBelow = placeBelow;
        Text message = Text.of("AutoBlock: Place below " + (placeBelow ? "enabled" : "disabled"));
        if (MinecraftClient.getInstance() != null && MinecraftClient.getInstance().inGameHud != null) {
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(message);
        }
    }
}
