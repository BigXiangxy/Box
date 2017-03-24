package com.box.moremodule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.box.lib.loction.SayHelloService;
import com.box.lib.mvp.view.BaseActivity;

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
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("result1", "resultValue");
        setResult(14, intent);
        super.finish();
    }
}
