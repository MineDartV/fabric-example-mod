package com.example.javamod01;

import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.Text;
import java.util.function.Consumer;

/**
 * A custom checkbox widget that extends ClickableWidget and implements the necessary methods.
 */
public class CustomCheckboxWidget extends CheckboxWidget {
    private final Consumer<CustomCheckboxWidget> onValueChange;

    /**
     * Creates a new CustomCheckboxWidget instance.
     * 
     * @param x            The x-coordinate of the widget.
     * @param y            The y-coordinate of the widget.
     * @param width        The width of the widget.
     * @param height       The height of the widget.
     * @param label        The label of the checkbox.
     * @param checked      The initial checked state of the checkbox.
     * @param onValueChange The callback to be executed when the checkbox's value changes.
     */
    public CustomCheckboxWidget(int x, int y, int width, int height, Text label, boolean checked, Consumer<CustomCheckboxWidget> onValueChange) {
        super(CheckboxWidget.builder(label, MinecraftClient.getInstance().textRenderer)
            .checked(checked)
            .dimensions(x, y, width, height)
            .build());
        this.onValueChange = onValueChange;
    }

    /**
     * Called when the checkbox is pressed.
     */
    @Override
    public void onPress() {
        super.onPress();
        if (onValueChange != null) {
            onValueChange.accept(this);
        }
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderButton(context, mouseX, mouseY, delta);
    }
}
