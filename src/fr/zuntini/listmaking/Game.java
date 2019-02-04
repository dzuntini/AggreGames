package fr.zuntini.listmaking;

import java.io.File;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

public class Game extends DefaultMutableTreeNode {

	private int appid; // Id of game
	 
	private String name; // Name of game
	private String locgame; // path of game
	private String comrun; // command to run it
	private String platform; // platform of the game
	private boolean installed; // is installed
	
	public Game(String chck[], String[] game)
	{
		super(chck);
		
		
		this.appid = Integer.parseInt(game[0]);
		this.name = game[1];
		this.locgame = game[2];
		this.comrun = game[3];
		this.platform = game[4];
	}
	

	
	
	public int getAppid() {
		return appid;
	}

	public void setAppid(int appid) {
		this.appid = appid;
	}

	public String getLocgame() {
		return locgame;
	}

	public void setLocgame(String locgame) {
		this.locgame = locgame;
	}

	public String getComrun() {
		return comrun;
	}

	public void setComrun(String comrun) {
		this.comrun = comrun;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isInstalled() {
		return installed;
	}

	public void setInstalled(boolean installed) {
		this.installed = installed;
	}




	@Override
	public String toString() {
		return name + " Plateforme : " + platform;
	}
	
}
