package com.openclassrooms.oc_p7.views.adapters;

import com.openclassrooms.oc_p7.databinding.ActivityDetailsBinding;
import com.openclassrooms.oc_p7.databinding.SliderViewBinding;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderViewHolder extends SliderViewAdapter.ViewHolder {

    public final SliderViewBinding sliderViewBinding;
    public final ActivityDetailsBinding activityDetailsBinding;

    public SliderViewHolder(SliderViewBinding sliderViewBinding, ActivityDetailsBinding activityDetailsBinding) {
        super(sliderViewBinding.getRoot());
        this.sliderViewBinding = sliderViewBinding;
        this.activityDetailsBinding = activityDetailsBinding;

    }
}
