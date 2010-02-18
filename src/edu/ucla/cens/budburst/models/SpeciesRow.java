package edu.ucla.cens.budburst.models;

import java.util.ArrayList;
import java.util.HashMap;

import edu.ucla.cens.budburst.Budburst;
import edu.ucla.cens.budburst.data.Row;
import edu.ucla.cens.budburst.data.SyncableRow;

public class SpeciesRow extends SyncableRow {

	public String species_name;
	public String common_name;
	private final HashMap<String, ArrayList<Row>> phenophases = new HashMap<String, ArrayList<Row>>();

	public ArrayList<Row> phenophases(String type) {
		if (!phenophases.containsKey(type)) {
			if (_id > Budburst.MAX_PREDEFINED_SPECIES)
				phenophases.put(type, Budburst.getDatabaseManager().getDatabase("phenophase").find("type='" + type + "'"));
			else
				phenophases.put(type, hasMany("phenophase", "type='" + type + "'"));
		}

		return phenophases.get(type);
	}

	public String getImagePath() {
		// Hack to display the icon for user defined plants
		// Don't blame me I didn't decide to do it this way.
		if (_id > Budburst.MAX_PREDEFINED_SPECIES)
			return Budburst.SPECIES_PATH + "999.jpg";
		return Budburst.SPECIES_PATH + _id + ".jpg";
	}

}