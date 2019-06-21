package com.admin.util;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.annotation.RequiresApi;

import com.admin.util.photowall.GridViewActvity;
import com.admin.util.photowall.Images.Images;
import com.admin.utill.net.cache.DiskBitmapCache;
import com.admin.utill.net.cache.BitmapWorkerTask;
import com.admin.utill.net.cache.LruCachePhoto;
import com.admin.utill.permission.PermissionCheck;

public class MainActivity extends PermissionCheck {

    private DiskBitmapCache mDisk;
    private LruCachePhoto mCache;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GridViewActvity.class));
            }
        });

        mCache = new LruCachePhoto();
        mDisk = new DiskBitmapCache(MainActivity.this, "456");

        final ListView listView = findViewById(R.id.listview);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return Images.imageThumbUrls.length;
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                if (convertView == null) {
                    view = LayoutInflater.from(MainActivity.this).inflate(R.layout.photo_layout, null);
                } else {
                    view = convertView;
                }
                String url = Images.imageThumbUrls[position];
                ImageView imageView = view.findViewById(R.id.photo);
                imageView.setImageResource(R.drawable.log);
                imageView.setTag(url);
                BitmapWorkerTask task = new BitmapWorkerTask(mCache, mDisk, listView, url);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
                return view;
            }
        });
    }
}
