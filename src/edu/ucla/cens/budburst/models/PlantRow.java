package edu.ucla.cens.budburst.models;

import java.util.ArrayList;

import edu.ucla.cens.budburst.Budburst;
import edu.ucla.cens.budburst.data.Row;
import edu.ucla.cens.budburst.data.SyncableRow;

public class PlantRow extends SyncableRow {

	public Long species_id;
	public Long site_id;
	public Double latitude;
	public Double longitude;

	private SpeciesRow species;
	private SiteRow site;

	public ObservationRow observations(PhenophaseRow phenophase) {
		ArrayList<Row> observations = Budburst.getDatabaseManager().getDatabase("observation").find(
				"species_id=" + species_id + " AND site_id=" + site_id + " AND phenophase_id=" + phenophase._id);
		if (!observations.isEmpty())
			return (ObservationRow) observations.get(0);

		return null;
	}

	public SpeciesRow species() {
		// TODO: figure out why species is getting reused for different models
		if (species == null || species._id != species_id)
			species = (SpeciesRow) hasOne("species", species_id);
		return species;
	}

	public SiteRow site() {
		if (site == null)
			site = (SiteRow) hasOne("site", site_id);
		return site;
	}

	@Override
	public ArrayList<String> primaryKeys() {
		ArrayList<String> ret = new ArrayList<String>();
		ret.add("species_id");
		ret.add("site_id");
		return ret;
	}

}