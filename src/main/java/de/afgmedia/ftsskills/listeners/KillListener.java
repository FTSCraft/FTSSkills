package de.afgmedia.ftsskills.listeners;

import de.afgmedia.ftsskills.data.Values;
import de.afgmedia.ftsskills.main.Skills;
import de.afgmedia.ftsskills.skillsystem.SkillManager;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class KillListener implements Listener {

    private Skills plugin;
    private SkillManager manager;

    public KillListener(Skills plugin) {
        this.plugin = plugin;
        this.manager = plugin.getManager();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private ArrayList<Material> meats = new ArrayList<>(Arrays.asList(
            Material.BEEF,
            Material.MUTTON,
            Material.RABBIT,
            Material.CHICKEN,
            Material.PORKCHOP,
            Material.COOKED_BEEF,
            Material.COOKED_MUTTON,
            Material.COOKED_RABBIT,
            Material.COOKED_CHICKEN,
            Material.COOKED_PORKCHOP));

    private ArrayList<EntityType> entityTypesWhichStillDrop = new ArrayList<>(Arrays.asList(
            EntityType.ARMOR_STAND,
            EntityType.CREEPER,
            EntityType.PLAYER,
            EntityType.HORSE,
            EntityType.ZOMBIE_HORSE,
            EntityType.SKELETON_HORSE,
            EntityType.DONKEY
    ));

    @EventHandler
    public void onDeath(EntityDeathEvent event) {


        Player p = event.getEntity().getKiller();

        if (p != null) {

            plugin.getManager().addExperience(p, event.getDroppedExp());

            if (Values.DEBUG)
                System.out.println(event.getEntity().getType());

            boolean ableToLoot = manager.checkActivity(event.getEntity().getType(), p, SkillManager.Activity.MOB_LOOT);
            if (!ableToLoot) {
                ArrayList<ItemStack> remove = new ArrayList<>();
                for (ItemStack drop : event.getDrops()) {
                    if (!meats.contains(drop.getType())) {
                        remove.add(drop);
                    }
                }
                event.getDrops().removeAll(remove);
                p.sendMessage(Values.MESSAGE_NEED_TO_SKILL_MOB_LOOT);

            }


        } else {

            if (!entityTypesWhichStillDrop.contains(event.getEntityType())) {
                ArrayList<ItemStack> remove = new ArrayList<>();
                for (ItemStack drop : event.getDrops()) {
                    if (!meats.contains(drop.getType())) {
                        remove.add(drop);
                    }
                }
                event.getDrops().removeAll(remove);
            }

        }

    }


}
