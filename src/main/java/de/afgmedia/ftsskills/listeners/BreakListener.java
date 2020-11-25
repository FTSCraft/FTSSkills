package de.afgmedia.ftsskills.listeners;

import de.afgmedia.ftsskills.data.Values;
import de.afgmedia.ftsskills.main.Skills;
import de.afgmedia.ftsskills.skillsystem.SkillManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BreakListener implements Listener {

    private Skills plugin;
    private SkillManager manager;

    public BreakListener(Skills plugin) {
        this.plugin = plugin;
        this.manager = plugin.getManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {

        Player p = event.getPlayer();


        Material mat = event.getBlock().getType();

        boolean ableToGet = manager.checkActivity(mat, p, SkillManager.Activity.BLOCK_LOOT);

        if (!ableToGet) {
            event.setDropItems(false);
            p.sendMessage(Values.MESSAGE_NEED_TO_SKILL_BLOCK_LOOT);
        }

        if(!event.isCancelled()) {

            plugin.getManager().addExperience(event.getPlayer(), event.getExpToDrop());

        }


    }

}
