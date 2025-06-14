package com.example.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.example.autoblockmod.AutoBlockConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

@Mixin(ClientPlayerEntity.class)
public class PlayerInteractionMixin {
    private static long lastRightClick = 0;
    private static final int COOLDOWN = 200;
    private static boolean isPlacing = false;

    @Inject(method = "swingHand", at = @At("HEAD"))
    private void onSwingHand(Hand hand, CallbackInfo ci) {
        if (hand == Hand.MAIN_HAND && AutoBlockConfig.isEnabled() && AutoBlockConfig.isPlaceOnAir() && !isPlacing) {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;

            if (client.currentScreen == null) {
                long currentTime = System.currentTimeMillis();
                
                // Only process if cooldown has passed
                if (currentTime - lastRightClick < COOLDOWN) {
                    return;
                }

                // Only place if player is holding a block
                if (player.getMainHandStack().isEmpty()) {
                    return;
                }

                // Calculate the position to place the block
                Vec3d playerPos = player.getPos();
                Vec3d lookVec = player.getRotationVector();
                Vec3d hitPos = playerPos.add(lookVec.multiply(5)); // Extend 5 blocks in front of the player
                BlockPos targetPos = new BlockPos((int)hitPos.x, (int)hitPos.y, (int)hitPos.z);

                // Get the item stack and world
                ItemStack stack = player.getMainHandStack();
                World world = player.getWorld();

                // Place the block directly in the world
                if (world != null && stack != null) {
                    isPlacing = true;
                    Block block = Block.getBlockFromItem(stack.getItem());
                    BlockState state = block.getDefaultState();
                    world.setBlockState(targetPos, state);
                    isPlacing = false;
                }

                // Update last right click time
                lastRightClick = currentTime;
            }
        }
    }
}
