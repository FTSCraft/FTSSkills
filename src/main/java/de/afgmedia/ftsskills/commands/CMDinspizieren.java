package de.afgmedia.ftsskills.commands;

import de.afgmedia.ftsskills.data.Values;
import de.afgmedia.ftsskills.main.Skills;
import de.afgmedia.ftsskills.skillsystem.Skill;
import de.afgmedia.ftsskills.skillsystem.SkillUser;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CMDinspizieren implements CommandExecutor {

    private Skills plugin;

    public CMDinspizieren(Skills plugin) {
        this.plugin = plugin;
        plugin.getCommand("inspizieren").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {

        if(!(cs instanceof Player)) {
            return true;
        }

        if(args.length == 1) {

            String tName = args[0];
            Player t = Bukkit.getPlayer(tName);

            if(t != null) {

                Player p = (Player) cs;

                if (p.getLocation().distance(t.getLocation()) < 10) {

                    SkillUser tUser = plugin.getManager().getUser(t);

                    cs.sendMessage(Values.PREFIX + "Infos über: §e" + t.getName());
                    cs.sendMessage(Values.PREFIX + "Level: §e" + tUser.getLevel());
                    if (tUser.getHighestLevel() != tUser.getLevel()) {
                        cs.sendMessage(Values.PREFIX + "Höchstes Level: §e" + tUser.getHighestLevel());
                        cs.sendMessage(Values.PREFIX + "Wenn dein Level nicht so hoch ist wie dein höchstes, kannst du, bis du es wieder erriecht hast, keine Skillpunkte bekommen!");
                    }
                    cs.sendMessage(Values.PREFIX + "XP bis zum nächsten LevelUp: §e" + (plugin.getManager().getLevelManager().getNeededXPForLevel(tUser.getLevel()) - tUser.getExperience()));
                    cs.sendMessage(Values.PREFIX + "Skillpoints: §e" + tUser.getSkillPoints());
                    cs.sendMessage(Values.PREFIX + "Skills: ");
                    for (Skill skill : tUser.getSkills()) {
                        cs.sendMessage("§e- " + skill.getName());
                    }

                }
                else p.sendMessage(Values.PREFIX +"Du musst in der Nähe des Spielers sein um ihn zu untersuchen! (10 Blöcke)");

            } else cs.sendMessage(Values.PREFIX +"Dieser Spieler ist nicht online!");

        } else cs.sendMessage(Values.PREFIX + "Du musst den Command so benutzen: §e/inspizieren <Spieler>");

        return false;
    }
}
