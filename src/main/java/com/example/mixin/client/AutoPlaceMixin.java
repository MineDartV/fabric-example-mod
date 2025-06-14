package com.example.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.example.autoblockmod.AutoBlockConfig;

@Mixin(ClientPlayerEntity.class)
public class AutoPlaceMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        MinecraftClient client = MinecraftClient.getInstance();
        
        // Only auto-place if mod is enabled
        if (!AutoBlockConfig.isEnabled()) return;
        
        // Only auto-place if place below is enabled
        if (!AutoBlockConfig.isPlaceBelow()) return;
        
        // Only auto-place if player is holding a block item
        if (player.getMainHandStack().isEmpty()) return;

        // Place block below player
        BlockPos targetPos = player.getBlockPos().down();
        BlockHitResult hitResult = new BlockHitResult(
            player.getPos().add(0, -1, 0),
            Direction.UP,
            targetPos,
            false
        );
        
        // Try to place the block
        player.swingHand(Hand.MAIN_HAND);
        client.interactionManager.interactBlock(player, Hand.MAIN_HAND, hitResult);
    }
}
