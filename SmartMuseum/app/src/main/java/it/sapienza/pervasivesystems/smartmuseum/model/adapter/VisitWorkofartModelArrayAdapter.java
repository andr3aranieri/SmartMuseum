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
import it.sapienza.pervasivesystems.smartmuseum.model.entity.VisitWorkofartModel;
import it.sapienza.pervasivesystems.smartmuseum.view.DetailOfExhibitActivity;
import it.sapienza.pervasivesystems.smartmuseum.view.DetailOfObjectActivity;
import it.sapienza.pervasivesystems.smartmuseum.view.Utilities;

/**
 * Created by andrearanieri on 11/07/16.
 */
public class VisitWorkofartModelArrayAdapter extends ArrayAdapter<VisitWorkofartModel> {

    Context context;
    int layoutResourceId;
    List<VisitWorkofartModel> visitsArray = new ArrayList<VisitWorkofartModel>();

    public VisitWorkofartModelArrayAdapter(Context context, int layoutResourceId, List<VisitWorkofartModel> visits) {
        super(context, layoutResourceId, visits);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.visitsArray = visits;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View item = convertView;
        VisitWorkofartWrapper workofartWrapper = null;

        if (item == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            item = inflater.inflate(layoutResourceId, parent, false);

            workofartWrapper = new VisitWorkofartWrapper();
            workofartWrapper.image = (ImageView) item.findViewById(R.id.woa_image);
            workofartWrapper.title = (TextView) item.findViewById(R.id.woa_title);
            workofartWrapper.shortDesc = (TextView) item.findViewById(R.id.woa_short_desc);
            workofartWrapper.exhDescription = (TextView) item.findViewById(R.id.exh_description);
            workofartWrapper.exhTimestamp = (TextView) item.findViewById(R.id.woa_timestamp);
            workofartWrapper.exhTimestampLayout = (RelativeLayout) item.findViewById(R.id.exh_timestamp_layout);
            workofartWrapper.detailBtn = (Button) item.findViewById(R.id.detailBtn);
            workofartWrapper.exhibitBtn = (Button) item.findViewById(R.id.exhibitBtn);

            item.setTag(workofartWrapper);

        } else {
            workofartWrapper = (VisitWorkofartWrapper) item.getTag();
        }

        final VisitWorkofartModel visitWorkofartModel = visitsArray.get(position);

        Picasso.with(context).load(visitWorkofartModel.getWorkofartModel().getImage()).into(workofartWrapper.image);
        workofartWrapper.title.setText(visitWorkofartModel.getWorkofartModel().getTitle());
        workofartWrapper.shortDesc.setText(visitWorkofartModel.getWorkofartModel().getShortDescription());
        workofartWrapper.exhDescription.setText(visitWorkofartModel.getWorkofartModel().getExhibitModel().getTitle());
        workofartWrapper.exhTimestampLayout.setVisibility(View.VISIBLE);
        workofartWrapper.exhTimestamp.setText(new Utilities().formatDateToString(visitWorkofartModel.getTimestamp(), "dd/MM/yyyy HH:mm"));

        workofartWrapper.detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmartMuseumApp.saveVisit = false;
                Intent intent = new Intent(context, DetailOfObjectActivity.class);
                intent.putExtra("workOfArtModel", visitsArray.get(position).getWorkofartModel());
                intent.putExtra("visitTimestamp", new Utilities().formatDateToString(visitsArray.get(position).getTimestamp(), "dd/MM/yyyy HH:mm"));
                context.startActivity(intent);
            }

        });

        workofartWrapper.exhibitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailOfExhibitActivity.class);
                intent.putExtra("exhibitModel", visitsArray.get(position).getWorkofartModel().getExhibitModel());
                context.startActivity(intent);
            }

        });

        return item;

    }

    /**
     * Wrapper class that has to fill the exhibition items
     */
    static class VisitWorkofartWrapper {
        ImageView image;
        TextView title;
        TextView shortDesc;
        TextView exhDescription;

        Button detailBtn;
        Button exhibitBtn;
        TextView exhTimestamp;
        RelativeLayout exhTimestampLayout;
    }

}
