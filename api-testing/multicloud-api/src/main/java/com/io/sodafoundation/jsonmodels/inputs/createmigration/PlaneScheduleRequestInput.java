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
package com.io.sodafoundation.jsonmodels.inputs.createmigration;

public class PlaneScheduleRequestInput
{
    private Filter filter;

    private boolean remainSource;

    private String policyId;

    private DestConnInput destConn;

    private String name;

    private String description;

    private String type;

    private SourceConnInput sourceConn;

    private boolean policyEnabled;

    public Filter getFilter ()
    {
        return filter;
    }

    public void setFilter (Filter filter)
    {
        this.filter = filter;
    }

    public boolean getRemainSource ()
    {
        return remainSource;
    }

    public void setRemainSource (boolean remainSource)
    {
        this.remainSource = remainSource;
    }

    public String getPolicyId ()
    {
        return policyId;
    }

    public void setPolicyId (String policyId)
    {
        this.policyId = policyId;
    }

    public DestConnInput getDestConn ()
    {
        return destConn;
    }

    public void setDestConn (DestConnInput destConn)
    {
        this.destConn = destConn;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getDescription ()
    {
        return description;
    }

    public void setDescription (String description)
    {
        this.description = description;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    public SourceConnInput getSourceConn ()
    {
        return sourceConn;
    }

    public void setSourceConn (SourceConnInput sourceConn)
    {
        this.sourceConn = sourceConn;
    }

    public boolean getPolicyEnabled ()
    {
        return policyEnabled;
    }

    public void setPolicyEnabled (boolean policyEnabled)
    {
        this.policyEnabled = policyEnabled;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [filter = "+filter+", remainSource = "+remainSource+", policyId = "+policyId+", destConn = "+destConn+", name = "+name+", description = "+description+", type = "+type+", sourceConn = "+sourceConn+", policyEnabled = "+policyEnabled+"]";
    }
}