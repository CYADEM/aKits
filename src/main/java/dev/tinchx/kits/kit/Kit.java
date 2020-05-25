package dev.tinchx.kits.kit;

import com.google.common.collect.Lists;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import dev.tinchx.kits.KitsPlugin;
import dev.tinchx.kits.utility.ItemSerializer;
import dev.tinchx.root.utilities.document.DocumentSerializer;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Setter
public class Kit implements DocumentSerializer {

    private static List<Kit> kits = Lists.newArrayList();

    @Getter
    private final String name;
    private String items;
    @Getter
    private String permission, delay;
    private List<ItemStack> stacks;

    public Kit(String name) {
        this.name = name;
        this.stacks = Lists.newArrayList();

        kits.add(this);
    }

    public void equip(Player player) {
        List<ItemStack> items = ItemSerializer.stringToItems(this.items);

        if (!items.isEmpty()) {
            for (ItemStack stack : items) {
                for (ItemStack itemStack : player.getInventory().addItem(stack).values()) {
                    player.getWorld().dropItem(player.getLocation(), itemStack);
                }
            }
        }

        player.updateInventory();
    }

    public void save() {
        KitsPlugin.getInstance().getMongoManager().getKits().replaceOne(Filters.eq("name", name), serialize(), new UpdateOptions().upsert(true));
    }

    public void delete() {
        KitsPlugin.getInstance().getMongoManager().getKits().deleteOne(Filters.eq("name", name));
        kits.remove(this);
    }

    @Override
    public Document serialize() {
        Document document = new Document();
        document.put("name", name);
        if (stacks != null) {
            document.put("items", ItemSerializer.itemsToString(stacks));
        }
        if (permission != null) {
            document.put("permission", permission);
        }
        if (delay != null) {
            document.put("delay", delay);
        }
        return document;
    }

    public static Kit getByName(String name) {
        return kits.stream().filter(kit -> kit.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static void loadKits() {
        try (MongoCursor cursor = KitsPlugin.getInstance().getMongoManager().getKits().find().iterator()) {
            while (cursor.hasNext()) {
                Document document = (Document) cursor.next();

                Kit kit = new Kit(document.getString("name"));
                if (document.containsKey("items")) {
                    kit.setItems(document.getString("items"));
                }
                if (document.containsKey("permission")) {
                    kit.setPermission(document.getString("permission"));
                }
                if (document.containsKey("delay")) {
                    kit.setDelay(document.getString("delay"));
                }
            }
        }
    }
}
