package de.afgmedia.ftsskills.listeners;

import de.afgmedia.ftsskills.main.Skills;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceExtractEvent;

public class FurnaceExtractListener implements Listener {

    private Skills plugin;

    public FurnaceExtractListener(Skills plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onFurnaceExtract(FurnaceExtractEvent event) {

        plugin.getManager().addExperience(event.getPlayer(), event.getExpToDrop());

    }

}
