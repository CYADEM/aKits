package dev.tinchx.kits.command.arguments;

import dev.tinchx.kits.kit.Kit;
import dev.tinchx.root.utilities.RootUtils;
import dev.tinchx.root.utilities.chat.ColorText;
import dev.tinchx.root.utilities.command.RootArgument;
import org.bukkit.command.CommandSender;

public class KitDeleteArgument extends RootArgument {

    public KitDeleteArgument() {
        super("delete", "Delete an existent kit");
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
            if (kit == null) {
                sender.sendMessage(ColorText.translate("&cA kit named '" + args[1] + "&c' was not found."));
            } else {
                sender.sendMessage(ColorText.translate("&cKit '" + kit.getName() + "' has been successfully deleted."));
                kit.delete();
            }
        }
    }
}
