package com.yub.yplayer.view;

/*
 * create by yubo on 2019/01/09
 */
public interface IVideoFragment {
    void startPlay();
    void stopPlay();
    void hideBottomView();
    void showBottomView();
    long getCurrentPosition();
    long getDuration();
    void pause();
    boolean isPlaying();
    void setTextViewPosition();
}
