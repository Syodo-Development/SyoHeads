package xyz.syodo.syoheads.utils;

import cn.nukkit.block.BlockPlayerHead;
import cn.nukkit.blockentity.BlockEntitySkull;
import cn.nukkit.entity.data.Skin;
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
        location.yaw = blockFace == BlockFace.UP ? (blockEntity.namedTag.getByte("Rot") * 22.5 + 180) % 360 : blockFace.getHorizontalIndex() * 90;
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
        Skin skin = new Skin();
        skin.setGeometryData(PLACED_SKULL_GEOMETRY);
        skin.setGeometryName(PLACED_SKULL_GEOMETRY_NAME);
        skin.setCapeId("nocape");
        skin.setSkinData(skinData);
        skin.setTrusted(true);
        CompoundTag skinTag = new CompoundTag()
                .putString("ModelId", skin.getSkinId())
                .putByteArray("Data", skin.getSkinData().data)
                .putInt("SkinImageWidth", skin.getSkinData().width)
                .putInt("SkinImageHeight", skin.getSkinData().height)
                .putString("CapeId", skin.getCapeId())
                .putByteArray("CapeData", skin.getCapeData().data)
                .putInt("CapeImageWidth", skin.getCapeData().width)
                .putInt("CapeImageHeight", skin.getCapeData().height)
                .putByteArray("SkinResourcePatch", skin.getSkinResourcePatch().getBytes(StandardCharsets.UTF_8))
                .putByteArray("GeometryData", skin.getGeometryData().getBytes(StandardCharsets.UTF_8))
                .putByteArray("SkinAnimationData", skin.getAnimationData().getBytes(StandardCharsets.UTF_8))
                .putBoolean("PremiumSkin", skin.isPremium())
                .putBoolean("PersonaSkin", skin.isPersona())
                .putBoolean("CapeOnClassicSkin", skin.isCapeOnClassic())
                .putString("ArmSize", skin.getArmSize())
                .putString("SkinColor", skin.getSkinColor())
                .putBoolean("IsTrustedSkin", true);
        return skinTag;
    }

}
