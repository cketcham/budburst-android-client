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

	public static final int LEAVES = 0;
	public static final int FLOWERS = 1;
	public static final int FRUITS = 2;

	public BudburstDatabaseManager(Context context) {
		super(context);
		createDatabase("species_phenophase", R.raw.species_phenophase_db, new SpeciesPhenophaseRow());
		createDatabase("phenophase", R.raw.phenophase_db, new PhenophaseRow());
		createDatabase("species", R.raw.species_db, new SpeciesRow());

		String downSiteURL = context.getString(R.string.phone_service_url) + "?get_my_sites";
		String upSiteURL = "";
		createSyncableDatabase("site", downSiteURL, upSiteURL, new SiteRow());

		String downPlantURL = context.getString(R.string.phone_service_url) + "?get_my_plants";
		String upPlantURL = "";
		createSyncableDatabase("plant", downPlantURL, upPlantURL, new PlantRow());

		String downObsURL = context.getString(R.string.phone_service_url) + "?get_my_obs";
		String upObsURL = context.getString(R.string.uploadObservationsUrl);
		createSyncableDatabase("observation", downObsURL, upObsURL, new ObservationRow());
	}

}
