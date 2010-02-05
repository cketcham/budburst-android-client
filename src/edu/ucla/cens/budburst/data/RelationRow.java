package edu.ucla.cens.budburst.data;

import java.util.ArrayList;


public abstract class RelationRow extends Row {

	@Override
	public String getName() {
		return from() + "_" + to();
	}
	
	public String from() {
		String name = this.getClass().getSimpleName();
		int loc = name.indexOf(name.split("[A-Z]")[2]) - 1;
		return name.substring(0, loc).toLowerCase();
	}
	
	public String to() {
		String name = this.getClass().getSimpleName();
		int loc = name.indexOf(name.split("[A-Z]")[2]) - 1;
		return name.substring(loc, name.length() - 3).toLowerCase();
	}	
}
