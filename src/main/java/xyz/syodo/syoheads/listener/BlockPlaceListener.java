package xyz.syodo.syoheads.listener;

import cn.nukkit.block.BlockPlayerHead;
import cn.nukkit.blockentity.BlockEntitySkull;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.nbt.tag.CompoundTag;
import xyz.syodo.syoheads.entity.EntityHead;
import xyz.syodo.syoheads.utils.SkinUtils;

public class BlockPlaceListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(BlockPlaceEvent event) {
        if(event.isCancelled()) return;
        if(event.getBlock() instanceof BlockPlayerHead head) {
            if(event.getItem().getOrCreateNamedTag().containsCompound("HeadData")) {
                event.getBlock().getLevel().getScheduler().scheduleDelayedTask(() -> {
                    BlockEntitySkull skull = head.getOrCreateBlockEntity();
                    if(!skull.namedTag.containsCompound("HeadEntityData")) {
                        CompoundTag itemHeadData = event.getItem().getNamedTag().getCompound("HeadData");
                        CompoundTag skullDataTag = SkinUtils.nbt(head, itemHeadData.getByteArray("SkinData"));
                        skullDataTag.putString("Owner", itemHeadData.getString("Owner"));
                        skull.namedTag.putCompound("HeadEntityData", skullDataTag);
                    }
                    EntityHead entityHead = EntityHead.create(skull);
                    skull.namedTag.putLong("headEntityId", entityHead.getId());
                }, 1);
            }
        }
    }
}
