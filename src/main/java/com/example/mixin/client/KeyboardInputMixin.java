package com.example.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.example.autoblockmod.AutoBlockConfig;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {
    private static long lastToggleTime = 0;
    private static final int TOGGLE_DELAY = 200; // 200ms delay between toggles

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        
        if (client.currentScreen == null) {
            long currentTime = System.currentTimeMillis();
            
            // Toggle place on air with N key
            if (InputUtil.isKeyPressed(client.getWindow().getHandle(), 'N')) {
                if (currentTime - lastToggleTime > TOGGLE_DELAY) {
                    AutoBlockConfig.setPlaceOnAir(!AutoBlockConfig.isPlaceOnAir());
                    lastToggleTime = currentTime;
                }
            }

            // Toggle place below with M key
            if (InputUtil.isKeyPressed(client.getWindow().getHandle(), 'M')) {
                if (currentTime - lastToggleTime > TOGGLE_DELAY) {
                    AutoBlockConfig.setPlaceBelow(!AutoBlockConfig.isPlaceBelow());
                    lastToggleTime = currentTime;
                }
            }
        }
    }
}
