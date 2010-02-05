package edu.ucla.cens.budburst;

import models.PhenophaseRow;
import models.SpeciesPhenophaseRow;
import models.SpeciesRow;
import android.content.Context;
import edu.ucla.cens.budburst.data.DatabaseManager;

public class BudburstDatabaseManager extends DatabaseManager {

	public BudburstDatabaseManager(Context context) {
		super(context);
        createDatabase("species_phenophase", R.raw.species_phenophase_db, new SpeciesPhenophaseRow());
        createDatabase("phenophase", R.raw.phenophase_db, new PhenophaseRow());
        createDatabase("species", R.raw.species_db, new SpeciesRow());
	}

}
