package br.com.laboon.sw.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

import lombok.Getter;

public class PlayerQuitInGameEvent extends PlayerEvent {

	@Getter
	public static HandlerList handlerList = new HandlerList();
	
	public PlayerQuitInGameEvent(Player who) {
		super(who);
	}

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}

}
