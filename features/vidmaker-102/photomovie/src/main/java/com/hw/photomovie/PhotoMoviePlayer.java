package com.hw.photomovie;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;

import com.hw.photomovie.model.ErrorReason;
import com.hw.photomovie.model.PhotoData;
import com.hw.photomovie.model.PhotoSource;
import com.hw.photomovie.music.IMusicPlayer;
import com.hw.photomovie.music.MusicPlayer;

import com.hw.photomovie.render.GLMovieRenderer;
import com.hw.photomovie.render.GLSurfaceMovieRenderer;
import com.hw.photomovie.render.MovieRenderer;
import com.hw.photomovie.segment.MovieSegment;
import com.hw.photomovie.timer.IMovieTimer;
import com.hw.photomovie.timer.MovieTimer;
import com.hw.photomovie.util.AppResources;
import com.hw.photomovie.util.DLog;

import java.io.FileDescriptor;
import java.util.List;

public class PhotoMoviePlayer implements IMovieTimer.MovieListener {
    private static final String TAG = "PhotoMoviePlayer";

    protected static final float FIRST_SEGMENT_PREPARE_RATE = 0.05f;
    // all possible internal states
    public static final int STATE_ERROR = -1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_PREPARING = 1;
    public static final int STATE_PREPARED = 2;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_PAUSED = 4;
    public static final int STATE_PLAYBACK_COMPLETED = 5;

    private int mCurrentState = STATE_IDLE;


    public boolean mLoop;
    private IMovieTimer.MovieListener mMovieListener;

    public MovieRenderer mMovieRenderer;
    private IMovieTimer mMovieTimer;

    public IMusicPlayer mMusicPlayer = new MusicPlayer();

    public OnPreparedListener mOnPreparedListener;

    public PhotoMovie mPhotoMovie;

    public interface OnPreparedListener {
        void onError(PhotoMoviePlayer photoMoviePlayer);

        void onPrepared(PhotoMoviePlayer photoMoviePlayer, int i, int i2);

        void onPreparing(PhotoMoviePlayer photoMoviePlayer, float f);
    }

    public PhotoMoviePlayer(Context context) {
        mMusicPlayer = new MusicPlayer();
        AppResources.getInstance().init(context.getResources());
    }

    public void setMusicPlayer(IMusicPlayer musicPlayer) {
        mMusicPlayer = musicPlayer;
    }

    public void setDataSource(PhotoMovie photoMovie) {
        if (mPhotoMovie != null && mMovieRenderer != null) {
            mMovieRenderer.release(mPhotoMovie.getMovieSegments());
        }
        setStateValue(STATE_IDLE);
        mPhotoMovie = photoMovie;
        mMovieTimer = new MovieTimer(mPhotoMovie);
        mMovieTimer.setMovieListener(this);
        if (mMovieRenderer != null && mPhotoMovie != null) {
            mPhotoMovie.setMovieRenderer(mMovieRenderer);
            mMovieRenderer.setPhotoMovie(mPhotoMovie);
        }
        setLoop(mLoop);
    }

    public void setMovieRenderer(MovieRenderer movieRenderer) {
        mMovieRenderer = movieRenderer;
        if (mMovieRenderer != null && mPhotoMovie != null) {
            mPhotoMovie.setMovieRenderer(mMovieRenderer);
            mMovieRenderer.setPhotoMovie(mPhotoMovie);
        }
    }

    public void setMusic(String str) {
        this.mMusicPlayer.setDataSource(str);
    }

    public void setMusic(Context context, Uri uri, MusicPlayer.OnMusicOKListener onMusicOKListener) {
        this.mMusicPlayer.setDataSource(context, uri, onMusicOKListener);
    }

    public void setMusic(FileDescriptor fileDescriptor) {
        this.mMusicPlayer.setDataSource(fileDescriptor);
    }

    public void setMusic(AssetFileDescriptor assetFileDescriptor) {
        this.mMusicPlayer.setDataSource(assetFileDescriptor);
    }

    public IMusicPlayer getMusicPlayer() {
        return mMusicPlayer;
    }

    public void prepare() {
        if (mPhotoMovie == null || mPhotoMovie.getPhotoSource() == null) {
            throw new NullPointerException("PhotoSource is null!");
        }
        prepare(mPhotoMovie.getPhotoSource().size());
    }


