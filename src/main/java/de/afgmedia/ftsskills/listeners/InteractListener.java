package de.afgmedia.ftsskills.listeners;

import de.afgmedia.ftsskills.data.Values;
import de.afgmedia.ftsskills.main.Skills;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;

public class InteractListener implements Listener {

    Skills plugin;

    ArrayList<Player> cooldown = new ArrayList<>();

    public InteractListener(Skills plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

            boolean onCooldown = cooldown.contains(event.getPlayer());
            boolean cancelled = false;

            if (event.getClickedBlock().getType().equals(Material.COMPOSTER)) {

                cancelled = !plugin.getManager().checkIfAbleToUseComposter(event.getPlayer());

                event.setCancelled(cancelled);

                if (!onCooldown && cancelled) {
                    event.getPlayer().sendMessage(Values.PREFIX + "Du brauchst dafür den Skill §cKompostieren");
                    cooldown.add(event.getPlayer());
                }

                if(cancelled) {
                    Levelled levelled = (Levelled) event.getClickedBlock().getBlockData();
                    levelled.setLevel(0);
                }

            }

            if (event.getClickedBlock().getType().equals(Material.BEEHIVE)) {

                cancelled = !plugin.getManager().checkIfAbleToMakeHoney(event.getPlayer());

                event.setCancelled(cancelled);

                if (!onCooldown && cancelled) {
                    event.getPlayer().sendMessage(Values.PREFIX + "Du brauchst dafür den Skill §cImkerei");
                    cooldown.add(event.getPlayer());
                }
                
            }

            if(!onCooldown) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    cooldown.remove(event.getPlayer());
                }, 10);
            }

        }

    }

}
