package models;

import java.util.ArrayList;

import android.util.Log;
import edu.ucla.cens.budburst.data.Row;

public class SpeciesRow extends Row {

	public String species_name;
	public String common_name;
	public ArrayList<Row> phenophases;

	@Override
	public void setupRelations() {
		Log.d("SpeciesRow", "setupRelations");
		// phenophases = hasMany("phenophase");
	}

}