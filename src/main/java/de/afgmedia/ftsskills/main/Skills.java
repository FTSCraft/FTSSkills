package de.afgmedia.ftsskills.main;

import de.afgmedia.ftsskills.commands.CMDftsskills;
import de.afgmedia.ftsskills.commands.CMDftsskillsadmin;
import de.afgmedia.ftsskills.commands.CMDinspizieren;
import de.afgmedia.ftsskills.listeners.*;
import de.afgmedia.ftsskills.skillsystem.Skill;
import de.afgmedia.ftsskills.skillsystem.SkillManager;
import de.afgmedia.ftsskills.skillsystem.gui.MainMenuGUI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Skills extends JavaPlugin {

    private SkillManager manager;

    //Sound.UI_TOAST_CHALLENGE_COMPLETE - LevelUp

    private MainMenuGUI mainMenuGUI;

    Economy economy;
    LuckPerms luckPerms;

    @Override
    public void onEnable() {
        init();
    }

    @Override
    public void onDisable() {

    }

    private void init() {
        this.manager = new SkillManager(this);
        manager.getDataManager().loadSkills();
        manager.getDataManager().loadCategories();

        manager.getDataManager().loadCategoryGuis();
        mainMenuGUI = new MainMenuGUI(this, new File(getDataFolder() + "//gui//main.yml"));

        new CraftListener(this);
        new EnchantListener(this);
        new ForgeListener(this);
        new BreedListener(this);
        new FishListener(this);
        new BreakListener(this);
        new KillListener(this);
        new JoinListener(this);
        new GuiListener(this);
        new ExperienceListener(this);
        new FurnaceExtractListener(this);
        new QuitListener(this);
        new InteractListener(this);

        new CMDftsskills(this);
        new CMDftsskillsadmin(this);
        new CMDinspizieren(this);

        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        luckPerms = LuckPermsProvider.get();

    }

    public SkillManager getManager() {
        return manager;
    }

    public MainMenuGUI getMainMenuGUI() {
        return mainMenuGUI;
    }

    public Economy getEconomy() {
        return economy;
    }

    public LuckPerms getPermission() {
        return luckPerms;
    }
}
