package com.xxy.bx.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.box.lib.http.HttpBase;
import com.box.lib.mvp.view.BaseActivity;
import com.jakewharton.rxbinding2.view.RxView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xxy.bx.R;
import com.xxy.bx.component.DaggerViewComponent;
import com.xxy.bx.service.TextService;
import com.xxy.bx.view.iv.MView;
import com.xxy.bx.view.presenter.MainPresenter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MainActivity extends BaseActivity implements MView {
    @Inject
    MainPresenter presenter;
    @BindView(R.id.text)
    Button text;
    @BindView(R.id.start)
    Button start;
    @BindView(R.id.stop)
    Button stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        DaggerViewComponent.builder().lifecycleComponent(getLifecycleComponet()).build().inject(this);
        presenter.setView(this);
        initEvent();
    }

    private void initEvent() {
        RxView.clicks(start)
                .throttleFirst(600, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
//                    presenter.start();
                    down();
                });
        RxView.clicks(stop)
                .throttleFirst(600, TimeUnit.MILLISECONDS)
                .subscribe(o -> presenter.stop());
        RxView.clicks(text)
                .throttleFirst(600, TimeUnit.MILLISECONDS)
                .compose(new RxPermissions(getActivity()).ensure(Manifest.permission.CAMERA))
                .subscribe(o -> {
                    if (o)
                        navigation();
                    else
                        Log.e("--", "被拒绝");
                });
    }

    private void down() {
        HttpBase.getInstance().createApi(TextService.class)
                .downloadFile("/uploads/blog/201309/16/20130916142913_tkxNZ.jpeg", "down_1", "up_2","12","123","1234","12345","123456","1234567")
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .map(new Function<ResponseBody, Boolean>() {
                    @Override
                    public Boolean apply(ResponseBody responseBody) throws Exception {
//                        Log.e("---", ":" + new Gson().toJson(responseBody));
                        return writeFileToSD("1235689.jpg", responseBody);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean)
                            Toast.makeText(MainActivity.this, "pic download finish", Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e("aa", "------------------");
                    }
                });
    }

    private void navigation() {
        Postcard postcard = ARouter.getInstance().build("/more/main");
        Bundle bundle = postcard.getExtras();
        bundle.putString("key1", "Value1");
//                postcard.navigation();
        postcard.withFlags(3).navigation(this, 5, new NavigationCallback() {
            @Override
            public void onFound(Postcard postcard) {//能找到path，即使被拦截
                Log.e("navigation", "onFound");
            }

            @Override
            public void onLost(Postcard postcard) {//不能找到path
                Log.e("navigation", "onLost");
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 5:
                if (data == null) return;
                showToast(data.getStringExtra("result1"));
                break;
        }
    }


    private static boolean writeFileToSD(String pathName, ResponseBody responseBody) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            throw new RuntimeException("SD card is not avaiable/writeable right now.");
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), pathName);
            //判断目标文件所在的目录是否存在
            if (!file.getParentFile().exists()) {
                //如果目标文件所在的目录不存在，则创建父目录
                System.out.println("目标文件所在目录不存在，准备创建它！");
                if (!file.getParentFile().mkdirs()) {
                    System.out.println("创建目标文件所在目录失败！");
                    return false;
                }
            }
            if (!file.exists())
                file.createNewFile();
            Log.e("File", file.getPath());
            InputStream is = responseBody.byteStream();
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                fos.flush();
            }
            fos.close();
            bis.close();
            is.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
