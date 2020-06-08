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

import com.google.gson.Gson;
import com.io.sodafoundation.httphandler.HttpHandler;
import com.io.sodafoundation.jsonmodels.akskresponses.SignatureKey;
import com.io.sodafoundation.jsonmodels.authtokensresponses.AuthTokenHolder;
import com.io.sodafoundation.jsonmodels.inputs.addbackend.AddBackendInputHolder;
import com.io.sodafoundation.jsonmodels.inputs.createbucket.CreateBucketFileInput;
import com.io.sodafoundation.jsonmodels.tokensresponses.TokenHolder;
import com.io.sodafoundation.jsonmodels.typesresponse.Type;
import com.io.sodafoundation.jsonmodels.typesresponse.TypesHolder;
import com.io.sodafoundation.utils.Constant;
import com.io.sodafoundation.utils.Logger;
import com.io.sodafoundation.utils.TextUtils;
import com.io.sodafoundation.utils.Utils;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static com.io.sodafoundation.utils.Constant.*;

public class BaseTestClass {
    public static AuthTokenHolder getAuthTokenHolder() {
        return mAuthTokenHolder;
    }

    public TypesHolder getTypesHolder() {
        return mTypesHolder;
    }

    public static HttpHandler getHttpHandler() {
        return mHttpHandler;
    }

    private static AuthTokenHolder mAuthTokenHolder = null;
    private static TypesHolder mTypesHolder = null;
    private static HttpHandler mHttpHandler = new HttpHandler();

    @org.junit.jupiter.api.BeforeAll
    static void setUp() {
        TokenHolder tokenHolder = getHttpHandler().loginAndGetToken();
        mAuthTokenHolder = getHttpHandler().getAuthToken(tokenHolder.getResponseHeaderSubjectToken());
        mTypesHolder = getHttpHandler().getTypes(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                getAuthTokenHolder().getToken().getProject().getId());
    }

