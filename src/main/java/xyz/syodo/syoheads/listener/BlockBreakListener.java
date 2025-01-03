package xyz.syodo.syoheads.listener;

import cn.nukkit.block.BlockPlayerHead;
import cn.nukkit.blockentity.BlockEntitySkull;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import xyz.syodo.syoheads.entity.EntityHead;
import xyz.syodo.syoheads.utils.ItemUtils;

public class BlockBreakListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(BlockBreakEvent event) {
        if(event.isCancelled()) return;
        if(event.getBlock() instanceof BlockPlayerHead head) {
            BlockEntitySkull skull = head.getOrCreateBlockEntity();
            if(skull.namedTag.containsCompound("HeadEntityData")) {
                CompoundTag headData = skull.namedTag.getCompound("HeadEntityData");
                if(skull.namedTag.contains("headEntityId")) {
                    Entity entity = skull.getLevel().getEntity(skull.namedTag.getLong("headEntityId"));
                    if(entity != null) {
                        if(entity instanceof EntityHead entityHead) {
                            if(event.getDrops().length != 0) {
                                event.setDrops(new Item[]{ItemUtils.createSkullItem(headData.getString("Owner"), entityHead.getSkin().getSkinData().data)});
                            }
                            entity.close();
                        }
                    }
                }
            }
        }
    }
}
