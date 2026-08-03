package org.powernukkitx.heads.listener;

import org.powernukkitx.block.BlockPlayerHead;
import org.powernukkitx.blockentity.BlockEntitySkull;
import org.powernukkitx.entity.Entity;
import org.powernukkitx.event.EventHandler;
import org.powernukkitx.event.EventPriority;
import org.powernukkitx.event.Listener;
import org.powernukkitx.event.block.BlockBreakEvent;
import org.powernukkitx.item.Item;
import org.powernukkitx.nbt.tag.CompoundTag;
import org.powernukkitx.heads.entity.EntityHead;
import org.powernukkitx.heads.utils.ItemUtils;

public class BlockBreakListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(BlockBreakEvent event) {
        if(event.isCancelled()) return;
        if(event.getBlock() instanceof BlockPlayerHead head) {
            BlockEntitySkull skull = head.getOrCreateBlockEntity();
            if(skull.getNbt().containsCompound("HeadEntityData")) {
                CompoundTag headData = skull.getNbt().getCompound("HeadEntityData");
                if(skull.getNbt().contains("headEntityId")) {
                    Entity entity = skull.getLevel().getEntity(skull.getNbt().getLong("headEntityId"));
                    if(entity != null) {
                        if(entity instanceof EntityHead entityHead) {
                            if(event.getDrops().length != 0) {
                                event.setDrops(new Item[]{ItemUtils.createSkullItem(headData.getString("Owner"), entityHead.getSkin().getSkin().getSkinData().getImage())});
                            }
                            entity.close();
                        }
                    }
                }
            }
        }
    }
}
