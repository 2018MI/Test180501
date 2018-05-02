package org.chengpx.test180501;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    private VideoView vv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initView();
        initData();
    }

    private void initData() {
        MediaController mediaController = new MediaController(this);
        vv.setMediaController(mediaController);
        vv.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "//" + R.raw.test));
        vv.start();
    }

    private void initView() {
        vv = findViewById(R.id.vv);
    }
}
