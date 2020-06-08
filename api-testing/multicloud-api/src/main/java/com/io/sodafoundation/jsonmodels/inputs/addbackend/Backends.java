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
package com.io.sodafoundation.jsonmodels.inputs.addbackend;

public class Backends
{
    private String bucketName;

    private String endpoint;

    private String tenantId;

    private String name;

    private String id;

    private String type;

    private String region;

    private String userId;

    public String getBucketName ()
    {
        return bucketName;
    }

    public void setBucketName (String bucketName)
    {
        this.bucketName = bucketName;
    }

    public String getEndpoint ()
    {
        return endpoint;
    }

    public void setEndpoint (String endpoint)
    {
        this.endpoint = endpoint;
    }

    public String getTenantId ()
    {
        return tenantId;
    }

    public void setTenantId (String tenantId)
    {
        this.tenantId = tenantId;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public String getRegion ()
    {
        return region;
    }

    public void setRegion (String region)
    {
        this.region = region;
    }

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }
}