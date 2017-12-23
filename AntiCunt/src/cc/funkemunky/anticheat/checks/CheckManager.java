package cc.funkemunky.anticheat.checks;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class CheckManager {
	
	@Getter private List<Check> checks;
	
	public CheckManager() {
		checks = new ArrayList<Check>();	
	}
		
	public List<Check> getChecks() {
		return checks;
	}

	public void registerChecks() {
		
	}
	

}
