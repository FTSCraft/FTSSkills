package de.afgmedia.ftsskills.listeners;

import de.afgmedia.ftsskills.data.Values;
import de.afgmedia.ftsskills.main.Skills;
import de.afgmedia.ftsskills.skillsystem.SkillManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Animals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;

public class BreedListener implements Listener {

    private Skills plugin;
    private SkillManager manager;

    public BreedListener(Skills plugin) {
        this.plugin = plugin;
        this.manager = plugin.getManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBreed(EntityBreedEvent event) {

        Player p = (Player) event.getBreeder();

        EntityType type = event.getEntityType();

        boolean ableToBreed = manager.checkActivity(type, p, SkillManager.Activity.BREED);

        if(!ableToBreed) {
            //Make animals not breedable
            Animals a = (Animals) event.getMother();
            a.setBreed(false);
            Animals b = (Animals) event.getFather();
            b.setBreed(false);

            //After 3 mins make them breedable again
            Bukkit.getScheduler().runTaskLater(plugin, () -> {

                a.setBreed(true);
                b.setBreed(true);

            }, 20 * 60 * 3);

            p.sendMessage(Values.MESSAGE_NEED_TO_SKILL_BREED);
            event.setCancelled(true);
        } else {

            plugin.getManager().addExperience(p, event.getExperience());

        }

    }

}
