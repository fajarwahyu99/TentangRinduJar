package com.example.infolabsolution.databasemoviejar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    ArrayList<String> mReviews;
    Context mContext;

    public ReviewAdapter(Context context, ArrayList<String> reviews) {
        mContext = context;
        mReviews = reviews;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout itemView = (LinearLayout) LayoutInflater.from(mContext)
                .inflate(R.layout.review_list_item, null, false);
        ReviewAdapter.ReviewViewHolder reviewViewHolder = new ReviewAdapter.ReviewViewHolder(itemView);
        return reviewViewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        String reviewContent = mReviews.get(position);
        if (reviewContent.length() != 0) {
            holder.mReviewTextView.setText(reviewContent);
        } else {
            holder.mReviewTextView.setText(mContext.getResources().getText(R.string.empty_review_content));
        }
    }

    @Override
    public int getItemCount() {
        if (mReviews != null) {
            return mReviews.size();
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
