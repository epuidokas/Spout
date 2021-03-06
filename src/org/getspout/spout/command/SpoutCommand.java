package org.getspout.spout.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;
import org.getspout.spout.Spout;
import org.getspout.spout.player.SpoutCraftPlayer;
import org.getspout.spoutapi.player.SpoutPlayer;

public class SpoutCommand implements CommandExecutor {

	private final Spout p;
	private String motd_temp = "";

	public SpoutCommand(Spout p) {
		this.p = p;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		sender.sendMessage("[Spout] Server version: " + p.getDescription().getVersion());
		
		if (args.length == 0) {
			return true;
		}
		
		if (!sender.isOp() && args.length > 0) {
			sender.sendMessage("[Spout] This command is Op only");
			return true;
		}
		
		String c = args[0];

		if (c.equals("version")) {
			
			CommandSender target = sender;
			
			if (args.length > 1) {
				target = p.getServer().getPlayer(args[1]);
				if (target == null) {
					sender.sendMessage("[Spout] Unknown player: " + args[1]);
					return true;
				}
			}

			if (!(target instanceof Player)) {
				sender.sendMessage("[Spout] Client version: no client");
			} if (!(target instanceof SpoutPlayer)) {
				sender.sendMessage("[Spout] Client version: standard client");
			} else {
				SpoutCraftPlayer sp = (SpoutCraftPlayer)target;
				if (!sp.isSpoutCraftEnabled()) {
					sender.sendMessage("[Spout] Client version: Not a Spout client");
				} else {
					sender.sendMessage("[Spout] Client version: " + sp.getVersionString());
				}
			}
			return true;
		} else if (c.equals("verify") && args.length > 1 && sender.isOp() && sender instanceof ConsoleCommandSender) {
			sender.sendMessage("[Spout] Temporarily setting the motd to: " + args[1]);
			sender.sendMessage("[Spout] It will return to its oringinal setting in ~5 mins");
			motd_temp = ((CraftServer) Bukkit.getServer()).getHandle().server.p;
			((CraftServer) Bukkit.getServer()).getHandle().server.p = args[1];
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(p, new Runnable() {
				@Override
				public void run() {
					((CraftServer) Bukkit.getServer()).getHandle().server.p = motd_temp;
				}
			}, 20 * 60 * 5);
		}
		
		return false;
		
	}

}
