package it.sapienza.pervasivesystems.smartmuseum.model.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.ExhibitBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.VisitExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.view.DetailOfExhibitActivity;
import it.sapienza.pervasivesystems.smartmuseum.view.ListOfObjectsActivity;
import it.sapienza.pervasivesystems.smartmuseum.view.ListOfUHObjectsActivity;
import it.sapienza.pervasivesystems.smartmuseum.view.Utilities;

/**
 * Created by andrearanieri on 11/07/16.
 */
public class VisitExhibitModelArrayAdapter extends ArrayAdapter<VisitExhibitModel> {

    Context context;
    int layoutResourceId;
    List<VisitExhibitModel> visitsArray = new ArrayList<VisitExhibitModel>();

    public VisitExhibitModelArrayAdapter(Context context, int layoutResourceId, List<VisitExhibitModel> visits) {
        super(context, layoutResourceId, visits);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.visitsArray = visits;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View item = convertView;
        VisitExhibitWrapper exhibitWrapper = null;

        if (item == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            item = inflater.inflate(layoutResourceId, parent, false);

            exhibitWrapper = new VisitExhibitWrapper();
            exhibitWrapper.image = (ImageView) item.findViewById(R.id.exh_image);
            exhibitWrapper.title = (TextView) item.findViewById(R.id.exh_title);
            exhibitWrapper.shortDesc = (TextView) item.findViewById(R.id.exh_short_desc);
            exhibitWrapper.exhDate = (TextView) item.findViewById(R.id.exh_date);
            exhibitWrapper.exhTimestamp = (TextView) item.findViewById(R.id.exh_timestamp);
            exhibitWrapper.exhTimestampLayout = (RelativeLayout) item.findViewById(R.id.exh_timestamp_layout);
            exhibitWrapper.detailBtn = (Button) item.findViewById(R.id.detailBtn);
            exhibitWrapper.objectBtn = (Button) item.findViewById(R.id.objectBtn);

            item.setTag(exhibitWrapper);

        } else {
            exhibitWrapper = (VisitExhibitWrapper) item.getTag();
        }

        final VisitExhibitModel visitExhibitModel = visitsArray.get(position);

        Picasso.with(context).load(visitExhibitModel.getExhibitModel().getImage()).into(exhibitWrapper.image);
        exhibitWrapper.title.setText(visitExhibitModel.getExhibitModel().getTitle());
        exhibitWrapper.shortDesc.setText(visitExhibitModel.getExhibitModel().getShortDescription());
        exhibitWrapper.exhDate.setText(visitExhibitModel.getExhibitModel().getPeriod());
        exhibitWrapper.exhTimestampLayout.setVisibility(View.VISIBLE);
        exhibitWrapper.exhTimestamp.setText(new Utilities().formatDateToString(visitExhibitModel.getTimeStamp(), "dd/MM/yyyy HH:mm"));

        exhibitWrapper.objectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;

                //if the user is inside the museum, the list of object activity will be called.
                if (SmartMuseumApp.isUserInsideMuseum) {
                    intent = new Intent(context, ListOfObjectsActivity.class);
                    intent.putExtra("exhibitId", new ExhibitBusiness().getExhibitHashmapKey(visitExhibitModel.getExhibitModel()));
                } else {
                    intent = new Intent(context, ListOfUHObjectsActivity.class);
                    intent.putExtra("exhibitId", visitExhibitModel.getExhibitModel().getId());
                }
                context.startActivity(intent);
            }
        });
        exhibitWrapper.detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailOfExhibitActivity.class);
                intent.putExtra("exhibitModel", visitExhibitModel.getExhibitModel());
                context.startActivity(intent);
            }

        });

        return item;

    }

    /**
     * Wrapper class that has to fill the exhibition items
     */
    static class VisitExhibitWrapper {
        ImageView image;
        TextView title;
        TextView shortDesc;
        TextView exhDate;

        Button detailBtn;
        Button objectBtn;
        TextView exhTimestamp;
        RelativeLayout exhTimestampLayout;
    }

}