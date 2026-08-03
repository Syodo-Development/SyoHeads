package xyz.syodo.syoheads.listener;

import org.powernukkitx.block.BlockPlayerHead;
import org.powernukkitx.blockentity.BlockEntitySkull;
import org.powernukkitx.event.EventHandler;
import org.powernukkitx.event.EventPriority;
import org.powernukkitx.event.Listener;
import org.powernukkitx.event.block.BlockPlaceEvent;
import org.powernukkitx.nbt.tag.CompoundTag;
import xyz.syodo.syoheads.entity.EntityHead;
import xyz.syodo.syoheads.utils.SkinUtils;

public class BlockPlaceListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(BlockPlaceEvent event) {
        if(event.isCancelled()) return;
        if(event.getBlock() instanceof BlockPlayerHead head) {
            if(event.getItem().getOrCreateNbt().containsCompound("HeadData")) {
                event.getBlock().getLevel().getScheduler().scheduleDelayedTask(() -> {
                    BlockEntitySkull skull = head.getOrCreateBlockEntity();
                    if(!skull.getNbt().containsCompound("HeadEntityData")) {
                        CompoundTag itemHeadData = event.getItem().getNbt().getCompound("HeadData");
                        CompoundTag skullDataTag = SkinUtils.nbt(head, itemHeadData.getByteArray("SkinData"));
                        skullDataTag.putString("Owner", itemHeadData.getString("Owner"));
                        skull.getNbt().putCompound("HeadEntityData", skullDataTag);
                    }
                    EntityHead entityHead = EntityHead.create(skull);
                    skull.getNbt().putLong("headEntityId", entityHead.getId());
                }, 1);
            }
        }
    }
}
