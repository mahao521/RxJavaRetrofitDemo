package com.confress.learnandroid.Ibiz;

import com.confress.learnandroid.bean.LoginInfo;

import org.json.JSONObject;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by admin on 2016/5/17.
 */
public interface IloginBiz {

    @GET("/TianMen/LoginAction!defaultMethod.action")
    Observable<LoginInfo> loginUser(@Query("username") String username,@Query("password") String password);


    @GET("/TianMen/LoginAction!defaultMethod.action")
    Observable<JSONObject> loginUser1(@Query("username") String username,@Query("password") String password);



}
