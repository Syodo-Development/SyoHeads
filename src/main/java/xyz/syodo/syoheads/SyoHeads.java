package xyz.syodo.syoheads;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import xyz.syodo.syoheads.commands.HeadCommand;
import xyz.syodo.syoheads.database.Database;
import xyz.syodo.syoheads.listener.BlockBreakListener;
import xyz.syodo.syoheads.listener.BlockPlaceListener;
import xyz.syodo.syoheads.listener.ChunkLoadListener;

public class SyoHeads extends PluginBase {

    private static SyoHeads INSTANCE;

    @Override
    public void onLoad() {
        if(INSTANCE == null) {
            INSTANCE = this;
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new BlockPlaceListener(), get());
        manager.registerEvents(new BlockBreakListener(), get());
        manager.registerEvents(new ChunkLoadListener(), get());
        manager.registerEvents(new Database(), get());

        getServer().getCommandMap().register("head", new HeadCommand());
    }

    public static SyoHeads get() {
        return INSTANCE;
    }
}
