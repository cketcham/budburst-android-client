package edu.ucla.cens.budburst.data;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
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

	@Override
	public Field[] getFields() {
		ArrayList<Field> ret = new ArrayList<Field>();
		Field[] fields = this.getClass().getFields();
		for (int i = 0; i < fields.length; i++) {
			if (Modifier.isPublic(fields[i].getModifiers()) && !fields[i].getName().equals("_id"))
				ret.add(fields[i]);
		}
		return ret.toArray(new Field[0]);
	}
}
