import com.google.gson.Gson;
import com.soda.jsonmodels.akskresponses.SignatureKey;
import com.soda.jsonmodels.inputs.createlifecycle.AddLifecycleInputHolder;
import com.soda.utils.Logger;
import com.soda.utils.TextUtils;
import com.soda.utils.Utils;
import okhttp3.Response;
import org.json.JSONException;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.soda.utils.Constant.*;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LifecycleTests extends BaseTestClass {

    @Test
    @Order(1)
    @DisplayName("Test creating bucket and backend on OPENSDS")
    public void testCreateBucketAndBackend() throws IOException, JSONException {
        testCreateBucketAndBackend(CREATE_BUCKET_PATH);
    }

    @Test
    @Order(2)
    @DisplayName("Test uploading object in a bucket")
    public void testUploadObject() throws IOException, JSONException {
        testUploadObject(CREATE_BUCKET_PATH);
    }

    @Test
    @Order(3)
    @DisplayName("Test creating Lifecycle")
    public void testCreateLifecycle() throws IOException {
        Gson gson = new Gson();
        List<File> listOfIBucketInputs =
                Utils.listFilesBeginsWithPattern("bucket", CREATE_BUCKET_PATH);
        SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                getAuthTokenHolder().getToken().getProject().getId());
        for (File bucketFile : listOfIBucketInputs) {
            String bucketContent = Utils.readFileContentsAsString(bucketFile);
            assertNotNull(bucketContent);

            // filename format is "bucket_<bucketname>.json", get the bucket name here
            String bName = Utils.getFileNameFromDelim(bucketFile);
            //Lifecycle creation started
            List<File> listOfLifeCycleInputs =
                    Utils.listFilesBeginsWithPattern("lifecycle",
                            CREATE_LIFECYCLE_PATH);
            String createLifeCycleContent = Utils.readFileContentsAsString(listOfLifeCycleInputs.get(0));
            assertNotNull(createLifeCycleContent);
            AddLifecycleInputHolder lifecycleInputHolder = gson.fromJson(createLifeCycleContent
                    ,AddLifecycleInputHolder.class);
            Response response = getHttpHandler().createLifecycle(lifecycleInputHolder.getXmlCreateLifecycle(),
                    bName,signatureKey);
            String res = response.body().string();
            int code = response.code();
            Logger.logString("Response: " +res);
            Logger.logString("Response Code: " + code);
            assertEquals("Life cycle creation failed: ",200, code);
        }
    }

    @Test
    @Order(4)
    @DisplayName("Test creating Lifecycle rule with same name")
    public void testCreateLifecycleSameRule() throws IOException {
        Gson gson = new Gson();
        List<File> listOfIBucketInputs =
                Utils.listFilesBeginsWithPattern("bucket", CREATE_BUCKET_PATH);
        SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                getAuthTokenHolder().getToken().getProject().getId());
        for (File bucketFile : listOfIBucketInputs) {
            String bucketContent = Utils.readFileContentsAsString(bucketFile);
            assertNotNull(bucketContent);

            // filename format is "bucket_<bucketname>.json", get the bucket name here
            String bName = Utils.getFileNameFromDelim(bucketFile);
            //Lifecycle creation started
            List<File> listOfLifeCycleInputs =
                    Utils.listFilesBeginsWithPattern("lifecycle",
                            CREATE_LIFECYCLE_PATH);
            String createLifeCycleContent = Utils.readFileContentsAsString(listOfLifeCycleInputs.get(0));
            assertNotNull(createLifeCycleContent);
            AddLifecycleInputHolder lifecycleInputHolder = gson.fromJson(createLifeCycleContent
                    ,AddLifecycleInputHolder.class);
            Response response = getHttpHandler().createLifecycle(lifecycleInputHolder.getXmlCreateLifecycle(),
                    bName,signatureKey);
            String res = response.body().string();
            int code = response.code();
            Logger.logString("Response: " +res);
            Logger.logString("Response Code: " + code);
            assertEquals("Rule already exists: ",409, code);
        }
    }

    @Test
    @Order(5)
    @DisplayName("Test creating Lifecycle rule with less days")
    public void testCreateLifecycleLessDays() throws IOException {
        Gson gson = new Gson();
        List<File> listOfIBucketInputs =
                Utils.listFilesBeginsWithPattern("bucket", CREATE_BUCKET_PATH);
        SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                getAuthTokenHolder().getToken().getProject().getId());
        for (File bucketFile : listOfIBucketInputs) {
            String bucketContent = Utils.readFileContentsAsString(bucketFile);
            assertNotNull(bucketContent);

            // filename format is "bucket_<bucketname>.json", get the bucket name here
            String bName = Utils.getFileNameFromDelim(bucketFile);
            //Lifecycle creation started
            List<File> listOfLifeCycleInputs =
                    Utils.listFilesBeginsWithPattern("lifecycle",
                            CREATE_LIFECYCLE_PATH);
            String createLifeCycleContent = Utils.readFileContentsAsString(listOfLifeCycleInputs.get(0));
            assertNotNull(createLifeCycleContent);
            AddLifecycleInputHolder lifecycleInputHolder = gson.fromJson(createLifeCycleContent
                    ,AddLifecycleInputHolder.class);
            Response response = getHttpHandler().createLifecycle(lifecycleInputHolder.getXmlCreateLifecycleLessDays(),
                    bName,signatureKey);
            String res = response.body().string();
            int code = response.code();
            Logger.logString("Response: " +res);
            Logger.logString("Response Code: " + code);
            assertEquals("Days for transitioning object to tier_99 must not be less than 30: ",400, code);
        }
    }

    @Test@Order(6)
    @DisplayName("Test creating Lifecycle rule with extended days")
    public void testCreateLifecycleExtendedDays() throws IOException {
        Gson gson = new Gson();
        List<File> listOfIBucketInputs =
                Utils.listFilesBeginsWithPattern("bucket", CREATE_BUCKET_PATH);
        SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                getAuthTokenHolder().getToken().getProject().getId());
        // create the bucket specified in each file
        for (File bucketFile : listOfIBucketInputs) {
            String bucketContent = Utils.readFileContentsAsString(bucketFile);
            assertNotNull(bucketContent);

            // filename format is "bucket_<bucketname>.json", get the bucket name here
            String bName = Utils.getFileNameFromDelim(bucketFile);
            //Lifecycle creation started
            List<File> listOfLifeCycleInputs =
                    Utils.listFilesBeginsWithPattern("lifecycle",
                            CREATE_LIFECYCLE_PATH);
            String createLifeCycleContent = Utils.readFileContentsAsString(listOfLifeCycleInputs.get(0));
            assertNotNull(createLifeCycleContent);
            AddLifecycleInputHolder lifecycleInputHolder = gson.fromJson(createLifeCycleContent
                    , AddLifecycleInputHolder.class);
            Response response = getHttpHandler().createLifecycle(lifecycleInputHolder.getXmlCreateLifecycleExtendedDays(),
                    bName, signatureKey);
            String res = response.body().string();
            int code = response.code();
            Logger.logString("Response: " + res);
            Logger.logString("Response Code: " + code);
            assertEquals("Minimum days for an object in the current storage class is less before transition action"
                    ,400, code);
        }
    }

    @Test
    @Order(7)
    @DisplayName("Test creating Lifecycle rule without Expiration")
    public void testCreateLifecycleWithoutExpiration() throws IOException {
        Gson gson = new Gson();
        List<File> listOfIBucketInputs =
                Utils.listFilesBeginsWithPattern("bucket", CREATE_BUCKET_PATH);
        SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                getAuthTokenHolder().getToken().getProject().getId());
        for (File bucketFile : listOfIBucketInputs) {
            String bucketContent = Utils.readFileContentsAsString(bucketFile);
            assertNotNull(bucketContent);

            // filename format is "bucket_<bucketname>.json", get the bucket name here
            String bName = Utils.getFileNameFromDelim(bucketFile);
            //Lifecycle creation started
            List<File> listOfLifeCycleInputs =
                    Utils.listFilesBeginsWithPattern("lifecycle",
                            CREATE_LIFECYCLE_PATH);
            String createLifeCycleContent = Utils.readFileContentsAsString(listOfLifeCycleInputs.get(0));
            assertNotNull(createLifeCycleContent);
            AddLifecycleInputHolder lifecycleInputHolder = gson.fromJson(createLifeCycleContent
                    , AddLifecycleInputHolder.class);
            Response response = getHttpHandler().createLifecycle(lifecycleInputHolder
                            .getXmlCreateLifecycleWithoutExpiration(),
                    bName, signatureKey);
            String res = response.body().string();
            int code = response.code();
            Logger.logString("Response: " + res);
            Logger.logString("Response Code: " + code);
            assertEquals("Lifecycle rule without Expiration: ",200, code);
            assertFalse(TextUtils.isEmpty(res),"Response message is empty: ");
        }
    }

    @Test
    @Order(8)
    @DisplayName("Test creating Lifecycle rule without Expiration and transition")
    public void testCreateLifecycleWithoutExpirationTransition() throws IOException {
        Gson gson = new Gson();
        List<File> listOfIBucketInputs =
                Utils.listFilesBeginsWithPattern("bucket", CREATE_BUCKET_PATH);
        SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                getAuthTokenHolder().getToken().getProject().getId());
        for (File bucketFile : listOfIBucketInputs) {
            String bucketContent = Utils.readFileContentsAsString(bucketFile);
            assertNotNull(bucketContent);
            // filename format is "bucket_<bucketname>.json", get the bucket name here
            String bName = Utils.getFileNameFromDelim(bucketFile);
            //Lifecycle creation started
            List<File> listOfLifeCycleInputs =
                    Utils.listFilesBeginsWithPattern("lifecycle",
                            CREATE_LIFECYCLE_PATH);
            String createLifeCycleContent = Utils.readFileContentsAsString(listOfLifeCycleInputs.get(0));
            assertNotNull(createLifeCycleContent);
            AddLifecycleInputHolder lifecycleInputHolder = gson.fromJson(createLifeCycleContent
                    , AddLifecycleInputHolder.class);
            Response response = getHttpHandler().createLifecycle(lifecycleInputHolder
                            .getXmlCreateLifecycleWithoutExpirationTransition(),
                    bName, signatureKey);
            String res = response.body().string();
            int code = response.code();
            Logger.logString("Response: " + res);
            Logger.logString("Response Code: " + code);
            assertEquals("Lifecycle rule without Expiration and transition: ",200, code);
            assertFalse(TextUtils.isEmpty(res),"Response message is empty: ");
        }
    }

    @Test
    @Order(9)
    @DisplayName("Test creating Lifecycle rule without name")
    public void testCreateLifecycleWithoutName() throws IOException {
        Gson gson = new Gson();
        List<File> listOfIBucketInputs =
                Utils.listFilesBeginsWithPattern("bucket", CREATE_BUCKET_PATH);
        SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                getAuthTokenHolder().getToken().getProject().getId());
        for (File bucketFile : listOfIBucketInputs) {
            String bucketContent = Utils.readFileContentsAsString(bucketFile);
            assertNotNull(bucketContent);
            // filename format is "bucket_<bucketname>.json", get the bucket name here
            String bName = Utils.getFileNameFromDelim(bucketFile);
            //Lifecycle creation started
            List<File> listOfLifeCycleInputs =
                    Utils.listFilesBeginsWithPattern("lifecycle",
                            CREATE_LIFECYCLE_PATH);
            String createLifeCycleContent = Utils.readFileContentsAsString(listOfLifeCycleInputs.get(0));
            assertNotNull(createLifeCycleContent);
            AddLifecycleInputHolder lifecycleInputHolder = gson.fromJson(createLifeCycleContent
                    , AddLifecycleInputHolder.class);
            Response response = getHttpHandler().createLifecycle(lifecycleInputHolder
                            .getXmlCreateLifecycleWithoutName(),
                    bName, signatureKey);
            String res = response.body().string();
            int code = response.code();
            Logger.logString("Response: " + res);
            Logger.logString("Response Code: " + code);
            assertEquals("Rule Id is blank: ", 400, code);
            assertFalse(TextUtils.isEmpty(res),"Response message is empty: ");
        }
    }

    @Test
    @Order(10)
    @DisplayName("Listing Lifecycle")
    public void testDisplayLifecycle() throws IOException {
        // backend added, now create buckets
        List<File> listOfIBucketInputs =
                Utils.listFilesBeginsWithPattern("bucket", CREATE_BUCKET_PATH);
        for (File bucketFile : listOfIBucketInputs) {
            String bucketContent = Utils.readFileContentsAsString(bucketFile);
            assertNotNull(bucketContent);
            // filename format is "bucket_<bucketname>.json", get the bucket name here
            String bName = Utils.getFileNameFromDelim(bucketFile);
            SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                    getAuthTokenHolder().getToken().getProject().getId());
            //Display Lifecycle
            Response codeResponse = getHttpHandler().ListOfLifecycle(bName, signatureKey);
            String jsonRes = codeResponse.body().string();
            int code = codeResponse.code();
            Logger.logString("Response: "+jsonRes);
            Logger.logString("Response code: "+code);
            assertEquals("List Of Lifecycle", 200, code);
        }
    }

    @Test
    @Order(11)
    @DisplayName("Listing Lifecycle with wrong bucket name")
    public void testDisplayLifecycleWithWrongBucket() throws IOException {
        SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                getAuthTokenHolder().getToken().getProject().getId());
        Response codeResponse = getHttpHandler().ListOfLifecycle("wrongBucketName", signatureKey);
        String jsonRes = codeResponse.body().string();
        int code = codeResponse.code();
        Logger.logString("Response: "+jsonRes);
        Logger.logString("Response code: "+code);
        assertEquals("The specified bucket does not exist: ", 404, code);
    }

    @Test
    @Order(12)
    @DisplayName("Test deleting lifecycle")
    public void testDeleteLifecycle() throws IOException {
        List<File> listOfIBucketInputs =
                Utils.listFilesBeginsWithPattern("bucket", CREATE_BUCKET_PATH);
        SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                        getAuthTokenHolder().getToken().getProject().getId());
        for (File bucketFile : listOfIBucketInputs) {
            String bucketContent = Utils.readFileContentsAsString(bucketFile);
            assertNotNull(bucketContent);
            // filename format is "bucket_<bucketname>.json", get the bucket name here
            String bName = Utils.getFileNameFromDelim(bucketFile);
            // now delete the lifecycle
            Response response = getHttpHandler().deleteLifecycle(bName, "abcc", signatureKey);
            String res = response.body().string();
            int code = response.code();
            Logger.logString("Response: "+res);
            Logger.logString("Response code: "+code);
            assertEquals("lifecycle is not deleted: ", 200, code);
            assertFalse(TextUtils.isEmpty(res),"Response message is empty: ");
        }
    }

    @Test
    @Order(13)
    @DisplayName("Test deleting lifecycle with non existing bucket name")
    public void testDeleteLifecycleNonExist() throws IOException {
        SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                getAuthTokenHolder().getToken().getProject().getId());
        Response response = getHttpHandler().deleteLifecycle("NonExistentBucket", "abcc", signatureKey);
        String res = response.body().string();
        int code = response.code();
        Logger.logString("Response: "+res);
        Logger.logString("Response code: "+code);
        assertEquals("The specified bucket may be exist: ", 404, code);
    }

    @Test
    @Order(14)
    @DisplayName("Test deleting lifecycle on already deleted lifecycle")
    public void testDeleteLifecycleAlreadyDeleted() throws IOException{
        List<File> listOfIBucketInputs =
                Utils.listFilesBeginsWithPattern("bucket", CREATE_BUCKET_PATH);
        SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                getAuthTokenHolder().getToken().getProject().getId());
        for (File bucketFile : listOfIBucketInputs) {
            String bucketContent = Utils.readFileContentsAsString(bucketFile);
            assertNotNull(bucketContent);
            // filename format is "bucket_<bucketname>.json", get the bucket name here
            String bName = Utils.getFileNameFromDelim(bucketFile);
            // now delete the lifecycle
            Response response = getHttpHandler().deleteLifecycle(bName, "abcc", signatureKey);
            String res = response.body().string();
            int code = response.code();
            Logger.logString("Response: "+res);
            Logger.logString("Response code: "+code);
            assertEquals("The specified bucket does not have LifeCycle configured: ",404, code);
            assertFalse(TextUtils.isEmpty(res),"Response message is empty: ");
        }
    }

    @Test
    @Order(15)
    @DisplayName("Test deleting lifecycle without rule")
    public void testDeleteLifecycleWithoutrule() throws IOException {
        List<File> listOfIBucketInputs =
                Utils.listFilesBeginsWithPattern("bucket", CREATE_BUCKET_PATH);
        SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                getAuthTokenHolder().getToken().getProject().getId());
        // create the bucket specified in each file
        for (File bucketFile : listOfIBucketInputs) {
            String bucketContent = Utils.readFileContentsAsString(bucketFile);
            assertNotNull(bucketContent);
            // filename format is "bucket_<bucketname>.json", get the bucket name here
            String bName = Utils.getFileNameFromDelim(bucketFile);
            // now delete the lifecycle
            Response response = getHttpHandler().deleteLifecycle(bName, "", signatureKey);
            String res = response.body().string();
            int code = response.code();
            Logger.logString("Response: "+res);
            Logger.logString("Response code: "+code);
            assertEquals("No rule is mentioned",404, code);
            assertFalse(TextUtils.isEmpty(res),"Response message is empty: ");
        }
    }

    @Test
    @Order(16)
    @DisplayName("Test deleting lifecycle with non exist name of the rule")
    public void testDeleteLifecycleWithWrongName() throws IOException {
        List<File> listOfIBucketInputs =
                Utils.listFilesBeginsWithPattern("bucket", CREATE_BUCKET_PATH);
        SignatureKey signatureKey = getHttpHandler().getAkSkList(getAuthTokenHolder().getResponseHeaderSubjectToken(),
                getAuthTokenHolder().getToken().getProject().getId());
        for (File bucketFile : listOfIBucketInputs) {
            String bucketContent = Utils.readFileContentsAsString(bucketFile);
            assertNotNull(bucketContent);
            // filename format is "bucket_<bucketname>.json", get the bucket name here
            String bName = Utils.getFileNameFromDelim(bucketFile);
            // now delete the lifecycle
            Response response = getHttpHandler().deleteLifecycle(bName, "wrongName", signatureKey);
            String res = response.body().string();
            int code = response.code();
            Logger.logString("Response: "+res);
            Logger.logString("Response code: "+code);
            assertEquals("Rule with non exist name: ", 404, code);
            assertFalse(TextUtils.isEmpty(res),"Response message is empty: ");
        }
    }
}
