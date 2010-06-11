package edu.ucla.cens.budburst;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// TODO: how does budburst create a user defined species?

public class CreateSpecies extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_species);
		
		//when this button is clicked, the species_id 10 is returned to addPlant
		Button add_species = (Button) this.findViewById(R.id.add_species);
		add_species.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent data = new Intent();
				data.putExtra("species_id", new Long(10)); // this is the species_id to be returned
				
				// just automatically returns the new species_id
				setResult(Activity.RESULT_OK, data);
				finish();			
				}
		});

	}

}