package cc.funkemunky.Meme.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

public class Utils {
	
	public static List<Location> rayTrace(Location from, Location to)
	  {
	    List<Location> a = new ArrayList<Location>();
	    if ((from == null) || (to == null)) {
	      return a;
	    }
	    if (!from.getWorld().equals(to.getWorld())) {
	      return a;
	    }
	    if (from.distance(to) > 10.0D) {
	      return a;
	    }
	    double x1 = from.getX();
	    double y1 = from.getY() + 1.62D;
	    double z1 = from.getZ();
	    double x2 = to.getX();
	    double y2 = to.getY();
	    double z2 = to.getZ();
	    
	    boolean scanning = true;
	    while (scanning)
	    {
	      a.add(new Location(from.getWorld(), x1, y1, z1));
	      x1 += (x2 - x1) / 10.0D;
	      y1 += (y2 - y1) / 10.0D;
	      z1 += (z2 - z1) / 10.0D;
	      if ((Math.abs(x1 - x2) < 0.01D) && (Math.abs(y1 - y2) < 0.01D) && (Math.abs(z1 - z2) < 0.01D)) {
	        scanning = false;
	      }
	    }
	    return a;
	  }
	
	  public static boolean checkPhase(Material m)
	  {
	    int[] whitelist = { 355, 196, 194, 197, 195, 193, 64, 96, 187, 184, 186, 107, 185, 183, 192, 189, 139, 191, 85, 101, 190, 113, 188, 160, 102, 163, 157, 0, 145, 49, 77, 135, 108, 67, 164, 136, 114, 156, 180, 128, 143, 109, 134, 53, 126, 44, 416, 8, 425, 138, 26, 397, 372, 13, 135, 117, 108, 39, 81, 92, 71, 171, 141, 118, 144, 54, 139, 67, 127, 59, 115, 330, 164, 151, 178, 32, 28, 93, 94, 175, 122, 116, 130, 119, 120, 51, 140, 147, 154, 148, 136, 65, 10, 69, 31, 105, 114, 372, 33, 34, 36, 29, 90, 142, 27, 104, 156, 66, 40, 330, 38, 180, 149, 150, 75, 76, 55, 128, 6, 295, 323, 63, 109, 78, 88, 134, 176, 11, 9, 44, 70, 182, 83, 50, 146, 132, 131, 106, 177, 68, 8, 111, 30, 72, 53, 126, 37 };
	    for (int ids : whitelist) {
	      if (m.getId() == ids) {
	        return true;
	      }
	    }
	    return false;
	  }
	  
	  public static void debug(String string) {
		  Bukkit.broadcastMessage(ChatColor.BLUE + "DEBUG: " + string);
	  }

}
