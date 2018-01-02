package com.example.zq.ziot.ui.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.Button;

import com.example.zq.ziot.util.IotUtil;

/**
 * Created by zq on 2017/12/27.
 */

public class DragButton  extends Button{

    int sW = 0;
    int sH = 0;
    float moveX;
    float moveY;

    public DragButton(Context context) {
        super(context);
        this.sW = IotUtil.getScreenWidth(context);
        this.sH = IotUtil.getScreenHeight(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moveX = event.getX();
                moveY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if( getY() + getHeight()  >= sH || getX() + getWidth() >= sW ){
                    setTranslationX(getX() -  getWidth());
                    setTranslationY(getY() - getHeight());
                }else {
                    setTranslationX(getX() + (event.getX() - moveX));
                    setTranslationY(getY() + (event.getY() - moveY));
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;
    }
}
