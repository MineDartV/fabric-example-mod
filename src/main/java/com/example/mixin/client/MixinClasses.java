package com.example.mixin.client;

import net.minecraft.client.network.ClientPlayerInteractionManager;

public class MixinClasses {
    public static final Class<?>[] CLIENT_PLAYER_INTERACTION_MANAGER = {ClientPlayerInteractionManager.class};
}
