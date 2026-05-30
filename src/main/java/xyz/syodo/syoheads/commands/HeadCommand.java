package xyz.syodo.syoheads.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.tree.ParamList;
import cn.nukkit.command.utils.CommandLogger;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.human.Skin;
import cn.nukkit.item.Item;
import org.cloudburstmc.protocol.bedrock.data.command.CommandParamType;
import xyz.syodo.syoheads.database.Database;
import xyz.syodo.syoheads.utils.ItemUtils;

import java.util.ArrayList;
import java.util.Map;

public class HeadCommand extends Command {

    public HeadCommand() {
        super("head");
        setPermission("syohead.get");
        setUsage("§c/head (bedrock|java|database) [playername]");
        this.commandParameters.clear();
        this.commandParameters.put("bedrock", new CommandParameter[]{
                CommandParameter.newEnum("bedrock", new String[]{"bedrock"}),
                CommandParameter.newType("player", CommandParamType.WILDCARD_SELECTION),
        });
        this.commandParameters.put("java", new CommandParameter[]{
                CommandParameter.newEnum("java", new String[]{"java"}),
                CommandParameter.newType("player", CommandParamType.RAW_TEXT),
        });
        this.commandParameters.put("database", new CommandParameter[]{
                CommandParameter.newEnum("database", new String[]{"database"})
        });
        this.enableParamTree();
    }

    @Override
    public int execute(CommandSender sender, String commandLabel, Map.Entry<String, ParamList> result, CommandLogger log) {
        Item item = null;
        var list = result.getValue();
        if(sender instanceof Player player) {
            switch (result.getKey()) {
                case "database" -> {
                    Database.openDatabaseScreen(player);
                    return 1;
                }
                case "bedrock" -> {
                    ArrayList<Entity> entities = list.getResult(1);
                    if(entities.size() == 1) {
                        if(entities.get(0) instanceof Player target) {
                            if(target.isOnline()) {
                                Skin skin = target.getSkin();
                                if(!skin.getSkin().isPersona()) {
                                    item = ItemUtils.createSkullItem(target.getName(), skin.getSkin().getSkinData().getImage());
                                } else sender.sendMessage("§cThe players skin cannot be used for heads.");
                            } else sender.sendMessage("§cThis player is offline!");
                        } else sender.sendMessage("§cThe target is not a player.");
                    } else sender.sendMessage("§cPlease target only one player.");
                }
                case "java" -> {
                    item = ItemUtils.createJavaSkullItem(list.getResult(1));
                }
            }
            if(item != null) {
                player.getInventory().addItem(item);
                player.sendMessage("§aHead created successfully.");
            }
        } else sender.sendMessage("Only a player can execute this command!");
        return 1;
    }
}
