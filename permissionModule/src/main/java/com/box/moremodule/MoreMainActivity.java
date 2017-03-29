package com.box.moremodule;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.box.lib.loction.SayHelloService;
import com.box.lib.mvp.view.BaseActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * Created by Administrator on 2017/3/10 0010.
 */
@Route(path = "/more/main", extras = (1 << 0) + (1 << 4) + (1 << 31) + (1 << 30) + (1 << 29))
public class MoreMainActivity extends BaseActivity {
    TextView text;
    @Autowired(name = "/service/sayhello")
    SayHelloService sayHelloService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_activity_main);
        ARouter.getInstance().inject(this);
        text = (TextView) findViewById(R.id.text);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            text.setText(bundle.getString("key1") + bundle.getString("extra") + "  " + getIntent().getFlags() + "   " + sayHelloService.say("LILI"));
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance().build("/map/main").navigation();
                }
            });
        }

        new RxPermissions(this).request(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        Log.e("--","----------->"+aBoolean);
                        if (aBoolean){
                        }else {
                            if (! ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                Log.e("--","---------不再询问-->"+aBoolean);
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, 12);
                            }
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_CANCELED){
            new RxPermissions(this).request(Manifest.permission.CAMERA)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            Log.e("--","----------->"+aBoolean);
                            if (aBoolean){
                            }else {
                                if (! ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                    Log.e("--","---------不再询问-->"+aBoolean);
                                }
                            }
                        }
                    });
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("result1", "resultValue");
        setResult(14, intent);
        super.finish();
    }
}
