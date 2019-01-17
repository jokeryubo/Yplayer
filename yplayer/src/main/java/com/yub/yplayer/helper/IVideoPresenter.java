package com.yub.yplayer.helper;

/*
 * create by yubo on 2019/01/09
 */
public interface IVideoPresenter {
    void stop();
    void showBottomView();
    void hideBottomView();
    long getCurrentPosition();
    long getDuration();
    boolean checkPlayState();
}
