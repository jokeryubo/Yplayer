package com.yub.yplayer.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yub.yplayer.R;
import com.yub.yplayer.utils.LogUtils;
import com.yub.yplayer.utils.Utils;
import com.yub.yplayer.view.IVideoFragment;
import com.yub.yplayer.widget.VideoTextureView;
import com.yub.yplayer.helper.VideoHelper;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/*
 * create by yubo on 2019/01/07
 */
public class VideoFragment extends Fragment implements View.OnClickListener, IVideoFragment ,VideoTextureView.VideoPlayer{
    private static final String TAG = "VideoFragment";
    private VideoTextureView mVideoTextureView ;
    private ImageView mImageViewScreenState;
    private boolean isFullScreen;
    private VideoHelper mVideoHelper ;
    private String videoPath;
    private TextView TV_Total , TV_Current;
    private ImageView Img_Pause;
    private SeekBar mSeekBar;
    private LinearLayout mBottomController;

    public VideoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_video,container ,false);
        init(view);
        mVideoHelper = new VideoHelper();
        mVideoHelper.setIVideoFragment(this);
        LogUtils.dLog(TAG , " onCreateView ");
       // 获取传过来的第三方的视频路径
        String action = getActivity().getIntent().getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            String str = getActivity().getIntent().getData().toString() ;
            LogUtils.dLog(TAG ,getActivity().getIntent().getData() + "");
            if (!TextUtils.isEmpty(str)){
                videoPath = str ;

                mVideoTextureView.setVideoPath(videoPath);
            }
        }

        return view;
    }
    private void init(View view){
        //加载native库
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } catch (Exception e) {
            e.printStackTrace();
        }

        mVideoTextureView =view.findViewById(R.id.textureView);
        mImageViewScreenState = view.findViewById(R.id.screen_change);
        Img_Pause = view.findViewById(R.id.playAndPause);
        TV_Current = view.findViewById(R.id.currentPosition);
        TV_Total = view.findViewById(R.id.totalPosition);
        mSeekBar = view.findViewById(R.id.seek);
        mBottomController = view.findViewById(R.id.bottom_controller);


        mVideoTextureView.setVideoPlayer(this);
        mVideoTextureView.setOnClickListener(this);
        mImageViewScreenState.setOnClickListener(this);
        Img_Pause.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int progress = seekBar.getProgress();
                if (b){
                    mVideoTextureView.seekto(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mVideoHelper.stop();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.screen_change:
                break;
            case R.id.playAndPause:
                if (mVideoHelper.checkPlayState()){
                    Img_Pause.setImageResource(R.drawable.ic_player_pause);
                }else {
                    Img_Pause.setImageResource(R.drawable.ic_player_start);
                }
                break;
            case R.id.textureView:
                 mVideoTextureView.pause();
                break;
                default:
                    break;
        }
    }

    @Override
    public void startPlay() {
        mVideoTextureView.start();
    }

    @Override
    public void stopPlay() {
        mVideoTextureView.stop();

    }

    @Override
    public void hideBottomView() {
        mBottomController.setVisibility(View.GONE);
    }

    @Override
    public void showBottomView() {
        mBottomController.setVisibility(View.VISIBLE);
    }

    @Override
    public long getCurrentPosition() {
        return mVideoTextureView.getCurrenPosition();
    }

    @Override
    public long getDuration() {
        return mVideoTextureView.getDuration();
    }

    @Override
    public void pause() {
        mVideoTextureView.pause();
    }

    @Override
    public boolean isPlaying() {
        return mVideoTextureView.isPlaying();
    }

    @Override
    public void setTextViewPosition() {

    }

    @Override
    public void onVideoPrepared() {
        mVideoTextureView.start();
        TV_Total.setText(Utils.formatTime(mVideoTextureView.getDuration()));
        mSeekBar.setMax((int) mVideoTextureView.getDuration());
    }

    @Override
    public void onPosition(long msec) {
        TV_Current.setText(Utils.formatTime(msec));
        mSeekBar.setProgress((int) msec);
    }

}
