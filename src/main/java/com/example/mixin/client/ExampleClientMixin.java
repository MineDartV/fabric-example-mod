package com.example.mixin.client;

import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class ExampleClientMixin {
    @Inject(method = "main", at = @At("HEAD"))
    private static void onInitialize(String[] args, CallbackInfo info) {
        System.out.println("ExampleClientMixin initialized!");
    }
}
