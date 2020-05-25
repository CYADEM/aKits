package dev.tinchx.kits.command;

import dev.tinchx.kits.command.arguments.*;
import dev.tinchx.kits.kit.Kit;
import dev.tinchx.kits.profile.Profile;
import dev.tinchx.root.utilities.RootUtils;
import dev.tinchx.root.utilities.chat.ColorText;
import dev.tinchx.root.utilities.command.RootArgument;
import dev.tinchx.root.utilities.command.RootCommand;
import dev.tinchx.root.utilities.task.TaskUtil;
import dev.tinchx.root.utilities.time.TimeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class KitCommand extends RootCommand {

    public KitCommand() {
        super("kit", null, "akit");

        register(new KitCreateArgument());
        register(new KitDeleteArgument());
        register(new KitDelayArgument());
        register(new KitSetLootArgument());
        register(new KitPermissionArgument());
        register(new KitDelayArgument());
        register(new KitInfoArgument());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            RootArgument argument = getArgument(args[0]);

            if (argument == null || (argument.getPermission() != null && !sender.hasPermission(argument.getPermission()))) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(RootUtils.ONLY_PLAYERS);
                    return false;
                }
                Player player = ((Player) sender);

                Kit kit = Kit.getByName(args[0]);
                Profile profile = Profile.getProfile(player);

                if (kit == null) {
                    sender.sendMessage(ColorText.translate("&cA kit with that name was not found."));
                } else if (kit.getPermission() != null && !player.hasPermission(kit.getPermission())) {
                    sender.sendMessage(ColorText.translate("&cYou can't use this kit."));
                } else if (profile.getRemaining(kit) > 0L) {
                    sender.sendMessage(ColorText.translate("&cYou're on cooldown for another " + DurationFormatUtils.formatDurationWords(profile.getRemaining(kit), true, true) + '.'));
                } else {
                    kit.equip(player);

                    player.sendMessage(ColorText.translate("&aKit '" + kit.getName() + "' loaded."));
                    if (kit.getDelay() != null) {
                        profile.setCooldown(kit, TimeUtils.parse(kit.getDelay()));
                    }
                }
            } else {
                if ((argument.isOnlyPlayers() || isOnlyPlayers()) && sender instanceof ConsoleCommandSender) {
                    sender.sendMessage(RootUtils.ONLY_PLAYERS);
                    return false;
                }
                if (argument.isAsync()) {
                    TaskUtil.runTaskAsync(() -> argument.execute(sender, label, args));
                } else {
                    argument.execute(sender, label, args);
                }
            }
        } else {
            sender.sendMessage(ColorText.translate("&m" + StringUtils.repeat("-", 16) + "&5&m" + StringUtils.repeat("-", 16) + "&r&m" + StringUtils.repeat("-", 16)));
            sender.sendMessage(ColorText.translate("&5Available sub-command(s) for '&f" + command.getName() + "&5'."));
            sender.sendMessage("");
            sender.sendMessage(ColorText.translate(" &f/"+label + " <kitName> &7- &dEquip a kit"));
            sender.sendMessage("");

            getArguments().stream().filter(argument -> argument.getPermission() == null || sender.hasPermission(argument.getPermission())).forEach(argument -> {
                sender.sendMessage(ColorText.translate(' ' + argument.getUsage(label) + (argument.getDescription() == null ? "" : " &7- &d" + argument.getDescription())));
            });

            sender.sendMessage(ColorText.translate("&m" + StringUtils.repeat("-", 16) + "&5&m" + StringUtils.repeat("-", 16) + "&r&m" + StringUtils.repeat("-", 16)));
        }

        return true;
    }
}
