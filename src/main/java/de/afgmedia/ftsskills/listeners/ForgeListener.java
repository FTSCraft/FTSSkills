package de.afgmedia.ftsskills.listeners;

import de.afgmedia.ftsskills.skillsystem.SkillManager;
import de.afgmedia.ftsskills.data.Values;
import de.afgmedia.ftsskills.main.Skills;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;

public class ForgeListener implements Listener {

    private Skills plugin;
    private SkillManager manager;

    public ForgeListener(Skills plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.manager = plugin.getManager();
    }


    //Because there is no anvil event which we can use, we will use the click event and check if its a anvilinventory
    @EventHandler
    public void onForge(InventoryClickEvent event) {

        //Get Inventory
        Inventory inventory = event.getInventory();

        //Check if Inventory is an instance of an AnvilInventory
        if (inventory instanceof AnvilInventory) {

            //Get the Material
            if (event.getCurrentItem() == null)
                return;
            Material mat = event.getCurrentItem().getType();
            //Get the player
            Player p = (Player) event.getWhoClicked();

            boolean ableToForge = manager.checkActivity(mat, p, SkillManager.Activity.FORGING);

            event.setCancelled(!ableToForge);

            //If player isnt able to do it, send player a message
            if (!ableToForge) {

                p.sendMessage(Values.MESSAGE_NEED_TO_SKILL);

            }

        }

    }

    @EventHandler
    public void onSmithing(SmithItemEvent event) {

        Player p = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null)
            return;

        boolean ableToSmith = manager.checkActivity(event.getCurrentItem().getType(), p, SkillManager.Activity.CRAFTING);

        if (!ableToSmith) {
            p.sendMessage(Values.MESSAGE_NEED_TO_SKILL);
            event.setCancelled(true);
        }

    }

}
