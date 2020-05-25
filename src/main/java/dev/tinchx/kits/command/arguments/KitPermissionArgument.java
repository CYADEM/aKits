package dev.tinchx.kits.command.arguments;

import dev.tinchx.kits.kit.Kit;
import dev.tinchx.root.utilities.RootUtils;
import dev.tinchx.root.utilities.chat.ColorText;
import dev.tinchx.root.utilities.command.RootArgument;
import org.bukkit.command.CommandSender;

public class KitPermissionArgument extends RootArgument {

    public KitPermissionArgument() {
        super("permission", "Set permissions");

        setPermission(RootUtils.PERMISSION + "kit." + getName());
    }

    @Override
    public String getUsage(String s) {
        return '/' + s + ' ' + getName() + " <kitName> <permission/remove>";
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ColorText.translate("&cUsage: " + getUsage(label)));
        } else {
            Kit kit = Kit.getByName(args[1]);
            if (kit == null) {
                sender.sendMessage(ColorText.translate("&cA kit named '" + args[1] + "&c' was not found."));
            } else {
                if (args[1].equalsIgnoreCase("remove")) {
                    kit.setPermission(null);

                    sender.sendMessage(ColorText.translate("&cPermission removed from " + kit.getName() + " Kit."));
                } else {
                    kit.setPermission(args[1]);

                    sender.sendMessage(ColorText.translate("&aPermission set'" + args[1] + "' to " + kit.getName() + " Kit."));
                }

                kit.save();
            }
        }
    }
}
