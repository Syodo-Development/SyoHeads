package org.powernukkitx.heads.listener;

import org.powernukkitx.blockentity.BlockEntity;
import org.powernukkitx.blockentity.BlockEntitySkull;
import org.powernukkitx.event.EventHandler;
import org.powernukkitx.event.EventPriority;
import org.powernukkitx.event.Listener;
import org.powernukkitx.event.level.ChunkLoadEvent;
import org.powernukkitx.heads.entity.EntityHead;

public class ChunkLoadListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(ChunkLoadEvent event) {
        if(event.isNewChunk()) return;
        event.getLevel().getScheduler().scheduleDelayedTask(() -> {
            for(BlockEntity entity : event.getChunk().getBlockEntities().values()) {
                if(entity instanceof BlockEntitySkull skull) {
                    if(skull.getNbt().containsCompound("HeadEntityData")) {
                        EntityHead entityHead = EntityHead.create(skull);
                        skull.getNbt().putLong("headEntityId", entityHead.getId());
                    }
                }
            }
        }, 1);
    }
}
