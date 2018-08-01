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
					p.sendMessage("��e=================================================");
					p.sendMessage("��c��l����:��b�����ڳ���������������޸����� ������c��l�����ϸ�Ķ�:");
					p.sendMessage("��b1.�˲���һ��ִ�У��޷��ָ�����c��l�������ݾ���Ҳ�ز�����");
					p.sendMessage("��b2.���������c��lһ��������� �����ɾ� ͳ�� ������ĩӰ����Ʒ");
					p.sendMessage("��b3.��Ӧ����Щ��Ʒ�Ҹ��ط��ź� ��Ӱ�����ĵ��� ��������ص�����");
					p.sendMessage("��b4.��������ȷ��֮�� 1������������������");
					p.sendMessage("��c��l�������Ҫ������? ȷ�������� ��e/fixmydata confirm");
					p.sendMessage("��e=================================================");
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
						p.sendMessage("��c��l�㻹û�в鿴��ʹ��˵������ʹ�ô�Σ��ָ��Ƿ�����������ƭ������� ����ǣ���ǰ��http://forum.mcsunnyside.com�ٱ�����ң�");
						return true;
					}
					UUID UUID = p.getUniqueId();
					p.kickPlayer("��c�������������޸� 10����������������");
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
			if (oldfile.exists()) { // �ļ�����ʱ
				InputStream inStream = new FileInputStream(oldPath); // ����ԭ�ļ�
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
//	               int  length;  
				while ((byteread = inStream.read(buffer)) != -1) {
//	                   bytesum  +=  byteread;  //�ֽ���  �ļ���С  
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
