package com.example.javamod01;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;

public class PreviewState {
    private static final Logger LOGGER = ModLogger.LOGGER;
    private static BlockPos previewPos = null;
    private static BlockState previewState = null;
    private static boolean previewing = false;
    private static long lastRightClick = 0;
    private static final long COOLDOWN = 100L; // 100ms cooldown

    public static void setPreviewPos(BlockPos pos) {
        previewPos = pos;
    }

    public static BlockPos getPreviewPos() {
        return previewPos;
    }

    public static void setPreviewState(BlockState state) {
        previewState = state;
    }

    public static BlockState getPreviewState() {
        return previewState;
    }

    public static void setPreviewing(boolean previewing) {
        PreviewState.previewing = previewing;
    }

    public static boolean isPreviewing() {
        return previewing;
    }

    public static void setLastRightClick(long time) {
        lastRightClick = time;
    }

    public static long getLastRightClick() {
        return lastRightClick;
    }

    public static long getCOOLDOWN() {
        return COOLDOWN;
    }
}
