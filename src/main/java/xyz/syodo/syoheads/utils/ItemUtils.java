package xyz.syodo.syoheads.utils;

import cn.nukkit.block.BlockPlayerHead;
import cn.nukkit.block.BlockState;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.registry.Registries;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteOrder;
import java.util.Base64;

public class ItemUtils {

    public static Item createJavaSkullItem(String username) {
        Item item = Item.AIR;
        try {
            URL url = new URL("https://minecraft.tools/download-skin/" + username);
            BufferedImage image = ImageIO.read(url);
            final byte[] imageData = new byte[image.getHeight() * image.getWidth() * 4];
            int cursor = 0;
            for(int y = 0; y < image.getHeight(); y++) {
                for(int x = 0; x < image.getWidth(); x++) {
                    final int color = image.getRGB(x, y);
                    imageData[cursor++] = (byte) ((color >> 16) & 0xFF);
                    imageData[cursor++] = (byte) ((color >> 8) & 0xFF);
                    imageData[cursor++] = (byte) (color & 0xFF);
                    imageData[cursor++] = (byte) ((color >> 24) & 0xFF);
                }
            }
            item = createSkullItem(username, imageData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return item;
    }

    public static Item createSkullItem(String owner, byte[] skinData) {
        byte[] blockTag = Base64.getDecoder().decode("CgAAAwoAbmV0d29ya19pZGXgF6gECQBuYW1lX2hhc2icXBlvsIvwRggEAG5hbWUVAG1pbmVjcmFmdDpwbGF5ZXJfaGVhZAMHAHZlcnNpb24ZMhUBCgYAc3RhdGVzAxAAZmFjaW5nX2RpcmVjdGlvbgAAAAAAAA==");
        CompoundTag blockCompoundTag = null;
        try {
            blockCompoundTag = NBTIO.read(blockTag, ByteOrder.LITTLE_ENDIAN);
        } catch (Exception e) {
            e.printStackTrace();
            return Item.AIR;
        }
        int blockHash = blockCompoundTag.getInt("network_id");
        BlockState block = Registries.BLOCKSTATE.get(blockHash);
        Item item = new BlockPlayerHead(block).toItem(); item.setBlockUnsafe(block.toBlock());
        Item updateDamage = block.toBlock().toItem();
        if (updateDamage.getDamage() != 0) {
            item.setDamage(updateDamage.getDamage());
        }
        item.setDamage (updateDamage.getDamage());
        item.setCount(1);
        CompoundTag itemData = new CompoundTag();
        itemData.putByteArray("SkinData", skinData);
        itemData.putString("Owner", owner);
        item.getOrCreateNamedTag().putCompound("HeadData", itemData);
        item.setCustomName(owner + (owner.endsWith("s") ? "" : "'s") + " Head");
        return item;
    }
}
