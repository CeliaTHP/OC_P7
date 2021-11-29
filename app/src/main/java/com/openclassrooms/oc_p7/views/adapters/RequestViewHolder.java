package com.openclassrooms.oc_p7.views.adapters;

import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.oc_p7.databinding.ItemLayoutRequestBinding;

public class RequestViewHolder extends RecyclerView.ViewHolder {

    public final ItemLayoutRequestBinding itemLayoutRequestBinding;

    public RequestViewHolder(ItemLayoutRequestBinding itemLayoutRequestBinding) {
        super(itemLayoutRequestBinding.getRoot());
        this.itemLayoutRequestBinding = itemLayoutRequestBinding;
    }
}
