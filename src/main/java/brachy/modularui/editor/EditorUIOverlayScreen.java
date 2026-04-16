package brachy.modularui.editor;

import brachy.modularui.ModularUI;
import brachy.modularui.api.IMuiScreen;
import brachy.modularui.api.drawable.Text;
import brachy.modularui.drawable.GuiTextures;
import brachy.modularui.drawable.Rectangle;
import brachy.modularui.screen.ModularPanel;
import brachy.modularui.screen.ModularScreen;

import brachy.modularui.utils.Color;
import brachy.modularui.value.BoolValue;
import brachy.modularui.widget.AbstractParentWidget;
import brachy.modularui.widget.Widget;
import brachy.modularui.widgets.ListWidget;

import brachy.modularui.widgets.ToggleButton;
import brachy.modularui.widgets.layout.Flow;

import lombok.Getter;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.jetbrains.annotations.ApiStatus;

import java.util.function.Supplier;

@ApiStatus.Internal
@OnlyIn(Dist.CLIENT)
public class EditorUIOverlayScreen extends ModularScreen {

    private final IMuiScreen mainScreen;
    private final ModularPanel<?> uiRoot;
    @Getter
    private Widget<?> editorSelectedWidget;

    public EditorUIOverlayScreen(IMuiScreen screen) {
        super(ModularUI.MOD_ID, c -> new ModularPanel<>("editor").fullScreenInvisible());
        mainScreen = screen;
        uiRoot = screen.screen().getMainPanel();
        editorSelectedWidget = uiRoot;
        buildUI(getMainPanel());
    }

    private void buildUI(ModularPanel<?> editorMainPanel) {
        editorMainPanel.child(getWidgetTreeList())
                .child(new WidgetInfoPanel(this));
    }

    private ListWidget<Widget<?>, ?> getWidgetTreeList() {
        ListWidget<Widget<?>, ?> list = new ListWidget<>();
        list
                .rightRel(0f)
                .topRel(0.5f)
                .maxSizeRel(0.5f)
                .widthRel(0.20f)
                .childSeparator(GuiTextures.SEPERATOR_SIMPLE.asIcon())
                .background(new Rectangle().color(Color.GREY.darker(3)));

        addWidgetTreeDisplay(list, uiRoot, 0);
        return list;
    }


    private Supplier<Component> displayLabel(Widget<?> widget) {
        return () -> {
            var name = (widget.getName() == null ? "" : widget.getName() + " ");
            var type = widget.getClass().getSimpleName();
            return Component.literal(name + type)
                    .withStyle(widget == editorSelectedWidget ? ChatFormatting.GREEN : ChatFormatting.WHITE);
        };
    }

    private void addWidgetTreeDisplay(ListWidget<Widget<?>, ?> list, Widget<?> widget, int depth) {
        var type = widget.getClass().getSimpleName();

        var flow = Flow.row().coverChildrenHeight().padding(2)
                        .child(new Rectangle().color(Color.GREY.darker(1)).asWidget().size(depth * 8, 2))
                                .child(new ToggleButton()
                                        .value(new BoolValue.Dynamic(() -> widget == editorSelectedWidget, $ -> editorSelectedWidget = widget))
                                        .paddingLeft(4)
                                        .width(200)
                                        .child(Text.dynamic(displayLabel(widget)).asWidget())
                                );

        list.child(flow);

        if (widget instanceof AbstractParentWidget<?,?> parent) {
            depth++;
            for (var child: parent.getChildren()) {
                addWidgetTreeDisplay(list, (Widget<?>)child, depth);
            }
        }

    }

}
