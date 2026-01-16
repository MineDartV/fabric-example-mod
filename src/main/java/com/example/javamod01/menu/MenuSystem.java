package com.example.javamod01.menu;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import com.example.javamod01.ModLogger;

public class MenuSystem {
    private static final Logger LOGGER = ModLogger.LOGGER;
    private static boolean isOpen = false;
    private static int selectedCategory = 0;
    private static KeyBinding toggleMenuKey;
    private static long lastToggleTime = 0;
    private static final long TOGGLE_DELAY = 500; // 500ms delay between toggles
    private static final String[] CATEGORIES = {"Autoclicker", "Block Placement", "Settings"};

    private static boolean initialized = false;

    public static void init() {
        if (initialized) {
            LOGGER.warn("MenuSystem already initialized!");
            return;
        }

        LOGGER.info("Initializing MenuSystem");
        
        try {
            // Register HUD render callback first
            HudRenderCallback.EVENT.register((context, tickDelta) -> {
                if (isOpen() && MinecraftClient.getInstance().currentScreen == null) {
                    render(context, MinecraftClient.getInstance().getWindow().getScaledWidth(), MinecraftClient.getInstance().getWindow().getScaledHeight());
                }
            });
            
            // Register key binding for menu toggle
            toggleMenuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.javamod01.menu_toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.javamod01"
            ));

            // Register client tick event to handle key presses
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (client.currentScreen == null && client.player != null) {
                    checkKeyBindings();
                }
            });
            
            initialized = true;
            LOGGER.info("MenuSystem initialized successfully");
        } catch (Exception e) {
            LOGGER.error("Error initializing MenuSystem", e);
            throw e;
        }
    }

    private static void checkKeyBindings() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (toggleMenuKey.wasPressed()) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastToggleTime >= TOGGLE_DELAY) {
                isOpen = !isOpen;
                if (!isOpen) {
                    selectedCategory = 0;
                }
                lastToggleTime = currentTime;
                LOGGER.info("Menu toggled to: {}", isOpen);
            }
        }
    }

    public static void tick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen == null && client.player != null) {
            // Handle category navigation using mouse movement
            if (isOpen) {
                // Get mouse position in window coordinates
                double mouseX = client.mouse.getX() * client.getWindow().getScaledWidth() / client.getWindow().getWidth();
                double mouseY = client.mouse.getY() * client.getWindow().getScaledHeight() / client.getWindow().getHeight();
                
                // Convert to screen coordinates (0,0 is top-left)
                mouseX = client.getWindow().getScaledWidth() - mouseX;
                mouseY = client.getWindow().getScaledHeight() - mouseY;
                
                // Calculate menu position
                int menuWidth = 200;
                int menuHeight = 200;
                int x = client.getWindow().getScaledWidth() / 2 - menuWidth / 2;
                int y = client.getWindow().getScaledHeight() / 2 - menuHeight / 2;

                // Check if mouse is within menu bounds
                if (mouseX >= x && mouseX <= x + menuWidth && mouseY >= y && mouseY <= y + menuHeight) {
                    // Calculate which category is hovered
                    int buttonHeight = 25;
                    int buttonSpacing = 5;
                    int buttonY = y + 20;

                    for (int i = 0; i < CATEGORIES.length; i++) {
                        int buttonBottom = buttonY + buttonHeight;
                        if (mouseY >= buttonY && mouseY <= buttonBottom) {
                            selectedCategory = i;
                            break;
                        }
                        buttonY += buttonHeight + buttonSpacing;
                    }
                }
            }
        }
    }

    public static void render(DrawContext context, int scaledWidth, int scaledHeight) {
        if (!isOpen) {
            return;
        }
        if (isOpen) {
            // Draw menu background
            int menuWidth = 200;
            int menuHeight = 200;
            int x = scaledWidth / 2 - menuWidth / 2;
            int y = scaledHeight / 2 - menuHeight / 2;

            // Draw background rectangle (black with blue border)
            context.fill(x, y, x + menuWidth, y + menuHeight, 0xFF000000); // Black background
            context.fill(x, y, x + menuWidth, y + 2, 0xFF0000FF); // Top blue border
            context.fill(x, y + menuHeight - 2, x + menuWidth, y + menuHeight, 0xFF0000FF); // Bottom blue border
            context.fill(x, y, x + 2, y + menuHeight, 0xFF0000FF); // Left blue border
            context.fill(x + menuWidth - 2, y, x + menuWidth, y + menuHeight, 0xFF0000FF); // Right blue border

            // Draw category buttons
            int buttonHeight = 25;
            int buttonSpacing = 5;
            int buttonY = y + 20;

            for (int i = 0; i < CATEGORIES.length; i++) {
                boolean isSelected = i == selectedCategory;
                int buttonX = x + 10;
                int buttonColor = isSelected ? 0xFF0000FF : 0xFF000000; // Blue for selected, black for others
                
                // Draw button background
                context.fill(buttonX, buttonY, buttonX + menuWidth - 20, buttonY + buttonHeight, buttonColor);
                
                // Draw category text in blue
                context.drawTextWithShadow(
                    MinecraftClient.getInstance().textRenderer,
                    CATEGORIES[i],
                    buttonX + 5,
                    buttonY + 5,
                    0xFF0000FF
                );
                
                buttonY += buttonHeight + buttonSpacing;
            }
        }
    }

    public static boolean isOpen() {
        return isOpen;
    }

    public static void close() {
        isOpen = false;
    }
}
