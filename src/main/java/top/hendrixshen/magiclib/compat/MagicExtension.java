package top.hendrixshen.magiclib.compat;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;
import top.hendrixshen.magiclib.util.MixinUtil;

public class MagicExtension implements IExtension {
    @Override
    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    @Override
    public void preApply(ITargetClassContext context) {

    }

    @Override
    public void postApply(ITargetClassContext context) {
        ClassNode classNode = context.getClassNode();
        MixinUtil.applyPublic(classNode);
        MixinUtil.applyInit(classNode);
//        SortedSet<IMixinInfo> iMixinInfos = MixinUtil.getMixins(context);
//        for (IMixinInfo iMixinInfo: iMixinInfos) {
//            MixinUtil.applyInterfaceRemap(MixinUtil.getMixinClassNode(iMixinInfo), MixinUtil.getClassInfo());
//        }
        for (IMixinInfo iMixinInfo : MixinUtil.getMixins(context)) {
            MixinUtil.applyInnerClass(classNode, iMixinInfo.getClassNode(ClassReader.SKIP_CODE));
        }
        MixinUtil.applyRemap(classNode);

    }

    @Override
    public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {

    }
}