    /**
     * @param discPrepareNum 只预先下载好PhotoSource里一部分的资源，其它的边播边下
     */
    public void prepare(int discPrepareNum) {
        if (mPhotoMovie == null || mPhotoMovie.getPhotoSource() == null) {
            throw new NullPointerException("PhotoSource is null!");
        }
        setStateValue(STATE_PREPARING);
        mPhotoMovie.getPhotoSource().setOnSourcePreparedListener(new PhotoSource.OnSourcePrepareListener() {

            @Override
            public void onPreparing(PhotoSource photoSource, float progress) {
                if (mOnPreparedListener != null) {
                    mOnPreparedListener.onPreparing(PhotoMoviePlayer.this, progress * (1 - FIRST_SEGMENT_PREPARE_RATE));
                }
            }

            @Override
            public void onPrepared(PhotoSource photoSource, int downloaded, List<PhotoData> prepareFailList) {
                if (prepareFailList == null || prepareFailList.size() == 0) {
                    //没有任务加载失败
                    prepareFirstSegment(downloaded, photoSource.size());
                } else if (photoSource.size() > 0) {
                    //部分PhotoData加载失败，重新分配
                    mPhotoMovie.reAllocPhoto();
                    prepareFirstSegment(downloaded, photoSource.size() + prepareFailList.size());
                } else {
                    //全部任务加载失败
                    if (mOnPreparedListener != null) {
                        mOnPreparedListener.onError(PhotoMoviePlayer.this);
                    }
                    setStateValue(STATE_ERROR);
                    DLog.e("数据加载失败");
                }
            }

            @Override
            public void onError(PhotoSource photoSource, PhotoData photoData, ErrorReason errorReason) {
            }
        });
        mPhotoMovie.getPhotoSource().prepare(discPrepareNum);
    }

    public void setStateValue(int state) {
        mCurrentState = state;
        if (mMovieRenderer != null) {
            switch (mCurrentState) {
                case STATE_ERROR:
                case STATE_IDLE:
                    mMovieRenderer.enableDraw(false);
                    break;
                case STATE_PREPARED:
                    mMovieRenderer.enableDraw(true);
                    break;
                case STATE_PREPARING:
                    mMovieRenderer.enableDraw(false);
                    break;
            }
        }
    }


    private void prepareFirstSegment(final int prepared, final int total) {
        List<MovieSegment> segmentList = mPhotoMovie.getMovieSegments();
        if (segmentList == null || segmentList.size() < 1) {
            setStateValue(STATE_PREPARED);
            if (mOnPreparedListener != null) {
                onPrepared(prepared, total);
            }
            return;
        }
        final MovieSegment firstSegment = segmentList.get(0);
        firstSegment.setOnSegmentPrepareListener(new MovieSegment.OnSegmentPrepareListener() {
            @Override
            public void onSegmentPrepared(boolean success) {
                firstSegment.setOnSegmentPrepareListener(null);
                setStateValue(STATE_PREPARED);
                if (mOnPreparedListener != null) {
                    mOnPreparedListener.onPreparing(PhotoMoviePlayer.this, 1f);
                    onPrepared(prepared, total);
                }
            }
        });
        firstSegment.prepare();
    }

    public void seekTo(int movieTime) {
        onMovieUpdate(movieTime);
    }

    public int getCurrentPlayTime() {
        return mMovieTimer.getCurrentPlayTime();
    }

    public void pause() {
        if (mMovieTimer != null) {
            mMovieTimer.pause();
        }
    }

    public void start() {
        if (!isPrepared()) {
            DLog.e(TAG, "start error!not prepared!");
            return;
        }
        if (this.mCurrentState != 4) {
            this.mPhotoMovie.calcuDuration();
        }
        IMovieTimer iMovieTimer = this.mMovieTimer;
        if (iMovieTimer != null) {
            iMovieTimer.start();
        }
    }

    public void stop() {
        if (this.mCurrentState >= 2) {
            pause();
            seekTo(0);
        }
    }

    public boolean isPlaying() {
        return this.mCurrentState == 3;
    }


    private boolean isInPlaybackState() {
        return (mPhotoMovie != null &&
                mCurrentState != STATE_ERROR &&
                mCurrentState != STATE_IDLE &&
                mCurrentState != STATE_PREPARING);
    }

    public void setMovieListener(IMovieTimer.MovieListener movieListener) {
        this.mMovieListener = movieListener;
    }

    public void onMovieUpdate(int i) {
        IMovieTimer.MovieListener movieListener = this.mMovieListener;
        if (movieListener != null) {
            movieListener.onMovieUpdate(i);
        }
        PhotoMovie photoMovie = this.mPhotoMovie;
        if (photoMovie != null) {
            photoMovie.updateProgress(i);
        }
    }

