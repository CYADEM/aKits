package dev.tinchx.kits.profile;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProfileListener implements Listener {

    @EventHandler
    final void onPlayerJoin(PlayerJoinEvent event) {
        new Profile(event.getPlayer().getUniqueId());
    }

    @EventHandler
    final void onPlayerQuit(PlayerQuitEvent event) {
        Profile profile = Profile.getProfile(event.getPlayer());
        profile.save();
        profile.remove();
    }
}
