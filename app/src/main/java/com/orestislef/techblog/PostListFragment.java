package com.orestislef.techblog;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;

public class PostListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<PostModel> list;
    private ArrayList<PostMedia> mediaList;
    private RecyclerViewAdapter adapter;
    private SwipeRefreshLayout swipeContainer;

    private int category;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);

        category = getArguments().getInt("category");

        recyclerView = view.findViewById(R.id.recycler_view_home);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        loadData();

        adapter = new RecyclerViewAdapter(list, mediaList, getContext());
        recyclerView.setAdapter(adapter);

        swipeContainer = view.findViewById(R.id.swipe_container);
        swipeContainer.setOnRefreshListener(this);

        return view;
    }

    private void getRetrofitData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayApi service = retrofit.create(RetrofitArrayApi.class);
        Call<List<WPPost>> call = service.getPostByCategory(category);

        call.enqueue(new Callback<List<WPPost>>() {
            @Override
            public void onResponse(Call<List<WPPost>> call, Response<List<WPPost>> response) {
                Log.e(TAG, "onResponse: " + response.body());

                adapter.clearModel();

                for (int i = 0; i < response.body().size(); i++) {
                    int mId = response.body().get(i).getId();
                    Log.d(TAG, "onResponseID: " + mId);
                    String mediaUrl = response.body().get(i).getLinks().getWpAttachment().get(0).getHref();
                    String mTitle = response.body().get(i).getTitle().getRendered();
                    String mSubtitle = response.body().get(i).getExcerpt().getRendered();

                    mSubtitle = mSubtitle.replace("<p>", "");
                    mSubtitle = mSubtitle.replace("</p>", "");
                    mSubtitle = mSubtitle.replace("[&hellip;]", "");

                    String mContent = response.body().get(i).getContent().getRendered();


                    Log.d(TAG, "onResponse: " +
                            "\n========================================================================================================================"
                            + "\nid: \t\t" + mId
                            + "\nTitle: \t\t" + mTitle
                            + "\nSubtitle: \t" + mSubtitle
                            + "\nContent: \t\t" + mContent
                            + "\n========================================================================================================================");

                    list.add(new PostModel(PostModel.IMAGE_TYPE, mId, mTitle, mSubtitle, mContent));
                    getRetrofitImage(mediaUrl);
                    saveDataList();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<WPPost>> call, Throwable t) {

            }
        });
    }

    private void getRetrofitImage(final String mediaUrl) {

        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayApi service2 = retrofit2.create(RetrofitArrayApi.class);

        adapter.clearPostMediaList();

        Call<List<WPMediaId>> call2 = service2.getWpAttachment(mediaUrl);
        Log.d(TAG, "getRetrofitImageMediaUrl: " + mediaUrl);
        call2.enqueue(new Callback<List<WPMediaId>>() {
                          @Override
                          public void onResponse(Call<List<WPMediaId>> call, Response<List<WPMediaId>> response) {

                              Log.e(TAG, "onResponse: " + response.body());
                              Log.d(TAG, "onResponse: mediaUrl: " + mediaUrl);

                              if (response.body().size() != 0) {
                                  String mediaUrl = response.body().get(0).getLink();
                                  mediaList.add(new PostMedia(mediaUrl));
                                  saveDataImageList();
                                  Log.d(TAG, "onResponseImage: " + "\n******************************" + "\n\t" + mediaUrl + "\n******************************");
                              } else {
                                  String mediaUrl = null;
//                                  String mediaUrl = "https://i2.wp.com/frankmedilink.in/wp-content/uploads/2017/02/no-preview-big1.jpg";
                                  mediaList.add(new PostMedia(mediaUrl));
                                  saveDataImageList();
                                  Log.d(TAG, "onResponseImage: " + "\n******************************" + "\n\t" + mediaUrl + "\n******************************");
                              }
                              adapter.notifyDataSetChanged();
                          }

                          @Override
                          public void onFailure(Call<List<WPMediaId>> call, Throwable t) {
                          }
                      }
        );
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Stop animation (This will be after 1 seconds)

                list.clear();
                adapter.clearModel();

                mediaList.clear();
                adapter.clearPostMediaList();

                getRetrofitData();
                swipeContainer.setRefreshing(false);
            }
        }, 1000); // Delay in millis
    }

    private void saveDataList() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(category + "SHARED_PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(category + "TASK_LIST", json);

        editor.apply();
    }

    private void saveDataImageList() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(category + "SHARED_PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson2 = new Gson();
        String json2 = gson2.toJson(mediaList);
        editor.putString(category + "TASK_IMAGE_LIST", json2);

        editor.apply();
    }


    private void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(category + "SHARED_PREFERENCES", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(category + "TASK_LIST", null);
        Type type = new TypeToken<ArrayList<PostModel>>() {
        }.getType();
        list = gson.fromJson(json, type);

        Gson gson2 = new Gson();
        String json2 = sharedPreferences.getString(category + "TASK_IMAGE_LIST", null);
        Type type2 = new TypeToken<ArrayList<PostMedia>>() {
        }.getType();
        mediaList = gson2.fromJson(json2, type2);

        if (list == null) {
            list = new ArrayList<PostModel>();
        }
        if (mediaList == null) {
            mediaList = new ArrayList<PostMedia>();
        }
        if (json == null || json2 == null) {
            getRetrofitData();
        }
    }
}
