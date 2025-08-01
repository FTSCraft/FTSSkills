package de.afgmedia.ftsskills.listeners;

import de.afgmedia.ftsskills.skillsystem.SkillManager;
import de.afgmedia.ftsskills.data.Values;
import de.afgmedia.ftsskills.main.Skills;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.SmithItemEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

public class ForgeListener implements Listener {

    private Skills plugin;
    private SkillManager manager;

    public ForgeListener(Skills plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.manager = plugin.getManager();
    }

    // Use PrepareAnvilEvent instead of InventoryClickEvent
    @EventHandler
    public void onForge(PrepareAnvilEvent event) {

        // Get Inventory
        AnvilInventory anvilInv = event.getInventory();

        // Get the first item
        ItemStack firstItem = anvilInv.getItem(0);
        if (firstItem == null)
            return;

        if (firstItem.getType().isAir())
            return;

        // Get the result
        ItemStack result = event.getResult();
        if (result == null)
            return;

        if (result.getType().isAir())
            return;

        // Check if there's a player viewing
        if (event.getViewers().isEmpty())
            return;

        // Get the player
        Player p = (Player) event.getViewers().get(0);

        // Check the second item
        ItemStack secondItem = anvilInv.getItem(1);
        boolean isEnchantingBook = secondItem != null && secondItem.getType() == Material.ENCHANTED_BOOK;

        // Check if the player can perform the activity
        boolean ableToForge = isEnchantingBook
                ? manager.checkActivity(result.getType(), p, SkillManager.Activity.ENCHANTING)
                : manager.checkActivity(result.getType(), p, SkillManager.Activity.FORGING);

        // If player can't forge, remove the result and send message
        if (!ableToForge) {
            event.setResult(null);
            p.sendMessage(Values.MESSAGE_NEED_TO_SKILL);
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
