package com.openclassrooms.oc_p7.view.home.workmates;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.databinding.ItemLayoutWorkmatesBinding;
import com.openclassrooms.oc_p7.model.User;

import java.util.List;

public class WorkmateAdapter extends RecyclerView.Adapter<WorkmateViewHolder.ViewHolder> {

    private List<User> workmateList;

    public WorkmateAdapter(List<User> workmateList) {
        this.workmateList = workmateList;
    }

    @NonNull
    @Override
    public WorkmateViewHolder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLayoutWorkmatesBinding itemLayoutWorkmatesBinding = ItemLayoutWorkmatesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new WorkmateViewHolder.ViewHolder(itemLayoutWorkmatesBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmateViewHolder.ViewHolder holder, int position) {
        User workmate = workmateList.get(position);
        holder.itemLayoutWorkmatesBinding.workmateText.setText(holder.itemView.getContext().getString(R.string.workmate_text, workmate.getName(), workmate.getLunch(), "Etoile dorée"));
        Glide.with(holder.itemView.getContext())
                .load(workmate.getPic())
                .centerCrop()
                .into(holder.itemLayoutWorkmatesBinding.workmatePic);

    }

    @Override
    public int getItemCount() {
        return workmateList.size();
    }
}
