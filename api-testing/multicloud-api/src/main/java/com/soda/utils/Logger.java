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
 * This class used for logs print.
 * TODO: Need to create generic method.
 */
public class Logger {

    /**
     * Print log in string.
     *
     * @param message Message
     */
    public static void logString(String message) {
        System.out.println(message);
    }

    /**
     * Print log in int.
     *
     * @param message Message
     */
    public static void logInt(Integer message) {
        System.out.println(message);
    }

    /**
     * Print log in double.
     *
     * @param message Message
     */
    public static void logDouble(Double message) {
        System.out.println(message);
    }

    /**
     * Print log in object.
     *
     * @param message Message
     */
    public static void logObject(Object message) {
        System.out.println(message);
    }
}
