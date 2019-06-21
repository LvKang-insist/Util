package com.admin.utill.net.cache;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * @author Lv
 * Created at 2019/6/16
 */
@SuppressWarnings("WeakerAccess")
public class LruCachePhoto {
    /**
     * 图片 缓存技术的核心类，用于缓存下载好的所有图片，
     * 在程序内存达到设定值后会将最少最近使用的图片移除掉
     */
    private LruCache<String, Bitmap> mMenoryCache;

    public LruCachePhoto() {
        //获取应用最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //设置 缓存文件大小为 程序最大可用内存的 1/8
        int cacheSize = maxMemory / 8;

        mMenoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    /**
     * 从 LruCache 中获取一张图片，如果不存在 就返回 null
     *
     * @param key LurCache 的键，这里是 图片的地址
     * @return 返回对应的 Bitmap对象，找不到则为 null
     */
    public Bitmap getBitmapFromMemoryCache(String key) {
        return mMenoryCache.get(key);
    }

    /**
     * 添加一张图片
     *
     * @param key    key
     * @param bitmap bitmap
     */
    public void addBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMenoryCache.put(key, bitmap);
        }
    }
}
