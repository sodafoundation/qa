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
package com.io.sodafoundation.httphandler;

import com.google.gson.Gson;
import com.io.sodafoundation.jsonmodels.akskresponses.AKSKHolder;
import com.io.sodafoundation.jsonmodels.akskresponses.SignatureKey;
import com.io.sodafoundation.jsonmodels.authtokensrequests.Project;
import com.io.sodafoundation.jsonmodels.authtokensrequests.Scope;
import com.io.sodafoundation.jsonmodels.authtokensrequests.Token;
import com.io.sodafoundation.jsonmodels.authtokensresponses.AuthTokenHolder;
import com.io.sodafoundation.jsonmodels.inputs.addbackend.AddBackendInputHolder;
import com.io.sodafoundation.jsonmodels.inputs.createbucket.CreateBucketFileInput;
import com.io.sodafoundation.jsonmodels.logintokensrequests.*;
import com.io.sodafoundation.jsonmodels.tokensresponses.TokenHolder;
import com.io.sodafoundation.jsonmodels.typesresponse.TypesHolder;
import com.io.sodafoundation.utils.BinaryUtils;
import com.io.sodafoundation.utils.ConstantUrl;
import com.io.sodafoundation.utils.Logger;
import com.io.sodafoundation.utils.okhttputils.OkHttpRequests;
import okhttp3.*;
import okio.BufferedSink;
import okio.Okio;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.io.sodafoundation.utils.Constant.*;
import static com.io.sodafoundation.utils.HeadersName.*;

/**
 *  In this class handled http request.
 */
public class HttpHandler extends OkHttpRequests {
    private OkHttpClient client = new OkHttpClient();

