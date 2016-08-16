package com.lookcook.gamble;

import java.util.Random;

import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class Gamble extends JavaPlugin {

	public static Economy economy = null;

	public void onEnable() {
		setupEconomy();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("gamble")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "You must be a player to do this.");
				return false;
			}
			Player player = (Player) sender;
			if (args.length != 1) {
				player.sendMessage(ChatColor.RED + "Usage: /gamble <number>");
				return false;
			} else {
				if (NumberUtils.isNumber(args[0])) {
					Double bet = Double.parseDouble(args[0]);
					if (economy.has(player, bet)) {
						Random rand = new Random();
						if (rand.nextBoolean()) {
							player.sendMessage(ChatColor.GREEN + "You received $" + args[0] + ".");
							economy.depositPlayer(player, bet);
						} else {
							player.sendMessage(ChatColor.RED + "Awww... you lost $" + args[0] + ".");
							economy.withdrawPlayer(player, bet);
						}
					} else {
						player.sendMessage(ChatColor.RED + "You don't have $" + args[0] + " to gamble with.");
					}
				} else {
					player.sendMessage(ChatColor.RED + args[0] + " is not a number.");
				}
			}
		}
		return false;
	}

	private void setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}
	}
}
