package brachy.modularui.editor;

import brachy.modularui.ModularUI;
import brachy.modularui.api.IMuiScreen;
import brachy.modularui.screen.ModularPanel;
import brachy.modularui.screen.ModularScreen;

import brachy.modularui.widget.Widget;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@OnlyIn(Dist.CLIENT)
public class EditorUIOverlayScreen extends ModularScreen {

    public final IMuiScreen mainScreen;
    public final ModularPanel<?> uiRoot;
    public Widget<?> editorSelectedWidget;

    public EditorUIOverlayScreen(IMuiScreen screen) {
        super(ModularUI.MOD_ID, c -> new ModularPanel<>("editor").fullScreenInvisible());
        mainScreen = screen;
        uiRoot = screen.screen().getMainPanel();
        editorSelectedWidget = uiRoot;
        buildUI(getMainPanel());
    }

    private void buildUI(ModularPanel<?> editorMainPanel) {
        editorMainPanel.child(new WidgetTreePanel(this))
                .child(new WidgetInfoPanel(this));
    }

}
