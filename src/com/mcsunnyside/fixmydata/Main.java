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
					p.sendMessage("��b3.��Ӧ����Щ��Ʒ�Ҹ��ط��ź� ��Ӱ�����ĵ���Ͳ��������");
					p.sendMessage("��b4.��������ȷ��֮���������ݽ����������");
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
					UUID uuid = p.getUniqueId();
					p.kickPlayer("��c������������������ɾ�� 10������������ ����������뾡����ϵ����Ա");
					Bukkit.broadcastMessage("��7��ҡ�e"+p.getName()+"��7�����˸�������.");
					Bukkit.getServer().savePlayers();
					
					File mainWorld = new File(Bukkit.getWorldContainer(),Bukkit.getWorlds().get(0).getName());
					File playerDataFolder = new File(mainWorld,"playerdata");
					File playerDataFile = new File(playerDataFolder,uuid.toString()+".dat");
					File playerDataBakFile = new File(playerDataFolder,uuid.toString()+".dat.bak");
					if(!playerDataBakFile.exists())
						try {
						playerDataBakFile.createNewFile();
						}catch (Exception e) {
							// TODO: handle exception
						}
					this.moveFile(playerDataFile.getPath(), playerDataBakFile.getPath());
				}
			}
		}
		return false;
	}

	@SuppressWarnings("resource")
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
		File newfile = new File(newPath);
		if(newfile.exists()) {
			UUID uuid = UUID.randomUUID();
			getLogger().warning("Found exist backup file,Backing up with UUID "+uuid.toString()+"...");
			copyFile(newPath, newPath+".alert."+uuid.toString()+".");
		}
		delFile(newPath);
		copyFile(oldPath, newPath);
		delFile(oldPath);

	}

}
