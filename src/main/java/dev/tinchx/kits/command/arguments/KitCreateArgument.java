package dev.tinchx.kits.command.arguments;

import dev.tinchx.kits.kit.Kit;
import dev.tinchx.root.utilities.RootUtils;
import dev.tinchx.root.utilities.chat.ColorText;
import dev.tinchx.root.utilities.command.RootArgument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCreateArgument extends RootArgument {

    public KitCreateArgument() {
        super("create", "Create a kit");
        setPermission(RootUtils.PERMISSION + "kit." + getName());
    }

    @Override
    public String getUsage(String s) {
        return '/' + s + ' ' + getName() + " <kitName>";
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ColorText.translate("&cUsage: " + getUsage(label)));
        } else {
            Kit kit = Kit.getByName(args[1]);
            if (kit != null) {
                sender.sendMessage(ColorText.translate("&cA kit named '" + kit.getName() + "&c' already exists."));
            } else {
                kit = new Kit(args[1]);
                kit.save();

                sender.sendMessage(ColorText.translate("&aKit successfully created."));
                if (sender instanceof Player) {
                    sender.sendMessage(ColorText.translate("&c&lREMEMBER: &7Use '/kit setloot " + kit.getName() + "'."));
                }
            }
        }
    }
}
