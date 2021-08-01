package com.openclassrooms.oc_p7.views.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.callbacks.OnWorkmateClickListener;
import com.openclassrooms.oc_p7.databinding.ItemLayoutWorkmatesBinding;
import com.openclassrooms.oc_p7.models.Workmate;

import java.util.List;

public class WorkmateAdapter extends RecyclerView.Adapter<WorkmateViewHolder.ViewHolder> {

    private List<Workmate> workmateList;
    private Boolean inDetails;
    private final OnWorkmateClickListener onWorkmateClickListener;


    public WorkmateAdapter(List<Workmate> workmateList, Boolean inDetails, OnWorkmateClickListener onWorkmateClickListener) {
        this.workmateList = workmateList;
        this.inDetails = inDetails;
        this.onWorkmateClickListener = onWorkmateClickListener;
    }

    @NonNull
    @Override
    public WorkmateViewHolder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLayoutWorkmatesBinding itemLayoutWorkmatesBinding = ItemLayoutWorkmatesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WorkmateViewHolder.ViewHolder(itemLayoutWorkmatesBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmateViewHolder.ViewHolder holder, int position) {
        Workmate workmate = workmateList.get(position);
        if (workmate.getRestaurantName() != null) {
            if (inDetails) {
                holder.itemLayoutWorkmatesBinding.workmateText.setText(holder.itemView.getContext().getString(R.string.workmate_text_details, workmate.getName()));

            } else {
                holder.itemLayoutWorkmatesBinding.workmateText.setText(holder.itemView.getContext().getString(R.string.workmate_text, workmate.getName(), workmate.getRestaurantName()));
            }
        } else {
            holder.itemLayoutWorkmatesBinding.workmateText.setText(holder.itemView.getContext().getString(R.string.workmate_text_no_lunch, workmate.getName()));
            TextViewCompat.setTextAppearance(holder.itemLayoutWorkmatesBinding.workmateText, R.style.workmate_list_text_no_lunch);
        }

        if (workmate.getPicUrl() != null) {
            Glide.with(holder.itemView.getContext())
                    .load(workmate.getPicUrl())
                    .centerCrop()
                    .into(holder.itemLayoutWorkmatesBinding.workmatePic);
        }

        holder.itemLayoutWorkmatesBinding.getRoot().setOnClickListener(v -> onWorkmateClickListener.onWorkmateClick(workmate));


    }


    @Override
    public int getItemCount() {
        return workmateList.size();
    }
}
