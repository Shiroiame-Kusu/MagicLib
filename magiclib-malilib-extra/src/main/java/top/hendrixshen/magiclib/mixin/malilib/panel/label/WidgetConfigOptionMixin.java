package top.hendrixshen.magiclib.mixin.malilib.panel.label;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.widgets.WidgetConfigOption;
import fi.dy.masa.malilib.gui.widgets.WidgetConfigOptionBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptions;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptionsBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;
import top.hendrixshen.magiclib.impl.malilib.config.gui.MagicConfigGui;
import top.hendrixshen.magiclib.mixin.malilib.accessor.WidgetListConfigOptionsAccessor;

import java.util.function.Function;

@Mixin(value = WidgetConfigOption.class, remap = false)
public abstract class WidgetConfigOptionMixin extends WidgetConfigOptionBase<GuiConfigsBase.ConfigOptionWrapper> {
    public WidgetConfigOptionMixin(int x, int y, int width, int height, WidgetListConfigOptionsBase<?, ?> parent,
                                   GuiConfigsBase.ConfigOptionWrapper entry, int listIndex) {
        super(x, y, width, height, parent, entry, listIndex);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    @Unique
    private boolean magiclib$isMagicGui() {
        return this.parent instanceof WidgetListConfigOptions &&
                ((WidgetListConfigOptionsAccessor) this.parent).magiclib$getParent() instanceof MagicConfigGui;
    }

    @ModifyArg(
            method = "addConfigOption",
            at = @At(
                    value = "INVOKE",
                    target = "Lfi/dy/masa/malilib/gui/widgets/WidgetConfigOption;addLabel(IIIII[Ljava/lang/String;)V"
            )
    )
    private String[] re(String[] strings) {
        return strings;
    }

    // FIXME: ModifyArgs broken on Forge 1.17+
    @ModifyArgs(
            method = "addConfigOption",
            at = @At(
                    value = "INVOKE",
                    target = "Lfi/dy/masa/malilib/gui/widgets/WidgetConfigOption;addLabel(IIIII[Ljava/lang/String;)V"
            )
    )
    private void applyConfigLabelTextModifier(Args args, int x_, int y_, float zLevel, int labelWidth,
                                              int configWidth, IConfigBase config) {
        if (!this.magiclib$isMagicGui()) {
            return;
        }

        if (!(config instanceof MagicIConfigBase)) {
            return;
        }

        String[] originalLines = args.get(5);
        Function<String, String> modifier = ((MagicIConfigBase) config).getGuiDisplayLineModifier();

        for (int i = 0; i < originalLines.length; i++) {
            originalLines[i] = modifier.apply(originalLines[i]);
        }
    }
}