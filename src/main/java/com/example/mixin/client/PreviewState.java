package com.example.mixin.client;

import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockState;

public class PreviewState {
    private static long lastRightClick = 0;
    private static final int COOLDOWN = 200;
    private static BlockPos previewPos = null;
    private static BlockState previewState = null;
    private static boolean isPreviewing = false;

    public static long getLastRightClick() {
        return lastRightClick;
    }

    public static void setLastRightClick(long lastRightClick) {
        PreviewState.lastRightClick = lastRightClick;
    }

    public static int getCOOLDOWN() {
        return COOLDOWN;
    }

    public static BlockPos getPreviewPos() {
        return previewPos;
    }

    public static void setPreviewPos(BlockPos previewPos) {
        PreviewState.previewPos = previewPos;
    }

    public static BlockState getPreviewState() {
        return previewState;
    }

    public static void setPreviewState(BlockState previewState) {
        PreviewState.previewState = previewState;
    }

    public static boolean isPreviewing() {
        return isPreviewing;
    }

    public static void setPreviewing(boolean previewing) {
        PreviewState.isPreviewing = previewing;
    }
}
