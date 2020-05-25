package dev.tinchx.kits.command.arguments;

import dev.tinchx.kits.kit.Kit;
import dev.tinchx.root.utilities.RootUtils;
import dev.tinchx.root.utilities.chat.ColorText;
import dev.tinchx.root.utilities.command.RootArgument;
import org.bukkit.command.CommandSender;

public class KitDelayArgument extends RootArgument {

    public KitDelayArgument() {
        super("delay", "Set delay");

        setPermission(RootUtils.PERMISSION + "kit." + getName());
    }

    @Override
    public String getUsage(String s) {
        return '/' + s + ' ' + getName() + " <kitName> <delay/remove>";
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
                if (args[2].equalsIgnoreCase("remove")) {
                    kit.setDelay(null);

                    sender.sendMessage(ColorText.translate("&cDelay removed from " + kit.getName() + " Kit."));
                } else {
                    kit.setDelay(args[2]);

                    sender.sendMessage(ColorText.translate("&aDelay set to " + kit.getName() + " Kit."));
                }

                kit.save();
            }
        }
    }
}
