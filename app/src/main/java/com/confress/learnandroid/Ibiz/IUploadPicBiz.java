package com.confress.learnandroid.Ibiz;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * Created by admin on 2016/5/17.
 */
public interface IUploadPicBiz {
    @Multipart
    @POST("/TianMen/UploadFile.servlet")
    Observable<ResponseBody> uploadpic(@Part MultipartBody.Part file);



    @Multipart
    @POST("/TianMen/UploadFile.servlet")
    Observable<ResponseBody> uploadpics(@PartMap Map<String, RequestBody> params);


}
