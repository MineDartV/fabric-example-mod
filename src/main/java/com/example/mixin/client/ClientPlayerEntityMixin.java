package com.example.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.example.javamod01.BlockPlacementHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.BlockItem;
import org.lwjgl.glfw.GLFW;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;
import net.minecraft.util.Hand;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.MinecraftClient;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger("javamod01");

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        MinecraftClient client = BlockPlacementHandler.getClient();
        LOGGER.debug("Starting tick with client: {}", client);
        
        if (client.currentScreen == null) {
            LOGGER.debug("No screen is open, checking block placement modes");
            LOGGER.debug("Place on air: {}", BlockPlacementHandler.isPlaceOnAir());
            LOGGER.debug("Place below: {}", BlockPlacementHandler.isPlaceBelow());
            
            if (BlockPlacementHandler.isPlaceOnAir()) {
                LOGGER.debug("Air placement mode is active");
                long window = client.getWindow().getHandle();
                boolean rightClickPressed = GLFW.glfwGetMouseButton(window, GLFW.GLFW_MOUSE_BUTTON_RIGHT) == GLFW.GLFW_PRESS;
                LOGGER.debug("Right click pressed: {}", rightClickPressed);
                
                if (rightClickPressed) {
                    ItemStack stack = client.player.getMainHandStack();
                    LOGGER.debug("Main hand stack: {}", stack);
                    
                    if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem)) {
                        LOGGER.debug("No block in hand or invalid item");
                        return;
                    }

                    Vec3d hitPos = client.player.getPos().add(0, 1.5, 0);
                    double range = 5.0;
                    Vec3d lookVec = client.player.getRotationVec(1.0f);
                    
                    LOGGER.debug("Looking for air block in range {} with position {} and direction {}", range, hitPos, lookVec);
                    
                    BlockPos targetPos = null;
                    for (double d = 0.0; d <= range; d += 0.1) {
                        Vec3d pos = hitPos.add(lookVec.x * d, lookVec.y * d, lookVec.z * d);
                        BlockPos blockPos = new BlockPos((int)pos.x, (int)pos.y, (int)pos.z);
                        
                        LOGGER.debug("Checking position {}", blockPos);
                        boolean isAir = client.player.getWorld().getBlockState(blockPos).isAir();
                        LOGGER.debug("Position {} is air: {}", blockPos, isAir);
                        
                        if (isAir) {
                            targetPos = blockPos;
                            break;
                        }
                    }
                    
                    if (targetPos != null) {
                        LOGGER.info("Found air position: {}", targetPos);
                        BlockHitResult hitResult = new BlockHitResult(new Vec3d(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5), Direction.UP, targetPos, false);
                        
                        LOGGER.debug("Creating hit result: {}", hitResult);
                        client.player.swingHand(Hand.MAIN_HAND);
                        LOGGER.debug("Swung hand");
                        
                        try {
                            LOGGER.debug("Attempting to place block at {}", targetPos);
                            // Use client.interactionManager directly
                            client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hitResult);
                            LOGGER.info("Attempted to place block at {}", targetPos);
                        } catch (Exception e) {
                            LOGGER.error("Failed to place block", e);
                        }
                    } else {
                        LOGGER.debug("No air position found in range");
                    }
                }
            }
            
            if (BlockPlacementHandler.isPlaceBelow()) {
                LOGGER.debug("Below placement mode is active");
                ItemStack stack = client.player.getMainHandStack();
                LOGGER.debug("Main hand stack: {}", stack);
                
                if (stack.isEmpty() || !(stack.getItem() instanceof BlockItem)) {
                    LOGGER.debug("No block in hand or invalid item");
                    return;
                }

                BlockPos playerPos = client.player.getBlockPos();
                BlockPos belowPos = playerPos.down();
                
                LOGGER.debug("Checking below position: {}", belowPos);
                
                if (client.player.getWorld().getBlockState(belowPos).isAir()) {
                    LOGGER.info("Found air below player");
                    BlockHitResult hitResult = new BlockHitResult(new Vec3d(belowPos.getX() + 0.5, belowPos.getY() + 0.5, belowPos.getZ() + 0.5), Direction.UP, belowPos, false);
                    
                    LOGGER.debug("Creating hit result: {}", hitResult);
                    client.player.swingHand(Hand.MAIN_HAND);
                    LOGGER.debug("Swung hand");
                    
                    try {
                        LOGGER.debug("Attempting to place block below player at {}", belowPos);
                        // Use client.interactionManager directly
                        client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hitResult);
                        LOGGER.info("Attempted to place block below player at {}", belowPos);
                    } catch (Exception e) {
                        LOGGER.error("Failed to place block below player", e);
                    }
                } else {
                    LOGGER.debug("Position below player is not air");
                }
            }
        }
    }
}
