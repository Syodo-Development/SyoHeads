package xyz.syodo.syoheads.listener;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySkull;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.level.ChunkLoadEvent;
import xyz.syodo.syoheads.entity.EntityHead;

public class ChunkLoadListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(ChunkLoadEvent event) {
        if(event.isNewChunk()) return;
        event.getLevel().getScheduler().scheduleDelayedTask(() -> {
            for(BlockEntity entity : event.getChunk().getBlockEntities().values()) {
                if(entity instanceof BlockEntitySkull skull) {
                    if(skull.namedTag.containsCompound("HeadEntityData")) {
                        EntityHead entityHead = EntityHead.create(skull);
                        skull.namedTag.putLong("headEntityId", entityHead.getId());
                    }
                }
            }
        }, 1);
    }
}
