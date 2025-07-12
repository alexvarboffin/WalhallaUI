//package com.video.maker.activity;
//
//import com.hw.photomovie.PhotoMovie;
//import com.hw.photomovie.PhotoMovieFactory;
//import com.hw.photomovie.PhotoMoviePlayer;
//import com.hw.photomovie.model.PhotoSource;
//import com.hw.photomovie.render.GLSurfaceMovieRenderer;
//import com.hw.photomovie.render.GLTextureMovieRender;
//import com.hw.photomovie.render.GLTextureView;
//import com.hw.photomovie.timer.IMovieTimer;
//import com.video.maker.util.DLog;
//
//public class DemoPresenter implements IMovieTimer.MovieListener {
//
//    private GLSurfaceMovieRenderer mMovieRenderer;
//    public PhotoMoviePlayer mPhotoMoviePlayer;
//
//
//    public void initMoviePlayer(final GLTextureView mGLTextureView) {
//        this.mMovieRenderer = new GLTextureMovieRender(mGLTextureView);
//        this.mPhotoMoviePlayer = new PhotoMoviePlayer(getApplicationContext());
//        this.mPhotoMoviePlayer.setMovieRenderer(this.mMovieRenderer);
//        this.mPhotoMoviePlayer.setMovieListener(this);
//        this.mPhotoMoviePlayer.setLoop(true);
//        this.mPhotoMoviePlayer.setOnPreparedListener(new PhotoMoviePlayer.OnPreparedListener() {
//
//            @Override
//            public void onPreparing(PhotoMoviePlayer photoMoviePlayer, float f) {
//                DLog.d("@@");
//            }
//
//            public void onPrepared(PhotoMoviePlayer photoMoviePlayer, int i, int i2) {
//                DLog.d("@@");
//                VideoPreviewActivity.this.runOnUiThread(() -> VideoPreviewActivity.this.mPhotoMoviePlayer.start());
//            }
//
//            public void onError(PhotoMoviePlayer photoMoviePlayer) {
//                DLog.i("onPrepare error");
//            }
//        });
//    }
//    public void onPauseVideo() {
//        this.mPhotoMoviePlayer.pause();
//    }
//
//    public void onResumeVideo() {
//        this.mPhotoMoviePlayer.start();
//    }
//
//    private void startPlay(PhotoSource photoSource) {
//        PhotoMovie generatePhotoMovie = PhotoMovieFactory.generatePhotoMovie(photoSource, this.mMovieType, this.duration);
//        this.mPhotoMovie = generatePhotoMovie;
//        this.mPhotoMoviePlayer.setDataSource(generatePhotoMovie);
//        this.mPhotoMoviePlayer.prepare();
//    }
//
//    @Override
//    public void onMovieEnd() {
//
//    }
//
//    @Override
//    public void onMoviePaused() {
//        DLog.d("@@@");
//        mPhotoMoviePlayer.prepare();
//    }
//
//    @Override
//    public void onMovieResumed() {
//
//    }
//
//    @Override
//    public void onMovieStarted() {
//
//    }
//
//    @Override
//    public void onMovieUpdate(int i) {
//
//    }
//}
