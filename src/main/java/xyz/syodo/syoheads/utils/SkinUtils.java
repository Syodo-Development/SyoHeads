package xyz.syodo.syoheads.utils;

import cn.nukkit.block.BlockPlayerHead;
import cn.nukkit.blockentity.BlockEntitySkull;
import cn.nukkit.level.Location;
import cn.nukkit.math.BlockFace;
import cn.nukkit.nbt.tag.*;

import java.nio.charset.StandardCharsets;

public class SkinUtils {

    public static final String PLACED_SKULL_GEOMETRY_NAME = "geometry.custom.skullEntity";
    public static final String PLACED_SKULL_GEOMETRY = "{\"format_version\":\"1.12.0\",\"minecraft:geometry\":[{\"description\":{\"identifier\":\"geometry.custom.skullEntity\",\"texture_width\":64,\"texture_height\":64,\"visible_bounds_width\":2,\"visible_bounds_height\":1,\"visible_bounds_offset\":[0,0,0]},\"bones\":[{\"name\":\"head\",\"pivot\":[0,24,0],\"cubes\":[{\"origin\":[-4,0,-4],\"size\":[8,8,8],\"uv\":[0,0]}]},{\"name\":\"hat\",\"parent\":\"head\",\"pivot\":[0,24,0],\"cubes\":[{\"origin\":[-4,0,-4],\"size\":[8,8,8],\"inflate\":0.2,\"uv\":[32,0]}]}]}]}";

    public static CompoundTag nbt(BlockPlayerHead skull, byte[] skinData) {
        BlockEntitySkull blockEntity = skull.getBlockEntity();
        final BlockFace blockFace = skull.getBlockFace();
        if(blockFace == BlockFace.DOWN) return null;
        Location location = Location.fromObject(skull.add(0.5, -0.00735, 0.5));
        location.yaw = blockFace == BlockFace.UP ? (blockEntity.getNbt().getByte("Rot") * 22.5 + 180) % 360 : blockFace.getHorizontalIndex() * 90;
        if(blockFace != BlockFace.UP)
            location = location.add(blockFace.getUnitVector().multiply(-0.23895).add(0, 0.25, 0));
        CompoundTag nbt = new CompoundTag()
                .putList("Pos",new ListTag<>()
                        .add(new DoubleTag(location.x))
                        .add(new DoubleTag(location.y))
                        .add(new DoubleTag(location.z)))
                .putList("Motion", new ListTag<DoubleTag>()
                        .add(new DoubleTag(0))
                        .add(new DoubleTag(0))
                        .add(new DoubleTag(0)))
                .putList("Rotation", new ListTag<FloatTag>()
                        .add(new FloatTag(location.yaw))
                        .add(new FloatTag(0)))
                .putBoolean("Invulnerable", true)
                .putString("NameTag", "")
                .putFloat("Scale", 1.01f);
        nbt.putCompound("Skin", createSkinTag(skinData));
        nbt.putBoolean("ishuman", true);
        return nbt;
    }

    private static CompoundTag createSkinTag(byte[] skinData) {
        int[] skinDimensions = getSkinDimensions(skinData);
        CompoundTag skinTag = new CompoundTag()
                .putString("ModelId", "syoheads")
                .putByteArray("Data", skinData)
                .putInt("SkinImageWidth", skinDimensions[0])
                .putInt("SkinImageHeight", skinDimensions[1])
                .putString("CapeId", "nocape")
                .putByteArray("CapeData", new byte[0])
                .putInt("CapeImageWidth", 0)
                .putInt("CapeImageHeight", 0)
                .putByteArray("SkinResourcePatch", ("{\"geometry\":{\"default\":\"" + PLACED_SKULL_GEOMETRY_NAME + "\"}}").getBytes(StandardCharsets.UTF_8))
                .putByteArray("GeometryData", PLACED_SKULL_GEOMETRY.getBytes(StandardCharsets.UTF_8))
                .putByteArray("SkinAnimationData", new byte[0])
                .putBoolean("PremiumSkin", false)
                .putBoolean("PersonaSkin", false)
                .putBoolean("CapeOnClassicSkin", false)
                .putString("ArmSize", "wide")
                .putString("SkinColor", "")
                .putBoolean("IsTrustedSkin", true);
        return skinTag;
    }

    private static int[] getSkinDimensions(byte[] skinData) {
        return switch (skinData.length) {
            case 8192 -> new int[]{64, 32};
            case 16384 -> new int[]{64, 64};
            case 32768 -> new int[]{128, 64};
            case 65536 -> new int[]{128, 128};
            default -> new int[]{64, 64};
        };
    }

}
