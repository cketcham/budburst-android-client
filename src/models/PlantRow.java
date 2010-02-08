package models;

import java.util.ArrayList;

import edu.ucla.cens.budburst.Budburst;
import edu.ucla.cens.budburst.data.Row;
import edu.ucla.cens.budburst.data.SyncableRow;

public class PlantRow extends SyncableRow {

	public Long species_id;
	public Long site_id;
	public Long latitude;
	public Long longitude;
	
	public SpeciesRow species;
	public SiteRow site;
	public ArrayList<Row> observations;

	@Override
	public void setupRelations() {
		//Log.d("SpeciesRow", "setupRelations");
		species = (SpeciesRow) hasOne("species", species_id);
		site = (SiteRow) hasOne("site", site_id);
		observations = (ArrayList<Row>) Budburst.getDatabaseManager().getDatabase("observation").find("species_id=" + species_id + " AND site_id=" +site_id);
	}

	@Override
	public ArrayList<String> primaryKeys() {
		 ArrayList<String> ret = new ArrayList<String>();
		 ret.add("species_id");
		 ret.add("site_id");
		 return ret;
	}

}