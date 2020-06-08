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
package com.io.sodafoundation.jsonmodels.tokensresponses;

import com.google.gson.annotations.SerializedName;

public class Domain {

    public @SerializedName("id") String id;
    public @SerializedName("name") String name;

    public Domain(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "\n\tDomain{" +
                "\n\t\tid='" + id + '\'' +
                "\n\t\tname='" + name + '\'' +
                '}';
    }
}