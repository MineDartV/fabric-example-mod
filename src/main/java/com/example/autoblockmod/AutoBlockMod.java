package com.example.autoblockmod;

import net.fabricmc.api.ClientModInitializer;

public class AutoBlockMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Initialize mod configuration
        AutoBlockConfig.init();
        AutoBlockConfig.setEnabled(true);
        AutoBlockConfig.setPlaceBelow(false);
        AutoBlockConfig.setPlaceOnAir(false);
    }
}
