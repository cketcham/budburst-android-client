package models;

import java.util.ArrayList;

import edu.ucla.cens.budburst.data.SyncableRow;

public class SiteRow extends SyncableRow {

	public String name;
	public Long latitude;
	public Long longitude;
	public String city;
	public String state;
	public Long postal;
	public String country;
	public String comments;
}