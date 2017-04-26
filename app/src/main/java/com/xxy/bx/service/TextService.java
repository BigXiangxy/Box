package com.xxy.bx.service;

import com.box.lib.http.HttpBase;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public interface TextService {

    @POST("{path}")
    @Streaming
    @FormUrlEncoded
    Observable<ResponseBody> downloadFile(@Path("path") String path,
                                          @Header(HttpBase.PROGRESS_DOWN_TAG) String downTag,
                                          @Header(HttpBase.PROGRESS_UP_TAG) String upTag,
                                          @Field("aaaa1") String a1,
                                          @Field("aaaa2") String a2,
                                          @Field("aaaa3") String a3,
                                          @Field("aaaa4") String a4,
                                          @Field("aaaa5") String a5,
                                          @Field("aaaa6") String a6);


    @POST("{path}")
    @Streaming
    @FormUrlEncoded
    Call<ResponseBody> downloadFile(@Path("path") String path,
                                    @Header(HttpBase.PROGRESS_DOWN_TAG) String downTag,
                                    @Header(HttpBase.PROGRESS_UP_TAG) String upTag,
                                    @Field("aaaa1") String a1,
                                    @Field("aaaa2") String a2,
                                    @Field("aaaa3") String a3,
                                    @Field("aaaa4") String a4,
                                    @Field("aaaa5") String a5);
}
