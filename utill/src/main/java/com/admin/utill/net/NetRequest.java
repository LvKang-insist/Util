package com.admin.utill.net;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * @author Lv
 * Created at 2019/6/14
 */
@SuppressWarnings("AlibabaAvoidManuallyCreateThread")
public class NetRequest {
    private OkHttpClient client = new OkHttpClient();
    public byte[] request(final String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try {
            final ResponseBody body = client.newCall(request).execute().body();
            if (body != null) {
                return body.bytes();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
