package edu.ucla.cens.budburst.models;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import edu.ucla.cens.budburst.Budburst;
import edu.ucla.cens.budburst.data.Database;
import edu.ucla.cens.budburst.data.Row;

public class PhenophaseRow extends Row {

	public String name;
	public String comment;
	public String type;

	public InputStream getImageStream(Context ctx, long protocol_id) {
		Database sp = Budburst.getDatabaseManager().getDatabase("species_phenophase");
		ArrayList<Row> rows = sp.find("phenophase_id = " + _id + " AND protocol_id =" + protocol_id);

		try {
			return ctx.getAssets().open("phenophase_images/p" + ((SpeciesPhenophaseRow) rows.get(0)).icon_id + ".png");
		} catch (IOException e) {
			e.printStackTrace();
			try {
				return ctx.getAssets().open("phenophase_images/p1.png");
			} catch (IOException e2) {
				e2.printStackTrace();
				return null;
			}
		}
	}

	public String getAboutText(long protocol_id) {
		Database sp = Budburst.getDatabaseManager().getDatabase("species_phenophase");
		ArrayList<Row> rows = sp.find("phenophase_id = " + _id + " AND protocol_id =" + protocol_id);

		return ((SpeciesPhenophaseRow) rows.get(0)).description;
	}
}