package com.yyz.ard.cactus.uiaf;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.yyz.ard.cactus.uiaf.joggle.IHandlerReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * SingleHandler 进程通讯
 *
 * @author yyz
 * @date 7/8/2016.`
 */
public class SingleHandler {

    private static volatile SingleHandler baseHandler = null;
    private Lock lock = new ReentrantLock(true);
    private List<IHandlerReceiver> mTargetQueue = new ArrayList();
    private Handler mHandler;

    private SingleHandler() {
        HandleCallBack callBack = new HandleCallBack();
        mHandler = new Handler(Looper.getMainLooper(), callBack);
    }

    public static SingleHandler getInstance() {
        if (baseHandler == null) {
            synchronized (SingleHandler.class) {
                if (baseHandler == null) {
                    baseHandler = new SingleHandler();
                }
            }
        }
        return baseHandler;
    }

    public Handler getHandler() {
        return mHandler;
    }


    public void bind(IHandlerReceiver receiver) {
        lock.lock();
        try {
            mTargetQueue.add(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public synchronized void unBind(IHandlerReceiver receiver) {
        lock.lock();
        try {
            mTargetQueue.remove(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public Message obtainMessage() {
        return mHandler.obtainMessage();
    }

    public void sendMessage(Message message) {
        mHandler.sendMessage(message);
    }

    private class HandleCallBack implements Handler.Callback {

        @Override
        public boolean handleMessage(Message msg) {
            lock.lock();
            try {
                for (IHandlerReceiver receiver : mTargetQueue) {
                    receiver.onHasMessage(msg.what, msg.getData(), msg.obj, msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            return true;
        }
    }

}
