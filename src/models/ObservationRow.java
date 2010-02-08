package models;

import java.util.ArrayList;

import edu.ucla.cens.budburst.data.SyncableRow;

public class ObservationRow extends SyncableRow {
	
	public Long species_id;
	public Long site_id;
	public Long phenophase_id;
	public Long image_id;
	
	@Override
	public ArrayList<String> primaryKeys() {
		 ArrayList<String> ret = new ArrayList<String>();
		 ret.add("species_id");
		 ret.add("site_id");
		 ret.add("phenophase_id");
		 return ret;
	}

}