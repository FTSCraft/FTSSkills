package de.afgmedia.ftsskills.listeners;

import de.afgmedia.ftsskills.main.Skills;
import de.afgmedia.ftsskills.skillsystem.Skill;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class ExperienceListener implements Listener {


    private Skills plugin;

    public ExperienceListener(Skills plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onExperienceGain(PlayerExpChangeEvent event) {

        //Check if player gets xp from grindstone by looking if player is in grindstone inventory


    }

    @EventHandler
    public void onLevelUp(PlayerLevelChangeEvent event) {



    }

}
