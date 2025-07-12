package com.video.maker.activity;

import static com.video.maker.activity.ShareActivity.KEY_ARG_VIDEO;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.video.maker.R;

public class VideoPlayerActivity extends BaseActivity implements View.OnClickListener {

    public MediaController mediaController;
    Uri uri = null;

    public VideoView videoView;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_video_player);
        addControls();
        getDataShowVideo();
    }

    private void getDataShowVideo() {
        this.uri = getIntent().getParcelableExtra(KEY_ARG_VIDEO);
        if (this.mediaController == null) {
            MediaController mediaController2 = new MediaController(this);
            this.mediaController = mediaController2;
            mediaController2.setAnchorView(this.videoView);
            this.videoView.setMediaController(this.mediaController);
        }
        this.videoView.setVideoURI(this.uri);
        this.videoView.requestFocus();
        this.videoView.setOnPreparedListener(mediaPlayer -> {
            VideoPlayerActivity.this.videoView.start();
            mediaPlayer.setOnVideoSizeChangedListener((mediaPlayer1, i, i2) -> VideoPlayerActivity.this.mediaController.setAnchorView(VideoPlayerActivity.this.videoView));
        });
    }

    private void addControls() {
        this.videoView = findViewById(R.id.videoView);
        findViewById(R.id.btnBackFinal).setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnBackFinal) {
            super.onBackPressed();
        }
    }



    public void onDestroy() {
        super.onDestroy();
    }
}
