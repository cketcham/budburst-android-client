package models;

import java.util.ArrayList;

import edu.ucla.cens.budburst.data.Row;

import android.util.Log;

public class SpeciesRow extends Row {

	public String species_name;
	public String common_name;
	public ArrayList<Row> phenophases;
	
	@Override
	public void setupRelations() {
		Log.d("SpeciesRow", "setupRelations");
		phenophases = hasMany("phenophase");
	}

	@Override
	public Row newRow() {
		return new SpeciesRow();
	}
}