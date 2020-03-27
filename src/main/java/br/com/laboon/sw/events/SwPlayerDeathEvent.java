package br.com.laboon.sw.events;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class SwPlayerDeathEvent extends PlayerEvent {
	
	public static HandlerList handlers;
	private Player killer;

	static {
		handlers = new HandlerList();
	}

	public SwPlayerDeathEvent(Player p, Player killer) {
		super(p);
		if (killer != null)
			this.killer = killer;
	}

	public UUID getPlayerUUID() {
		return player.getUniqueId();
	}
	public UUID getKillerUUID() {
		return killer.getUniqueId();
	}

	public boolean hasKiller() {
		return killer != null;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}
