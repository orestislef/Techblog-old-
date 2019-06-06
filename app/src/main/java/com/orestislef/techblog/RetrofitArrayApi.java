package com.orestislef.techblog;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RetrofitArrayApi {

    @GET("wp-json/wp/v2/posts/")
    Call<List<WPPost>> getPostInfo();

    @GET("wp-json/wp/v2/post/")
    Call<List<WPPostID>> getPostById(@Query("id") int postId);

    @GET("wp-json/wp/v2/posts/")
    Call<List<WPPost>> getPostPerPage(@Query("per_page") int postPerPage);

    @GET("wp-json/wp/v2/posts/")
    Call<List<WPPost>> getPostByCategory(@Query("categories") int categoryId);

    @GET("wp-json/wp/v2/media/")
    Call<List<WPMediaId>> getMediaById(@Query("parent") int mediaId);

    @GET
    Call<List<WPPostID>> getWpAttachment(@Url String url);
}
