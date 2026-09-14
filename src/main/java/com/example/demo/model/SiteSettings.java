/*
 * (C) Copyright  Hyland (http://hyland.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Abhigyan Majumder
 */
package com.example.demo.model;

import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "siteSettings")
public class SiteSettings {
  @Id private String id;

  private SiteName siteName;
  private String contactEmail;
  private String defaultShareImageUrl;
  private Instant updatedAt;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public SiteName getSiteName() {
    return siteName;
  }

  public void setSiteName(SiteName siteName) {
    this.siteName = siteName;
  }

  public String getContactEmail() {
    return contactEmail;
  }

  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }

  public String getDefaultShareImageUrl() {
    return defaultShareImageUrl;
  }

  public void setDefaultShareImageUrl(String defaultShareImageUrl) {
    this.defaultShareImageUrl = defaultShareImageUrl;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public static class SiteName {
    private String english;
    private String bengali;

    public String getEnglish() {
      return english;
    }

    public void setEnglish(String english) {
      this.english = english;
    }

    public String getBengali() {
      return bengali;
    }

    public void setBengali(String bengali) {
      this.bengali = bengali;
    }
  }
}
