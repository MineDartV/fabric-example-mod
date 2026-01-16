package com.example.javamod01;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.javamod01.menu.MenuSystem;

@Environment(EnvType.CLIENT)
public class JavaMod01 implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger(JavaMod01.class);

    @Override
    public void onInitializeClient() {
        LOGGER.info("JavaMod01 initialized!");
        BlockPlacementHandler.init();
        AutoClicker.init();
        MenuSystem.init();
        HUDOverlay.init();
        
        // Check if key bindings were registered
        if (BlockPlacementHandler.getPlaceAirKey() == null) {
            LOGGER.error("Place Air key binding was not registered!");
        }
        if (BlockPlacementHandler.getPlaceBelowKey() == null) {
            LOGGER.error("Place Below key binding was not registered!");
        }
    }
}
