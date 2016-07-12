package it.sapienza.pervasivesystems.smartmuseum.model.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.ExhibitBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.WorkofartBusiness;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.VisitWorkofartModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.WorkofartModel;
import it.sapienza.pervasivesystems.smartmuseum.view.DetailOfObjectActivity;
import it.sapienza.pervasivesystems.smartmuseum.view.Utilities;

/**
 * Created by Guamaral on 5/9/2016.
 */
public class WorkOfArtModelAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<WorkofartModel> workofartModels = new ArrayList<WorkofartModel>();
    private LayoutInflater inflater;

    public WorkOfArtModelAdapter(Context c, ArrayList<WorkofartModel> workofartModels) {
        mContext = c;
        this.workofartModels = workofartModels;
        inflater = LayoutInflater.from(c);
    }

    public int getCount() {
        return workofartModels.size();
    }

    public Object getItem(int position) {
        return workofartModels.get(position);
    }

    public long getItemId(int position) {
        return workofartModels.get(position).getIdWork();
    }

    public class ExhibitWrapper {
        TextView obj_title;
        ImageView obj_image;
        ImageView obj_timestamp_logo;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {

        ExhibitWrapper wrapper=new ExhibitWrapper();
        View rowView;

        rowView = inflater.inflate(R.layout.activity_item_of_objects, parent, false);

        wrapper.obj_title=(TextView) rowView.findViewById(R.id.obj_title);
        wrapper.obj_image=(ImageView) rowView.findViewById(R.id.obj_img);
        wrapper.obj_timestamp_logo=(ImageView) rowView.findViewById(R.id.obj_timestamp_logo);

        WorkofartModel work = workofartModels.get(position);

        wrapper.obj_title.setText(Utilities.trimString(work.getTitle(), 30));
        Picasso.with(mContext).load(work.getImage()).into(wrapper.obj_image);

        String key = new WorkofartBusiness().getWorkofartHashmapKey(new ExhibitBusiness().getExhibitHashmapKey(work.getExhibitModel()), work);
        VisitWorkofartModel visitWorkofartModel = SmartMuseumApp.totalVisitedWorksofart.get(key);
        if(visitWorkofartModel != null) {
            wrapper.obj_timestamp_logo.setVisibility(View.VISIBLE);
        } else {
            wrapper.obj_timestamp_logo.setVisibility(View.GONE);
        }


        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailOfObjectActivity.class);

                intent.putExtra("workOfArtModel", workofartModels.get(position));
                mContext.startActivity(intent);
            }
        });

        return rowView;

    }

}