    public void onMovieStarted() {
        DLog.i(TAG, "onMovieStarted");
        IMovieTimer.MovieListener movieListener = this.mMovieListener;
        if (movieListener != null) {
            movieListener.onMovieStarted();
        }
        this.mMusicPlayer.start();
        setStateValue(3);
    }

    public void onMoviePaused() {
        DLog.i(TAG, "onMoviePaused");
        IMovieTimer.MovieListener movieListener = this.mMovieListener;
        if (movieListener != null) {
            movieListener.onMoviePaused();
        }
        this.mMusicPlayer.pause();
        setStateValue(4);
    }

    public void onMovieResumed() {
        DLog.i(TAG, "onMovieResumed");
        IMovieTimer.MovieListener movieListener = this.mMovieListener;
        if (movieListener != null) {
            movieListener.onMovieResumed();
        }
        this.mMusicPlayer.start();
        setStateValue(3);
    }

    public void onMovieEnd() {
        DLog.i(TAG, "onMovieEnd");
        IMovieTimer.MovieListener movieListener = this.mMovieListener;
        if (movieListener != null) {
            movieListener.onMovieEnd();
        }
        new StopMusicTask().execute();
    }

    class StopMusicTask extends AsyncTask<Void, Void, Void> {
        StopMusicTask() {
        }


        public Void doInBackground(Void... voidArr) {
            PhotoMoviePlayer.this.mMusicPlayer.stop();
            return null;
        }


        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            PhotoMoviePlayer.this.setStateValue(5);
            if (PhotoMoviePlayer.this.mLoop) {
                PhotoMoviePlayer.this.releaseAndRestart();
            } else {
                PhotoMoviePlayer.this.mMovieRenderer.release();
            }
        }
    }


    public void releaseAndRestart() {
        MovieRenderer movieRenderer = this.mMovieRenderer;
        if (!(movieRenderer instanceof GLSurfaceMovieRenderer) || ((GLSurfaceMovieRenderer) movieRenderer).isSurfaceCreated()) {
            final Handler handler = new Handler();
            this.mMovieRenderer.setOnReleaseListener(new MovieRenderer.OnReleaseListener() {
                public void onRelease() {
                    PhotoMoviePlayer.this.mMovieRenderer.setOnReleaseListener(null);
                    handler.post(new Runnable() {
                        public void run() {
                            PhotoMoviePlayer.this.restartImpl();
                        }
                    });
                }
            });
            this.mMovieRenderer.release();
            return;
        }
        restartImpl();
    }


    public void restartImpl() {
        List movieSegments = this.mPhotoMovie.getMovieSegments();
        if (movieSegments != null && movieSegments.size() != 0) {
            setStateValue(1);
            final MovieSegment movieSegment = (MovieSegment) movieSegments.get(0);
            movieSegment.setOnSegmentPrepareListener(new MovieSegment.OnSegmentPrepareListener() {
                public void onSegmentPrepared(boolean success) {
                    movieSegment.setOnSegmentPrepareListener(null);
                    PhotoMoviePlayer.this.setStateValue(2);
                    PhotoMoviePlayer.this.start();
                }
            });
            movieSegment.prepare();
        }
    }

    public void destroy() {
        pause();
        setMovieListener(null);
        setOnPreparedListener(null);
        IMovieTimer iMovieTimer = this.mMovieTimer;
        if (iMovieTimer != null) {
            iMovieTimer.setMovieListener(null);
        }
        this.mMovieTimer = null;
    }

    public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        this.mOnPreparedListener = onPreparedListener;
    }


    private void onPrepared(final int prepared, final int total) {
        if (mMovieRenderer instanceof GLMovieRenderer) {
            ((GLMovieRenderer) mMovieRenderer).checkGLPrepared(new GLMovieRenderer.OnGLPrepareListener() {
                @Override
                public void onGLPrepared() {
                    mOnPreparedListener.onPrepared(PhotoMoviePlayer.this, prepared, total);
                }
            });
        } else {
            mOnPreparedListener.onPrepared(PhotoMoviePlayer.this, prepared, total);
        }

    }

    public int getState() {
        return this.mCurrentState;
    }

    public void setLoop(boolean z) {
        this.mLoop = z;
    }

    public boolean isPrepared() {
        int i = this.mCurrentState;
        return i == 2 || i == 4 || i == 5;
    }
}
