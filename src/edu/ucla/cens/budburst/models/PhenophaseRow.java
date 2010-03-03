package edu.ucla.cens.budburst.models;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import edu.ucla.cens.budburst.data.Row;

public class PhenophaseRow extends Row {

	public String name;
	public String comment;
	public String type;

	public InputStream getImageStream(Context ctx) {
		try {
			return ctx.getAssets().open("phenophase_images/" + _id + ".png");
		} catch (IOException e) {
			e.printStackTrace();
			try {
				return ctx.getAssets().open("phenophase_images/1.png");
			} catch (IOException e2) {
				e2.printStackTrace();
				return null;
			}
		}
	}
}