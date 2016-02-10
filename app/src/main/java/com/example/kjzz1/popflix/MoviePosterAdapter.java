package com.example.kjzz1.popflix;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.view.ViewGroup.*;


public class MoviePosterAdapter extends ArrayAdapter<MovieData> {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<MovieData> mGridData = new ArrayList<>();

    public MoviePosterAdapter(Context mContext, int layoutResourceId, ArrayList<MovieData> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }

    public void setGridData(ArrayList<MovieData> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        MovieData item = mGridData.get(position);

        if (convertView==null){
            //poster dimensions set here
            Integer posterHeight = item.getPosterHeight();
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);

            ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
            final LayoutParams params = imageView.getLayoutParams();

            params.height = posterHeight;

            holder = new ViewHolder();
            holder.imageView = imageView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Picasso
                .with(mContext)
                .load(item.getImage())
                .fit()
                .centerInside()
                .placeholder(R.drawable.blankposter)
                .into(holder.imageView);
        return convertView;

    }

    static class ViewHolder {
        ImageView imageView;
    }
}
