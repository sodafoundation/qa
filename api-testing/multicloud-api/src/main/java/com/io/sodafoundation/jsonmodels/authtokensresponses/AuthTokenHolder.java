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
package com.io.sodafoundation.jsonmodels.authtokensresponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthTokenHolder {

    String responseHeaderSubjectToken;

    @SerializedName("token")
    @Expose
    private Token token;

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "AuthTokenHolder{" +
                "responseHeaderSubjectToken='" + responseHeaderSubjectToken + '\'' +
                ", token=" + token +
                '}';
    }

    public String getResponseHeaderSubjectToken() {
        return responseHeaderSubjectToken;
    }

    public void setResponseHeaderSubjectToken(String responseHeaderSubjectToken) {
        this.responseHeaderSubjectToken = responseHeaderSubjectToken;
    }
}
