package models;

import edu.ucla.cens.budburst.data.SyncableRow;

public class PlantRow extends SyncableRow {

	public Long species_id;
	public Long site_id;
	public Long latitude;
	public Long longitude;

	@Override
	public void setupRelations() {
		//Log.d("SpeciesRow", "setupRelations");
		//species = hasOne("species", species_id);
	}

}