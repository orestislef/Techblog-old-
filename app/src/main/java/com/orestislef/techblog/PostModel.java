package com.orestislef.techblog;

public class PostModel {

    public static final int IMAGE_TYPE = 1;
    public int id;
    public String mediaUrl,title, excerpt, content;
    public int type ;

    public PostModel(int mtype, String mmediaUrl, int mid, String mtitle, String mexcerpt, String mContent){
        this.type = mtype;
        this.id = mid;
        this.mediaUrl = mmediaUrl;
        this.title = mtitle;
        this.excerpt = mexcerpt;
        this.content = mContent;
    }
}
