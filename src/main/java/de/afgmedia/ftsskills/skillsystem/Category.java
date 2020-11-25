package de.afgmedia.ftsskills.skillsystem;

import org.bukkit.Material;

import java.util.ArrayList;

public class Category {

    private ArrayList<Skill> skills;
    private String name;
    private Material mat;
    private String description;

    public Category(ArrayList<Skill> skills, String name, Material mat, String description) {
        this.skills = skills;
        this.name = name;
        this.mat = mat;
        this.description = description;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public String getName() {
        return name;
    }

    public Material getMat() {
        return mat;
    }

    public String getDescription() {
        return description;
    }
}
