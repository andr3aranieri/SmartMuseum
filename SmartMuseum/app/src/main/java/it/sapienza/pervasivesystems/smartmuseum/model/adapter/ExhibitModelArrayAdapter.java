package it.sapienza.pervasivesystems.smartmuseum.model.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import it.sapienza.pervasivesystems.smartmuseum.model.entity.ExhibitModel;

/**
 * Created by Guamaral on 5/1/2016.
 */
public class ExhibitModelArrayAdapter extends ArrayAdapter<ExhibitModel> {

    Context context;
    int layoutResourceId;
    private Button objectBtn;
    private Button detailBtn;
    ArrayList<ExhibitModel> exhibitModels = new ArrayList<ExhibitModel>();

    public ExhibitModelArrayAdapter(Context context, int layoutResourceId, ArrayList<ExhibitModel> exhibitModelsNew) {

        super(context, layoutResourceId, exhibitModelsNew);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.exhibitModels = exhibitModelsNew;
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        View item = convertView;
        this.objectBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                final int position = getListView().getPositionForView(v);
                Toast.makeText(context, "Edit", Toast.LENGTH_LONG).show();
            }

        });
        this.detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Delete", Toast.LENGTH_LONG).show();
            }

        });

        return item;

    }

}
