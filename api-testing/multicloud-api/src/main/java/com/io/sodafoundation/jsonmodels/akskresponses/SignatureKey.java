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
package com.io.sodafoundation.jsonmodels.akskresponses;

public class SignatureKey {
    String secretAccessKey;
    String dateStamp;
    String dayDate;
    String regionName;
    String serviceName;
    String AccessKey;

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }

    public String getDateStamp() {
        return dateStamp;
    }

    public void setDateStamp(String dateStamp) {
        this.dateStamp = dateStamp;
    }

    public String getDayDate() {
        return dayDate;
    }

    public void setDayDate(String dayDate) {
        this.dayDate = dayDate;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAccessKey() {
        return AccessKey;
    }

    public void setAccessKey(String accessKey) {
        AccessKey = accessKey;
    }

    @Override
    public String toString() {
        return "SignatureKey{" +
                "secretAccessKey='" + secretAccessKey + '\'' +
                ", dateStamp='" + dateStamp + '\'' +
                ", dayDate='" + dayDate + '\'' +
                ", regionName='" + regionName + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", AccessKey='" + AccessKey + '\'' +
                '}';
    }
}
