package cc.funkemunky.kit.utils;

import cc.funkemunky.api.utils.BoundingBox;
import cc.funkemunky.kit.Kit;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

@Getter
@Setter
public class LocationUtil {
    @Getter
    private static Cube cube;
    @Getter
    @Setter
    private static Location spawnLocation;

    public LocationUtil() {
        cube = new Cube();

        if(Kit.getInstance().getConfig().get("location.one") != null && Kit.getInstance().getConfig().get("location.two") != null) {
            cube.setOne(getLocationFromString(Kit.getInstance().getConfig().getString("location.one")));
            cube.setTwo(getLocationFromString(Kit.getInstance().getConfig().getString("location.two")));
        }

        if(Kit.getInstance().getConfig().get("location.point") != null) {
            spawnLocation = getLocationFromString(Kit.getInstance().getConfig().getString("location.point"));
        }
    }

    public static Location getLocationFromString(String locString) {
        String[] splitOne = locString.split(";");
        return new Location(Bukkit.getWorld(splitOne[0]), Double.parseDouble(splitOne[1]), Double.parseDouble(splitOne[2]), Double.parseDouble(splitOne[3]));
    }

    public static String locationToString(Location location) {
        return location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ();
    }

    public static boolean isWithinSpawn(LivingEntity entity) {
        return cube.getOne() != null && cube.getTwo() != null && new BoundingBox(cube.getOne().toVector(), cube.getTwo().toVector()).intersectsWithBox(entity.getLocation().clone().toVector());
    }

}
