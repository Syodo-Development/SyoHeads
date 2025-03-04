package xyz.syodo.syoheads.database;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.simple.ButtonImage;
import cn.nukkit.form.window.SimpleForm;
import cn.nukkit.item.Item;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import xyz.syodo.syoheads.utils.ItemUtils;

import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class Database implements Listener {

    public static final String API_URL = "https://headdb.org/api/category/";

    public static final HashMap<String, JsonObject> CACHE = new HashMap<>();

    public static final String[] CATEGORIES = {
            "Blocks",
            "Characters",
            "Christmas",
            "Electronics",
            "Flags",
            "Food",
            "Halloween",
            "Letters",
            "YouTubers"
    };

    public static void openDatabaseScreen(Player player) {
        SimpleForm formWindowSimple = new SimpleForm("§eheaddb.org", "");
        for(String category : CATEGORIES) {
            formWindowSimple.addButton("§e" + category, ButtonImage.Type.URL.of("https://headdb.org/img/categories/" + category.toLowerCase() + ".png"));
        }
        player.sendForm(formWindowSimple, "syoheads.categories".hashCode());
    }

    private static void openCategory(Player player, String category) {

        if(!CACHE.containsKey(category)) {
            try {
                CACHE.put(category, JsonParser.parseString(new Scanner(new URL(API_URL + category).openStream(), "UTF-8").useDelimiter("\\A").next()).getAsJsonObject());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        JsonObject categoryData = CACHE.get(category);
        SimpleForm formWindowSimple = new SimpleForm("§eheaddb.org", category);
        for(String head : categoryData.keySet()) {
            formWindowSimple.addButton("§e" + categoryData.get(head).getAsJsonObject().get("name").getAsString(), ButtonImage.Type.URL.of("https://headdb.org/img/renders/" + head + ".png"));
        }
        player.sendForm(formWindowSimple, "syoheads.category".hashCode());
    }

    @EventHandler
    public void on(PlayerFormRespondedEvent event) {
        Player player = event.getPlayer();
        if(event.getFormID() == "syoheads.categories".hashCode()) {
            if(event.getWindow() instanceof SimpleForm fw) {
                if(fw.response() != null) {
                    openCategory(player, CATEGORIES[fw.response().buttonId()].toLowerCase());
                }
            }
        } else if(event.getFormID() == "syoheads.category".hashCode()) {
            if(event.getWindow() instanceof SimpleForm fw) {
                if(fw.response() != null) {
                    String category = fw.content();
                    if(!CACHE.containsKey(category)) {
                        try {
                            CACHE.put(category, JsonParser.parseString(new Scanner(new URL(API_URL + category).openStream(), "UTF-8").useDelimiter("\\A").next()).getAsJsonObject());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    JsonObject categoryData = CACHE.get(category);
                    String key = (String) categoryData.keySet().toArray()[fw.response().buttonId()];
                    String url = categoryData.get(key).getAsJsonObject()
                            .get("valueDecoded").getAsJsonObject()
                            .get("textures").getAsJsonObject()
                            .get("SKIN").getAsJsonObject()
                            .get("url").getAsString();
                     try {
                        Item item = ItemUtils.createSkullFromUrl(fw.response().button().text(), new URL(url));
                        player.getInventory().addItem(item);
                        player.sendMessage("§aHead created successfully.");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
