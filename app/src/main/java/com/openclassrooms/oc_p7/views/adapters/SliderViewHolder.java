package com.openclassrooms.oc_p7.views.adapters;

import com.openclassrooms.oc_p7.databinding.SliderViewBinding;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderViewHolder {

    public static class ViewHolder extends SliderViewAdapter.ViewHolder {

        public final SliderViewBinding sliderViewBinding;

        public ViewHolder(SliderViewBinding sliderViewBinding) {
            super(sliderViewBinding.getRoot());
            this.sliderViewBinding = sliderViewBinding;
        }
    }
}
