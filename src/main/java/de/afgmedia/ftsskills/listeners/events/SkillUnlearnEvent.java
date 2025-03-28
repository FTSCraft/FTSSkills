package de.afgmedia.ftsskills.listeners.events;

import de.afgmedia.ftsskills.skillsystem.Skill;
import de.afgmedia.ftsskills.skillsystem.SkillUser;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SkillUnlearnEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private SkillUser user;
    private Skill skill;

    public SkillUnlearnEvent(SkillUser user, Skill skill) {
        this.user = user;
        this.skill = skill;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public Skill getSkill() {
        return skill;
    }

    public SkillUser getUser() {
        return user;
    }
}
