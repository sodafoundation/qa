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
import com.io.sodafoundation.jsonmodels.akskresponses.SignatureKey;
import com.io.sodafoundation.jsonmodels.inputs.addbackend.AddBackendInputHolder;
import com.io.sodafoundation.jsonmodels.inputs.addbackend.Backends;
import com.io.sodafoundation.jsonmodels.inputs.addbackend.BackendsInputHolder;
import com.io.sodafoundation.jsonmodels.inputs.createbucket.CreateBucketFileInput;
import com.io.sodafoundation.jsonmodels.typesresponse.Type;
import com.io.sodafoundation.utils.Logger;
import com.io.sodafoundation.utils.TextUtils;
import com.io.sodafoundation.utils.Utils;
import okhttp3.Response;
import org.json.JSONException;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertEquals;
import static com.io.sodafoundation.utils.Constant.*;

// how to get POJO from any response JSON, use this site
// http://pojo.sodhanalibrary.com/

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CreateBucketBackendTest extends BaseTestClass {

    @Test
    @Order(1)
    @DisplayName("Test creating bucket and backend on OPENSDS")
    public void testCreateBucketAndBackend() throws IOException, JSONException {
        testCreateBucketAndBackend(CREATE_BUCKET_PATH);
    }

    @Test
    @Order(2)
    @DisplayName("Test creating bucket using Invalid name")
    public void testCreateBucketUsingInvalidName() {
        // load input files for each type.
        for (Type t : getTypesHolder().getTypes()) {
            List<File> listOfIInputsForType =
                    Utils.listFilesBeginsWithPattern(t.getName(),
                            CREATE_BUCKET_PATH);
            Gson gson = new Gson();
            // add the backend specified in each file
            for (File file : listOfIInputsForType) {
                String content = Utils.readFileContentsAsString(file);
                assertNotNull(content);
                //now create buckets
                List<File> listOfIBucketInputs =
                        Utils.listFilesBeginsWithPattern("bucket",
                                CREATE_BUCKET_PATH);
                SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                        getAuthTokenHolder().getToken().getProject().getId());
                // create the bucket specified in each file
                for (File bucketFile : listOfIBucketInputs) {
                    String bucketContent = Utils.readFileContentsAsString(bucketFile);
                    assertNotNull(bucketContent);
                    // filename format is "bucket_<bucketname>.json", get the bucket name here
                    CreateBucketFileInput bfi = gson.fromJson(bucketContent, CreateBucketFileInput.class);
                    // now create buckets
                    int cbCode = getHttpHandler().createBucket(bfi, "RATR_@#", signatureKey);
                    assertEquals(400, cbCode);
                }
            }
        }
    }

    @Test
    @Order(3)
    @DisplayName("Test re-creating backend with same name on OPENSDS")
    public void testReCreateBackend() {
        // load input files for each type and create the backend
        for (Type t : getTypesHolder().getTypes()) {
            List<File> listOfIInputsForType =
                    Utils.listFilesBeginsWithPattern(t.getName(),
                            CREATE_BUCKET_PATH);
            Gson gson = new Gson();
            // Re-create backend specified in each file
            for (File file : listOfIInputsForType) {
                String content = Utils.readFileContentsAsString(file);
                assertNotNull(content);
                AddBackendInputHolder inputHolder = gson.fromJson(content, AddBackendInputHolder.class);
                int code = getHttpHandler().addBackend(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                        getAuthTokenHolder().getToken().getProject().getId(),
                        inputHolder);
                assertEquals("Re-create backend with same name:Response code not matched:",409, code);
            }
        }
    }

    @Test
    @Order(4)
    @DisplayName("Test re-creating bucket with same name on OPENSDS")
    public void testReCreateBucket() {
        // load input files for each type
        for (Type t : getTypesHolder().getTypes()) {
            List<File> listOfIInputsForType =
                    Utils.listFilesBeginsWithPattern(t.getName(),
                            CREATE_BUCKET_PATH);
            Gson gson = new Gson();
            for (File file : listOfIInputsForType) {
                String content = Utils.readFileContentsAsString(file);
                assertNotNull(content);
                List<File> listOfIBucketInputs =
                        Utils.listFilesBeginsWithPattern("bucket",
                                CREATE_BUCKET_PATH);
                SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                        getAuthTokenHolder().getToken().getProject().getId());
                // create the bucket specified in each file
                for (File bucketFile : listOfIBucketInputs) {
                    String bucketContent = Utils.readFileContentsAsString(bucketFile);
                    assertNotNull(bucketContent);
                    CreateBucketFileInput bfi = gson.fromJson(bucketContent, CreateBucketFileInput.class);
                    // filename format is "bucket_<bucketname>.json", get the bucket name here
                    String bName = Utils.getFileNameFromDelim(bucketFile);
                    // now create buckets
                    int cbCode = getHttpHandler().createBucket(bfi, bName, signatureKey);
                    assertEquals("Re-create bucket with same name failed:Response code not matched: "
                            ,409, cbCode);
                }
            }
        }
    }

    @Test
    @Order(5)
    @DisplayName("Test create bucket with empty name")
    public void testCreateBucketWithEmptyName() throws IOException, JSONException {
        Logger.log("Verifying response code: Input (Backend name) with empty value in payload and bucket" +
                " name is empty");
        SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                getAuthTokenHolder().getToken().getProject().getId());
        File bucketFile = new File(EMPTY_FIELD_PATH, "bucket_emptyvalue.json");
        String bucketContent = Utils.readFileContentsAsString(bucketFile);
        assertNotNull(bucketContent);
        Gson gson = new Gson();
        CreateBucketFileInput bfi = gson.fromJson(bucketContent, CreateBucketFileInput.class);
        int cbCode = getHttpHandler().createBucket(bfi, "", signatureKey);
        assertEquals("Bucket name and backend name is empty in payload :Response code not matched: "
                ,405, cbCode);
        boolean isBucketExist = testGetListBuckets("", signatureKey);
        assertFalse(isBucketExist);

        File file = new File(EMPTY_FIELD_PATH, "bucket_b1324.json");
        String content = Utils.readFileContentsAsString(file);
        assertNotNull(content);
        String bName = file.getName().substring(bucketFile.getName().indexOf("_") + 1,
                bucketFile.getName().indexOf("."));

        CreateBucketFileInput input = gson.fromJson(content, CreateBucketFileInput.class);
        int code = getHttpHandler().createBucket(input, bName, signatureKey);
        Logger.log("Verifying response code: In input (Backend name) with not valid value but bucket name is valid");
        assertEquals("Backend does not exist:Response code not matched: ",404, code);
    }

    @Test
    @Order(6)
    @DisplayName("Test request body with empty value,try to create backend")
    public void testRequestBodyWithEmptyFieldBackend() {
        Gson gson = new Gson();
        File file = new File(EMPTY_FIELD_PATH+"ibm-cos_b1321.json");
        String content = Utils.readFileContentsAsString(file);
        assertNotNull(content);
        AddBackendInputHolder inputHolder = gson.fromJson(content, AddBackendInputHolder.class);
        int code = getHttpHandler().addBackend(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                getAuthTokenHolder().getToken().getProject().getId(),
                inputHolder);
        Logger.log("Backend Input: "+content);
        assertEquals("Request body with empty value:Response code not matched:",400, code);
    }

    @Test
    @Order(7)
    @DisplayName("Test uploading object in a bucket")
    public void testUploadObject() throws IOException, JSONException {
         testUploadObject(CREATE_BUCKET_PATH);
    }

    @Test
    @Order(8)
    @DisplayName("Test uploading object failed scenario")
    public void testUploadObjectFailed(){
        File fileRawData = new File(RAW_DATA_PATH);
        File[] files = fileRawData.listFiles();
        String mFileName = null;
        File mFilePath = null;
        for (File fileName : files) {
            mFileName = fileName.getName();
            mFilePath = fileName;
        }
        SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                getAuthTokenHolder().getToken().getProject().getId());
        int cbCode = getHttpHandler().uploadObject(signatureKey, "bucketName", mFileName, mFilePath);
        Logger.log("Verifying Upload object with non existing bucket");
        assertEquals("Upload object with non existing bucket: Response code not matched", 404, cbCode);

        List<File> listOfIBucketInputs =
                Utils.listFilesBeginsWithPattern("bucket",
                        CREATE_BUCKET_PATH);
        for (File bucketFile : listOfIBucketInputs) {
            String bucketName = Utils.getFileNameFromDelim(bucketFile);
            int code = getHttpHandler().uploadObject(signatureKey,
                    bucketName, "", mFilePath);
            Logger.log("Verifying upload object in existing bucket with file name is empty");
            assertEquals("Upload object with existing bucket with file name empty: Response code not matched"
                    , 400, code);
        }
    }

    @Test
    @Order(9)
    @DisplayName("Test verifying download non exist file")
    public void testDownloadNonExistFile() throws IOException {
        List<File> listOfIBucketInputs =
                Utils.listFilesBeginsWithPattern("bucket",
                        CREATE_BUCKET_PATH);
        SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                getAuthTokenHolder().getToken().getProject().getId());
        for (File bucketFile : listOfIBucketInputs) {
            String bucketContent = Utils.readFileContentsAsString(bucketFile);
            assertNotNull(bucketContent);
            String fileName = "download_image.jpg";
            String bucketName = Utils.getFileNameFromDelim(bucketFile);
            Response response = getHttpHandler().downloadObject(signatureKey,
                    bucketName, "23455", fileName);
            int code = response.code();
            String body = response.body().string();
            Logger.log("Response Code: " + code);
            Logger.log("Response: " + body);
            assertEquals("Downloading non exist file: ",404, code);
        }
    }

    @Test
    @Order(10)
    @DisplayName("Test verifying download file from non exist bucket")
    public void testDownloadFileFromNonExistBucket() throws IOException {
        String dFileName = "download_image.jpg";
        File fileRawData = new File(RAW_DATA_PATH);
        File[] files = fileRawData.listFiles();
        String mFileName = null;
        for (File fileName : files) {
            mFileName = fileName.getName();
        }
        SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                getAuthTokenHolder().getToken().getProject().getId());
        Response response = getHttpHandler().downloadObject(signatureKey,
                "hfhfhd", mFileName, dFileName);
        int code = response.code();
        String body = response.body().string();
        Logger.log("Response Code: " + code);
        Logger.log("Response: " + body);
        assertEquals("Downloading file from non exist bucket: ", 404, code);
    }

    @Test
    @Order(11)
    @DisplayName("Test verifying download file from non exist bucket and file name")
    public void testDownloadNonExistBucketAndFile() throws IOException {
        String fileName = "download_image.jpg";
        Logger.log("Verifying download file from non exist bucket and file name");
        SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                getAuthTokenHolder().getToken().getProject().getId());
        Response response = getHttpHandler().downloadObject(signatureKey, "ghjhb", "yuyiyh",
                fileName);
        int code = response.code();
        String body = response.body().string();
        Logger.log("Response Code: " + code);
        Logger.log("Response: " + body);
        assertEquals("Downloading file from non exist bucket and file name: ",404, code);
        assertEquals("Response message is not valid, bucket and filename not exist: ", body);
    }

    @Test
    @Order(12)
    @DisplayName("Test downloading object in a folder")
    public void testDownloadObject() throws IOException {
        testDownloadObject(CREATE_BUCKET_PATH,"download_image.jpg");
    }

    @Test
    @Order(13)
    @DisplayName("Test verifying backends list and single backend")
    public void testAddBackendGetBackends() throws IOException {
        for (Type t : getTypesHolder().getTypes()) {
            List<File> listOfIInputsForType =
                    Utils.listFilesBeginsWithPattern(t.getName(),
                            CREATE_BUCKET_PATH);
            Gson gson = new Gson();
            for (File file : listOfIInputsForType) {
                String content = Utils.readFileContentsAsString(file);
                assertNotNull(content);
                AddBackendInputHolder inputHolder = gson.fromJson(content, AddBackendInputHolder.class);
                Response response = getHttpHandler().getBackends(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                        getAuthTokenHolder().getToken().getProject().getId());
                int code = response.code();
                String responseBody = response.body().string();
                Logger.log("Response Code: " + code);
                assertEquals("Get Backends List Failed: ",200, code);
                assertNotNull(responseBody);
                BackendsInputHolder backendsInputHolder = gson.fromJson(responseBody,
                        BackendsInputHolder.class);
                // Filter backend
                List<Backends> backendFilter = backendsInputHolder.getBackends().stream()
                        .filter(p -> !TextUtils.isEmpty(p.getName()))
                        .collect(Collectors.toList());

                List<Backends> backend = backendFilter.stream()
                        .filter(p -> p.getName().equals(inputHolder.getName()))
                        .collect(Collectors.toList());

                assertNotNull(backend);

                // Get backend
                for (int i = 0; i < backend.size(); i++) {
                    Response responseBackend = getHttpHandler().getBackend(getAuthTokenHolder()
                                    .getResponseHeaderSubjectToken(), getAuthTokenHolder().getToken()
                            .getProject().getId(), backend.get(i).getId());
                    int resCode = response.code();
                    String responseBackendBody = responseBackend.body().string();
                    Logger.log("Response Code: " + resCode);
                    assertEquals(200, resCode);
                    assertNotNull(responseBackendBody);
                    Backends backends = gson.fromJson(responseBackendBody, Backends.class);
                    assertNotNull(backends);
                    assertEquals("Backend name not matched: ",inputHolder.getName(), backends.getName());
                }
            }
        }
    }

    @Test
    @Order(14)
    @DisplayName("Test verifying non exist backend")
    public void testNonExistBackend() {
        Response responseBackend = getHttpHandler().getBackend(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                getAuthTokenHolder().getToken().getProject().getId(), "reuiu5475");
        int code = responseBackend.code();
        Logger.log("Response Code: " + code);
        assertEquals("Get backend failed:Response code not matched: ", 400, code);
    }

    @Test
    @Order(15)
    @DisplayName("Test verifying delete non empty backend")
    public void testDeleteNonEmptyBackend() throws IOException {
        for (Type t : getTypesHolder().getTypes()) {
            List<File> listOfIInputsForType =
                    Utils.listFilesBeginsWithPattern(t.getName(),
                            CREATE_BUCKET_PATH);
            Gson gson = new Gson();
            for (File file : listOfIInputsForType) {
                String content = Utils.readFileContentsAsString(file);
                assertNotNull(content);
                AddBackendInputHolder inputHolder = gson.fromJson(content, AddBackendInputHolder.class);
                // Get backend list
                Response response = getHttpHandler().getBackends(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                        getAuthTokenHolder().getToken().getProject().getId());
                int code = response.code();
                Logger.log("Response Code: " + code);
                assertEquals(200, code);
                String responseBody = response.body().string();
                assertNotNull(responseBody);
                BackendsInputHolder backendsInputHolder = gson.fromJson(responseBody,
                        BackendsInputHolder.class);
                // Filter backend
                List<Backends> backendFilter = backendsInputHolder.getBackends().stream()
                        .filter(p -> !TextUtils.isEmpty(p.getName()))
                        .collect(Collectors.toList());

                List<Backends> backend = backendFilter.stream()
                        .filter(p -> p.getName().equals(inputHolder.getName()))
                        .collect(Collectors.toList());
                assertNotNull(backend);

                // Get backend
                for (int i = 0; i < backend.size(); i++) {
                    Response responseBackend = getHttpHandler().getBackend(getAuthTokenHolder()
                            .getResponseHeaderSubjectToken(), getAuthTokenHolder().getToken()
                            .getProject().getId(), backend.get(i).getId());
                    int resCode = response.code();
                    String responseBackendBody = responseBackend.body().string();
                    Logger.log("Response Code: " + resCode);
                    assertEquals(200, responseBackend.code());
                    assertNotNull(responseBackendBody);
                    Backends backends = gson.fromJson(responseBackendBody, Backends.class);
                    assertNotNull(backends);
                    assertEquals(backends.getName(), inputHolder.getName());
                    Response responseDeleteBackend= getHttpHandler().getDeleteBackend(getAuthTokenHolder()
                                    .getResponseHeaderSubjectToken(), getAuthTokenHolder().getToken()
                            .getProject().getId(), backend.get(i).getId());
                    int responseCode = responseDeleteBackend.code();
                    String resp = responseDeleteBackend.body().string();
                    Logger.log("Response Code: " + responseCode);
                    Logger.log("Response: " + resp);
                    assertEquals("Deleting Non empty backend:Response code not matched: ",
                            409, responseCode);
                }
            }
        }
    }

    @Test
    @Order(16)
    @DisplayName("Test deleting non empty bucket")
    public void testDeleteNonEmptyBucket() throws IOException {
        for (Type t : getTypesHolder().getTypes()) {
            List<File> listOfIInputsForType =
                    Utils.listFilesBeginsWithPattern(t.getName(),
                            CREATE_BUCKET_PATH);
            for (File file : listOfIInputsForType) {
                String content = Utils.readFileContentsAsString(file);
                assertNotNull(content);
                SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                        getAuthTokenHolder().getToken().getProject().getId());
                List<File> listOfIBucketInputs =
                        Utils.listFilesBeginsWithPattern("bucket",
                                CREATE_BUCKET_PATH);
                for (File bucketFile : listOfIBucketInputs) {
                    String bucketContent = Utils.readFileContentsAsString(bucketFile);
                    assertNotNull(bucketContent);
                    // filename format is "bucket_<bucketname>.json", get the bucket name here
                    String bName = Utils.getFileNameFromDelim(bucketFile);
                    // Verifying Bucket not empty
                    Response response = getHttpHandler().deleteBucketNotEmpty(signatureKey, bName);
                    int responseCode = response.code();
                    String responseBody = response.body().string();
                    Logger.log("Response Code: "+responseCode);
                    Logger.log("Response: "+responseBody);
                    assertEquals("Verifying Bucket not empty: ",409, responseCode);
                }
            }
        }
    }
    
    @Test
    @Order(17)
    @DisplayName("Test deleting non exist object")
    public void testDeleteNonExistObject() throws IOException, JSONException {
        for (Type t : getTypesHolder().getTypes()) {
            List<File> listOfIInputsForType =
                    Utils.listFilesBeginsWithPattern(t.getName(),
                            CREATE_BUCKET_PATH);
            for (File file : listOfIInputsForType) {
                String content = Utils.readFileContentsAsString(file);
                assertNotNull(content);
                List<File> listOfIBucketInputs =
                        Utils.listFilesBeginsWithPattern("bucket",
                                CREATE_BUCKET_PATH);
                SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder()
                                .getResponseHeaderSubjectToken(), getAuthTokenHolder().getToken()
                                .getProject().getId());
                for (File bucketFile : listOfIBucketInputs) {
                    String bucketContent = Utils.readFileContentsAsString(bucketFile);
                    assertNotNull(bucketContent);
                    // filename format is "bucket_<bucketname>.json", get the bucket name here
                    String bName = Utils.getFileNameFromDelim(bucketFile);
                    String fileName = "hjdhj";
                    // now delete the object
                    Response response = getHttpHandler().deleteObject(signatureKey, bName, fileName);
                    int resCode = response.code();
                    String resBody = response.body().string();
                    Logger.log("Response Code: " + resCode);
                    Logger.log("Response: " + resBody);
                    assertEquals("Delete non exist object: Response code not matched: ",404, resCode);
                    boolean isUploaded = testGetListOfObjectFromBucket(bName, fileName, signatureKey);
                    assertFalse(isUploaded,"Object is exist");
                }
            }
        }
    }

    @Test
    @Order(18)
    @DisplayName("Test deleting non exist object with bucket")
    public void testDeleteNonExistObjectWithBucket() throws IOException {
        SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                getAuthTokenHolder().getToken().getProject().getId());
        String bName = "fhy5657";
        Response response = getHttpHandler().deleteObject(signatureKey, bName, "hjdhj");
        int code = response.code();
        String body = response.body().string();
        Logger.log("Response Code: " + code);
        Logger.log("Response: " + body);
        assertEquals("Delete non exist object: Response code not matched: ", 404, code);
        Response listObjectResponse = getHttpHandler().getBucketObjects(bName,signatureKey);
        int resCode = listObjectResponse.code();
        String resBody = listObjectResponse.body().string();
        Logger.log("Response Code: " + resCode);
        Logger.log("Response: " + resBody);
        assertEquals("Bucket name not exist: Response code not matched: ",404, resCode);
    }

    @Test
    @Order(19)
    @DisplayName("Test deleting bucket and object")
    public void testDeleteBucketAndObject() throws IOException, JSONException {
        for (Type t : getTypesHolder().getTypes()) {
            List<File> listOfIInputsForType =
                    Utils.listFilesBeginsWithPattern(t.getName(),
                            CREATE_BUCKET_PATH);
            for (File file : listOfIInputsForType) {
                String content = Utils.readFileContentsAsString(file);
                assertNotNull(content);

                List<File> listOfIBucketInputs =
                        Utils.listFilesBeginsWithPattern("bucket",
                                CREATE_BUCKET_PATH);
                SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                        getAuthTokenHolder().getToken().getProject().getId());
                for (File bucketFile : listOfIBucketInputs) {
                    String bucketContent = Utils.readFileContentsAsString(bucketFile);
                    assertNotNull(bucketContent);
                    // filename format is "bucket_<bucketname>.json", get the bucket name here
                    String bName = Utils.getFileNameFromDelim(bucketFile);
                    String fileName = "Screenshot_1.jpg";
                    // now delete the object
                    Response response = getHttpHandler().deleteObject(signatureKey, bName, fileName);
                    int code = response.code();
                    String body = response.body().string();
                    Logger.log("Response Code: " + code);
                    Logger.log("Response: " + body);
                    assertFalse(TextUtils.isEmpty(body),"Response message is empty: ");
                    assertEquals("Verifying object is deleted: Response code not matched: ",204, code);
                    boolean isUploaded = testGetListOfObjectFromBucket(bName, fileName, signatureKey);
                    assertFalse(isUploaded,"Object is not Deleted");

                    // now delete the bucket
                    Response responseDeleteBucket = getHttpHandler().deleteBucket(signatureKey, bName);
                    int codeRes = responseDeleteBucket.code();
                    String bodyRes = responseDeleteBucket.body().string();
                    Logger.log("Response Code: " + codeRes);
                    Logger.log("Response: " + bodyRes);
                    assertFalse(TextUtils.isEmpty(bodyRes),"Response message is empty: ");
                    assertNotNull(bodyRes);
                    assertEquals("Delete bucket may be does not exist: ",204, codeRes);

                    boolean isBucketExist = testGetListBuckets(bName, signatureKey);
                    assertFalse(isBucketExist, "Bucket is exist: ");
                }
            }
        }
    }

    @Test
    @Order(20)
    @DisplayName("Test deleting backend")
    public void testDeleteBackend() throws IOException {
        for (Type t : getTypesHolder().getTypes()) {
            List<File> listOfIInputsForType =
                    Utils.listFilesBeginsWithPattern(t.getName(),
                            CREATE_BUCKET_PATH);
            Gson gson = new Gson();
            for (File file : listOfIInputsForType) {
                String content = Utils.readFileContentsAsString(file);
                assertNotNull(content);
                AddBackendInputHolder inputHolder = gson.fromJson(content, AddBackendInputHolder.class);
                // Get backend list
                Response response = getHttpHandler().getBackends(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                        getAuthTokenHolder().getToken().getProject().getId());
                int code = response.code();
                Logger.log("Response Code: " + code);
                assertEquals(200, code);
                String responseBody = response.body().string();
                assertFalse(TextUtils.isEmpty(responseBody),"Response message is empty: ");
                BackendsInputHolder backendsInputHolder = gson.fromJson(responseBody,
                        BackendsInputHolder.class);
                // Filter backend
                List<Backends> backendFilter = backendsInputHolder.getBackends().stream()
                        .filter(p -> !TextUtils.isEmpty(p.getName()))
                        .collect(Collectors.toList());

                List<Backends> backend = backendFilter.stream()
                        .filter(p -> p.getName().equals(inputHolder.getName()))
                        .collect(Collectors.toList());
                assertNotNull(backend);

                // Get backend
                for (int i = 0; i < backend.size(); i++) {
                    Response responseDeleteBackend= getHttpHandler().getDeleteBackend(getAuthTokenHolder()
                            .getResponseHeaderSubjectToken(), getAuthTokenHolder().getToken()
                            .getProject().getId(), backend.get(i).getId());
                    int responseCode = responseDeleteBackend.code();
                    String resp = responseDeleteBackend.body().string();
                    Logger.log("Response Code: " + responseCode);
                    Logger.log("Response: " + resp);
                    assertEquals(200, responseCode);
                    assertFalse(TextUtils.isEmpty(resp),"Response message is empty: ");
                }
            }
        }
    }
}