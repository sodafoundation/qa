package com.opensds.utils.okhttputils;

import com.opensds.utils.Logger;
import okhttp3.*;

import java.io.IOException;

public abstract class OkHttpRequests {

    /**
     * Put Call
     *
     * @param client OkHttpClient object
     * @param url url
     * @param requestBody RequestBody object
     * @param headers Headers
     * @return response
     * @throws IOException io exception
     */
    protected static Response putCall(OkHttpClient client, String url, RequestBody requestBody,
                                      Headers headers) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .headers(headers)
                .build();
        Logger.logString("Request Details: " + request.headers() + " " + request.body() + " " + request.method() + ""
                + request.url());
        return client.newCall(request).execute();
    }

    /**
     * Post Call
     *
     * @param client OkHttpClient object
     * @param url url
     * @param requestBody RequestBody object
     * @param headers Headers
     * @return Response
     * @throws IOException IO Exception
     */
    protected static Response postCall(OkHttpClient client, String url, RequestBody requestBody,
                                       Headers headers) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .headers(headers)
                .build();
        Logger.logString("Request Details: " + request.headers() + " " + request.body() + " " + request.method() + ""
                + request.url());
        return client.newCall(request).execute();
    }

    /**
     * Get Call
     *
     * @param client OkHttpClient
     * @param url URL
     * @param headers Headers
     * @return Response
     * @throws IOException IO Exception
     */
    protected static Response getCall(OkHttpClient client, String url,
                                      Headers headers) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .headers(headers)
                .build();
        Logger.logString("Request Details: " + request.headers() + " " + request.body() + " " + request.method() + ""
                + request.url());
        return client.newCall(request).execute();
    }

    /**
     * Delete Call
     *
     * @param client OkHttpClient
     * @param url URL
     * @param headers Headers
     * @return Response
     * @throws IOException IO Exception
     */
    protected static Response deleteCall(OkHttpClient client, String url,
                                      Headers headers) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .headers(headers)
                .build();
        Logger.logString("Request Details: " + request.headers() + " " + request.body() + " " + request.method() + ""
                + request.url());
        return client.newCall(request).execute();
    }
}
