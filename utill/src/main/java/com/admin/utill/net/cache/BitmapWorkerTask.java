package com.admin.utill.net.cache;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.admin.utill.net.NetRequest;


/**
 * @author Lv
 * Created at 2019/6/15
 */
@SuppressLint("StaticFieldLeak")
public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
    private LruCachePhoto mCachePhoto;
    private GridView mGridView;
    private DiskBitmapCache mDataCache;
    private ListView mListView;
    private ImageView mImageView;
    private Object mTag;

    private static final String TAG = "BitmapWorkerTask";

    /**
     * @param mCachePhoto 用于缓存下载好的图片，将图片缓存到内存
     * @param dataCache   磁盘缓存，传入前 必须调用 open 方法
     * @param gridView    需要显示图片的 控件
     * @param Tag         每一个条目的 Tag
     */
    public BitmapWorkerTask(LruCachePhoto mCachePhoto, DiskBitmapCache dataCache, GridView gridView, Object Tag) {
        this.mGridView = gridView;
        init(mCachePhoto, dataCache, Tag);
    }

    public BitmapWorkerTask(LruCachePhoto mCachePhoto, DiskBitmapCache dataCache, ListView listView, Object tag) {
        this.mListView = listView;
        init(mCachePhoto, dataCache, tag);
    }


    public BitmapWorkerTask(LruCachePhoto mCachePhoto, DiskBitmapCache dataCache, ImageView imageView) {
        init(mCachePhoto, dataCache, null);
        this.mImageView = imageView;
    }

    private void init(LruCachePhoto mCachePhoto, DiskBitmapCache dataCache, Object tag) {
        this.mCachePhoto = mCachePhoto;
        this.mDataCache = dataCache;
        this.mTag = tag;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String imageUlr = strings[0];
        //获取内存的缓存
        Bitmap bitmap = mCachePhoto.getBitmapFromMemoryCache(imageUlr);
        if (bitmap!=null){
            return bitmap;
        }
        //判断本地是否有缓存
        if (!mDataCache.isCache(imageUlr)) {
            //没有缓存
            bitmap = downLoadBitmap(imageUlr);
        } else {
            byte[] bytes = mDataCache.readCache(imageUlr);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            mCachePhoto.addBitmapToCache(imageUlr, bitmap);
            return bitmap;
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        ImageView imageView;
        if (mGridView != null) {
            imageView = mGridView.findViewWithTag(mTag);
        } else if (mListView != null) {
            imageView = mListView.findViewWithTag(mTag);
        }  else {
            imageView = this.mImageView;
        }
        if (imageView != null && bitmap!= null) {
            imageView.setImageBitmap(bitmap);
            Log.e(TAG, "onPostExecute: " );
        }
    }
    private Bitmap downLoadBitmap(String imageUlr) {
        //如果没有缓存 就进行请求，然后进行缓存
        Bitmap bitmap = null;
        byte[] request = new NetRequest().request(imageUlr);
        if (request != null){
            bitmap = BitmapFactory.decodeByteArray(request, 0, request.length);
        }
        if (bitmap != null) {
            //将图片 缓存到内存
            mCachePhoto.addBitmapToCache(imageUlr, bitmap);
            //将图片 缓存到磁盘
            boolean b = mDataCache.writeData(imageUlr, bitmap);
            if (!b) {
                Log.e("PhotoCache", "磁盘缓存图片失败");
            }
            return bitmap;
        } else {
            return null;
        }
    }
}
