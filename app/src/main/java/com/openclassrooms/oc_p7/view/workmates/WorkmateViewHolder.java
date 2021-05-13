package com.openclassrooms.oc_p7.view.workmates;

import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.oc_p7.databinding.ItemLayoutWorkmatesBinding;

public class WorkmateViewHolder {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ItemLayoutWorkmatesBinding itemLayoutWorkmatesBinding;

        public ViewHolder(ItemLayoutWorkmatesBinding itemLayoutWorkmatesBinding) {
            super(itemLayoutWorkmatesBinding.getRoot());
            this.itemLayoutWorkmatesBinding = itemLayoutWorkmatesBinding;
        }
    }
}