    public SignatureKey getAkSkList(String xAuthToken, String userId) {
        SignatureKey signatureKey = new SignatureKey();
        String url = ConstantUrl.getInstance().getAksList(userId);
        Logger.log("URL: " + url);
        try {
            Gson gson = new Gson();
            Map<String, String> headersMap = new HashMap<>();
            headersMap.put(CONTENT_TYPE, CONTENT_TYPE_JSON);
            headersMap.put(X_AUTH_TOKEN, xAuthToken);
            Headers headers = Headers.of(headersMap);
            Response response = getCall(client, url, headers);
            String responseBody = response.body().string();
            Logger.log("Response: " + responseBody);
            AKSKHolder akskHolder = gson.fromJson(responseBody, AKSKHolder.class);
            Logger.log(akskHolder);
            // build the SignatureKey struct and set the values
            ((Runnable) () -> {
                signatureKey.setSecretAccessKey(akskHolder.getCredentials()[0].getBlobObj().getSecret());
                signatureKey.setAccessKey(akskHolder.getCredentials()[0].getBlobObj().getAccess());
                String regionName = "default";
                signatureKey.setRegionName(regionName);
                String serviceName = "s3";
                signatureKey.setServiceName(serviceName);
            }).run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signatureKey;
    }

    public TokenHolder loginAndGetToken() {
        TokenHolder tokenHolder = null;
        try {
            Auth auth = new Auth();
            auth.setIdentity(new Identity());
            auth.getIdentity().getMethods().add("password");
            auth.getIdentity().setPassword(new Password());
            auth.getIdentity().getPassword().setUser(new User());
            auth.getIdentity().getPassword().getUser().setName("admin");
            auth.getIdentity().getPassword().getUser().setPassword("opensds@123");
            auth.getIdentity().getPassword().getUser().setDomain(new Domain());
            auth.getIdentity().getPassword().getUser().getDomain().setName("Default");

            AuthHolder authHolder = new AuthHolder();
            authHolder.setAuth(auth);

            Gson gson = new Gson();
            RequestBody requestBody = RequestBody.create(gson.toJson(authHolder),
                    MediaType.parse(CONTENT_TYPE_JSON_CHARSET));
            String url = ConstantUrl.getInstance().getTokenLogin();
            Logger.log("URL: " + url);
            Map<String, String> headersMap = new HashMap<>();
            headersMap.put(CONTENT_TYPE, CONTENT_TYPE_JSON);
            Headers  headers = Headers.of(headersMap);
            Response response = postCall(client, url, requestBody, headers);
            String responseBody = response.body().string();
            Logger.log("Response code: " + response.code());
            Logger.log("Response body: " + responseBody);
            tokenHolder = gson.fromJson(responseBody, TokenHolder.class);
            tokenHolder.setResponseHeaderSubjectToken(response.header(X_SUBJECT_TOKEN));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tokenHolder;
    }

    public AuthTokenHolder getAuthToken(String x_auth_token) {
        AuthTokenHolder tokenHolder = null;
        try {
            com.io.sodafoundation.jsonmodels.authtokensrequests.Auth auth = new com.io.sodafoundation.jsonmodels.authtokensrequests.Auth();
            auth.setIdentity(new com.io.sodafoundation.jsonmodels.authtokensrequests.Identity());
            auth.getIdentity().getMethods().add("token");
            auth.getIdentity().setToken(new Token(x_auth_token));

            auth.setScope(new Scope());
            auth.getScope().setProject(new Project());
            auth.getScope().getProject().setName("admin");
            auth.getScope().getProject().setDomain(new com.io.sodafoundation.jsonmodels.authtokensrequests.Domain());
            auth.getScope().getProject().getDomain().setId("default");
            com.io.sodafoundation.jsonmodels.authtokensrequests.AuthHolder authHolder = new com.io.sodafoundation.jsonmodels.authtokensrequests.AuthHolder();
            authHolder.setAuth(auth);

            Gson gson = new Gson();
            RequestBody requestBody = RequestBody.create(gson.toJson(authHolder),
                    MediaType.parse(CONTENT_TYPE_JSON_CHARSET));
            String url = ConstantUrl.getInstance().getTokenLogin();
            Logger.log("URL: " + url);
            Map<String, String> headersMap = new HashMap<>();
            headersMap.put(CONTENT_TYPE, CONTENT_TYPE_JSON);
            Headers  headers = Headers.of(headersMap);
            Response response = postCall(client, url, requestBody, headers);
            String responseBody = response.body().string();
            Logger.log("Response code: " + response.code());
            Logger.log("Response body: " + responseBody);
            tokenHolder = new AuthTokenHolder();
            tokenHolder = gson.fromJson(responseBody, AuthTokenHolder.class);
            tokenHolder.setResponseHeaderSubjectToken(response.header(X_SUBJECT_TOKEN));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tokenHolder;
    }

    public TypesHolder getTypes(String x_auth_token, String projId) {
        TypesHolder typesHolder = null;
        try {
            Gson gson = new Gson();
            String url = ConstantUrl.getInstance().getTypesUrl(projId);
            Logger.log("URL: " + url);
            Map<String, String> headersMap = new HashMap<>();
            headersMap.put(CONTENT_TYPE, CONTENT_TYPE_JSON);
            headersMap.put(X_AUTH_TOKEN, x_auth_token);
            Headers headers = Headers.of(headersMap);
            Response response = getCall(client, url, headers);
            String responseBody = response.body().string();
            Logger.log("Response code: " + response.code());
            Logger.log("Response body: " + responseBody);
            typesHolder = gson.fromJson(responseBody, TypesHolder.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return typesHolder;
    }

    public int addBackend(String xAuthToken, String tenantId, AddBackendInputHolder inputHolder) {
        Gson gson = new Gson();
        RequestBody requestBody = RequestBody.create(gson.toJson(inputHolder),
                MediaType.parse(CONTENT_TYPE_JSON_CHARSET));
        String url = ConstantUrl.getInstance().getAddBackendUrl(tenantId);
        Response response = postCallWithXauth(client, url, xAuthToken, requestBody);
        int code = response.code();
        Logger.log("Response code: " + code);
        return code;
    }

    public int createBucket(CreateBucketFileInput input, String bucketName, SignatureKey signatureKey) {
        int code = -1;
        try {
            String url = ConstantUrl.getInstance().getCreateBucketUrl(bucketName);
            RequestBody requestBody = RequestBody.create(input.getXmlPayload(),
                    MediaType.parse(CONTENT_TYPE_XML));
            String payload = BinaryUtils.toHex(BinaryUtils.hash(input.getXmlPayload()));
            Response response = putCallResponse(client, url, payload, requestBody, signatureKey);
            code = response.code();
            Logger.log("Response Code: " + code);
            Logger.log("Response: " + response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    public Response getBuckets(SignatureKey signatureKey) {
        String url = ConstantUrl.getInstance().getListBucketUrl();
        return getCallResponse(client, url, signatureKey);
    }

    public int uploadObject(SignatureKey signatureKey, String bucketName, String fileName, File mFilePath) {
        int code = -1;
        try {
            RequestBody requestBody = RequestBody.create(mFilePath,
                    MediaType.parse(CONTENT_TYPE_XML));
            String url = ConstantUrl.getInstance().getUploadObjectUrl(bucketName, fileName);
            String payload = BinaryUtils.toHex(BinaryUtils.computeSHA256TreeHash(mFilePath));
            Response response = putCallResponse(client, url, payload, requestBody, signatureKey);
            code = response.code();
            Logger.log("Response Code: " + code);
            Logger.log("Response: " + response.body().string());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    public Response getBucketObjects(String bucketName, SignatureKey signatureKey) {
        String url = ConstantUrl.getInstance().getListOfBucketObjectUrl(bucketName);
        return getCallResponse(client, url, signatureKey);
    }

    public Response downloadObject(SignatureKey signatureKey, String bucketName, String fileName, String downloadFileName) {
        Response response = null;
        try {
            String url = ConstantUrl.getInstance().getDownloadObjectUrl(bucketName, fileName);
            response = getCallResponse(client, url, signatureKey);
            int code = response.code();
            if (code == 200) {
                BufferedSink sink = Okio.buffer(Okio.sink(new File(DOWNLOAD_FILES_PATH, downloadFileName)));
                sink.writeAll(response.body().source());
                sink.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response getBackends(String xAuthToken, String tenantId) {
        String url = ConstantUrl.getInstance().getBackendsUrl(tenantId);
        return getCallWithXauth(client, url, xAuthToken);
    }

    public Response getBackend(String xAuthToken, String tenantId, String id) {
        String url = ConstantUrl.getInstance().getBackendUrl(tenantId, id);
        return getCallWithXauth(client, url, xAuthToken);
    }

    public Response getDeleteBackend(String xAuthToken, String tenantId, String id) {
        String url = ConstantUrl.getInstance().getDeleteBackendUrl(tenantId, id);
        return deleteCallWithXauth(client, url, xAuthToken);
    }

    public Response deleteBucketNotEmpty(SignatureKey signatureKey, String bucketName) {
        String url = ConstantUrl.getInstance().getDeleteBucketUrl(bucketName);
        return deleteCallWithV4Sign(client, url, signatureKey);
    }

    public Response deleteObject(SignatureKey signatureKey, String bucketName, String objectName) {
        String url = ConstantUrl.getInstance().getDeleteObjectUrl(bucketName, objectName);
        return deleteCallWithV4Sign(client, url, signatureKey);
    }

    public Response deleteBucket(SignatureKey signatureKey, String bucketName) {
        String url = ConstantUrl.getInstance().getDeleteBucketUrl(bucketName);
        return deleteCallWithV4Sign(client, url, signatureKey);
    }

    public Response createPlans(String xAuthToken, String body, String tenantId) {
        Response response = null;
        RequestBody requestBody = RequestBody.create(body,
                MediaType.parse(CONTENT_TYPE_JSON_CHARSET));
        String url = ConstantUrl.getInstance().getCreatePlansUrl(tenantId);
        Logger.log("URL: "+url);
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(X_AUTH_TOKEN, xAuthToken);
        headersMap.put(CONTENT_TYPE, CONTENT_TYPE_JSON);
        Headers headers = Headers.of(headersMap);
        try {
            response = postCall(client, url, requestBody, headers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response runPlans(String xAuthToken, String id, String tenantId) {
        Response response = null;
        RequestBody requestBody = RequestBody.create("",
                MediaType.parse(CONTENT_TYPE_JSON_CHARSET));
        String url = ConstantUrl.getInstance().getRunPlanUrl(tenantId, id);
        Logger.log("URL: "+url);
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(X_AUTH_TOKEN, xAuthToken);
        headersMap.put(CONTENT_TYPE, CONTENT_TYPE_JSON);
        Headers headers = Headers.of(headersMap);
        try {
            response = postCall(client, url, requestBody, headers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response getJob(String xAuthToken, String jobId, String tenantId) {
        String url = ConstantUrl.getInstance().getJobUrl(tenantId, jobId);
        return getCallWithXauth(client, url, xAuthToken);
    }

    public Response getPlansList(String xAuthToken, String tenantId) {
        String url = ConstantUrl.getInstance().getPlansListUrl(tenantId);
        return getCallWithXauth(client, url, xAuthToken);
    }

    public Response deletePlan(String xAuthToken, String tenantId, String id) {
        String url = ConstantUrl.getInstance().getDeletePlansUrl(tenantId, id);
        return deleteCallWithXauth(client, url, xAuthToken);
    }

    public Response getJobsList(String xAuthToken, String tenantId) {
        String url = ConstantUrl.getInstance().getListJobUrl(tenantId);
        return getCallWithXauth(client, url, xAuthToken);
    }

    public Response createPlanPolicies(String xAuthToken, String body, String tenantId) {
        RequestBody requestBody = RequestBody.create(body, MediaType.parse(CONTENT_TYPE_JSON_CHARSET));
        String url = ConstantUrl.getInstance().getPoliciesUrl(tenantId);
        return postCallWithXauth(client, url, xAuthToken, requestBody);
    }

    public Response scheduleMigStatus(String xAuthToken, String tenantId, String planeName) {
        String url = ConstantUrl.getInstance().getScheduleMigStatusUrl(tenantId, planeName);
        return getCallWithXauth(client, url, xAuthToken);
    }

    public Response createLifecycle(String input, String bucketName, SignatureKey signatureKey) {
        Response response = null;
        try {
            String url = ConstantUrl.getInstance().getCreateLifeCycleUrl(bucketName);
            RequestBody requestBody = RequestBody.create(input,
                    MediaType.parse(CONTENT_TYPE_XML));
            String payload = BinaryUtils.toHex(BinaryUtils.hash(input));
            response = putCallResponse(client, url, payload, requestBody, signatureKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response ListOfLifecycle(String bucketName, SignatureKey signatureKey) {
        String url = ConstantUrl.getInstance().getListOfLifeCycleUrl(bucketName);
        return getCallResponse(client, url, signatureKey);
    }

    public Response deleteLifecycle(String bucketName, String ruleName, SignatureKey signatureKey) {
        String url = ConstantUrl.getInstance().getDeleteLifecycleUrl(bucketName, ruleName);
        return deleteCallWithV4Sign(client, url, signatureKey);
    }
}
