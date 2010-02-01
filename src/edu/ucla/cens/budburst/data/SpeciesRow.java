package edu.ucla.cens.budburst.data;

public class SpeciesRow extends Row {

	public String species_name;
	public String common_name;

	@Override
	public Row newRow() {
		return new SpeciesRow();
	}
}