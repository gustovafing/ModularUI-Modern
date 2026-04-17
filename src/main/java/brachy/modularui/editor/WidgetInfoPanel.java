package brachy.modularui.editor;

import brachy.modularui.api.GuiAxis;
import brachy.modularui.api.drawable.Text;
import brachy.modularui.drawable.GuiTextures;
import brachy.modularui.utils.Alignment;
import brachy.modularui.utils.Color;
import brachy.modularui.value.BoolValue;
import brachy.modularui.value.EnumValue;
import brachy.modularui.value.FloatValue;
import brachy.modularui.value.IntValue;
import brachy.modularui.value.StringValue;
import brachy.modularui.widget.Widget;
import brachy.modularui.widget.sizer.DimensionSizer;
import brachy.modularui.widget.sizer.Unit;
import brachy.modularui.widgets.TextWidget;
import brachy.modularui.widgets.ToggleButton;
import brachy.modularui.widgets.layout.Flow;
import brachy.modularui.widgets.menu.DropdownWidget;
import brachy.modularui.widgets.textfield.TextFieldWidget;

import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.ApiStatus;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

@ApiStatus.Internal
public class WidgetInfoPanel extends Flow {

    private final EditorUIOverlayScreen editor;

    public WidgetInfoPanel(EditorUIOverlayScreen editor) {
        super(GuiAxis.Y);
        this.editor = editor;

        leftRel(0f);
        topRel(0.5f);
        background(GuiTextures.MC_BACKGROUND);
        padding(5);
        widthRel(0.30f);
        crossAxisAlignment(Alignment.CrossAxis.START);
        coverChildrenHeight();

        child(InfoPanelWidgets.configRow("Name:")
                .child(new TextFieldWidget()
                        .width(150)
                        .value(new StringValue.Dynamic(() -> selectedWidget().getName() != null ? selectedWidget().getName() : "",
                                (s) -> selectedWidget().setName(s))).paddingLeft(4))
                .child(Text.dynamic(() -> Component.literal("Type: " + selectedWidget().getClass().getSimpleName())).asWidget()
                        .width(100)));

        child(InfoPanelWidgets.categoryName("Position"));

        unitPositionWidgets();
        paddingAndMarginWidgets();
        extraSizingWidgets();
    }

    private Widget<?> selectedWidget() {
        return editor.editorSelectedWidget;
    }

    private DimensionSizer sizer(GuiAxis axis) {
        return axis == GuiAxis.X ? selectedWidget().resizer().getX() : selectedWidget().resizer().getY();
    }

    private Unit unit(GuiAxis axis, boolean first) {
        return first ? sizer(axis).getP1() : sizer(axis).getP2();
    }

    private void unitPositionWidgets() {

        child(Flow.row().coverChildren()
                .child(Text.str("Relative").asWidget().size(40, 20).textAlign(Alignment.Center).marginLeft(40))
                .child(Text.str("Value").asWidget().size(50, 20).textAlign(Alignment.Center).marginLeft(5))
                .child(Text.str("Offset").asWidget().size(50, 20).textAlign(Alignment.Center).marginLeft(5))
                .child(Text.str("Anchor").asWidget().size(50, 20).textAlign(Alignment.Center).marginLeft(5))
        );

        child(unitRow(true, GuiAxis.X).marginBottom(3));
        child(unitRow(false, GuiAxis.X).marginBottom(3));
        child(unitRow(true, GuiAxis.Y).marginBottom(3));
        child(unitRow(false, GuiAxis.Y).marginBottom(3));
    }

