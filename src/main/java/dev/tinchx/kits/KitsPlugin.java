package dev.tinchx.kits;

import dev.tinchx.kits.command.GiveKitCommand;
import dev.tinchx.kits.command.KitCommand;
import dev.tinchx.kits.kit.Kit;
import dev.tinchx.kits.mongo.MongoManager;
import dev.tinchx.kits.profile.ProfileListener;
import dev.tinchx.root.utilities.command.CommandHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class KitsPlugin extends JavaPlugin {

    @Getter
    private static KitsPlugin instance;

    @Getter
    private MongoManager mongoManager;

    @Override
    public void onEnable() {
        load();
    }

    @Override
    public void onDisable() {
        mongoManager.close();
    }

    private void load() {
        instance = this;
        (mongoManager = new MongoManager()).load();

        Kit.loadKits();

        CommandHandler handler = new CommandHandler(this);
        handler.register(new KitCommand());
        handler.register(new GiveKitCommand());

        Bukkit.getPluginManager().registerEvents(new ProfileListener(), this);
    }
}
