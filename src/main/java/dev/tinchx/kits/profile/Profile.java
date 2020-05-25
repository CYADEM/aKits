package dev.tinchx.kits.profile;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import dev.tinchx.kits.KitsPlugin;
import dev.tinchx.kits.kit.Kit;
import dev.tinchx.root.utilities.document.DocumentSerializer;
import org.bson.Document;
import org.bukkit.OfflinePlayer;

import java.util.Map;
import java.util.UUID;

public class Profile implements DocumentSerializer {

    private static Map<UUID, Profile> profileMap = Maps.newHashMap();

    private UUID uuid;
    private Map<String, Long> cooldownMap;

    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.cooldownMap = Maps.newHashMap();

        load();
        profileMap.put(uuid, this);
    }

    public void setCooldown(Kit kit, long duration) {
        cooldownMap.remove(kit.getName());
        cooldownMap.put(kit.getName(), duration + System.currentTimeMillis());
    }

    public long getRemaining(Kit kit) {
        return cooldownMap.getOrDefault(kit.getName(), 0L) - System.currentTimeMillis();
    }

    public void save() {
        KitsPlugin.getInstance().getMongoManager().getProfiles().replaceOne(Filters.eq("uuid", uuid.toString()), serialize(), new UpdateOptions().upsert(true));
    }

    private void load() {
        Document document = (Document) KitsPlugin.getInstance().getMongoManager().getProfiles().find(Filters.eq("uuid", uuid.toString())).first();

        if (document != null) {
            if (document.containsKey("cooldowns")) {
                if (document.get("cooldowns") instanceof String) {
                    JsonArray array = new JsonParser().parse(document.getString("cooldowns")).getAsJsonArray();

                    for (JsonElement jsonElement : array) {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();

                        cooldownMap.put(jsonObject.get("name").getAsString(), jsonObject.get("value").getAsLong());
                    }
                }
            }
        }
    }

    public void remove() {
        profileMap.remove(uuid);
    }

    @Override
    public Document serialize() {
        JsonArray elements = new JsonArray();

        cooldownMap.forEach((s, aLong) -> {
            JsonObject object = new JsonObject();

            if (aLong > 0L) {
                object.addProperty("name", s);
                object.addProperty("value", aLong);

                elements.add(object);
            }
        });

        Document document = new Document();
        document.put("uuid", uuid.toString());

        if (elements.size() > 0) {
            document.put("cooldowns", elements.toString());
        }
        return document;
    }

    public static Profile getProfile(OfflinePlayer player) {
        Profile profile = profileMap.get(player.getUniqueId());

        if (profile == null) profile = new Profile(player.getUniqueId());

        return profile;
    }
}
