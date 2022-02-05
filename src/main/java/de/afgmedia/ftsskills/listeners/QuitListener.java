package de.afgmedia.ftsskills.listeners;

import de.afgmedia.ftsskills.main.Skills;
import de.afgmedia.ftsskills.skillsystem.SkillUser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {

    private Skills plugin;

    public QuitListener(Skills plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

        SkillUser user = plugin.getManager().getUser(event.getPlayer());

        plugin.getManager().getDataManager().savePlayerData(user);

        plugin.getManager().removePlayer(event.getPlayer());

    }

}
