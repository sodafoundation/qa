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
package com.io.sodafoundation.utils;

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