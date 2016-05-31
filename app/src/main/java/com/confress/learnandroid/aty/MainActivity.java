package com.confress.learnandroid.aty;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.confress.learnandroid.Ibiz.IUploadPicBiz;
import com.confress.learnandroid.R;
import com.confress.learnandroid.utils.CreatePhoto;
import com.confress.learnandroid.utils.PictureUtils;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    protected static final int CAMERA_REQUEST_CODE1 = 1;
    File createFile1;
    private CreatePhoto createPhoto1;
    private PictureUtils pictureUtils;
    private String image1_path = "";




    @Bind(R.id.iv_pic)
    ImageView ivPic;
    @Bind(R.id.btn_pic)
    Button btnPic;
    @Bind(R.id.btn_upload)
    Button btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);
        pictureUtils = new PictureUtils();

        createPhoto1 = new CreatePhoto("image", "InspectionMianProject");
        createFile1 = createPhoto1.createFile();
        image1_path=createPhoto1.getImagePath();

        btnPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri1 = Uri.fromFile(createFile1);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri1);
                startActivityForResult(intent, CAMERA_REQUEST_CODE1);
            }
        });



        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit=new Retrofit.Builder().baseUrl("http://192.168.1.105:8090")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build();

                IUploadPicBiz uploadPicBiz=retrofit.create(IUploadPicBiz.class);
                File file1=new File(Environment.getExternalStorageDirectory()+"/bb/a1.png");
                File file2=new File(image1_path);
                RequestBody requestFile1 =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file1);

                RequestBody requestFile2 =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file2);


                //包装RequestBody，在其内部实现上传进度监听
//                CountingRequestBody countingRequestBody=new CountingRequestBody(requestFile1, new CountingRequestBody.Listener() {
//                    @Override
//                    public void onRequestProgress(long bytesWritten, long contentLength) {
//                        Log.e(TAG, contentLength + ":" + bytesWritten);
//                    }
//                });



                //多文件上传
                Map<String,RequestBody> params = new LinkedHashMap<String, RequestBody>();

                params.put("file\";filename=\""+file1.getName(),requestFile1);
                params.put("file\";filename=\""+file2.getName(),requestFile2);

                uploadPicBiz.uploadpics(params)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<ResponseBody>() {
                            @Override
                            public void onCompleted() {
                                Log.i(TAG, "onCompleted: ");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.i(TAG, "onError: ");
                            }

                            @Override
                            public void onNext(ResponseBody responseBody) {
                                Log.i(TAG, "onNext: ");
                            }
                        });




                  //单文件上传
//                MultipartBody.Part body =
//                        MultipartBody.Part.createFormData("file", file2.getName(), countingRequestBody);
//                uploadPicBiz.uploadpic(body)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Subscriber<ResponseBody>() {
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
//                            public void onNext(ResponseBody responseBody) {
//                                Log.i(TAG, "onNext: ");
//                            }
//                        });
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE1 && resultCode == RESULT_OK) {
            if (image1_path == null) {
                return;
            }
            Uri uri1 = pictureUtils.compressImageFromFile(image1_path);
            if (uri1 != null && image1_path != null) {
                //加载图片
                ivPic.setImageURI(uri1);
            } else {
                ivPic.setImageResource(R.mipmap.ic_launcher);
            }
        }
    }
}
