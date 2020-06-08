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

public class Credentials
{
    private String blob;
    public Blob blobObj;

    private String user_id;

    private String project_id;

    private Links links;

    private String id;

    private String type;

    public String getblob ()
    {
        return blob;
    }

    public void setblob (String blob)
    {
        this.blob = blob;
        Gson gson = new Gson();
        this.blobObj = gson.fromJson(this.blob, Blob.class);
        System.out.println(this.blobObj);
    }

    public String getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
    }

    public String getProject_id ()
    {
        return project_id;
    }

    public void setProject_id (String project_id)
    {
        this.project_id = project_id;
    }

    public Links getLinks ()
    {
        return links;
    }

    public void setLinks (Links links)
    {
        this.links = links;
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

    @Override
    public String toString()
    {
        //System.out.println(blobObj);
        return "Credential [blob = "+blobObj+", user_id = "+user_id+", project_id = "+project_id+", links = "+links+", id = "+id+", type = "+type+"]";
    }

    public Blob getBlobObj ()
    {
        return blobObj;
    }
}