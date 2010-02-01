package edu.ucla.cens.budburst.data;

public class PhenophaseRow extends Row {

	public String name;
	public String comment;

	@Override
	public Row newRow() {
		return new PhenophaseRow();
	}
}