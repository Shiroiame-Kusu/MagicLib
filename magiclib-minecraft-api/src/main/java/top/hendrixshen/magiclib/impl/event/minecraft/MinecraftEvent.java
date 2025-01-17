package top.hendrixshen.magiclib.impl.event.minecraft;

import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.event.Event;
import top.hendrixshen.magiclib.api.event.minecraft.MinecraftListener;

import java.util.List;

public class MinecraftEvent {
    public static class PostInitEvent implements Event<MinecraftListener> {
        @Override
        public void dispatch(@NotNull List<MinecraftListener> listeners) {
            listeners.forEach(MinecraftListener::postInit);
        }

        @Override
        public Class<MinecraftListener> getListenerType() {
            return MinecraftListener.class;
        }
    }
}
