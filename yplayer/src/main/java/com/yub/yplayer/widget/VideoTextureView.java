package com.yub.yplayer.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.SurfaceTexture;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

import com.yub.yplayer.IVideoController;
import com.yub.yplayer.utils.LogUtils;
import com.yub.yplayer.utils.Utils;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/*
 * create by yubo on 2019/01/07
 */
public class VideoTextureView extends TextureView implements TextureView.SurfaceTextureListener, IVideoController {

    private static final String TAG ="VideoSurfaceView" ;

    private static final int GETPOSITION = 0x1001;
    private IMediaPlayer mMediaPlayer;
    private Surface mSurface;
    private String videoPath;

    private boolean SurfaceTextureAvailable;
    private int width,height;
    private Context mContext;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what){
                case GETPOSITION:
                    if (mMediaPlayer != null && mVideoPlayer != null){
                        sendEmptyMessageDelayed(GETPOSITION ,500);
                        mVideoPlayer.onPosition(mMediaPlayer.getCurrentPosition());
                    }

                    break;
                    default:

                        break;
            }
        }
    };
    public VideoTextureView(Context context) {
        super(context);
        mContext = context ;
        init();
    }

    public VideoTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context ;
        init();
    }

    public VideoTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void init(){

        setSurfaceTextureListener(this);
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
//        createPlayer();

    }
    /**
     * 加载视频
     */
    private void load() {
        //每次都要重新创建IMediaPlayer
        createPlayer();
        try {
            mMediaPlayer.setDataSource(videoPath);
            mMediaPlayer.setSurface(mSurface);
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * 创建一个新的player
     */
    private void createPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.setDisplay(null);
            mMediaPlayer.release();
        }
        IjkMediaPlayer ijkMediaPlayer = new IjkMediaPlayer();
        ijkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_ERROR);

        //开启硬解码
        // ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
        mMediaPlayer = ijkMediaPlayer;

            mMediaPlayer.setOnPreparedListener(mOnPreparedListener);
//            mMediaPlayer.setOnInfoListener(listener);
            mMediaPlayer.setOnSeekCompleteListener(mOnSeekCompleteListener);
//            mMediaPlayer.setOnBufferingUpdateListener(listener);
            mMediaPlayer.setOnErrorListener(mOnErrorListener);
    }
    private IMediaPlayer.OnPreparedListener mOnPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {
            Log.d(TAG ,"onPrepared ");
            changeVideoSize(width ,height);
            if (mVideoPlayer != null){
                mVideoPlayer.onVideoPrepared();
            }
            mHandler.sendEmptyMessage(GETPOSITION);
            mMediaPlayer.start();
        }
    };
    private IMediaPlayer.OnSeekCompleteListener mOnSeekCompleteListener = (mediaPlayer) ->{

    };
    private IMediaPlayer.OnErrorListener mOnErrorListener =(mediaPlayer ,what ,extra) -> {

        return false;
    };

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        Log.i(TAG ," onSurfaceTextureAvailable --");
        this.width = i;
        this.height =i1;
        mSurface = new Surface(surfaceTexture);
        load();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    public void changeVideoSize(int surfaceWidth ,int surfaceHeight) {
        int videoWidth = mMediaPlayer.getVideoWidth();
        int videoHeight = mMediaPlayer.getVideoHeight();
        LogUtils.dLog(TAG ," videoWidth:"+videoWidth + " videoHeight:"+videoHeight);
        if (videoWidth > videoHeight){
            Utils.scanForActivity(mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else {
            Utils.scanForActivity(mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
            mHandler.sendEmptyMessage(GETPOSITION);
        }
    }
    public  void stop(){
        if (mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.setDisplay(null);
            mMediaPlayer.release();
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mSurface != null){
            mSurface.release();
        }
        Utils.scanForActivity(mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    public void pause(){
        if (mMediaPlayer != null){
            mMediaPlayer.pause();
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void release() {
        if (mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.setDisplay(null);
            mMediaPlayer.release();
        }
    }

    @Override
    public void fast(long sec) {

    }

    @Override
    public void back(long sec) {

    }

    @Override
    public void seekto(long sec) {
        mMediaPlayer.seekTo(sec);
    }

    public long getCurrenPosition(){
        return mMediaPlayer.getCurrentPosition();
    }

    public long getDuration(){
        return  mMediaPlayer.getDuration();
    }

    public boolean isPlaying(){
        return  mMediaPlayer.isPlaying();
    }
    private VideoPlayer mVideoPlayer;

    public interface VideoPlayer{
        void onVideoPrepared();
        void onPosition(long msec);
    }

    public void setVideoPlayer(VideoPlayer videoPlayer) {
        mVideoPlayer = videoPlayer;
    }
}
