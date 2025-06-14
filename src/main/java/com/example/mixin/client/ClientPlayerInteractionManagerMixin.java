package com.example.mixin.client;

import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.example.autoblockmod.AutoBlockConfig;
import org.slf4j.Logger;
import com.example.javamod01.ModLogger;
import com.example.javamod01.BlockPlacementHandler;

@Mixin(value = ClientPlayerInteractionManager.class, remap = true)
public class ClientPlayerInteractionManagerMixin {
    private static final Logger LOGGER = ModLogger.LOGGER;
    private static long lastRightClick = 0;
    private static final int COOLDOWN = 200;

    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    private void onInteractBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<BlockHitResult> cir) {
        LOGGER.info("onInteractBlock called with hand: {}", hand);
        cir.setReturnValue(hitResult);
        cir.cancel();
        MinecraftClient client = MinecraftClient.getInstance();
        LOGGER.info("MinecraftClient instance: {}", client);
        
        if (client.currentScreen == null && BlockPlacementHandler.isPlaceOnAir()) {
            LOGGER.info("Place on air enabled: {}", BlockPlacementHandler.isPlaceOnAir());
            long currentTime = System.currentTimeMillis();
            
            // Only process if cooldown has passed
            if (currentTime - lastRightClick < COOLDOWN) {
                LOGGER.info("Cooldown not passed: {}ms remaining", COOLDOWN - (currentTime - lastRightClick));
                return;
            }

            // Only process right click (button 1)
            if (hand != Hand.MAIN_HAND) {
                LOGGER.info("Non-main hand click ignored");
                return;
            }

            // Only place if player is holding a block
            if (client.player.getMainHandStack().isEmpty()) {
                LOGGER.info("No item in main hand");
                return;
            }

            // Check if we can place here
            if (!BlockPlacementHandler.canPlaceAt(client.player.getWorld(), hitResult.getBlockPos())) {
                LOGGER.info("Cannot place at target block");
                return;
            }

            // Place the block
            LOGGER.info("Placing block at position: {}", hitResult.getBlockPos());
            client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hitResult);
            lastRightClick = currentTime;
            cir.setReturnValue(hitResult);
            cir.cancel();
        }
    }
}
