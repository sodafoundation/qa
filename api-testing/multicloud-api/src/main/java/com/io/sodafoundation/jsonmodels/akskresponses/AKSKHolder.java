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

import com.google.gson.Gson;

public class AKSKHolder {

    private Credentials[] credentials;

    private Links links;

    public Credentials[] getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials[] credentials) {
        this.credentials = credentials;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        StringBuffer sb = new StringBuffer();
        sb.append("AKSKHolder [credentials = ");
        for (Credentials c : credentials) {
            c.blobObj = gson.fromJson(c.getblob(), Blob.class);
        }
        for (Credentials c : credentials) {
            sb.append("\n\t");
            sb.append(c);
        }
        sb.append(", \nlinks = " + links + "]");
        return sb.toString();
    }
}
