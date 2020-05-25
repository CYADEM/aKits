package dev.tinchx.kits.utility;

import com.google.common.collect.Lists;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemSerializer {

    public static String itemsToString(List<ItemStack> items) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(serializeItemStack(items));
            oos.flush();
            return DatatypeConverter.printBase64Binary(bos.toByteArray());
        } catch (Exception ignored) {
        }
        return "";
    }

    public static List<ItemStack> stringToItems(String s) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(s));
            ObjectInputStream ois = new ObjectInputStream(bis);
            return deserializeItemStack((Map<String, Object>[]) ois.readObject());
        } catch (Exception ignored) {
        }
        return Lists.newArrayList();
    }

    private static Map<String, Object>[] serializeItemStack(List<ItemStack> items) {
        Map<String, Object>[] result = new Map[items.size()];

        for (int i = 0; i < items.size(); i++) {
            ItemStack is = items.get(i);
            if (is == null) {
                result[i] = new HashMap<>();
            } else {
                result[i] = is.serialize();
                if (is.hasItemMeta()) {
                    result[i].put("meta", is.getItemMeta().serialize());
                }
            }
        }

        return result;
    }

    private static List<ItemStack> deserializeItemStack(Map<String, Object>[] map) {
        List<ItemStack> stacks = Lists.newArrayList();

        for (Map<String, Object> s : map) {
            if (s.size() > 0) {
                try {
                    if (s.containsKey("meta")) {
                        Map<String, Object> im = new HashMap<>((Map<String, Object>) s.remove("meta"));
                        im.put("==", "ItemMeta");
                        ItemStack is = ItemStack.deserialize(s);
                        is.setItemMeta((ItemMeta) ConfigurationSerialization.deserializeObject(im));
                        stacks.add(is);
                    } else {
                        stacks.add(ItemStack.deserialize(s));
                    }
                } catch (Exception ignored) {
                }
            }

        }

        return stacks;
    }

}