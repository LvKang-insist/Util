package com.admin.utill.net.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.admin.utill.net.NetRequest;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lv
 * Created at 2019/6/14
 *
 * 图片缓存
 */
@SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
public class DiskBitmapCache {

    private DiskLruCache mDiskLruCache;
    private Handler mHandler = new Handler(Looper.myLooper());

    public DiskBitmapCache(Context context, String uniqueName){
        open(context,uniqueName);
    }
    public DiskBitmapCache(){}

    /**
     * 用于返回 硬盘缓存后的数据
     */
    public interface OnCacheDataListener {
        /**
         * 返回数据
         *
         * @param bytes 缓存的数据
         */
        void onData(byte[] bytes);
    }

    /**
     * 打开磁盘缓存
     *
     * @param context    Context
     * @param uniqueName 缓存文件夹的名字
     * @return 返回一个 boolean 类型，true 表示 创建成果
     */
    public boolean open(Context context, String uniqueName) {
        File cacheDir = getCacheDir(context, uniqueName);
        //路径是否存在，不存在则创建
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        try {
            // 1，数据的缓存地址，2，指定当前应用程序的版本号
            // 3，指定同一个 key 可以对应多少个缓存文件，基本都是1
            // 4，指定据图可以缓存多少字节的数据
            mDiskLruCache = DiskLruCache.open(cacheDir, getVersion(context),
                    1, 10 * 1024 * 1024);
            return mDiskLruCache != null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 传入对应的 url ，进行缓存
     *
     * @param url      需要缓存的图片 ，缓存的文件名字为 url
     * @param listener 回调，将数据进行缓存后，返回一份，如果不需要传入 null 即可。
     */
    public void writeData(String url, final OnCacheDataListener listener) {
        downloadData(url, listener);
    }

    /**
     * 传入指定的 key 和 bitmap ，对图片进行缓存
     *
     * @param key    这个key 为缓存文件的名字
     * @param bitmap 要缓存的图片
     * @return 返回缓存的结果
     */
    public boolean writeData(String key, Bitmap bitmap) {
        key = hashKeyForDisk(key);
        DiskLruCache.Editor edit = null;
        try {
            edit = mDiskLruCache.edit(key);
            if (edit != null) {
                OutputStream outputStream = edit.newOutputStream(0);
                boolean compress = bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                edit.commit();
                return compress;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (edit != null) {
                try {
                    edit.abort();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return false;
        }
    }

    /**
     * 根据 传入的 key 来查找缓存
     *
     * @param key 用来读取缓存的 key
     * @return 返回缓存的图片字节，没有则为 null
     */
    public byte[] readCache(String key) {
        try {
            List<Byte> data = new ArrayList<>();
            key = hashKeyForDisk(key);
            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
            if (snapShot != null) {
                InputStream is = snapShot.getInputStream(0);
                byte[] bytes = new byte[2048];
                int len;
                while ((len = is.read(bytes)) != -1) {
                    for (int i = 0; i < len; i++) {
                        data.add(bytes[i]);
                    }
                }
                bytes = new byte[data.size()];
                for (int i = 0; i < bytes.length; i++) {
                    bytes[i] = data.get(i);
                }
                return bytes;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据 key 删除指定的 缓存
     *
     * @param key 缓存的 key
     * @return 成功则返回 true
     */
    public boolean removeCache(String key) {
        String md5Key = hashKeyForDisk(key);
        boolean remove = false;
        try {
            remove = mDiskLruCache.remove(md5Key);
            return remove;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return remove;
    }

    /**
     *  判断 该key 是否由缓存的图片
     * @param key 缓存文件对应的 key
     * @return 返回true 表示 有缓存，可以直接读取
     */
    public boolean isCache(String key) {
        byte[] bytes = readCache(key);
        return bytes != null;
    }

    /**
     * @return 返回 DiskLruCache 的实例
     */
    public DiskLruCache getInstance() {
        return mDiskLruCache;
    }

    /**
     * @return 返回缓存的大小，以字节为单位
     */
    public long size() {
        return mDiskLruCache.size();
    }

    /**
     * 将内存中的操作记录同步到日志文件，这个方法非常重要
     * 频繁地调用这个这个方法不会有任何好处，标准的做法是在 onPause 中调用一次就可以了
     */
    public void flush() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 这个方法用于将DiskLruCache关闭掉，是和open()方法对应的一个方法。
     * 关闭掉了之后就不能再调用DiskLruCache中任何操作缓存数据的方法，
     * 通常只应该在Activity的onDestroy()方法中去调用close()方法
     */
    public void close() {
        if (mDiskLruCache != null) {
            try {
                mDiskLruCache.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 这个方法用于将所有的缓存数据全部删除
     */
    public void delete() {
        try {
            mDiskLruCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return 获取 缓存的路径
     */
    private File getCacheDir(Context context, String uniqueName) {
        String cachePath;
        // 判断 SD 卡是否可用
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //获取 有sd 卡时的路径
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            // 获取 无sd 卡时的路径
            cachePath = context.getCacheDir().getPath();
        }
        //File.separator 分隔符 /
        return new File(cachePath + File.separator + uniqueName);
    }

    private int getVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    private String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
    private void downloadData(final String url, final OnCacheDataListener listener) {
        try {
            String key = hashKeyForDisk(url);
            final DiskLruCache.Editor editor = mDiskLruCache.edit(key);
            final OutputStream ops = editor.newOutputStream(0);
            if (ops != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        NetRequest okhttp = new NetRequest();
                        try {
                            final byte[] result = okhttp.request(url);
                            ops.write(result);
                            editor.commit();
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listener.onData(result);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                            try {
                                if (editor != null) {
                                    editor.abort();
                                }
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}