    private void paddingAndMarginWidgets() {

        var padL = new IntValue.Dynamic(() -> selectedWidget().getArea().getPadding().left(), i -> selectedWidget().paddingLeft(i));
        var padR = new IntValue.Dynamic(() -> selectedWidget().getArea().getPadding().right(), i -> selectedWidget().paddingRight(i));
        var padT = new IntValue.Dynamic(() -> selectedWidget().getArea().getPadding().top(), i -> selectedWidget().paddingTop(i));
        var padB = new IntValue.Dynamic(() -> selectedWidget().getArea().getPadding().bottom(), i -> selectedWidget().paddingBottom(i));

        var mL = new IntValue.Dynamic(() -> selectedWidget().getArea().getMargin().left(), i -> selectedWidget().marginLeft(i));
        var mR = new IntValue.Dynamic(() -> selectedWidget().getArea().getMargin().right(), i -> selectedWidget().marginRight(i));
        var mT = new IntValue.Dynamic(() -> selectedWidget().getArea().getMargin().top(), i -> selectedWidget().marginTop(i));
        var mB = new IntValue.Dynamic(() -> selectedWidget().getArea().getMargin().bottom(), i -> selectedWidget().marginBottom(i));

        var padRow = InfoPanelWidgets.configRow("Padding:", 40);
        var mRow = InfoPanelWidgets.configRow("Margin:", 40);

        padRow.child(new TextFieldWidget().size(50, 20).setNumbers(d -> Math.max(d, 0))
                    .value(padL).tooltip(r -> r.add("Left")))
                .child(new TextFieldWidget().size(50, 20).setNumbers(d -> Math.max(d, 0))
                        .value(padR).tooltip(r -> r.add("Right")))
                .child(new TextFieldWidget().size(50, 20).setNumbers(d -> Math.max(d, 0))
                        .value(padT).tooltip(r -> r.add("Top")))
                .child(new TextFieldWidget().size(50, 20).setNumbers(d -> Math.max(d, 0))
                                .value(padB).tooltip(r -> r.add("Bottom")));
        mRow.child(new TextFieldWidget().size(50, 20).setNumbers(d -> Math.max(d, 0))
                        .value(mL).tooltip(r -> r.add("Left")))
                .child(new TextFieldWidget().size(50, 20).setNumbers(d -> Math.max(d, 0))
                        .value(mR).tooltip(r -> r.add("Right")))
                .child(new TextFieldWidget().size(50, 20).setNumbers(d -> Math.max(d, 0))
                        .value(mT).tooltip(r -> r.add("Top")))
                .child(new TextFieldWidget().size(50, 20).setNumbers(d -> Math.max(d, 0))
                        .value(mB).tooltip(r -> r.add("Bottom")));

        child(padRow);
        child(mRow);
    }

    private void extraSizingWidgets() {

        var widthCoverToggle = new BoolValue.Dynamic(() -> sizer(GuiAxis.X).getCoverChildrenMinSize() != -1,
                b -> sizer(GuiAxis.X).setCoverChildren(b ? 0 : -1, selectedWidget()));

        var widthCover = new IntValue.Dynamic(() -> sizer(GuiAxis.X).getCoverChildrenMinSize(),
                i -> sizer(GuiAxis.X).setCoverChildren(i, selectedWidget()));

        var heightCoverToggle = new BoolValue.Dynamic(() -> sizer(GuiAxis.Y).getCoverChildrenMinSize() != -1,
                b -> sizer(GuiAxis.Y).setCoverChildren(b ? 0 : -1, selectedWidget()));

        var heightCover = new IntValue.Dynamic(() -> sizer(GuiAxis.Y).getCoverChildrenMinSize(),
                i -> sizer(GuiAxis.Y).setCoverChildren(i, selectedWidget()));

        var decorationToggle = new BoolValue.Dynamic(() -> selectedWidget().resizer().isDecoration(),
                b -> selectedWidget().resizer().decoration(b));

        var expandedToggle = new BoolValue.Dynamic(() -> selectedWidget().resizer().isExpanded(), b -> selectedWidget().resizer().expanded(b));

        child(InfoPanelWidgets.configRow("Cover Children Width", 110)
                .child(InfoPanelWidgets.checkbox(widthCoverToggle))
                .child(new TextFieldWidget()
                        .marginLeft(4)
                        .size(50, 20)
                        .setNumbers(d -> Math.max(d, 1))
                        .tooltip(t -> t.add("Minimum Width"))
                        .value(widthCover)
                        .setEnabledIf(w -> widthCoverToggle.getBoolValue())));

        child(InfoPanelWidgets.configRow("Cover Children Height", 110)
                .child(InfoPanelWidgets.checkbox(heightCoverToggle))
                .child(new TextFieldWidget()
                        .marginLeft(4)
                        .size(50, 20)
                        .setNumbers(d -> Math.max(d, 1))
                        .tooltip(t -> t.add("Minimum Height"))
                        .value(heightCover)
                        .setEnabledIf(w -> heightCoverToggle.getBoolValue())));

        child(InfoPanelWidgets.configRow("Decoration:", 55)
                .child(InfoPanelWidgets.checkbox(decorationToggle)));
        child(InfoPanelWidgets.configRow("Expanded:", 55)
                .child(InfoPanelWidgets.checkbox(expandedToggle)));
    }

