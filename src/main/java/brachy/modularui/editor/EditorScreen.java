package brachy.modularui.editor;

import brachy.modularui.screen.ModularPanel;
import brachy.modularui.screen.ModularScreen;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public class EditorScreen extends ModularScreen {

    public EditorScreen(@NotNull String owner, @NotNull ModularPanel<?> mainPanel) {
        super(owner, mainPanel);
    }
}
