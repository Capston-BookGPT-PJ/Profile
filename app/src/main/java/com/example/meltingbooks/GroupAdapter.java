package com.example.meltingbooks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.FeedViewHolder> {

    private final List<FeedItem> feedList;
    private final Context context;

    public GroupAdapter(Context context, List<FeedItem> feedList) {
        this.context = context;
        this.feedList = feedList;
    }

    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
        FeedItem item = feedList.get(position);
        holder.userName.setText(item.getUserName());
        holder.reviewContent.setText(item.getReviewContent());
        holder.reviewDate.setText(item.getReviewDate());

        holder.chatButton.setOnClickListener(v -> {
            FeedItem currentItem = feedList.get(holder.getAdapterPosition());

            CommentBottomSheet bottomSheet = CommentBottomSheet.newInstance(currentItem.getPostId(), "group");
            bottomSheet.setOnCommentAddedListener(commentCount -> {
                holder.chatCount.setText(String.valueOf(commentCount));
            });

            bottomSheet.show(((AppCompatActivity)v.getContext()).getSupportFragmentManager(), "CommentBottomSheet");
        });


        // 이미지 표시
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            holder.feedImage.setVisibility(View.VISIBLE);
            Glide.with(context).load(item.getImageUrl()).into(holder.feedImage);
        } else {
            holder.feedImage.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return feedList.size();
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        TextView userName, reviewContent, reviewDate, chatCount;
        ImageView chatButton, feedImage;


        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            reviewContent = itemView.findViewById(R.id.groupWriteContent);
            reviewDate = itemView.findViewById(R.id.groupWriteDate);
            chatButton = itemView.findViewById(R.id.chat_button); // 댓글 버튼
            chatCount = itemView.findViewById(R.id.chat_count); // 댓글 수 표시 텍스트뷰
            feedImage = itemView.findViewById(R.id.groupImage); // 피드에 이미지가 있을경우 사용.
        }
    }
}
