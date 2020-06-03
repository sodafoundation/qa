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
}
