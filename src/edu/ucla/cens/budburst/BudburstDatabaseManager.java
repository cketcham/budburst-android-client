package edu.ucla.cens.budburst;

import android.content.Context;
import edu.ucla.cens.budburst.data.DatabaseManager;
import edu.ucla.cens.budburst.models.ObservationRow;
import edu.ucla.cens.budburst.models.PhenophaseRow;
import edu.ucla.cens.budburst.models.PlantRow;
import edu.ucla.cens.budburst.models.SiteRow;
import edu.ucla.cens.budburst.models.SpeciesPhenophaseRow;
import edu.ucla.cens.budburst.models.SpeciesRow;

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
		String upPlantURL = context.getString(R.string.phone_service_url) + "?add_plant";
		createSyncableDatabase("plant", downPlantURL, upPlantURL, new PlantRow());

		String downObsURL = context.getString(R.string.phone_service_url) + "?get_my_obs";
		String upObsURL = context.getString(R.string.uploadObservationsUrl);
		createSyncableDatabase("observation", downObsURL, upObsURL, new ObservationRow());
	}

}
