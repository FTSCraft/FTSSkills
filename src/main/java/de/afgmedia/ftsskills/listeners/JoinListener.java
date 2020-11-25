package de.afgmedia.ftsskills.listeners;

import de.afgmedia.ftsskills.main.Skills;
import de.afgmedia.ftsskills.skillsystem.Skill;
import de.afgmedia.ftsskills.skillsystem.SkillUser;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private Skills plugin;

    public JoinListener(Skills plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        Player p = event.getPlayer();

        //When user joins, import him into the system
        plugin.getManager().getDataManager().loadPlayerData(p);

    }

}
