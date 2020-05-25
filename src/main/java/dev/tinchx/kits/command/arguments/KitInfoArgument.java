package dev.tinchx.kits.command.arguments;

import dev.tinchx.kits.kit.Kit;
import dev.tinchx.root.utilities.RootUtils;
import dev.tinchx.root.utilities.chat.ColorText;
import dev.tinchx.root.utilities.command.RootArgument;
import dev.tinchx.root.utilities.time.TimeUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.command.CommandSender;

public class KitInfoArgument extends RootArgument {

    public KitInfoArgument() {
        super("info");
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
                sender.sendMessage(" ");
                sender.sendMessage(ColorText.translate("&a&l" + kit.getName() + "&7:"));
                sender.sendMessage(ColorText.translate(" &6> &fPermission: " + (kit.getPermission() == null ? "&cNone" : "&a" + kit.getPermission())));
                sender.sendMessage(ColorText.translate(" &6> &fDelay: " + (kit.getDelay() == null || kit.getDelay().equalsIgnoreCase("0L") ? "&cNone" : "&a" + DurationFormatUtils.formatDurationWords(TimeUtils.parse(kit.getDelay()), true, true))));
                sender.sendMessage(" ");
            }
        }
    }
}
