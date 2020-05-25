package dev.tinchx.kits.command;

import dev.tinchx.kits.kit.Kit;
import dev.tinchx.root.utilities.RootUtils;
import dev.tinchx.root.utilities.chat.ColorText;
import dev.tinchx.root.utilities.command.RootCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveKitCommand extends RootCommand {

    public GiveKitCommand() {
        super("givekit");
        setPermission(RootUtils.PERMISSION + getName());
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ColorText.translate("&cUsage: /" + label + " <kitName> <playerName>"));
        } else {
            Kit kit = Kit.getByName(args[0]);
            if (kit == null) {
                sender.sendMessage(ColorText.translate("&cA kit named '" + args[0] + "&c' was not found."));
            } else {
                Player player = Bukkit.getPlayer(args[1]);
                if (!RootUtils.isOnline(player)) {
                    sender.sendMessage(RootUtils.getPlayerNotFoundMessage(args[1]));
                } else {
                    kit.equip(player);

                    sender.sendMessage(ColorText.translate("&aKit gave to " + player.getName() + '.'));
                    player.sendMessage(ColorText.translate("&a" + sender.getName() + " gave you " + kit.getName() + " Kit."));
                }
            }
        }
    }
}
