package com.orestislef.techblog;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class PostDetailFragment extends Fragment {

    private TextView titleTV, contentTV;
    private String title, content;


    public PostDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);

        title = getArguments().getString("POST_TITLE");
        content = getArguments().getString("POST_CONTENT");

        titleTV = view.findViewById(R.id.post_det_title);
        contentTV = view.findViewById(R.id.post_det_content);

        titleTV.setText(title);
        contentTV.setText(Html.fromHtml(content));
        return view;
    }

}
