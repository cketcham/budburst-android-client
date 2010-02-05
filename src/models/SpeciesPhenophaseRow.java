package models;

import edu.ucla.cens.budburst.data.RelationRow;
import edu.ucla.cens.budburst.data.Row;

public class SpeciesPhenophaseRow extends RelationRow {
	
	public Long species_id;
	public Long phenophase_id;

	@Override
	public Row newRow() {
		return new SpeciesPhenophaseRow();
	}
}
