package com.example.javamod01.mixin;

import com.example.javamod01.AutoClicker;
import net.minecraft.client.Mouse;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class AutoClickerMixin {
    @Inject(at = @At("HEAD"), method = "onMouseButton")
    private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        if (AutoClicker.isEnabled && button == 1 && action == 1) {
            // Simulate right mouse button press
            MinecraftClient.getInstance().options.useKey.setPressed(true);
            ci.cancel(); // Cancel the original event
        } else if (button == 1 && action == 0) {
            // Handle right mouse button release
            MinecraftClient.getInstance().options.useKey.setPressed(false);
        }
    }
}
