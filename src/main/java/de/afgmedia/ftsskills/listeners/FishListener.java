package de.afgmedia.ftsskills.listeners;

import de.afgmedia.ftsskills.data.Values;
import de.afgmedia.ftsskills.main.Skills;
import de.afgmedia.ftsskills.skillsystem.SkillManager;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

public class FishListener implements Listener {

    private Skills plugin;
    private SkillManager manager;

    public FishListener(Skills plugin) {
        this.plugin = plugin;
        this.manager = plugin.getManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {

        Player p = event.getPlayer();

        if(event.getCaught() == null && !(event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY)) {
            return;
        }

        //The Entity will be an item
        if(!(event.getCaught() instanceof Item)) {
            return;
        }
        Item item = (Item) event.getCaught();

        //Get the material of the item
        Material mat = item.getItemStack().getType();

        boolean ableToFish = manager.checkActivity(mat, p, SkillManager.Activity.FISH);

        //event.setCancelled(!ableToFish);

        //If player isnt able to catch fish, replace it with kelp and give him no xp
        if(!ableToFish) {

            event.setExpToDrop(0);
            item.setItemStack(new ItemStack(Material.KELP));
            p.sendMessage(Values.MESSAGE_NEED_TO_SKILL_FISH);

        } else {

            manager.addExperience(p, event.getExpToDrop());

        }


    }

}
