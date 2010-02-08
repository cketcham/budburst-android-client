package edu.ucla.cens.budburst;

import models.ObservationRow;
import models.PhenophaseRow;
import models.PlantRow;
import models.SiteRow;
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
        
        String siteURL = context.getString(R.string.phone_service_url)+"?get_my_sites";
		createSyncableDatabase("site", siteURL, new SiteRow());
        
        String plantURL = context.getString(R.string.phone_service_url)+"?get_my_plants";
		createSyncableDatabase("plant", plantURL, new PlantRow());
		
		String obsURL = context.getString(R.string.phone_service_url)+"?get_my_obs";
		createSyncableDatabase("observation", obsURL, new ObservationRow());
	}

}
