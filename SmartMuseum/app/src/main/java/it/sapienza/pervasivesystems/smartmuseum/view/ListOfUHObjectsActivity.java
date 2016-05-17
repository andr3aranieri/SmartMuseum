package it.sapienza.pervasivesystems.smartmuseum.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;

import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.WorkofartBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.adapter.WorkOfArtModelAdapter;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.WorkofartModel;

public class ListOfUHObjectsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_uhobjects);

        Intent mIntent = getIntent();
        int exhibitId = mIntent.getIntExtra("exhibitId", 0);

        //TODO exhibitId bolon userModel 2iig avdag method iig duudna.

        ArrayList<WorkofartModel> workofartModels = new WorkofartBusiness().getWorkOfArtListFAKE();

        GridView gridview = (GridView) findViewById(R.id.gridView);
        gridview.setAdapter(new WorkOfArtModelAdapter(this, workofartModels));
    }
}
