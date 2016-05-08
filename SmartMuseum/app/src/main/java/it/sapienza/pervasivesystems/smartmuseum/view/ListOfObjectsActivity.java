package it.sapienza.pervasivesystems.smartmuseum.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import it.sapienza.pervasivesystems.smartmuseum.R;

public class ListOfObjectsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_objects);

        Intent mIntent = getIntent();
        int exhibitId = mIntent.getIntExtra("exhibitId", 1);
    }
}
