package de.afgmedia.ftsskills.skillsystem;

import de.afgmedia.ftsskills.data.Values;
import de.afgmedia.ftsskills.main.Skills;
import de.afgmedia.ftsskills.skillsystem.gui.GuiType;
import net.milkbowl.vault.Vault;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.ArrayList;

public class SkillUser {

    private ArrayList<Skill> skills = new ArrayList<>();
    private final Player player;
    private final Skills plugin;

    private boolean recievedFree;

    private double experience = 0;
    private int level = 0;
    private int skillPoints = 0;
    private int highestLevel = 0;

    private GuiType inventory;

    private int taskId = -1;

    BossBar bossBar;

    public SkillUser(Player player, Skills plugin) {
        this.player = player;
        this.plugin = plugin;

        bossBar = Bukkit.createBossBar(new NamespacedKey(plugin, player.getName().toUpperCase() + "-BOSSBAR_SKILL"), "", BarColor.PINK, BarStyle.SOLID);

        bossBar.addPlayer(player);

        bossBar.setVisible(false);

        plugin.getManager().addUser(this);
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public Player getPlayer() {
        return player;
    }

    public void addExperience(int amount) {

        if (level >= Values.MAX_LEVEL) {
            experience = 0;
            return;
        }

        //When player gains experience, add it up
        experience = experience + amount;

        //Checks if player got a level up
        checkLevelUp();
    }

    public void addSkill(Skill skill) {

        skillPoints = skillPoints - 1;

        skills.add(skill);

        for (String permission : skill.getPermissions()) {
            plugin.getPermission().playerAdd(player, permission);
        }

    }

    //Checks if he got a level up
    public void checkLevelUp(boolean cheat) {

        if (level >= Values.MAX_LEVEL) {
            return;
        }

        double neededXP = plugin.getManager().getLevelManager().getNeededXPForLevel(level);

        boolean lvlUp = false;

        if (neededXP <= experience || cheat) {
            if (!cheat) {
                level++;
                experience = experience - neededXP;
            } else {
                experience = 0;
            }
            //If the recent level is higher than his highest, update it

            if (isLevelUp()) {

                lvlUp = true;

                player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 100, 100);
                player.sendMessage(Values.PREFIX + "Du hast deine Fähigkeiten verbessert! Du kannst nun einen Skillpunkt investieren mit dem Befehl: " + Values.COMMAND);

                skillPoints++;

                bossBar.setTitle("§cLEVEL-UP");

                bossBar.setProgress(1.0d);

            }

        }

        if (level > highestLevel)
            highestLevel = level;

        bossBar.setVisible(true);

        double procents = experience / neededXP;

        if (cheat)
            bossBar.setProgress(0);
        else {
            try {
                bossBar.setProgress(procents);
            } catch (java.lang.IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        if (!lvlUp)

            bossBar.setTitle("Level: " + level);

        if (taskId == -1) {

            taskId = Bukkit.getScheduler().runTaskLater(plugin, () -> {
                bossBar.setVisible(false);
                taskId = -1;
            }, 20 * 6).getTaskId();

        } else {

            Bukkit.getScheduler().cancelTask(taskId);

            taskId = Bukkit.getScheduler().runTaskLater(plugin, () -> {
                bossBar.setVisible(false);
                taskId = -1;
            }, 20 * 6).getTaskId();

        }
    }

    public void checkLevelUp() {
        checkLevelUp(false);
    }

    private boolean isLevelUp() {
        //If the level isn't equal the highest reached, dont give him a levelup
        if (level > highestLevel)

            return level % Values.LEVELS_TO_SKILLPOINT == 0;

        return false;
    }

    public GuiType getInventoryType() {
        return inventory;
    }

    public int getSkillPoints() {
        return skillPoints;
    }

    public void setInventoryType(GuiType type) {

        this.inventory = type;
    }

    public double getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }

    public void setSkills(ArrayList<Skill> skills) {
        this.skills = skills;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }

    public void setLevel(int level, boolean startup) {
        this.level = level;
        if (!startup) {
            if (level > highestLevel)
                setHighestLevel(level);
            checkLevelUp(true);
        }
    }

    public void setLevel(int level) {
        setLevel(level, true);
    }

    public void setSkillPoints(int skillPoints) {
        this.skillPoints = skillPoints;
    }

    private int getNextLevelForLevelUp() {

        int a = level;

        for (int i = a; i < level + Values.LEVELS_TO_SKILLPOINT; i++) {

            if (i % Values.LEVELS_TO_SKILLPOINT == 0) {

                if (level % Values.LEVELS_TO_SKILLPOINT == 0)

                    return i + Values.LEVELS_TO_SKILLPOINT;

                return i;
            }


        }

        return -1;

    }

    public boolean unlearnSkill(Skill skill) {

        if (!skills.contains(skill)) {
            return false;
        }

        if(player.hasPermission("afglock.reisender")) {
            setSkillPoints(getSkillPoints() + 1);

            removeSkill(skill);
            return true;
        }

        if(plugin.getEconomy().getBalance(player) < 500) {
            return false;
        }

        setSkillPoints(getSkillPoints() + 1);
        removeSkill(skill);
        plugin.getEconomy().withdrawPlayer(player, 500);

        return true;
    }

    private void removeSkill(Skill skill) {
        skills.remove(skill);

        addExperience(0);

        for (String permission : skill.getPermissions()) {
            plugin.getPermission().playerRemove(player, permission);
        }
    }

    public int getHighestLevel() {
        return highestLevel;
    }

    public void setHighestLevel(int highestLevel) {
        this.highestLevel = highestLevel;
    }

    public void setRecievedFree(boolean recievedFree) {
        this.recievedFree = recievedFree;
    }

    public boolean hasRecievedFree() {
        return recievedFree;
    }
}
