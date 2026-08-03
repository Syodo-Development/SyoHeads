package org.powernukkitx.heads;

import org.powernukkitx.plugin.PluginBase;
import org.powernukkitx.plugin.PluginManager;
import org.powernukkitx.heads.commands.HeadCommand;
import org.powernukkitx.heads.database.Database;
import org.powernukkitx.heads.listener.BlockBreakListener;
import org.powernukkitx.heads.listener.BlockPlaceListener;
import org.powernukkitx.heads.listener.ChunkLoadListener;

public class Heads extends PluginBase {

    private static Heads INSTANCE;

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

    public static Heads get() {
        return INSTANCE;
    }
}
