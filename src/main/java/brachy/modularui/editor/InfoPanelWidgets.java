package brachy.modularui.editor;

import brachy.modularui.api.drawable.Text;
import brachy.modularui.api.value.IBoolValue;
import brachy.modularui.drawable.GuiTextures;
import brachy.modularui.widgets.TextWidget;
import brachy.modularui.widgets.ToggleButton;
import brachy.modularui.widgets.layout.Flow;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class InfoPanelWidgets {

    public static TextWidget<?> categoryName(String name) {
        return new TextWidget<>(Component.literal(name).withStyle(ChatFormatting.BOLD))
                .margin(0, 0, 4, 4).horizontalCenter();
    }

    public static TextWidget<?> categoryName(String name, int width) {
        return new TextWidget<>(Component.literal(name).withStyle(ChatFormatting.BOLD))
                .width(width)
                .margin(0, 0, 4, 4);
    }

    public static Flow configRow(String name) {
        return Flow.row()
                .child(Text.str(name).asWidget().marginRight(4))
                .marginBottom(2)
                .widthRel(1f)
                .coverChildrenHeight();
    }

    public static Flow configRow(String name, int nameWidth) {
        return Flow.row()
                .child(Text.str(name).asWidget().width(nameWidth).marginRight(4))
                .marginBottom(2)
                .widthRel(1f)
                .coverChildrenHeight();
    }

    public static ToggleButton checkbox(IBoolValue<Boolean> value) {
        return new ToggleButton()
                .size(20, 20)
                .selectedBackground(GuiTextures.CHECK_BOX_FULL)
                .background(GuiTextures.CHECK_BOX_EMPTY)
                .value(value);
    }

}