    public void testCreateBucketAndBackend(String path) throws IOException, JSONException {
        // load input files for each type and create the backend
        for (Type t : getTypesHolder().getTypes()) {
            List<File> listOfIInputsForType =
                    Utils.listFilesBeginsWithPattern(t.getName(), path);
            Gson gson = new Gson();
            // add the backend specified in each file
            for (File file : listOfIInputsForType) {
                String content = Utils.readFileContentsAsString(file);
                Assertions.assertNotNull(content);

                AddBackendInputHolder inputHolder = gson.fromJson(content, AddBackendInputHolder.class);
                int code = getHttpHandler().addBackend(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                        getAuthTokenHolder().getToken().getProject().getId(),
                        inputHolder);
                assertEquals(200, code);

                // backend added, now create buckets
                List<File> listOfIBucketInputs =
                        Utils.listFilesBeginsWithPattern("bucket", path);
                SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                        getAuthTokenHolder().getToken().getProject().getId());
                // create the bucket specified in each file
                for (File bucketFile : listOfIBucketInputs) {
                    String bucketContent = Utils.readFileContentsAsString(bucketFile);
                    Assertions.assertNotNull(bucketContent);
                    CreateBucketFileInput bfi = gson.fromJson(bucketContent, CreateBucketFileInput.class);
                    // filename format is "bucket_<bucketname>.json", get the bucket name here
                    String bName = Utils.getFileNameFromDelim(bucketFile);
                    // now create buckets
                    int cbCode = getHttpHandler().createBucket(bfi, bName, signatureKey);
                    assertEquals(200, cbCode);
                    boolean isBucketExist = testGetListBuckets(bName, signatureKey);
                    assertTrue(isBucketExist, "Bucket is not exist: ");
                }
            }
        }
    }

    public void testDownloadObject(String path, String objectName) throws IOException {
        List<File> listOfIBucketInputs =
                Utils.listFilesBeginsWithPattern("bucket", path);
        for (File bucketFile : listOfIBucketInputs) {
            String bucketContent = Utils.readFileContentsAsString(bucketFile);
            assertNotNull(bucketContent);
            String bucketName = Utils.getFileNameFromDelim(bucketFile);
            // Get object for upload.
            File fileRawData = new File(RAW_DATA_PATH);
            File[] files = fileRawData.listFiles();
            String mFileName = null;
            for (File fileName : files) {
                mFileName = fileName.getName();
            }
            String fileName = bucketName+objectName;
            File filePath = new File(DOWNLOAD_FILES_PATH);
            File downloadedFile = new File(DOWNLOAD_FILES_PATH, fileName);
            if (filePath.exists()) {
                if (downloadedFile.exists()) {
                    boolean isDownloadedFileDeleted = downloadedFile.delete();
                    assertTrue(isDownloadedFileDeleted, "Image deleting is failed");
                } else {
                    assertFalse(downloadedFile.exists());
                }
            } else {
                filePath.mkdirs();
            }
            SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                    getAuthTokenHolder().getToken().getProject().getId());
            Response response = getHttpHandler().downloadObject(signatureKey,
                    bucketName, mFileName, fileName);
            int code = response.code();
            String body = response.body().string();
            Logger.log("Response Code: " + code);
            Logger.log("Response: " + body);
            assertEquals("Downloading failed",200, code);
            assertTrue(downloadedFile.isFile(), "Downloaded Image is not available");
        }
    }

    public void testUploadObject(String path) throws IOException, JSONException {
        List<File> listOfIBucketInputs =
                Utils.listFilesBeginsWithPattern("bucket", path);
        // Get bucket name.
        for (File bucketFile : listOfIBucketInputs) {
            String bucketContent = Utils.readFileContentsAsString(bucketFile);
            assertNotNull(bucketContent);
            String bucketName = Utils.getFileNameFromDelim(bucketFile);
            // Get object for upload.
            File fileRawData = new File(Constant.RAW_DATA_PATH);
            File[] files = fileRawData.listFiles();
            String mFileName;
            File mFilePath;
            for (File fileName : files) {
                mFileName = fileName.getName();
                mFilePath = fileName;
                SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                        getAuthTokenHolder().getToken().getProject().getId());
                int cbCode = getHttpHandler().uploadObject(signatureKey,
                        bucketName, mFileName, mFilePath);
                assertEquals("Uploaded object failed", 200, cbCode);
                //Verifying object is uploaded in bucket.
                boolean isUploaded = testGetListOfObjectFromBucket(bucketName, mFileName, signatureKey);
                assertTrue(isUploaded,"Object is not uploaded");
            }
        }
    }

    public void testGetPlansListAndDelete() throws IOException, JSONException {
        Response  response = getHttpHandler().getPlansList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                getAuthTokenHolder().getToken().getProject().getId());
        String jsonRes = response.body().string();
        int code = response.code();
        Logger.log("Response: "+jsonRes);
        Logger.log("Response Code: "+code);
        assertEquals("Get plans list failed: Response code not matched: ",200, code);
        JSONArray jsonArray = new JSONObject(jsonRes).getJSONArray("plans");
        for (int i = 0; i < jsonArray.length(); i++) {
            String id = jsonArray.getJSONObject(i).get("id").toString();
            Response responseDeletePlan = getHttpHandler().deletePlan(getAuthTokenHolder()
                    .getResponseHeaderSubjectToken(), getAuthTokenHolder().getToken().getProject().getId(), id);
            String deletePlanResponse = responseDeletePlan.body().string();
            int deletePlanResponseCode = responseDeletePlan.code();
            Logger.log("Response: "+deletePlanResponse);
            Logger.log("Response Code: "+deletePlanResponseCode);
            assertEquals("Get plans list failed: Response code not matched: ",200, deletePlanResponseCode);
        }
    }

    /**
     * Get bucket list.
     *
     * @param bName Bucket name
     * @param signatureKey Signature key object
     * @return boolean If bucket is exist return true else false
     * @throws JSONException Json exception
     * @throws IOException Io exception
     */
    protected boolean testGetListBuckets(String bName, SignatureKey signatureKey)
            throws JSONException, IOException {
        Response listBucketResponse = getHttpHandler().getBuckets(signatureKey);
        int resCode = listBucketResponse.code();
        String responseBody = listBucketResponse.body().string();
        Logger.log("Response Code: " + resCode);
        Logger.log("Response: " + responseBody);
        JSONObject jsonObject = XML.toJSONObject(responseBody);
        JSONArray jsonObjectBucketList = jsonObject.getJSONObject("ListAllMyBucketsResult")
                .getJSONObject("Buckets").getJSONArray("Bucket");
        boolean isBucketExist = false;
        for (int i = 0; i < jsonObjectBucketList.length(); i++) {
            String bucketName = jsonObjectBucketList.getJSONObject(i).get("Name").toString();
            if (!TextUtils.isEmpty(bucketName)) {
                if (bucketName.equals(bName)) {
                    isBucketExist = true;
                }
            }
        }
        return isBucketExist;
    }

    /**
     * Get List of object from bucket
     *
     * @param bucketName   bucket name
     * @param fileName     file name
     * @param signatureKey signature key object
     * @return boolean object is upload then true else false
     * @throws JSONException json exception
     * @throws IOException   io exception
     */
    protected boolean testGetListOfObjectFromBucket(String bucketName, String fileName,
                                                    SignatureKey signatureKey)
            throws JSONException, IOException {
        Response listObjectResponse = getHttpHandler().getBucketObjects(bucketName, signatureKey);
        int resCode = listObjectResponse.code();
        String resBody = listObjectResponse.body().string();
        Logger.log("Response Code: " + resCode);
        Logger.log("Response: " + resBody);
        assertEquals("Get list of object failed",200, resCode);
        JSONObject jsonObject = XML.toJSONObject(resBody);
        JSONObject jsonObjectListBucket = jsonObject.getJSONObject("ListBucketResult");
        boolean isUploaded = false;
        if (jsonObjectListBucket.has("Contents")) {
            if (jsonObjectListBucket.get("Contents") instanceof JSONArray) {
                JSONArray objects = jsonObjectListBucket.getJSONArray("Contents");
                for (int i = 0; i < objects.length(); i++) {
                    if (!TextUtils.isEmpty(objects.getJSONObject(i).get("Key").toString())) {
                        if (objects.getJSONObject(i).get("Key").toString().equals(fileName)) {
                            isUploaded = true;
                        }
                    }
                }
            } else {
                if (!TextUtils.isEmpty(jsonObjectListBucket.getJSONObject("Contents")
                        .get("Key").toString())) {
                    if (jsonObjectListBucket.getJSONObject("Contents").get("Key").toString()
                            .equals(fileName)) {
                        isUploaded = true;
                    }
                }
            }
        }
        return isUploaded;
    }
}
