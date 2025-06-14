package com.example.javamod01;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class JavaMod01 implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(JavaMod01.class);

    @Override
    public void onInitializeClient() {
        LOGGER.info("JavaMod01 initialized!");
        BlockPlacementHandler.init();
        AutoClicker.init();
        HUDOverlay.init();
    }
}
