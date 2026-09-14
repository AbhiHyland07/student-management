package com.example.demo.model.enums;

public enum Permission {
  DASHBOARDS_VIEW("dashboards:views"),
  ARTICLE_VIEW("article:view"),
  ARTICLE_CREATE("article:create"),
  ARTICLE_EDIT("article:edit"),
  ARTICLE_DELETE("article:delete"),
  ARTICLE_PUBLISH("article:publish"),
  PRINT_ISSUE_VIEW("print_issue:view"),
  PRINT_ISSUE_MANAGE("print_issue:manage"),
  AUTHOR_VIEW("author:view"),
  AUTHOR_MANAGE("author:manage"),
  MEDIA_VIEW("media:view"),
  MEDIA_MANAGE("media:manage"),
  HOMEPAGE_MANAGE("homepage:manage"),
  USER_VIEW("user:view"),
  USER_MANAGE("user:manage"),
  SETTINGS_MANAGE("settings:manage");

  private final String value;

  Permission(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
