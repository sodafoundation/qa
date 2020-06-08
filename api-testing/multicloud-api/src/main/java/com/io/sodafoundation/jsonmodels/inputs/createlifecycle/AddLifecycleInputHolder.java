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
package com.io.sodafoundation.jsonmodels.inputs.createlifecycle;

public class AddLifecycleInputHolder {
    String xmlCreateLifecycle;

    String xmlCreateLifecycleSameRule;

    String xmlCreateLifecycleWithoutName;

    String xmlCreateLifecycleWithoutExpiration;

    String xmlCreateLifecycleWithoutExpirationTransition;

    String xmlCreateLifecycleExtendedDays;

    String xmlCreateLifecycleLessDays;

    public String getXmlCreateLifecycleWithoutExpirationTransition() {
        return xmlCreateLifecycleWithoutExpirationTransition;
    }

    public void setXmlCreateLifecycleWithoutExpirationTransition(String xmlCreateLifecycleWithoutExpirationTransition) {
        this.xmlCreateLifecycleWithoutExpirationTransition = xmlCreateLifecycleWithoutExpirationTransition;
    }

    public String getXmlCreateLifecycleWithoutExpiration() {
        return xmlCreateLifecycleWithoutExpiration;
    }

    public void setXmlCreateLifecycleWithoutExpiration(String xmlCreateLifecycleWithoutExpiration) {
        this.xmlCreateLifecycleWithoutExpiration = xmlCreateLifecycleWithoutExpiration;
    }

    public String getXmlCreateLifecycleLessDays() {
        return xmlCreateLifecycleLessDays;
    }

    public void setXmlCreateLifecycleLessDays(String xmlCreateLifecycleLessDays) {
        this.xmlCreateLifecycleLessDays = xmlCreateLifecycleLessDays;
    }

    public String getXmlCreateLifecycleWithoutName() {
        return xmlCreateLifecycleWithoutName;
    }

    public void setXmlCreateLifecycleWithoutName(String xmlCreateLifecycleWithoutName) {
        this.xmlCreateLifecycleWithoutName = xmlCreateLifecycleWithoutName;
    }

    public String getXmlCreateLifecycleExtendedDays() {
        return xmlCreateLifecycleExtendedDays;
    }

    public void setXmlCreateLifecycleExtendedDays(String xmlCreateLifecycleExtendedDays) {
        this.xmlCreateLifecycleExtendedDays = xmlCreateLifecycleExtendedDays;
    }

    public String getXmlCreateLifecycleSameRule() {
        return xmlCreateLifecycleSameRule;
    }

    public void setXmlCreateLifecycleSameRule(String xmlCreateLifecycleSameRule) {
        this.xmlCreateLifecycleSameRule = xmlCreateLifecycleSameRule;
    }

    public String getXmlCreateLifecycle() {
        return xmlCreateLifecycle;
    }

    public void setXmlCreateLifecycle(String xmlCreateLifecycle) {
        this.xmlCreateLifecycle = xmlCreateLifecycle;
    }
}