    private Flow unitRow(boolean firstUnit, GuiAxis axis) {

        Supplier<List<Unit.State>> unitStateSupplier = () -> Arrays.stream(Unit.State.values()).filter(s -> s != unit(axis, firstUnit).state && s != unit(axis, !firstUnit).state).toList();

        EnumValue.Dynamic<Unit.State> unitType = new EnumValue.Dynamic<>(Unit.State.class, () -> unit(axis, firstUnit).state, u -> {
            sizer(axis).getNext(selectedWidget(), u, firstUnit);
            selectedWidget().resizer().scheduleResize();
        });

        var dropdown = new DropdownWidget<>("unit" + axis.name() + (firstUnit ? "1" : "2"), Unit.State.class)
                .size(40, 20)
                .value(unitType)
                .options(unitStateSupplier.get())
                .optionToWidget((s, forSelected) ->
                        new TextWidget<>(s.getText(axis)).center().color(Color.WHITE.main).padding(0, 5))
                .onUpdateListener(d ->
                        d.clearOptions().options(unitStateSupplier.get()).deleteMenu());

        return Flow.row().height(20)
                .child(dropdown)
                .child(measure(firstUnit, axis))
                .child(value(firstUnit, axis))
                .child(offset(firstUnit, axis))
                .child(anchor(firstUnit, axis));
    }

    private ToggleButton measure(boolean firstUnit, GuiAxis axis) {
        var value = new BoolValue.Dynamic(() -> unit(axis, firstUnit).getMeasure() == Unit.Measure.RELATIVE,
                (b) -> {
                    var u = unit(axis, firstUnit);
                    var current = u.getMeasure();
                    u.setMeasure(b ? Unit.Measure.RELATIVE : Unit.Measure.PIXEL);
                    selectedWidget().resizer().scheduleResize();
                }
        );

        return new ToggleButton()
                .size(20, 20)
                .marginLeft(10).marginRight(10)
                .selectedBackground(GuiTextures.CHECK_BOX_FULL)
                .tooltip(r -> r.addLine("When disabled, values represent actual size in pixels").addLine("When enabled, values are relative to the parent widget"))
                .background(GuiTextures.CHECK_BOX_EMPTY)
                .setEnabledIf(f -> unit(axis, firstUnit).state != Unit.State.UNUSED)
                .value(value);
    }

    private TextFieldWidget value(boolean firstUnit, GuiAxis axis) {
        var value = new FloatValue.Dynamic(() -> unit(axis, firstUnit).getValue(), v -> {
            unit(axis, firstUnit).setValue(v);
            selectedWidget().resizer().scheduleResize();
        });

        return new TextFieldWidget()
                .size(50, 20)
                .marginLeft(5)
                .setNumbersDouble(d -> d)
                .setEnabledIf(f -> (unit(axis, firstUnit).state != Unit.State.UNUSED))
                .value(value);
    }

    private TextFieldWidget offset(boolean firstUnit, GuiAxis axis) {
        var value = new IntValue.Dynamic(() -> unit(axis, firstUnit).getOffset(), v -> {
            unit(axis, firstUnit).setOffset(v);
            selectedWidget().resizer().scheduleResize();
        });

        return new TextFieldWidget()
                .size(50, 20)
                .marginLeft(5)
                .tooltip(r -> r.addLine("Pixel offset applied after relative sizing"))
                .setNumbers()
                .setEnabledIf(f -> (unit(axis, firstUnit).state != Unit.State.UNUSED))
                .value(value);
    }

    private TextFieldWidget anchor(boolean firstUnit, GuiAxis axis) {
        var value = new FloatValue.Dynamic(() -> unit(axis, firstUnit).getAnchor(), v -> {
            unit(axis, firstUnit).setAnchor(v);
            selectedWidget().resizer().scheduleResize();
        });

        return new TextFieldWidget()
                .size(50, 20)
                .marginLeft(5)
                .setNumbersDouble(d -> d)
                .setEnabledIf(f -> (unit(axis, firstUnit).state != Unit.State.UNUSED))
                .value(value);
    }
}
