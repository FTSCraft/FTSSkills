package de.afgmedia.ftsskills.listeners;

import de.afgmedia.ftsskills.skillsystem.SkillManager;
import de.afgmedia.ftsskills.data.Values;
import de.afgmedia.ftsskills.main.Skills;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

public class EnchantListener implements Listener {

    private Skills plugin;
    private SkillManager manager;

    public EnchantListener(Skills plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.manager = plugin.getManager();
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {

        //Get player
        Player p = event.getEnchanter();

        //Which material is used
        Material mat = event.getItem().getType();

        //Checks if player is able to enchant
        boolean ableToEnchant = manager.checkActivity(mat, p, SkillManager.Activity.ENCHANTING);

        //Cancel the event if player is not able to enchant (invert because if he is able to, it will return true)
        event.setCancelled(!ableToEnchant);

        //If player isnt able to do it, send player a message
        if (!ableToEnchant) {
            p.sendMessage(Values.MESSAGE_NEED_TO_SKILL);
        }

    }

}
