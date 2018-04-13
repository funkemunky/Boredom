package cc.funkemunky.Meme.utils;

public class Lag {

	public static double getFreeRam() {
		return Math.round(Runtime.getRuntime().freeMemory() / 1000000);
	}

	public static double getMaxRam() {
		return Math.round(Runtime.getRuntime().maxMemory() / 1000000);
	}

}