package com.example.javamod01;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import net.minecraft.text.Text;

public class BlockPlacementHandler {
    private static final Logger LOGGER = ModLogger.LOGGER;

    private static KeyBinding PLACE_AIR_KEY;
    private static KeyBinding PLACE_BELOW_KEY;
    private static boolean placeOnAir = false;
    private static boolean placeBelow = false;
    private static long lastAirPlacement = 0;
    private static final int AIR_PLACEMENT_COOLDOWN = 50; // 50ms cooldown between placements
    private static MinecraftClient client;

    private static boolean placeBlock(BlockPos pos, Direction side) {
        if (client.player == null || client.world == null) {
            return false;
        }

        try {
            // Get the block state and ensure we can place
            BlockState state = client.world.getBlockState(pos);
            if (!state.isAir() || client.player.getMainHandStack().isEmpty()) {
                LOGGER.debug("Cannot place block at {}: either not air or no item in hand", pos);
                return false;
            }

            // Create a block hit result
            Vec3d hitPos = Vec3d.of(pos);
            BlockHitResult hitResult = new BlockHitResult(
                hitPos,
                side,
                pos,
                false
            );

            // Try to place the block
            client.interactionManager.interactBlock(
                client.player,
                Hand.MAIN_HAND,
                hitResult
            );

            // Verify placement immediately
            BlockState placedState = client.world.getBlockState(pos);
            if (!placedState.isAir()) {
                // Confirm with server
                client.interactionManager.updateBlockBreakingProgress(pos, side);
                return true;
            }

            return false;
        } catch (Exception e) {
            LOGGER.error("Error placing block at {}: {}", pos, e.getMessage());
            return false;
        }
    }

    public static boolean isPlaceOnAir() {
        return placeOnAir;
    }

    public static boolean isPlaceBelow() {
        return placeBelow;
    }

    public static void init() {
        PLACE_AIR_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.javamod01.place.air",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            "category.javamod01"
        ));

        PLACE_BELOW_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.javamod01.place.below",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_M,
            "category.javamod01"
        ));

        client = MinecraftClient.getInstance();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.currentScreen == null && client.player != null && client.world != null) {
                // Handle key presses
                if (PLACE_AIR_KEY.wasPressed()) {
                    placeOnAir = !placeOnAir;
                    client.player.sendMessage(Text.of("Place on air: " + placeOnAir), false);
                }
                if (PLACE_BELOW_KEY.wasPressed()) {
                    placeBelow = !placeBelow;
                    client.player.sendMessage(Text.of("Place below: " + placeBelow), false);
                }
                
                // Handle air placement when enabled
                if (placeOnAir) {
                    handleAirPlacement();
                }
                
                // Handle below placement
                if (placeBelow) {
                    handleBelowPlacement();
                }
            }
        });

        // Initialize key bindings
        PLACE_AIR_KEY.setPressed(false);
        PLACE_BELOW_KEY.setPressed(false);
    }

    public static void handleAirPlacement() {
        if (client.player == null || client.world == null) {
            return;
        }

        try {
            // Check cooldown
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastAirPlacement < AIR_PLACEMENT_COOLDOWN) {
                LOGGER.debug("Cooldown not ready yet");
                return;
            }

            // Get player position and look vector
            Vec3d playerPos = client.player.getPos();
            Vec3d lookVec = client.player.getRotationVector();
            
            // Calculate maximum reach distance (4.5 blocks)
            double reachDistance = 4.5;
            Vec3d targetPos = playerPos.add(lookVec.multiply(reachDistance));
            BlockPos targetBlockPos = new BlockPos((int)targetPos.x, (int)targetPos.y, (int)targetPos.z);
            
            // Calculate distance to target
            double distanceToTarget = playerPos.distanceTo(targetPos);
            if (distanceToTarget > reachDistance) {
                LOGGER.debug("Target too far away: {} > {}", distanceToTarget, reachDistance);
                return;
            }
            
            // Debug logging
            LOGGER.debug("Attempting to place block at: {}", targetBlockPos);
            LOGGER.debug("Player position: {}", playerPos);
            LOGGER.debug("Look vector: {}", lookVec);
            LOGGER.debug("Distance to target: {}", distanceToTarget);

            // Try to place the block
            if (placeBlock(targetBlockPos, Direction.UP)) {
                LOGGER.debug("Successfully placed block at: {}", targetBlockPos);
                lastAirPlacement = currentTime;
            } else {
                LOGGER.warn("Failed to place block at: {}", targetBlockPos);
            }
        } catch (Exception e) {
            LOGGER.error("Error in air placement: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    public static void handleBelowPlacement() {
        if (client.player == null || client.world == null) return;
        
        try {
            // Get the block below the player
            BlockPos targetBlockPos = client.player.getBlockPos().down();
            BlockState targetState = client.world.getBlockState(targetBlockPos);
            
            // Only place if it's air and we're not in spectator mode
            if (targetState.isAir() && !client.player.isSpectator()) {
                // Check if player is holding a block
                ItemStack heldItem = client.player.getMainHandStack();
                if (heldItem.isEmpty() || !(heldItem.getItem() instanceof BlockItem)) {
                    LOGGER.debug("No block item in hand");
                    return;
                }

                // Create a proper block hit result
                Vec3d hitPos = Vec3d.of(targetBlockPos);
                BlockHitResult hitResult = new BlockHitResult(
                    hitPos,
                    Direction.UP,
                    targetBlockPos,
                    false
                );

                // Try to place the block
                client.interactionManager.interactBlock(
                    client.player, 
                    Hand.MAIN_HAND, 
                    hitResult
                );
            }
        } catch (Exception e) {
            LOGGER.error("Error in below placement: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean canPlaceAt(net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos) {
        if (world == null) {
            LOGGER.warn("World is null!");
            return false;
        }

        BlockState blockState = world.getBlockState(pos);
        return blockState.isAir();
    }

    public static MinecraftClient getClient() {
        return client;
    }
}
