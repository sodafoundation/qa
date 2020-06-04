package com.soda.utils;

/**
 * In this class set Inputs path.
 */
public class Constant {
    public static final  String PATH = System.getenv("INPUT_PATH")+"/src/main/resources/inputs/";
    public static final  String CREATE_BUCKET_PATH = PATH+"createbucket";
    public static final  String EMPTY_FIELD_PATH = PATH+"emptyfield/";
    public static final  String RAW_DATA_PATH = PATH+"rawdata";
    public static final  String DOWNLOAD_FILES_PATH = PATH+"download";
    public static final  String CREATE_MIGRATION_PATH = PATH+"createmigration";
    // https://savvytime.com/converter/utc-to-ist
    //UTC time
    // 00    56      6       5          5             2
    //sec   mint    hr      date       Monthcount  Day count
    public static final  String SCHEDULE_TIME = System.getenv("SCHEDULE_TIME");
    public static final  String CREATE_LIFECYCLE_PATH = PATH+"createlifecycle";
}