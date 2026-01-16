package com.example.javamod01;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.ClickableWidget;

import net.minecraft.text.Text;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;
import com.example.autoblockmod.AutoBlockConfig;
import net.minecraft.client.MinecraftClient;

public class ModSettingsScreen extends Screen {
    private final Screen parent;
    private CheckboxWidget placeOnAirCheckbox;
    private CheckboxWidget placeBelowCheckbox;
    private CheckboxWidget autoClickerCheckbox;

    protected ModSettingsScreen(Screen parent) {
        super(Text.of("Mod Settings"));
        this.parent = parent;
    }



    private TextRenderer getTextRenderer() {
        return MinecraftClient.getInstance().textRenderer;
    }

    @Override
    protected void init() {
        super.init();
        
        int y = 30;
        
        // Place On Air checkbox
        placeOnAirCheckbox = CheckboxWidget.builder(Text.of("Place On Air"), MinecraftClient.getInstance().textRenderer)
            .checked(BlockPlacementHandler.isPlaceOnAir())
            .dimensions(this.width / 2 - 155, y, 20, 20)
            .onValueChange((button, checked) -> BlockPlacementHandler.setPlaceOnAir(checked))
            .build();
        
        // Place Below checkbox
        placeBelowCheckbox = CheckboxWidget.builder(Text.of("Place Below"), MinecraftClient.getInstance().textRenderer)
            .checked(BlockPlacementHandler.isPlaceBelow())
            .dimensions(this.width / 2 - 155, y + 24, 20, 20)
            .onValueChange((button, checked) -> BlockPlacementHandler.setPlaceBelow(checked))
            .build();
        
        // AutoClicker checkbox
        autoClickerCheckbox = CheckboxWidget.builder(Text.of("AutoClicker"), MinecraftClient.getInstance().textRenderer)
            .checked(AutoBlockConfig.isEnabled())
            .dimensions(this.width / 2 - 155, y + 48, 20, 20)
            .onValueChange((button, checked) -> AutoBlockConfig.setEnabled(checked))
            .build();
            20,
            Text.of("AutoClicker"),
            AutoBlockConfig.isEnabled(),
            () -> AutoBlockConfig.setEnabled(!AutoBlockConfig.isEnabled())
        );
        
        // Add all checkboxes to the screen
        this.addDrawableChild(placeOnAirCheckbox);
        this.addDrawableChild(placeBelowCheckbox);
        this.addDrawableChild(autoClickerCheckbox);
        
        // Back button
        this.addDrawableChild(ButtonWidget.builder(Text.of("Back"), (button) -> this.client.setScreen(parent))
            .position(this.width / 2 - 100, this.height - 28)
            .size(200, 20)
            .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, 0, 0, 0);
        super.render(context, mouseX, mouseY, delta);
        context.drawTextWithShadow(this.textRenderer, this.title, this.width / 2 - this.textRenderer.getWidth(this.title) / 2, 20, 16777215);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
