package com.admin.util.photowall;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.admin.util.MainActivity;
import com.admin.util.R;
import com.admin.util.photowall.Images.Images;
import com.admin.utill.net.cache.BitmapWorkerTask;
import com.admin.utill.net.cache.DiskBitmapCache;
import com.admin.utill.net.cache.LruCachePhoto;

public class GridViewActvity extends AppCompatActivity {
    private DiskBitmapCache mDisk;
    private LruCachePhoto mCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);
        mCache = new LruCachePhoto();
        mDisk = new DiskBitmapCache(this, "000");
        final GridView gridView= findViewById(R.id.gridView);
        gridView.setAdapter(new BaseAdapter() {
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
                    view = LayoutInflater.from(GridViewActvity.this).inflate(R.layout.photo_layout, null);
                } else {
                    view = convertView;
                }
                String url = Images.imageThumbUrls[position];
                ImageView imageView = view.findViewById(R.id.photo);
                imageView.setImageResource(R.drawable.log);
                imageView.setTag(url);
                BitmapWorkerTask task = new BitmapWorkerTask(mCache, mDisk, gridView, url);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
                return view;
            }
        });
    }
}
