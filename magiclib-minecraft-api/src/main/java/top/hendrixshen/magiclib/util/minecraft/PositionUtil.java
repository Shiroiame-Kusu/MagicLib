package top.hendrixshen.magiclib.util.minecraft;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.longs.Long2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class PositionUtil {
    public static @NotNull Vec3 centerOf(@NotNull BlockPos blockPos) {
        return new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
    }

    public static @NotNull Collection<BlockPos> boxSurface(@NotNull BlockPos pos1, @NotNull BlockPos pos2) {
        int minX = Math.min(pos1.getX(), pos2.getX());
        int minY = Math.min(pos1.getY(), pos2.getY());
        int minZ = Math.min(pos1.getZ(), pos2.getZ());
        int maxX = Math.max(pos1.getX(), pos2.getX());
        int maxY = Math.max(pos1.getY(), pos2.getY());
        int maxZ = Math.max(pos1.getZ(), pos2.getZ());
        LongOpenHashSet set = new LongOpenHashSet();
        List<BlockPos> result = Lists.newArrayList();
        Consumer<BlockPos> storage = pos -> {
            if (set.add(pos.asLong())) {
                result.add(pos);
            }
        };

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                storage.accept(new BlockPos(x, y, minZ));
                storage.accept(new BlockPos(x, y, maxZ));
            }
        }

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                storage.accept(new BlockPos(x, minY, z));
                storage.accept(new BlockPos(x, maxY, z));
            }
        }

        for (int y = minY; y <= maxY; y++) {
            for (int z = minZ; z <= maxZ; z++) {
                storage.accept(new BlockPos(minX, y, z));
                storage.accept(new BlockPos(maxX, y, z));
            }
        }

        return result;
    }

    public static Collection<BlockPos> beam(Vec3 startPos, @NotNull Vec3 endPos, double coneAngle, BeamMode mode) {
        Vec3 dir1 = endPos.subtract(startPos).normalize();

        if (dir1 == Vec3.ZERO) {
            return Collections.emptyList();
        }

        double maxLen = startPos.distanceTo(endPos);
        double step = 1 / (1 + Math.sin(coneAngle));
        Long2DoubleOpenHashMap positions = new Long2DoubleOpenHashMap();
        BlockPos lastMin = null;
        BlockPos lastMax = null;

        for (double len = 0, angle = coneAngle; len < maxLen + step; len += step) {
            double r = len * Math.sin(angle);
            Vec3 vec3 = startPos.add(dir1.scale(len));
            Vec3 a = vec3.add(-r, -r, -r);
            Vec3 b = vec3.add(+r, +r, +r);
            BlockPos pos1 = new BlockPos((int) Math.floor(a.x), (int) Math.floor(a.y), (int) Math.floor(a.z));
            BlockPos pos2 = new BlockPos((int) Math.ceil(b.x), (int) Math.ceil(b.y), (int) Math.ceil(b.z));

            if (lastMin != null) {
                // Optimize increasing PositionUtil.boxSurface
                int minX = pos1.getX();
                int minY = pos1.getY();
                int minZ = pos1.getZ();
                int maxX = pos2.getX();
                int maxY = pos2.getY();
                int maxZ = pos2.getZ();

                // minX changed
                if (minX != lastMin.getX()) {
                    for (int y = minY; y <= maxY; y++) {
                        for (int z = minZ; z <= maxZ; z++) {
                            positions.putIfAbsent(new BlockPos(minX, y, z).asLong(), angle);
                        }
                    }
                }

                // maxX changed
                if (maxX != lastMax.getX()) {
                    for (int y = minY; y <= maxY; y++) {
                        for (int z = minZ; z <= maxZ; z++) {
                            positions.putIfAbsent(new BlockPos(maxX, y, z).asLong(), angle);
                        }
                    }
                }

                // minY changed
                if (minY != lastMin.getY()) {
                    for (int x = minX; x <= maxX; x++) {
                        for (int z = minZ; z <= maxZ; z++) {
                            positions.putIfAbsent(new BlockPos(x, minY, z).asLong(), angle);
                        }
                    }
                }

                // minY changed
                if (maxY != lastMax.getY()) {
                    for (int x = minX; x <= maxX; x++) {
                        for (int z = minZ; z <= maxZ; z++) {
                            positions.putIfAbsent(new BlockPos(x, maxY, z).asLong(), angle);
                        }
                    }
                }

                // minZ changed
                if (minZ != lastMin.getZ()) {
                    for (int x = minX; x <= maxX; x++) {
                        for (int y = minY; y <= maxY; y++) {
                            positions.putIfAbsent(new BlockPos(x, y, minZ).asLong(), angle);
                        }
                    }
                }

                // maxZ changed
                if (maxZ != lastMin.getZ()) {
                    for (int x = minX; x <= maxX; x++) {
                        for (int y = minY; y <= maxY; y++) {
                            positions.putIfAbsent(new BlockPos(x, y, maxZ).asLong(), angle);
                        }
                    }
                }
            } else {
                for (BlockPos pos : PositionUtil.boxSurface(pos1, pos2)) {
                    positions.putIfAbsent(pos.asLong(), angle);
                }
            }

            lastMin = pos1;
            lastMax = pos2;

            switch (mode) {
                case BEAM:
                    angle = coneAngle * Math.max(1 - len / maxLen, 0);
                    break;
                case CONE:
                    angle = coneAngle;
                    break;
            }
        }

        List<BlockPos> result = Lists.newArrayList();
        positions.forEach((l, a) -> {
            BlockPos pos = BlockPos.of(l);
            Vec3 vec3 = PositionUtil.centerOf(pos).subtract(startPos);

            if (vec3.length() <= maxLen) {
                Vec3 dir2 = vec3.normalize();
                double cos = dir2.dot(dir1);

                if (cos >= Math.cos(a)) {
                    result.add(pos);
                }
            }
        });

        return result;
    }

    public enum BeamMode {
        /*
         *       / ---\
         *   / ---------\
         * x -----------|
         *   \ ---------/
         *       \ ---/
         */
        BEAM,

        /*
         *       /-|
         *     /---|
         *   /-----|
         * x ------|
         *   \-----|
         *     \---|
         *       \-|
         */
        CONE
    }
}
