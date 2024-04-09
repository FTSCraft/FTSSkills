package de.afgmedia.ftsskills.listeners;

import de.afgmedia.ftsskills.skillsystem.SkillManager;
import de.afgmedia.ftsskills.data.Values;
import de.afgmedia.ftsskills.main.Skills;
import de.ftscraft.ftsutils.items.ItemReader;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CraftListener implements Listener {

    private Skills plugin;
    private SkillManager manager;

    public CraftListener(Skills plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.manager = plugin.getManager();
    }


    @EventHandler
    public void onCraft(CraftItemEvent event) {

        //Get Player
        Player p = (Player) event.getWhoClicked();

        //Which item is getting crafted
        ItemStack is = event.getRecipe().getResult();
        //get material from item
        Material mat = is.getType();

        //Checks if player is able to craft
        boolean ableToCraft = manager.checkActivity(mat, p, SkillManager.Activity.CRAFTING);

        //check if it got a custom name

        String sign = ItemReader.getSign(is);
        if (sign != null && (sign.endsWith("_BACKPACK") || sign.equals("EMPTY_SADDLE"))) {
            ableToCraft = manager.checkIfAbleToCraftBackpack(p);
        } else if(sign != null && sign.equals("TP_SCROLL")) {
            ableToCraft = manager.checkIfAbleToCraftScrolls(p);
        }

        //Cancel the event if player is not able to craft (invert because if he is able to, it will return true)
        event.setCancelled(!ableToCraft);

        //If player isnt able to do it, send player a message
        if(!ableToCraft) {
            p.sendMessage(Values.MESSAGE_NEED_TO_SKILL);
        }

    }



}
