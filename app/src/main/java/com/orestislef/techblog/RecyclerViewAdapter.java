package com.orestislef.techblog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<PostModel> postDataset;
    private ArrayList<PostMedia> imageDataset;
    private Context mContext;

    public void clearModel() {
        int sizeDa = postDataset.size();
        postDataset.clear();
        notifyItemRangeRemoved(0, sizeDa);
    }

    public void clearPostMediaList() {
        int sizeIm = imageDataset.size();
        imageDataset.clear();
        notifyItemRangeRemoved(0, sizeIm);
    }

    public void addAll(ArrayList<PostModel> list, ArrayList<PostMedia> imageList) {
        postDataset.addAll(list);
        imageDataset.addAll(imageList);
    }

    public static class ImageTypeViewHolder extends RecyclerView.ViewHolder {

        TextView title, excerpt;
        ImageView imageView;

        public ImageTypeViewHolder(@NonNull View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.post_title);
            this.excerpt = itemView.findViewById(R.id.post_excerpt);
            this.imageView = itemView.findViewById(R.id.post_img);
        }
    }

    public RecyclerViewAdapter(ArrayList<PostModel> mlist, ArrayList<PostMedia> mMediaList, Context context) {
        this.postDataset = mlist;
        this.imageDataset = mMediaList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_details, viewGroup, false);
        return new ImageTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        super.onBindViewHolder(holder, position, payloads);

         final PostModel object = postDataset.get(position);
         final PostMedia object2 = imageDataset.get(position);

        String postMediaUrl = object2.PostMediaUrl;

        if (postMediaUrl == "NOIMAGE") {
            ((ImageTypeViewHolder) holder).imageView.setVisibility(View.GONE);
        } else {
            ((ImageTypeViewHolder) holder).imageView.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(postMediaUrl)
                    .into(((ImageTypeViewHolder) holder).imageView);
        }

        ((ImageTypeViewHolder) holder).title.setText(object.title);
        ((ImageTypeViewHolder) holder).excerpt.setText(object.excerpt);

        ((ImageTypeViewHolder) holder).imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetailPostFragment(object.title, object.content);
            }
        });
        ((ImageTypeViewHolder) holder).title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetailPostFragment(object.title, object.content);
            }
        });
        ((ImageTypeViewHolder) holder).excerpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetailPostFragment(object.title, object.content);
            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {

    }

    public void startDetailPostFragment(String title, String content) {
        FragmentManager fragmentManager = ((MainActivity) mContext).getSupportFragmentManager();
        PostDetailFragment postDetailFragment = new PostDetailFragment();
        Bundle postDetailFragmentBundle = new Bundle();
        postDetailFragmentBundle.putString("POST_TITLE", title);
        postDetailFragmentBundle.putString("POST_CONTENT", content);
        postDetailFragment.setArguments(postDetailFragmentBundle);
        fragmentManager.beginTransaction().replace(R.id.fragment_container, postDetailFragment).addToBackStack(null).commit();
    }

    @Override
    public int getItemCount() {
        return imageDataset.size();
    }
}
