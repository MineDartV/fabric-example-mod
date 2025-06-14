package com.example.mixin.client;

import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockState;

public interface PlayerPreview {
    BlockPos getPreviewPos();
    BlockState getPreviewState();
    boolean isPreviewing();
}
