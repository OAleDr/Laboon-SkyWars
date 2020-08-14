package br.com.laboon.sw;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

public class LaboonSW extends JavaPlugin {
	
	@Getter
	public static LaboonSW instance;

	
	@Override
	public void onEnable() {
		instance = this;
	}
	
}
