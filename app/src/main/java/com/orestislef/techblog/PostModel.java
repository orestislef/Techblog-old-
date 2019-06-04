package com.orestislef.techblog;

public class PostModel {

    public static final int IMAGE_TYPE = 1;
    public int id;
    public String title, excerpt, content;
    public int type ;

    public PostModel(int mtype, int mid, String mtitle, String mexcerpt, String mContent){
        this.type = mtype;
        this.id = mid;
        this.title = mtitle;
        this.excerpt = mexcerpt;
        this.content = mContent;
    }
}
