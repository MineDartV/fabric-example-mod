package com.example.javamod01;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;

public class JavaMod01 implements ClientModInitializer {
    private static final Logger LOGGER = ModLogger.LOGGER;
    
    @Override
    public void onInitializeClient() {
        LOGGER.info("JavaMod01 initialized!");
        BlockPlacementHandler.init();
    }
}
