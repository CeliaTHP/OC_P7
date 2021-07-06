package com.openclassrooms.oc_p7.views.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.openclassrooms.oc_p7.BuildConfig;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.databinding.ActivityDetailsBinding;
import com.openclassrooms.oc_p7.databinding.SliderViewBinding;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderViewHolder.ViewHolder> {

    private static final String TAG = "SliderAdapter";
    private List<String> picUrlList = new ArrayList<>();

    public void renewItems(List<String> sliderItems) {
        this.picUrlList = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.picUrlList.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(String sliderItem) {
        this.picUrlList.add(sliderItem);
        notifyDataSetChanged();
    }

    @Override
    public SliderViewHolder.ViewHolder onCreateViewHolder(ViewGroup parent) {
        SliderViewBinding sliderViewBinding = SliderViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        ActivityDetailsBinding activityDetailsBinding = ActivityDetailsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SliderViewHolder.ViewHolder(sliderViewBinding, activityDetailsBinding);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder.ViewHolder viewHolder, int position) {
        String picUrl = null;
        if (picUrlList.get(position) != null) {
            picUrl = viewHolder.itemView.getContext().getString(R.string.place_photo_url, BuildConfig.GoogleMapApiKey, picUrlList.get(position));
        }
        Glide.with(viewHolder.itemView)
                .load(picUrl)
                .placeholder(R.drawable.restaurant)
                .override(500, 500)
                .into(viewHolder.sliderViewBinding.sliderPic);
    }

    @Override
    public int getCount() {
        return picUrlList.size();
    }
}