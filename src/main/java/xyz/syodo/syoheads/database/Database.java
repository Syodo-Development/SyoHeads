package xyz.syodo.syoheads.database;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.window.FormWindowSimple;
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
        FormWindowSimple formWindowSimple = new FormWindowSimple("§eheaddb.org", "");
        for(String category : CATEGORIES) {
            formWindowSimple.addButton(new ElementButton("§e" + category, new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_URL, "https://headdb.org/img/categories/" + category.toLowerCase() + ".png")));
        }
        player.showFormWindow(formWindowSimple, "syoheads.categories".hashCode());
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
        FormWindowSimple formWindowSimple = new FormWindowSimple("§eheaddb.org", category);
        for(String head : categoryData.keySet()) {
            formWindowSimple.addButton(new ElementButton("§e" + categoryData.get(head).getAsJsonObject().get("name").getAsString(), new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_URL, "https://headdb.org/img/renders/" + head + ".png")));
        }
        player.showFormWindow(formWindowSimple, "syoheads.category".hashCode());
    }

    @EventHandler
    public void on(PlayerFormRespondedEvent event) {
        Player player = event.getPlayer();
        if(event.getFormID() == "syoheads.categories".hashCode()) {
            if(event.getWindow() instanceof FormWindowSimple fw) {
                if(fw.getResponse() != null) {
                    openCategory(player, CATEGORIES[fw.getResponse().getClickedButtonId()].toLowerCase());
                }
            }
        } else if(event.getFormID() == "syoheads.category".hashCode()) {
            if(event.getWindow() instanceof FormWindowSimple fw) {
                if(fw.getResponse() != null) {
                    String category = fw.getContent();
                    if(!CACHE.containsKey(category)) {
                        try {
                            CACHE.put(category, JsonParser.parseString(new Scanner(new URL(API_URL + category).openStream(), "UTF-8").useDelimiter("\\A").next()).getAsJsonObject());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    JsonObject categoryData = CACHE.get(category);
                    String key = (String) categoryData.keySet().toArray()[fw.getResponse().getClickedButtonId()];
                    String url = categoryData.get(key).getAsJsonObject()
                            .get("valueDecoded").getAsJsonObject()
                            .get("textures").getAsJsonObject()
                            .get("SKIN").getAsJsonObject()
                            .get("url").getAsString();
                     try {
                        Item item = ItemUtils.createSkullFromUrl(fw.getResponse().getClickedButton().getText(), new URL(url));
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
