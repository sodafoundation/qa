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

import java.util.ArrayList;

public class BackendsInputHolder
{
    private ArrayList<Backends> backends;
    private String next;

    public String getNext ()
    {
        return next;
    }

    public void setNext (String next)
    {
        this.next = next;
    }

    public ArrayList<Backends> getBackends ()
    {
        return backends;
    }

    public void setBackends (ArrayList<Backends> backends)
    {
        this.backends = backends;
    }
}