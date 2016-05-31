package com.confress.learnandroid.aty;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.confress.learnandroid.Ibiz.IloginBiz;
import com.confress.learnandroid.R;
import com.confress.learnandroid.converter.JsonConverterFactory;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by admin on 2016/5/17.
 */
public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    @Bind(R.id.username)
    EditText username;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        ButterKnife.bind(this);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit=new Retrofit.Builder().baseUrl("http://192.168.1.105:8090")
//                      .addConverterFactory(GsonConverterFactory.create())         //gson转换工场
                        .addConverterFactory(JsonConverterFactory.create())         //自定义的json转换工场
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();

                //gson解析对象返回
//
//                IloginBiz loginBiz=retrofit.create(IloginBiz.class);
//                loginBiz.loginUser(username.getText().toString(),password.getText().toString())
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Subscriber<LoginInfo>() {
//                            @Override
//                            public void onCompleted() {
//                                Log.i(TAG, "onCompleted: ");
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                Log.i(TAG, "onError: ");
//                            }
//
//                            @Override
//                            public void onNext(LoginInfo loginInfo) {
//                                Log.i(TAG, "onNext: "+loginInfo.getUser().getAccount());
//                            }
//                        });


                //返回原始的json对象
                IloginBiz loginBiz=retrofit.create(IloginBiz.class);
                loginBiz.loginUser1(username.getText().toString(),password.getText().toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<JSONObject>() {
                            @Override
                            public void onCompleted() {
                                Log.i(TAG, "onCompleted: ");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.i(TAG, "onError: ");
                            }

                            @Override
                            public void onNext(JSONObject s) {
                                Log.i(TAG, "onNext: "+s.toString());
                            }
                        });



            }
        });
    }
}
