package cc.funkemunky.anticheat.checks;

import org.bukkit.event.Listener;

import lombok.Getter;
import lombok.Setter;

public class Check implements Listener {
	
	@Getter @Setter private String name;
	@Getter @Setter private String displayName;
	@Getter @Setter private CheckType type;
	@Getter @Setter private int toBan = 6;
	@Getter @Setter private int toNotify = 0;
	
	public Check(String name, String displayName, CheckType type) {
		this.name = name;
		this.displayName = displayName;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public CheckType getType() {
		return type;
	}

	public void setType(CheckType type) {
		this.type = type;
	}

	public int getToBan() {
		return toBan;
	}

	public void setToBan(int toBan) {
		this.toBan = toBan;
	}

	public int getToNotify() {
		return toNotify;
	}

	public void setToNotify(int toNotify) {
		this.toNotify = toNotify;
	}

}
