package com.admin.utill.time;


import java.util.TimerTask;


public class BaseTimeTask extends TimerTask {

    private ITimeListener mITimeListener = null;

    public BaseTimeTask(ITimeListener timeListener) {
        this.mITimeListener = timeListener;
    }

    @Override
    public void run() {
        if (mITimeListener != null){
            mITimeListener.onTime();
        }
    }
}
