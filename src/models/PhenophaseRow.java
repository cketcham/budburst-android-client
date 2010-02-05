package models;

import edu.ucla.cens.budburst.data.Row;
import android.content.Context;

public class PhenophaseRow extends Row {

	public String name;
	public String comment;

	@Override
	public Row newRow() {
		return new PhenophaseRow();
	}
}