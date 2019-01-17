package com.yub.yplayer.helper;

import com.yub.yplayer.view.IVideoFragment;

/*
 * create by yubo on 2019/01/09
 */
public class VideoHelper implements IVideoPresenter {
    private IVideoFragment mIVideoFragment;
    private long mCurrentPosition;

    public void setIVideoFragment(IVideoFragment IVideoFragment) {
        mIVideoFragment = IVideoFragment;
    }

    public VideoHelper() {
    }

    @Override
    public void stop() {

    }

    @Override
    public void showBottomView() {

    }

    @Override
    public void hideBottomView() {

    }

    @Override
    public long getCurrentPosition() {
        mCurrentPosition = mIVideoFragment.getCurrentPosition();
        return mIVideoFragment.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return mIVideoFragment.getDuration();
    }


    public boolean checkPlayState(){
        if (mIVideoFragment.isPlaying()){
            mIVideoFragment.pause();
            return true;
        }else {
            mIVideoFragment.startPlay();
        }
        return false;
    }
}
