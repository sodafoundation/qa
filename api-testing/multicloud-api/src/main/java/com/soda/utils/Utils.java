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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class used for common utils.
 */
public class Utils {

    /**
     * format strings for the date/time and date stamps required during signing
     **/
    public static final String ISO8601BasicFormat = "yyyyMMdd'T'HHmmss'Z'";

    /**
     * Get List of files
     *
     * @param beginPattern begin pattern (bucket) e.g: bucket_b237
     * @param path folder path e.g: resource/inputs
     * @return list of files
     */
    public static List<File> listFilesBeginsWithPattern(final String beginPattern, String path) {
        List<File> retFileList = new ArrayList<>();
        File dir = new File(path);
        assert dir.isDirectory() : "Invalid directory path: " + path;
        File[] files = dir.listFiles((dir1, name) -> name.matches("^" + beginPattern + "+[a-z_1-9-]*.json"));
        assert files != null;
        Collections.addAll(retFileList, files);
        return retFileList;
    }

    /**
     * Read file content.
     *
     * @param file file.
     * @return file content.
     */
    public static String  readFileContentsAsString(File file) {
        String content = null;
        try {
            content = new String(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * Get Bucket Name.
     *
     * @param bucketFile file path
     * @return bucket name
     */
    public static String getFileNameFromDelim(File bucketFile){
        return bucketFile.getName().substring(bucketFile.getName().indexOf("_") + 1,
                bucketFile.getName().indexOf("."));
    }

    /**
     * Get date.
     *
     * @return date.
     */
    public static String getDate() {
        Date now = new Date();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(ISO8601BasicFormat);
        dateTimeFormat.setTimeZone(new SimpleTimeZone(0, "UTC"));
        return dateTimeFormat.format(now);
    }

    /**
     * Get Host
     *
     * @param url url e.g: http://192.168.34.45:6566
     * @return host e.g: 192.168.34.45:6566
     */
    public static String getHostAndPort(String url){
        String host = null;
        try {
            host = new URI(url).getHost()+":"+new URI(url).getPort();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return host;
    }

    /**
     * Generate random name.
     *
     * @param name any name
     * @return name
     */
    public static String getRandomName(String name){
        Random rand = new Random();
        int randInt = rand.nextInt(10000);
        return name+randInt;
    }
}