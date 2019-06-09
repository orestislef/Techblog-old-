package com.orestislef.techblog;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
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
import android.widget.AbsListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
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

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<PostModel> list;
    private ArrayList<PostMedia> mediaList;
    private RecyclerViewAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    private boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems;
    public Parcelable recyclerViewState;

    private int postsPerPage = 10;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_home);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        loadData();

        adapter = new RecyclerViewAdapter(list, mediaList, getContext());
        recyclerView.setAdapter(adapter);

        swipeContainer = view.findViewById(R.id.swipe_container);
        swipeContainer.setOnRefreshListener(this);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = mLayoutManager.getChildCount();
                totalItems = mLayoutManager.getItemCount();
                scrollOutItems = mLayoutManager.findFirstVisibleItemPosition();
                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    postsPerPage = postsPerPage + 10;
                    startAsyncDataTask(postsPerPage);

                }
                recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
            }
        });

        return view;
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
//
//                list.clear();
//                adapter.clearModel();
//
//                mediaList.clear();
//                adapter.clearPostMediaList();
                startAsyncDataTask(postsPerPage);
//                getRetrofitData();
//                swipeContainer.setRefreshing(false);
            }
        }, 1000); // Delay in millis
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("HOME_SHARED_PREFERENCES", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("HOME_TASK_LIST", null);
        Type type = new TypeToken<ArrayList<PostModel>>() {
        }.getType();
        list = gson.fromJson(json, type);

        Gson gson2 = new Gson();
        String json2 = sharedPreferences.getString("HOME_TASK_IMAGE_LIST", null);
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
            startAsyncDataTask(postsPerPage);
        }
    }

    private void startAsyncDataTask(final Integer postsPerPage) {
        final getRetrofitDataAsyncTask task = new getRetrofitDataAsyncTask(this);
        task.execute(postsPerPage);
    }

    private static class getRetrofitDataAsyncTask extends AsyncTask<Integer, Void, Boolean> {
        private WeakReference<HomeFragment> fragmentWeakReference;

        private boolean isComplete = false;

        getRetrofitDataAsyncTask(HomeFragment homeFragment) {
            fragmentWeakReference = new WeakReference<HomeFragment>(homeFragment);
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {

            final HomeFragment homeFragment = fragmentWeakReference.get();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(homeFragment.getResources().getString(R.string.base_url))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RetrofitArrayApi service = retrofit.create(RetrofitArrayApi.class);
            Call<List<WPPost>> call = service.getPostPerPage(integers[0]);

            call.enqueue(new Callback<List<WPPost>>() {
                @Override
                public void onResponse(Call<List<WPPost>> call, Response<List<WPPost>> response) {
                    Log.e(TAG, "onResponse: " + response.body());

                    homeFragment.adapter.clearModel();

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

                        Log.d(TAG, "onResponse: "
                                + "\n================================================================================================================================================================================================================================================"
                                + "\nid: \t\t" + mId
                                + "\nTitle: \t\t" + mTitle
                                + "\nSubtitle: \t" + mSubtitle
                                + "\nContent: \t\t" + mContent
                                + "\n================================================================================================================================================================================================================================================");

                        homeFragment.list.add(new PostModel(PostModel.IMAGE_TYPE, mediaUrl, mId, mTitle, mSubtitle, mContent));
                        homeFragment.getRetrofitImage(mediaUrl);
                        homeFragment.saveDataList();
                    }
                    isComplete = true;
                    homeFragment.adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<List<WPPost>> call, Throwable t) {

                }
            });
            return isComplete;
        }

        @Override
        protected void onPreExecute() {
            HomeFragment homeFragment = fragmentWeakReference.get();
            if (homeFragment == null || homeFragment.isDetached()) {
                return;
            }
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean isComplete) {
            super.onPostExecute(isComplete);
            HomeFragment homeFragment = fragmentWeakReference.get();
            if (homeFragment == null || homeFragment.isDetached()) {
                return;
            }
            homeFragment.swipeContainer.setRefreshing(false);
        }
    }

    private void getRetrofitImage(final String mediaUrl) {

        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayApi service2 = retrofit2.create(RetrofitArrayApi.class);

        adapter.clearPostMediaList();

        Call<List<WPPostID>> call2 = service2.getWpAttachment(mediaUrl);
        Log.d(TAG, "getRetrofitImageMediaUrl: " + mediaUrl);
        call2.enqueue(new Callback<List<WPPostID>>() {
                          @Override
                          public void onResponse(Call<List<WPPostID>> call, Response<List<WPPostID>> response) {

                              Log.e(TAG, "onResponse: " + response.body());
                              Log.d(TAG, "onResponse: mediaUrl: " + mediaUrl);

                              if (response.body().size() != 0) {
                                  String mediaUrl = response.body().get(0).getMediaDetails().getSizes().getThumbnail().getSourceUrl();
                                  mediaList.add(new PostMedia(mediaUrl));
                                  saveDataImageList();
                                  Log.d(TAG, "onResponseImage: " + "\n******************************" + "\n\t with media " + mediaUrl + "\n******************************");
                              } else {
                                  String mediaUrl = "NOIMAGE";
                                  mediaList.add(new PostMedia(mediaUrl));
                                  saveDataImageList();
                                  Log.d(TAG, "onResponseImage: " + "\n******************************" + "\n\t null media\n" + mediaUrl + "\n******************************");
                              }
                              adapter.notifyDataSetChanged();
                          }

                          @Override
                          public void onFailure(Call<List<WPPostID>> call, Throwable t) {
                          }
                      }
        );
    }

    public void saveDataList() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("HOME_SHARED_PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("HOME_TASK_LIST", json);

        editor.apply();
    }

    public void saveDataImageList() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("HOME_SHARED_PREFERENCES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson2 = new Gson();
        String json2 = gson2.toJson(mediaList);
        editor.putString("HOME_TASK_IMAGE_LIST", json2);

        editor.apply();
    }
}
