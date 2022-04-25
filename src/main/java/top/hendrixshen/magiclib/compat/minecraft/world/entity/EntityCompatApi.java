package top.hendrixshen.magiclib.compat.minecraft.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.UUID;

public interface EntityCompatApi {

    default Level getLevelCompat() {
        throw new UnsupportedOperationException();
    }

    default int getBlockXCompat() {
        return this.blockPositionCompat().getX();
    }

    default int getBlockYCompat() {
        return this.blockPositionCompat().getY();
    }

    default int getBlockZCompat() {
        return this.blockPositionCompat().getZ();
    }

    default float getYRotCompat() {
        throw new UnsupportedOperationException();
    }

    default void setYRotCompat(float yRot) {
        throw new UnsupportedOperationException();
    }

    default float getXRotCompat() {
        throw new UnsupportedOperationException();
    }

    default void setXRotCompat(float xRot) {
        throw new UnsupportedOperationException();
    }

    default BlockPos blockPositionCompat() {
        throw new UnsupportedOperationException();
    }

    default boolean isOnGroundCompat() {
        throw new UnsupportedOperationException();
    }

    default void setOnGroundCompat(boolean onGround) {
        throw new UnsupportedOperationException();
    }

    default void sendMessageCompat(Component component, UUID uuid) {
        throw new UnsupportedOperationException();
    }

    //#if MC <= 11701
    // fuck remap
    // It will cause remap fail...
    ////$$ default Level getLevel() {
    ////$$     return this.getLevelCompat();
    ////$$ }
    //#endif

    //#if MC <= 11605
    //$$ default int getBlockX() {
    //$$     return this.getBlockXCompat();
    //$$ }

    //$$ default int getBlockY() {
    //$$     return this.getBlockYCompat();
    //$$ }

    //$$ default int getBlockZ() {
    //$$     return this.getBlockZCompat();
    //$$ }

    //$$ default float getYRot() {
    //$$     return this.getYRotCompat();
    //$$ }

    //$$ default void setYRot(float yRot) {
    //$$     this.setYRotCompat(yRot);
    //$$ }

    //$$ default float getXRot() {
    //$$     return this.getXRotCompat();
    //$$ }

    //$$ default void setXRot(float xRot) {
    //$$     this.setXRotCompat(xRot);
    //$$ }
    //#endif

    //#if MC <= 11502
    //$$ default BlockPos blockPosition() {
    //$$     return this.blockPositionCompat();
    //$$ }

    //$$ default boolean isOnGround() {
    //$$     return this.isOnGroundCompat();
    //$$ }

    //$$ default void sendMessage(Component component, UUID uuid) {
    //$$     this.sendMessageCompat(component, uuid);
    //$$ }

    //$$ default void setOnGround(boolean onGround) {
    //$$     this.setOnGroundCompat(onGround);
    //$$ }
    //#endif

}