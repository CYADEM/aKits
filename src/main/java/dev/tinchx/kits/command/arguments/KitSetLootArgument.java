package dev.tinchx.kits.command.arguments;

import dev.tinchx.kits.kit.Kit;
import dev.tinchx.kits.utility.ItemSerializer;
import dev.tinchx.root.utilities.RootUtils;
import dev.tinchx.root.utilities.chat.ColorText;
import dev.tinchx.root.utilities.command.RootArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class KitSetLootArgument extends RootArgument {

    public KitSetLootArgument() {
        super("setloot", "Set kits loot");
        setPermission(RootUtils.PERMISSION + "kit." + getName());
        setOnlyPlayers(true);
    }

    @Override
    public String getUsage(String s) {
        return '/' + s + ' ' + getName() + " <kitName>";
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;

        if (args.length < 2) {
            sender.sendMessage(ColorText.translate("&cUsage: " + getUsage(label)));
        } else {
            Kit kit = Kit.getByName(args[1]);
            if (kit == null) {
                sender.sendMessage(ColorText.translate("&cA kit named '" + args[1] + "&c' was not found."));
            } else {
                kit.setStacks(Arrays.asList(player.getInventory().getContents()));
                kit.setItems(ItemSerializer.itemsToString(Arrays.asList(player.getInventory().getContents())));
                kit.save();

                player.sendMessage(ColorText.translate("&aKit loot successfully updated."));
            }
        }
    }
}
