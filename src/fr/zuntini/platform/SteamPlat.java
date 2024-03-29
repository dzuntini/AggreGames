package fr.zuntini.platform;

import fr.zuntini.game.Game;


import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.Scanner;





public class SteamPlat extends Platform 
{


	public SteamPlat(String path) {
		super("Steam", path);
		
		
		String[] a = this.path.split("steam.exe");
		File gfolder = new File((a[0] + "steamapps\\"));
		
		gfolder.listFiles();
		ftmakelist(gfolder);
		
	}

	//First time making list
	public ArrayList<Game> ftmakelist(File rep) 
	{
		Game control;
		
		
		File[] f = rep.listFiles();
		for (File file : f) {
			if (file.toString().contains("appmanifest")) {
				control = ftaddGame(file);
				if (control != null)
					glist.add(control);
			}
			if (file.toString().contains("library"))
				recursivelib(file);
		}
		//	System.out.println(p.toString());
		//	System.out.println(p.getAbsoluteFile());
		return null;
		
		
	}
	private Game ftaddGame(File rep)
	{
		Game g = null; // Game 0 = Appid // Game 1 = Name // Game 2 = Localisation // Game 3 = Command run
		String[] details = new String[5];
		Scanner sc;
		String str; // Temp str
		//System.out.println(rep.toString());
		try 
		{
			sc = new Scanner(rep);
			while (sc.hasNextLine())
				{	
						str = sc.nextLine();	
						if (str.contains("appid"))
							details[0] = destack("aid",str );
						if (str.contains("name"))
							details[1] = destack("name", str);
				}
		} catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		details[3] =("steam://rungameid/"+ details[0]);
		details[4] = "Steam";
		if (details[0] != null)
		{
			g = new Game(details);
			System.out.println(g.toString());
		}
		return g;
	}
	
	private void recursivelib(File f)
	{
		String a;
		int i = 0;
		try (Scanner sc = new Scanner (f))
		{				
			while (i < 4 )
			{
				a =  sc.nextLine();
				System.out.println("Library Line :"+ a);
				i++;
			}
			while (sc.hasNextLine())
			{
				a =  sc.nextLine();
				if (!a.contains("}"))
					ftmakelist(new File(destack("rec",a))); 
			}								
		} catch (FileNotFoundException e) {		
			e.printStackTrace();
		}	
	}
	
	private String destack(String param , String s)
		{
			String[] str;
			if (param.equals("aid"))
			{
				s = s.replaceAll("[\\D]", "");
			}
			if (param.equals("name"))
			{
				s = s.replaceAll("[\\s\"]", "");
				str = s.split("name");
				s = str[1];
			}
			if (param.equals("rec"))
			{
				s = s.trim();
				s = s.substring(5);
				s = s.replaceAll("[\\s\"]", "");
				s += "\\steamapps";		
			}
			return s;
		}
	
	@Override
	public ArrayList<Game> makelist() {
		
		return null;
	}

	@Override
	public String getinstallerlink() {
		
		return null;
	}
}