package android.com.changyou.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleyUtil {
    public static final String TAG = VolleyUtil.class.getSimpleName();
    public static final String BASE_URL = "http://192.168.15.101:8087/YouZu/";

    private static VolleyUtil mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    private VolleyUtil(Context context) {
        mCtx = context;
        // 创建 请求队列(RequestQueue) 对象
        mRequestQueue = getRequestQueue();

        // 创建 图片加载(ImageLoader) 对象,且创建 图片缓存 的匿名类
        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final int cacheSize = 10 * 1024 * 1024; // 10 Mib
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<>(cacheSize);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized VolleyUtil getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyUtil(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    /**
     * 把 request 对象加入到 请求队列
     *
     * @param req
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /**
     * 把 request 对象加入到 请求队列,且设置标记
     *
     * @param req
     * @param tag
     * @param <T>
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    /**
     * 取消指定标记的 request 对象
     *
     * @param tag
     */
    public void cancelRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * 绝对路径
     *
     * @param relativeUrl 相对路径
     * @return
     */
    public static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}
