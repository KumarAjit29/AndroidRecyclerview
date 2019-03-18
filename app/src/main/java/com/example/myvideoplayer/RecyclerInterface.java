package com.example.myvideoplayer;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecyclerInterface {

    //String JSONURL = "https://api.myjson.com/bins/";

   // @GET("14qq4q")
    String XmlURl="http://sample-firetv-web-app.s3-website-us-west-2.amazonaws.com/";
    @GET("feed_firetv.xml")
    Call<String> getString();
}
