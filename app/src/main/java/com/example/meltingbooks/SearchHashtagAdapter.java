package com.example.meltingbooks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchHashtagAdapter extends RecyclerView.Adapter<SearchHashtagAdapter.ViewHolder> {

    private List<Hashtag> hashtagList;

    public SearchHashtagAdapter(List<Hashtag> hashtagList) {
        this.hashtagList = hashtagList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView searchItemText;

        public ViewHolder(View itemView) {
            super(itemView);
            searchItemText = itemView.findViewById(R.id.searchItemText);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_hashtag_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Hashtag hashtag = hashtagList.get(position);
        holder.searchItemText.setText(hashtag.getTag());
    }

    @Override
    public int getItemCount() {
        return hashtagList.size();
    }
}

