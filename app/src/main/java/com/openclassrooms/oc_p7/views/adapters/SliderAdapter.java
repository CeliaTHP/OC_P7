package com.openclassrooms.oc_p7.views.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.openclassrooms.oc_p7.BuildConfig;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.databinding.SliderViewBinding;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderViewHolder.ViewHolder> {

    private List<String> picUrlList = new ArrayList<>();
    private Boolean isEmpty;

    public SliderAdapter(Boolean isEmpty) {
        this.isEmpty = isEmpty;

    }

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
        return new SliderViewHolder.ViewHolder(sliderViewBinding);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder.ViewHolder viewHolder, int position) {
        String picUrl = picUrlList.get(position);

        if (!isEmpty) {
            Glide.with(viewHolder.itemView)
                    .load(viewHolder.itemView.getContext().getString(R.string.place_photo_url, BuildConfig.GoogleMapApiKey, picUrl))
                    .centerCrop()
                    .into(viewHolder.sliderViewBinding.sliderPic);
        } else {
            viewHolder.sliderViewBinding.sliderPic.setImageResource(R.drawable.ic_cutlery);
        }
    }

    @Override
    public int getCount() {
        return picUrlList.size();
    }
}