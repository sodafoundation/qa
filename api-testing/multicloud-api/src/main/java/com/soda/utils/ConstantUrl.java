package com.soda.utils;

/**
 *  In this class set URL
 */
public class ConstantUrl {
    private static ConstantUrl mConstantUrl;
    private static  String URL = null;
    private static  String API_SERVER_PORT = null;
    private static  String S3_API_PORT = null;

    private ConstantUrl() {
        API_SERVER_PORT = getAPIServerPort();
        S3_API_PORT = getS3APIPort();
        URL = getHostIp();
    }

    public static ConstantUrl getInstance() {
        Logger.logString("**********************************************************************");
        if (mConstantUrl == null) {
            mConstantUrl = new ConstantUrl();
        }
        return mConstantUrl;
    }

    /**
     * Get API server port.
     */
    public String getAPIServerPort() {
        return System.getenv("API_SERVER_PORT");
    }

    /**
     * Get s3 API server port.
     */
    public String getS3APIPort() {
        return System.getenv("S3_API_PORT");
    }

    /**
     * Get Host Ip.
     */
    public String getHostIp() {
        return "http://" + System.getenv("HOST_IP");
    }

    /**
     * Get Token Login.
     */
    public String getTokenLogin() {
        return URL +"/identity/v3/auth/tokens";
    }

    /**
     * Get aks list.
     *
     * @param userId user id.
     */
    public String getAksList(String userId) {
        return URL +"/identity/v3/credentials?userId="+userId+"&type=ec2";
    }

    /**
     * Get Types
     *
     * @param tenantId tenant id.
     */
    public String getTypesUrl(String tenantId) {
        return URL+API_SERVER_PORT+"/"+tenantId+"/types";
    }

    /**
     * Add Backend
     *
     * @param tenantId admin tenant id.
     */
    public String getAddBackendUrl(String tenantId) {
        return URL+API_SERVER_PORT+"/"+ tenantId +"/backends";
    }

    /**
     * Create Bucket
     *
     * @param bucketName bucket name.
     */
    public String getCreateBucketUrl(String bucketName) {
        return URL+S3_API_PORT+"/"+ bucketName;
    }

    /**
     * Bucket List
     */
    public String getListBucketUrl() {
        return URL+S3_API_PORT+"/";
    }

    /**
     * Upload object
     *
     * @param bucketName bucket name.
     * @param fileName file name.
     */
    public String getUploadObjectUrl(String bucketName, String fileName) {
        return URL+S3_API_PORT+"/"+ bucketName +"/"+ fileName;
    }

    /**
     * Get list of bucket object.
     *
     * @param bucketName bucket name
     */
    public String getListOfBucketObjectUrl(String bucketName) {
        return URL+S3_API_PORT+"/"+bucketName;
    }

    /**
     * Download object
     *
     * @param bucketName bucket name.
     * @param fileName file name.
     */
    public String getDownloadObjectUrl(String bucketName, String fileName) {
        return URL+S3_API_PORT+"/"+ bucketName +"/"+ fileName;
    }

    /**
     * Get Backend List
     *
     * @param tenantId tenant id.
     */
    public String getBackendsUrl(String tenantId) {
        return URL+API_SERVER_PORT+"/"+tenantId+"/backends";
    }

    /**
     * Get Backend
     *
     * @param tenantId tenant id.
     * @param id admin tenant id.
     */
    public String getBackendUrl(String tenantId, String id) {
        return URL+API_SERVER_PORT+"/"+tenantId+"/backends/"+id;
    }

    /**
     * Delete Backend
     *
     * @param tenantId tenant id.
     * @param id admin tenant id.
     */
    public String getDeleteBackendUrl(String tenantId, String id) {
        return URL+API_SERVER_PORT+"/"+tenantId+"/backends/"+id;
    }

    /**
     * Delete bucket
     *
     * @param bucketName bucket name.
     */
    public String getDeleteBucketUrl(String bucketName) {
        return URL+S3_API_PORT+"/"+ bucketName;
    }

    /**
     * Delete object
     *
     * @param bucketName bucket name.
     * @param objectName object name.
     */
    public String getDeleteObjectUrl(String bucketName, String objectName) {
        return URL+S3_API_PORT+"/"+bucketName+"/"+objectName;
    }

    /**
     * Create plan.
     *
     * @param tenantId tenant id
     */
    public String getCreatePlansUrl(String tenantId) {
        return URL +API_SERVER_PORT+"/"+tenantId+"/plans";
    }

    /**
     * Run plan.
     *
     * @param tenantId tenant id
     * @param id id
     */
    public String getRunPlanUrl(String tenantId, String id) {
        return URL +API_SERVER_PORT+"/"+tenantId+"/plans/"+id+"/run";
    }

    /**
     * Get job
     *
     * @param tenantId tenant id
     * @param jobId job id
     */
    public String getJobUrl(String tenantId, String jobId) {
        return URL +API_SERVER_PORT+"/"+tenantId+"/jobs/"+jobId;
    }

    /**
     * Get list of plans
     *
     * @param tenantId admin tenant id
     */
    public String getPlansListUrl(String tenantId) {
        return URL+API_SERVER_PORT+"/"+tenantId+"/plans";
    }

    /**
     * Delete plans
     *
     * @param tenantId admin tenant id
     * @param id id
     */
    public String getDeletePlansUrl(String tenantId, String id) {
        return URL+API_SERVER_PORT+"/"+tenantId+"/plans/"+id;
    }

    /**
     * Get Job List
     *
     * @param tenantId tenant id
     */
    public String getListJobUrl(String tenantId) {
        return URL+API_SERVER_PORT+"/"+tenantId+"/jobs";
    }

    /**
     * Get policies.
     *
     * @param tenantId tenant id
     */
    public String getPoliciesUrl(String tenantId) {
        return URL+API_SERVER_PORT+"/"+tenantId+"/policies";
    }

    /**
     * Get Schedule Migration Status.
     *
     * @param tenantId tenant id
     * @param planeName plan name
     */
    public String getScheduleMigStatusUrl(String tenantId, String planeName) {
        return URL+API_SERVER_PORT+"/"+tenantId+"/jobs?planName="+planeName;
    }

    /**
     * Get Create Life Cycle.
     *
     * @param bucketName bucket name
     */
    public String getCreateLifeCycleUrl(String bucketName) {
        return URL +S3_API_PORT+"/"+bucketName+"/?lifecycle=";
    }

    /**
     * Get list of Life Cycle from bucket.
     *
     * @param bucketName bucket name
     */
    public String getListOfLifeCycleUrl(String bucketName) {
        return URL +S3_API_PORT+"/"+bucketName+"/?lifecycle=";
    }

    /**
     * Delete Lifecycle.
     *
     * @param bucketName bucket name
     * @param ruleName rule name
     */
    public String getDeleteLifecycleUrl(String bucketName, String ruleName) {
        return URL +S3_API_PORT+"/"+bucketName+"/?lifecycle=&ruleID="+ruleName;
    }
}
