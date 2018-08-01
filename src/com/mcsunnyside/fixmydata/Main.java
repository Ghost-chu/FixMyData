package com.mcsunnyside.fixmydata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	ArrayList<String> ReadyFix = new ArrayList<String>();

	@Override
	public void onEnable() {
		saveDefaultConfig();
		reloadConfig();
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		ReadyFix.remove(e.getPlayer().getName());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length == 0) {
				if (command.getName().equalsIgnoreCase("fixmydata")) {
					p.sendMessage("§e=================================================");
					p.sendMessage("§c§l警告:§b您正在尝试自助玩家数据修复工具 请您§c§l务必详细阅读:");
					p.sendMessage("§b1.此操作一旦执行，无法恢复，§c§l您的数据就再也回不来了");
					p.sendMessage("§b2.这会清除你§c§l一切玩家数据 包括成就 统计 背包和末影箱物品");
					p.sendMessage("§b3.您应把这些物品找个地方放好 不影响您的弹珠 阳光点和领地等数据");
					p.sendMessage("§b4.在你输入确认之后 1分钟内请勿进入服务器");
					p.sendMessage("§c§l请问真的要继续吗? 确认请输入 §e/fixmydata confirm");
					p.sendMessage("§e=================================================");
					if (!ReadyFix.contains(p.getName())) {
						ReadyFix.add(p.getName());
					}
					return true;
				}
			} else {
				
				if (args[0].equals("confirm")) {
					String worldname = getConfig().getString("mainworld");
					if (worldname.isEmpty()) {
						return false;
					}
					if (!ReadyFix.contains(p.getName())) {
						p.sendMessage("§c§l你还没有查看过使用说明就在使用此危险指令，是否有人正在欺骗你操作？ 如果是，请前往http://forum.mcsunnyside.com举报此玩家！");
						return true;
					}
					UUID UUID = p.getUniqueId();
					p.kickPlayer("§c您的数据正在修复 10秒内请勿加入服务器");
					Bukkit.getServer().savePlayers();
					this.moveFile(
							getDataFolder().getAbsolutePath().replace("plugins/FixMyData", "") + "/" + worldname
									+ "/playerdata/" + UUID.toString() + ".dat",
							getDataFolder().getAbsolutePath().replace("plugins/FixMyData", "") + "/" + worldname
									+ "/playerdata/" + UUID.toString() + ".dat.bak");
					this.moveFile(
							getDataFolder().getAbsolutePath().replace("plugins\\FixMyData", "") + "\\" + worldname
									+ "/playerdata/" + UUID.toString() + ".dat",
							getDataFolder().getAbsolutePath().replace("plugins\\FixMyData", "") + "\\" + worldname
									+ "/playerdata/" + UUID.toString() + ".dat.bak");
					getLogger().info("Player "+p.getName()+"("+UUID.toString()+")"+" successfully fix him playerdata.");

				}
			}
		}
		return false;
	}

	public void copyFile(String oldPath, String newPath) {
		try {
//	           int  bytesum  =  0;  
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
//	               int  length;  
				while ((byteread = inStream.read(buffer)) != -1) {
//	                   bytesum  +=  byteread;  //字节数  文件大小  
//	                   System.out.println(bytesum);  
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			e.getMessage();

		}

	}

	public void delFile(String filePathAndName) {
		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			java.io.File myDelFile = new java.io.File(filePath);
			myDelFile.delete();
		} catch (Exception e) {
			e.getMessage();
		}
	}
	public void moveFile(String oldPath, String newPath) {
		delFile(newPath);
		copyFile(oldPath, newPath);
		delFile(oldPath);

	}

}
