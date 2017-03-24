package com.xxy.bx.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.box.lib.mvp.view.BaseActivity;
import com.jakewharton.rxbinding2.view.RxView;
import com.xxy.bx.R;
import com.xxy.bx.component.DaggerViewComponent;
import com.xxy.bx.view.iv.MView;
import com.xxy.bx.view.presenter.MainPresenter;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        RxView.clicks(start)
                .throttleFirst(600, TimeUnit.MILLISECONDS)
                .subscribe(o -> presenter.start());
        RxView.clicks(stop)
                .throttleFirst(600, TimeUnit.MILLISECONDS)
                .subscribe(o -> presenter.stop());
        RxView.clicks(text)
                .throttleFirst(600, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
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
}
