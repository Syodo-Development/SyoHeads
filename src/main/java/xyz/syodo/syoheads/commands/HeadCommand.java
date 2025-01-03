package xyz.syodo.syoheads.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.Item;
import xyz.syodo.syoheads.utils.ItemUtils;

public class HeadCommand extends Command {

    public HeadCommand() {
        super("head");
        setPermission("syohead.get");
        setUsage("§c/head (bedrock|java) [playername]");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if(sender instanceof Player player) {
            if(args.length > 0) {
                if(args.length == 2) {
                    Item item = null;
                    if(args[0].equalsIgnoreCase("bedrock")) {
                        Player target = Server.getInstance().getPlayer(args[1]);
                        if(target != null) {
                            if(target.isOnline()) {
                                Skin skin = target.getSkin();
                                if(!skin.isPersona()) {
                                    item = ItemUtils.createSkullItem(target.getName(), skin.getSkinData().data);
                                } else sender.sendMessage("§cThe players skin cannot be used for heads.");
                            } else sender.sendMessage("§cThis player is offline!");
                        } else sender.sendMessage("§cThis player does not exist.");
                    } else if(args[0].equalsIgnoreCase("java")) {
                        item = ItemUtils.createJavaSkullItem(args[1]);
                    } else sender.sendMessage(getUsage());
                    if(item != null) {
                        player.getInventory().addItem(item);
                        player.sendMessage("§aHead created successfully.");
                    }
                } else sender.sendMessage(getUsage());
            } else sender.sendMessage(getUsage());
        } else sender.sendMessage("Only a player can execute this command!");
        return true;
    }
}
