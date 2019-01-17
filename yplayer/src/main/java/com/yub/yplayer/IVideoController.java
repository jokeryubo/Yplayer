package com.yub.yplayer;

/*
 * create by yubo on 2019/01/08
 * video 控制類
 */
public interface IVideoController {
    void start();
    void stop();
    void pause();
    void release();
    void fast(long sec);
    void back(long sec);
    void seekto(long sec);
}
