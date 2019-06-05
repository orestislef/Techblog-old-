package com.orestislef.techblog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.manager.SupportRequestManagerFragment;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private ArrayList<PostModel> dataset;
    private ArrayList<PostMedia> postMedia;
    private Context mContext;

    public void clearModel() {
        final int sizeDa = dataset.size();
        dataset.clear();

        notifyItemRangeRemoved(0, sizeDa);
    }

    public void clearPostMediaList() {
        final int sizeIm = postMedia.size();
        postMedia.clear();
        notifyItemRangeRemoved(0, sizeIm);
    }

    public void addAll(ArrayList<PostModel> list, ArrayList<PostMedia> mediaList) {
        dataset.addAll(list);
        mediaList.addAll(mediaList);
        notifyDataSetChanged();
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

    public RecyclerViewAdapter(ArrayList<PostModel> mlist, ArrayList<PostMedia> mPostMedia, Context context) {
        this.dataset = mlist;
        this.postMedia = mPostMedia;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_details, viewGroup, false);
        return new ImageTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {

        final PostModel object = dataset.get(i);

        ((ImageTypeViewHolder) viewHolder).title.setText(object.title);
        ((ImageTypeViewHolder) viewHolder).excerpt.setText(object.excerpt);


        final PostMedia object2 = postMedia.get(i);
        String postMediaUrl = object2.PostMediaUrl;
        Glide.with(mContext)
                .load(postMediaUrl)
                .into(((ImageTypeViewHolder) viewHolder).imageView);

        ((ImageTypeViewHolder)viewHolder).imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startDetailPostFragment(object.title,object.content);
            }
        });
        ((ImageTypeViewHolder)viewHolder).title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetailPostFragment(object.title,object.content);
            }
        });
        ((ImageTypeViewHolder)viewHolder).excerpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDetailPostFragment(object.title,object.content);
            }
        });
    }

    public void startDetailPostFragment(String title, String content){
        FragmentManager fragmentManager = ((MainActivity)mContext).getSupportFragmentManager();
        PostDetailFragment postDetailFragment = new PostDetailFragment();
        Bundle postDetailFragmentBundle= new Bundle();
        postDetailFragmentBundle.putString("POST_TITLE",title);
        postDetailFragmentBundle.putString("POST_CONTENT",content);
        postDetailFragment.setArguments(postDetailFragmentBundle);
        fragmentManager.beginTransaction().replace(R.id.fragment_container,postDetailFragment).addToBackStack(null).commit();
    }

    @Override
    public int getItemCount() {
        return postMedia.size();
    }
}
