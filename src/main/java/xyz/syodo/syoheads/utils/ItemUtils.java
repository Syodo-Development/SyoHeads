package xyz.syodo.syoheads.utils;

import cn.nukkit.block.BlockPlayerHead;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ItemUtils {

    public static Item createJavaSkullItem(String username) {
        Item item = null;
        try {
            item = createSkullFromUrl(username, new URL("https://minecraft.tools/download-skin/" + username));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return item;
    }

    public static Item createSkullFromUrl(String owner, URL url) {
        Item item = Item.AIR;
        try {
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
            item = createSkullItem(owner, imageData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return item;
    }

    public static Item createSkullItem(String owner, byte[] skinData) {
        if (!(cn.nukkit.block.Block.get("minecraft:player_head") instanceof BlockPlayerHead block)) {
            return Item.AIR;
        }
        Item item = block.toItem();
        item.setBlockUnsafe(block);
        Item updateDamage = block.toItem();
        if (updateDamage.getDamage() != 0) {
            item.setDamage(updateDamage.getDamage());
        }
        item.setDamage (updateDamage.getDamage());
        item.setCount(1);
        CompoundTag itemData = new CompoundTag();
        itemData.putByteArray("SkinData", skinData);
        itemData.putString("Owner", owner);
        item.getOrCreateNbt().putCompound("HeadData", itemData);
        item.setCustomName(owner + (owner.endsWith("s") ? "" : "'s") + " Head");
        return item;
    }
}
