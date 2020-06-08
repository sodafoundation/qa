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

import java.util.List;

public class Token {

    @SerializedName("is_domain")
    @Expose
    private Boolean isDomain;
    @SerializedName("methods")
    @Expose
    private List<String> methods = null;
    @SerializedName("roles")
    @Expose
    private List<Role> roles = null;
    @SerializedName("expires_at")
    @Expose
    private String expiresAt;
    @SerializedName("project")
    @Expose
    private Project project;
    @SerializedName("catalog")
    @Expose
    private List<Catalog> catalog = null;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("audit_ids")
    @Expose
    private List<String> auditIds = null;
    @SerializedName("issued_at")
    @Expose
    private String issuedAt;

    public Boolean getIsDomain() {
        return isDomain;
    }

    public void setIsDomain(Boolean isDomain) {
        this.isDomain = isDomain;
    }

    public List<String> getMethods() {
        return methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Catalog> getCatalog() {
        return catalog;
    }

    public void setCatalog(List<Catalog> catalog) {
        this.catalog = catalog;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getAuditIds() {
        return auditIds;
    }

    public void setAuditIds(List<String> auditIds) {
        this.auditIds = auditIds;
    }

    public String getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
    }

    @Override
    public String toString() {
        return "Token{" +
                "isDomain=" + isDomain +
                ", methods=" + methods +
                ", roles=" + roles +
                ", expiresAt='" + expiresAt + '\'' +
                ", project=" + project +
                ", catalog=" + catalog +
                ", user=" + user +
                ", auditIds=" + auditIds +
                ", issuedAt='" + issuedAt + '\'' +
                '}';
    }
}
