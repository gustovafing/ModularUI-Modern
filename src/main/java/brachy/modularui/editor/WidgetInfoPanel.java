package brachy.modularui.editor;

import brachy.modularui.api.GuiAxis;
import brachy.modularui.api.IThemeApi;
import brachy.modularui.api.drawable.Text;
import brachy.modularui.api.widget.IWidget;
import brachy.modularui.drawable.GuiTextures;
import brachy.modularui.drawable.ItemDrawable;
import brachy.modularui.utils.Alignment;
import brachy.modularui.utils.Color;
import brachy.modularui.value.BoolValue;
import brachy.modularui.value.EnumValue;
import brachy.modularui.value.FloatValue;
import brachy.modularui.value.IntValue;
import brachy.modularui.value.StringValue;
import brachy.modularui.widget.Widget;
import brachy.modularui.widget.sizer.Unit;
import brachy.modularui.widgets.TextWidget;
import brachy.modularui.widgets.ToggleButton;
import brachy.modularui.widgets.layout.Flow;
import brachy.modularui.widgets.menu.DropdownWidget;
import brachy.modularui.widgets.textfield.TextFieldWidget;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.function.Supplier;

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
                .coverChildren()
                .child(Text.str("Name:").asWidget().paddingRight(4))
                .child(new TextFieldWidget()
                        .width(200)
                        .value(new StringValue.Dynamic(() -> selectedWidget().getName() != null ? selectedWidget().getName() : "",
                                (s) -> selectedWidget().setName(s))).paddingLeft(4))
                .child(Text.dynamic(() -> Component.literal("Type: " + selectedWidget().getClass().getSimpleName())).asWidget()
                        .width(100)));
        child(categoryName("Position"));
        positionWidgets();
    }

    private Widget<?> selectedWidget() {
        return editor.getEditorSelectedWidget();
    }


    private static IWidget categoryName(String name) {
        return new TextWidget<>(Component.literal(name).withStyle(ChatFormatting.BOLD))
                .margin(0, 0, 4, 4);
    }

    private void positionWidgets() {

        var unitUI = Flow.col().coverChildrenHeight();

        unitUI.child(Flow.row()
                .child(Text.str("Relative").asWidget().size(40, 20).textAlign(Alignment.Center).marginLeft(40))
                .child(Text.str("Value").asWidget().size(50, 20).textAlign(Alignment.Center).marginLeft(5))
                .child(Text.str("Offset").asWidget().size(50, 20).textAlign(Alignment.Center).marginLeft(5))
                .child(Text.str("Anchor").asWidget().size(50, 20).textAlign(Alignment.Center).marginLeft(5))
        );

        unitUI.child(unitRow(() -> selectedWidget().resizer().getX().getP1(), true, GuiAxis.X).marginBottom(3));
        unitUI.child(unitRow(() -> selectedWidget().resizer().getX().getP2(), false, GuiAxis.X).marginBottom(3));
        unitUI.child(unitRow(() -> selectedWidget().resizer().getY().getP1(), true, GuiAxis.Y).marginBottom(3));
        unitUI.child(unitRow(() -> selectedWidget().resizer().getY().getP2(), false, GuiAxis.Y).marginBottom(3));

        child(unitUI);
    }

    private Flow unitRow(Supplier<Unit> unit, boolean firstUnit, GuiAxis axis) {

        EnumValue.Dynamic<Unit.State> unitType = new EnumValue.Dynamic<>(Unit.State.class, () -> unit.get().state, u -> {
            var dimSizer = axis == GuiAxis.X ? selectedWidget().resizer().getX() : selectedWidget().resizer().getY();
            dimSizer.getNext(selectedWidget(), u, firstUnit);
            selectedWidget().resizer().scheduleResize();
        });

        var dropdown = new DropdownWidget<>("unit" + axis.name() + (firstUnit ? "1" : "2"), Unit.State.class)
                .size(40, 20)
                .value(unitType)
                .options(Unit.State.START)
                .option(Unit.State.END)
                .option(Unit.State.SIZE)
                .option(Unit.State.UNUSED)
                .optionToWidget((s, forSelected) -> {
                    return new TextWidget<>(s.getText(axis)).center().color(Color.WHITE.main);
                });

        return Flow.row().height(20)
                .child(dropdown)
                .child(measure(unit))
                .child(value(unit))
                .child(offset(unit))
                .child(anchor(unit));
    }

    private ToggleButton measure(Supplier<Unit> unit) {
        var value = new BoolValue.Dynamic(() -> unit.get().getMeasure() == Unit.Measure.RELATIVE,
                (b) -> {
                    var current = unit.get().getMeasure();
                    unit.get().setMeasure(b ? Unit.Measure.RELATIVE : Unit.Measure.PIXEL);
                    selectedWidget().resizer().scheduleResize();
                }
        );

        return new ToggleButton()
                .size(20, 20)
                .marginLeft(10).marginRight(10)
                .selectedBackground(GuiTextures.CHECK_BOX_FULL)
                .tooltip(r -> r.addLine("When disabled, values represent actual size in pixels").addLine("When enabled, values are relative to the parent widget"))
                .background(GuiTextures.CHECK_BOX_EMPTY)
                .setEnabledIf(f -> (unit.get().state != Unit.State.UNUSED))
                .value(value);
    }

    private TextFieldWidget value(Supplier<Unit> unit) {
        var value = new FloatValue.Dynamic(() -> unit.get().getValue(), v -> {
            unit.get().setValue(v);
            selectedWidget().resizer().scheduleResize();
        });

        return new TextFieldWidget()
                .size(50, 20)
                .marginLeft(5)
                .setNumbersDouble(d -> d)
                .setEnabledIf(f -> (unit.get().state != Unit.State.UNUSED))
                .value(value);
    }

    private TextFieldWidget offset(Supplier<Unit> unit) {
        var value = new IntValue.Dynamic(() -> unit.get().getOffset(), v -> {
            unit.get().setOffset(v);
            selectedWidget().resizer().scheduleResize();
        });

        return new TextFieldWidget()
                .size(50, 20)
                .marginLeft(5)
                .tooltip(r -> r.addLine("Pixel offset applied after relative sizing"))
                .setNumbers()
                .setEnabledIf(f -> (unit.get().state != Unit.State.UNUSED))
                .value(value);
    }

    private TextFieldWidget anchor(Supplier<Unit> unit) {
        var value = new FloatValue.Dynamic(() -> unit.get().getAnchor(), v -> {
            unit.get().setAnchor(v);
            selectedWidget().resizer().scheduleResize();
        });

        return new TextFieldWidget()
                .size(50, 20)
                .marginLeft(5)
                .setNumbersDouble(d -> d)
                .setEnabledIf(f -> (unit.get().state != Unit.State.UNUSED))
                .value(value);
    }


}
