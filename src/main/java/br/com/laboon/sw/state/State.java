package br.com.laboon.sw.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import br.com.battlebits.commons.bukkit.scoreboard.modules.Line;
import br.com.laboon.sw.LaboonSW;
import br.com.laboon.sw.events.PlayerQuitInGameEvent;
import br.com.laboon.sw.manager.GameManager;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class State implements Listener {
	
	private GameManager gm;
	private Location location;
	@Setter
	private int time;
	
	private Consumer<List<Line>> scoreboardLines;
	
	public State(GameManager gm) {
		this.gm = gm;
		this.location = Bukkit.getWorld("world").getHighestBlockAt(0, 0).getLocation();
		LaboonSW.getInstance().getServer().getPluginManager().registerEvents(this, LaboonSW.getInstance());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitInGameEvent event) {
		event.getPlayer().getWorld().strikeLightningEffect(event.getPlayer().getLocation());
		dropItems(event.getPlayer(), event.getPlayer().getLocation());
	}
	
	public void stopState() {
		HandlerList.unregisterAll(this);
		time = -1;
	}
	
	public void setSpectator(Player player) {
		getGm().removePlayer(player);
		
		player.setAllowFlight(true);
		player.closeInventory();
		player.getInventory().clear();
		player.setHealth(20D);
		player.setFoodLevel(20);
		
		
	}
	
	public void respawn(Player player) {
		Random r = new Random();
		
		player.setHealth(20.0);
		player.setFoodLevel(20);
		player.getInventory().clear();
		player.getActivePotionEffects().clear();
		player.getInventory().setArmorContents(new ItemStack[4]);
		player.setFireTicks(0);
		player.setFoodLevel(20);
		player.setFlying(false);
		player.setAllowFlight(false);
		player.setSaturation(3.2F);
		player.setNoDamageTicks(20);
		player.getInventory().addItem(new ItemStack(Material.COMPASS));
		int x = r.nextInt(200) + 250, z = r.nextInt(200) + 250;
		x = r.nextBoolean() ? x : -x;
		z = r.nextBoolean() ? z : -z;
		player.teleport(player.getWorld().getHighestBlockAt(x, z).getLocation());
		r = null;
	}
	
	public void clearPlayer(Player player) {
		player.getActivePotionEffects().forEach(potion -> {
			player.removePotionEffect(potion.getType());
		});
		player.setNoDamageTicks(100);
		player.setHealth(20);
		player.setLevel(0);
		player.setFoodLevel(20);
		player.closeInventory();
		player.setVelocity(new Vector());
		player.setGameMode(GameMode.ADVENTURE);
		clearInventory(player);
	}
	
	public void clearInventory(Player player) {
		player.getInventory().setArmorContents(new ItemStack[4]);
		player.getInventory().clear();
		player.setItemOnCursor(new ItemStack(Material.AIR));
	}
	
	public void dropItems(Player p, Location l) {
		ArrayList<ItemStack> items = new ArrayList<>();
		for (ItemStack item : p.getInventory().getContents())
			if (item != null && item.getType() != Material.AIR)
				items.add(item.clone());
		for (ItemStack item : p.getInventory().getArmorContents()) 
			if (item != null && item.getType() != Material.AIR)
				items.add(item.clone());
		if (p.getItemOnCursor() != null && p.getItemOnCursor().getType() != Material.AIR)
			items.add(p.getPlayer().getItemOnCursor().clone());
		dropItems(p, items, l);
	}

	public void dropItems(Player p, List<ItemStack> items, Location l) {
		for (ItemStack item : items) {
			if (item != null && item.getType() != Material.AIR) {
				Item i = l.getWorld().dropItemNaturally(l, item.clone());
				i.setPickupDelay(20);
				if (item.hasItemMeta())
					i.getItemStack().setItemMeta(item.getItemMeta());
			}
		}
		if(p != null) {
			p.getInventory().setArmorContents(new ItemStack[4]);
			p.getInventory().clear();
			p.setItemOnCursor(new ItemStack(Material.AIR));
		}
		items.clear();
		items = null;
	}
	

}
