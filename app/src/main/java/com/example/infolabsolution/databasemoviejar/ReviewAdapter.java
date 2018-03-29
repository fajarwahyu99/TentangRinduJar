package com.example.infolabsolution.databasemoviejar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import java.util.ArrayList;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;



public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    ArrayList<String> teksReviews;
    Context movieContext;

    public ReviewAdapter(Context context, ArrayList<String> reviews) {
        movieContext = context;
        teksReviews = reviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout itemView = (LinearLayout) LayoutInflater.from(movieContext)
                .inflate(R.layout.review_list_item, null, false);
        ReviewAdapter.ReviewViewHolder reviewViewHolder = new ReviewAdapter.ReviewViewHolder(itemView);
        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        String reviewContent = teksReviews.get(position);
        if (reviewContent.length() != 0) {
            holder.mReviewTextView.setText(reviewContent);
        } else {
            holder.mReviewTextView.setText(movieContext.getResources().getText(R.string.empty_review_content));
        }
    }

    @Override
    public int getItemCount() {
        if (teksReviews != null) {
            return teksReviews.size();
        } else {
            return 0;
        }
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView mReviewTextView;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            mReviewTextView = (TextView) itemView.findViewById(R.id.review_text_view);
        }
    }
}
