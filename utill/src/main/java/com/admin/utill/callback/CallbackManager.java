package com.admin.utill.callback;

import java.util.WeakHashMap;

/**
 * Copyright (C)
 *
 * @file: CallbackManager
 * @author: 345
 * @Time: 2019/5/9 15:05
 * @description: 全局 接口的管理
 */
public class CallbackManager {
    private static final WeakHashMap<Object,IGlobalCallback> CALLBACKS = new WeakHashMap<>();

    private static class Holder{
        private static final CallbackManager IINSTANCE = new CallbackManager();
    }
    public static CallbackManager getInstance(){
        return Holder.IINSTANCE;
    }

    @SuppressWarnings("UnusedReturnValue")
    public CallbackManager addCallback(Object tag , IGlobalCallback callback ){
        CALLBACKS.put(tag,callback);
        return this;
    }
    public IGlobalCallback getCallBack(Object tag){
        return CALLBACKS.get(tag);
    }
}
