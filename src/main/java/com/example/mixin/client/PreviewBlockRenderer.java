package com.example.mixin.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.render.RenderLayer;

public class PreviewBlockRenderer {
    public static void renderPreviewBlock(MatrixStack matrices, VertexConsumerProvider vertexConsumers, float tickDelta) {
        if (!PreviewState.isPreviewing()) return;

        BlockRenderManager renderManager = MinecraftClient.getInstance().getBlockRenderManager();
        BlockState state = PreviewState.getPreviewState();
        BlockPos pos = PreviewState.getPreviewPos();
        
        if (state != null && pos != null) {
            matrices.push();
            matrices.translate(pos.getX(), pos.getY(), pos.getZ());
            matrices.translate(0.5, 0.5, 0.5);
            matrices.scale(1.01f, 1.01f, 1.01f);
            matrices.translate(-0.5, -0.5, -0.5);
            
            renderManager.renderBlock(state, pos, MinecraftClient.getInstance().world, matrices, vertexConsumers.getBuffer(RenderLayer.getSolid()), true, null);
            
            matrices.pop();
        }
    }
}
