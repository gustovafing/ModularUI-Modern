package brachy.modularui.editor;

import brachy.modularui.ModularUI;
import brachy.modularui.api.IUIHolder;
import brachy.modularui.factory.GuiData;
import brachy.modularui.screen.ModularPanel;
import brachy.modularui.screen.ModularScreen;
import brachy.modularui.screen.UISettings;
import brachy.modularui.test.TestGuis;
import brachy.modularui.value.sync.PanelSyncManager;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class EditorUIHolder implements IUIHolder<GuiData> {

    @Override
    public ModularScreen createScreen(GuiData data, ModularPanel<?> mainPanel) {
        return new EditorScreen(ModularUI.MOD_ID, mainPanel);
    }

    @Override
    public ModularPanel<?> buildUI(GuiData data, PanelSyncManager syncManager, UISettings settings) {
        return TestGuis.buildMachineLikeUI();
    }
}
