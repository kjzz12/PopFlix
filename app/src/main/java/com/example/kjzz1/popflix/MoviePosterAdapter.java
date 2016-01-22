package com.example.kjzz1.popflix;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MoviePosterAdapter extends ArrayAdapter<MovieDataTool> {
    private Context mContext;
    private int layoutResourceId;
    private ArrayList<MovieDataTool> mGridData = new ArrayList<>();

    public MoviePosterAdapter(Context mContext, int layoutResourceId, ArrayList<MovieDataTool> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }

    public void setGridData(ArrayList<MovieDataTool> mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = null;
        ViewHolder holder;

        if (convertView==null){
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId,parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MovieDataTool item = mGridData.get(position);
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
