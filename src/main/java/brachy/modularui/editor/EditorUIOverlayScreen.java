package brachy.modularui.editor;

import brachy.modularui.ModularUI;
import brachy.modularui.api.IMuiScreen;
import brachy.modularui.api.widget.IWidget;
import brachy.modularui.drawable.GuiTextures;
import brachy.modularui.screen.CustomModularScreen;
import brachy.modularui.screen.ModularPanel;
import brachy.modularui.screen.viewport.ModularGuiContext;

import brachy.modularui.utils.Alignment;

import brachy.modularui.widgets.ListWidget;

import brachy.modularui.widgets.TextWidget;

import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public class EditorUIOverlayScreen extends CustomModularScreen {

    @Getter
    private final IMuiScreen parent;

    private final ModularPanel<?> uiRoot;

    public EditorUIOverlayScreen(IMuiScreen screen) {
        super(ModularUI.MOD_ID);
        parent = screen;
        uiRoot = screen.screen().getMainPanel();
    }

    @Override
    public @NotNull ModularPanel<?> buildUI(ModularGuiContext context) {
        return new ModularPanel<>("editor")
                .fullScreenInvisible()
                .child(getWidgetTreeList());
    }


    private ListWidget<IWidget, ?> getWidgetTreeList() {
        ListWidget<IWidget, ?> list = new ListWidget<>();
        list.posRel(Alignment.CenterRight)
                .maxSizeRel(0.5f)
                .childSeparator(GuiTextures.SEPERATOR_SIMPLE.asIcon());

        uiRoot.getChildren().forEach(list::child);

        return list;
    }

    private TextWidget<?> getWidgetLabel(IWidget widget) {
        var name = widget.getName();
        var type = widget.getClass().getSimpleName();
        return new TextWidget<>(name + " " + type);
    }
}
