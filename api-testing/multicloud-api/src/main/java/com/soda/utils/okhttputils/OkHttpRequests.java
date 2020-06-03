/*
  Copyright 2020 The SODA Authors.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.soda.utils.okhttputils;

import com.soda.utils.Logger;
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
        Logger.log("Request Details: " + request.headers() + " " + request.body() + " " + request.method() + ""
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
        Logger.log("Request Details: " + request.headers() + " " + request.body() + " " + request.method() + ""
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
        Logger.log("Request Details: " + request.headers() + " " + request.body() + " " + request.method() + ""
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
        Logger.log("Request Details: " + request.headers() + " " + request.body() + " " + request.method() + ""
                + request.url());
        return client.newCall(request).execute();
    }
}
