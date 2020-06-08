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
package com.io.sodafoundation.utils.okhttputils;

import com.io.sodafoundation.jsonmodels.akskresponses.SignatureKey;
import com.io.sodafoundation.utils.BinaryUtils;
import com.io.sodafoundation.utils.HeadersName;
import com.io.sodafoundation.utils.Logger;
import com.io.sodafoundation.utils.Utils;
import com.io.sodafoundation.utils.signature.SodaV4Signer;
import okhttp3.*;
import uk.co.lucasweb.aws.v4.signer.Header;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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


    protected static Response getCallResponse(OkHttpClient client, String url, SignatureKey signatureKey) {
        Response response = null;
        String payload = BinaryUtils.toHex(BinaryUtils.hash(""));
        String date = Utils.getDate();
        Logger.log("URL: " + url);
        String host = Utils.getHostAndPort(url);
        Header[] signHeaders = new Header[]{new Header(HeadersName.HOST, host),
                new Header(HeadersName.X_AMZ_DATE, date),
                new Header(HeadersName.X_AMZ_CONTENT_SHA256, payload)};
        String authorization = SodaV4Signer.getSignature("GET", url, signatureKey.getAccessKey(),
                signatureKey.getSecretAccessKey(), payload, signatureKey.getRegionName(), signHeaders);
        Logger.log("Authorization: " + authorization);
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(HeadersName.AUTHORIZATION, authorization);
        headersMap.put(HeadersName.X_AMZ_CONTENT_SHA256, payload);
        headersMap.put(HeadersName.X_AMZ_DATE, date);
        Headers headers = Headers.of(headersMap);
        try {
            response = getCall(client, url, headers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    protected static Response putCallResponse(OkHttpClient client, String url,
                                              String payload, RequestBody requestBody,
                                              SignatureKey signatureKey) {
        Response response = null;

        String date = Utils.getDate();
        Logger.log("URL: " + url);
        String host = Utils.getHostAndPort(url);
        Header[] signHeaders = new Header[]{new Header(HeadersName.HOST, host),
                new Header(HeadersName.X_AMZ_DATE, date),
                new Header(HeadersName.X_AMZ_CONTENT_SHA256, payload)};
        String authorization = SodaV4Signer.getSignature("PUT", url, signatureKey.getAccessKey(),
                signatureKey.getSecretAccessKey(), payload, signatureKey.getRegionName(), signHeaders);
        Logger.log("Authorization: " + authorization);
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(HeadersName.HOST, host);
        headersMap.put(HeadersName.AUTHORIZATION, authorization);
        headersMap.put(HeadersName.X_AMZ_CONTENT_SHA256, payload);
        headersMap.put(HeadersName.CONTENT_TYPE, HeadersName.CONTENT_TYPE_XML);
        headersMap.put(HeadersName.X_AMZ_DATE, date);
        Headers headers = Headers.of(headersMap);
        try {
            response = putCall(client, url, requestBody, headers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    protected static Response getCallWithXauth(OkHttpClient client, String url, String xAuthToken){
        Response response = null;
        Logger.log("URL: "+url);
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(HeadersName.X_AUTH_TOKEN, xAuthToken);
        headersMap.put(HeadersName.CONTENT_TYPE, HeadersName.CONTENT_TYPE_JSON);
        Headers headers = Headers.of(headersMap);
        try {
            response = getCall(client, url, headers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    protected static Response deleteCallWithXauth(OkHttpClient client, String url, String xAuthToken){
        Response response = null;
        Logger.log("URL: "+url);
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(HeadersName.X_AUTH_TOKEN, xAuthToken);
        headersMap.put(HeadersName.CONTENT_TYPE, HeadersName.CONTENT_TYPE_JSON);
        Headers headers = Headers.of(headersMap);
        try {
            response = deleteCall(client, url, headers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    protected static Response deleteCallWithV4Sign(OkHttpClient client, String url, SignatureKey signatureKey) {
        Response response = null;
        String payload = BinaryUtils.toHex(BinaryUtils.hash(""));
        String date = Utils.getDate();
        Logger.log("URL: " + url);
        String host = Utils.getHostAndPort(url);
        Header[] signHeaders = new Header[]{new Header(HeadersName.HOST, host),
                new Header(HeadersName.X_AMZ_DATE, date),
                new Header(HeadersName.X_AMZ_CONTENT_SHA256, payload)};
        String authorization = SodaV4Signer.getSignature("DELETE", url, signatureKey.getAccessKey(),
                signatureKey.getSecretAccessKey(), payload, signatureKey.getRegionName(), signHeaders);
        Logger.log("Authorization: " + authorization);
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(HeadersName.AUTHORIZATION, authorization);
        headersMap.put(HeadersName.X_AMZ_CONTENT_SHA256, payload);
        headersMap.put(HeadersName.X_AMZ_DATE, date);
        Headers headers = Headers.of(headersMap);
        try {
            response = deleteCall(client, url, headers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    protected static Response postCallWithXauth(OkHttpClient client, String url,
                                                String xAuthToken, RequestBody requestBody) {
        Response response = null;
        Logger.log("URL: " + url);
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(HeadersName.CONTENT_TYPE, HeadersName.CONTENT_TYPE_JSON);
        headersMap.put(HeadersName.X_AUTH_TOKEN, xAuthToken);
        Headers headers = Headers.of(headersMap);
        try {
            response = postCall(client, url, requestBody, headers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
