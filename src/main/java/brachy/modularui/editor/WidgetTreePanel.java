package brachy.modularui.editor;

import brachy.modularui.api.drawable.Text;
import brachy.modularui.api.widget.IWidget;
import brachy.modularui.drawable.Rectangle;
import brachy.modularui.utils.Color;
import brachy.modularui.value.BoolValue;
import brachy.modularui.widget.AbstractParentWidget;
import brachy.modularui.widget.Widget;
import brachy.modularui.widgets.ListWidget;
import brachy.modularui.widgets.ToggleButton;
import brachy.modularui.widgets.layout.Flow;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

public class WidgetTreePanel extends ListWidget<IWidget, WidgetTreePanel> {

    private final EditorUIOverlayScreen editor;

    public WidgetTreePanel(EditorUIOverlayScreen editor) {
        this.editor = editor;

        rightRel(0f);
        topRel(0.5f);
        maxSizeRel(0.5f);
        widthRel(0.20f);
        background(new Rectangle().color(Color.GREY.darker(3)));

        addWidgetTreeDisplay(editor.uiRoot, 0);

    }

    private Supplier<Component> displayLabel(Widget<?> widget) {
        return () -> {
            var name = (widget.getName() == null ? "" : widget.getName() + " ");
            var type = widget.getClass().getSimpleName();
            return Component.literal(name + type)
                    .withStyle(widget == editor.editorSelectedWidget ? ChatFormatting.GREEN : ChatFormatting.WHITE);
        };
    }

    private void addWidgetTreeDisplay(Widget<?> widget, int depth) {
        var type = widget.getClass().getSimpleName();

        var flow = Flow.row().coverChildrenHeight().padding(2)
                .child(new Rectangle().color(Color.GREY.darker(1)).asWidget().size(depth * 8, 2))
                .child(new ToggleButton()
                        .value(new BoolValue.Dynamic(() -> widget == editor.editorSelectedWidget, $ -> editor.editorSelectedWidget = widget))
                        .paddingLeft(4)
                        .width(180)
                        .child(Text.dynamic(displayLabel(widget)).asWidget().width(180))
                );

        child(flow);

        if (widget instanceof AbstractParentWidget<?,?> parent) {
            depth++;
            for (var child: parent.getChildren()) {
                addWidgetTreeDisplay((Widget<?>)child, depth);
            }
        }

    }
}
