package brachy.modularui.editor;

import brachy.modularui.api.GuiAxis;
import brachy.modularui.api.drawable.Text;
import brachy.modularui.api.widget.IWidget;
import brachy.modularui.drawable.GuiTextures;
import brachy.modularui.value.StringValue;
import brachy.modularui.widget.Widget;
import brachy.modularui.widgets.TextWidget;
import brachy.modularui.widgets.layout.Flow;
import brachy.modularui.widgets.textfield.TextFieldWidget;

import net.minecraft.network.chat.Component;

public class WidgetInfoPanel extends Flow {

    private final EditorUIOverlayScreen editor;

    public WidgetInfoPanel(EditorUIOverlayScreen editor) {
        super(GuiAxis.Y);
        this.editor = editor;

        leftRel(0f);
        topRel(0.5f);
        background(GuiTextures.MC_BACKGROUND);
        padding(5);
        widthRel(0.20f);
        coverChildren();

        child(Flow.row()
                .coverChildrenHeight()
                .child(Text.str("Name:").asWidget().paddingRight(4))
                .child(new TextFieldWidget()
                        .width(200)
                        .value(new StringValue.Dynamic(() -> selectedWidget().getName() != null ? selectedWidget().getName() : "",
                                (s) -> selectedWidget().setName(s))).paddingLeft(4))
                .child(Text.dynamic(() -> Component.literal("Type: " + selectedWidget().getClass().getSimpleName())).asWidget()
                        .width(100)));
    }

    private Widget<?> selectedWidget() {
        return editor.getEditorSelectedWidget();
    }

    private static IWidget categoryName(String name) {
        return new TextWidget<>(name)
                .horizontalCenter()
                .padding(0, 5);
    }
}
