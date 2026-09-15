# Student Management System

Spring Boot + MongoDB backend for user, author, article, print issue, media, homepage config, and settings management with JWT authentication and permission-based authorization.

## Stack

- Java 25
- Spring Boot 3.5
- Spring Security
- Spring Data MongoDB
- springdoc OpenAPI / Swagger UI

## Run

```bash
mvn clean compile
mvn spring-boot:run
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

## Authentication

Login returns:
- `accessToken`
- `refreshToken`
- `expiresAt`
- `user`

Use the access token as:

```text
Authorization: Bearer <access_token>
```

## Main APIs

### Auth

- `POST /auth/login`
- `POST /auth/refresh`
- `POST /auth/logout`
- `GET /auth/me`
- `POST /auth/forgot-password`
- `POST /auth/reset-password`

### Dashboard

- `GET /dashboard/summary`
  - requires `dashboards:views`

Response:

```json
{
  "publishedArticleCount": 0,
  "draftArticleCounts": 0,
  "authorCounts": 0,
  "printIssueCount": 0,
  "recentArticles": [],
  "latestPrintIssue": null
}
```

### Users

- `GET /users?page=1&pageSize=20&search=abhi`
- `POST /users`
- `POST /users/invite`
- `PUT /users/{id}`
- `POST /users/{id}/suspend`
- `POST /users/{id}/reactivate`
- `DELETE /users/{id}`

`GET /users` response:

```json
{
  "items": [],
  "total": 0,
  "page": 1,
  "pageSize": 20
}
```

User APIs require:
- `ADMIN`
- `user:view` for listing
- `user:manage` for write operations

### Authors

- `GET /authors?page=1&pageSize=20&search=john`
- `POST /authors`
- `PUT /authors/{id}`
- `DELETE /authors/{id}`

Response:

```json
{
  "items": [],
  "totalCount": 0,
  "pageSize": 20,
  "page": 1
}
```

### Articles

- `GET /articles`
  - query params:
    - `page`
    - `pageSize`
    - `search`
    - `publicationSource`
    - `articleType`
    - `status`
    - `authorId`
    - `printIssueId`
    - `hasVideo`
    - `sortBy`
    - `sortDirection`
- `GET /articles/by-ids?ids=id1,id2`
- `GET /articles/{id}`
- `POST /articles`
- `PUT /articles/{id}`
- `DELETE /articles/{id}`
- `POST /articles/{id}/duplication`

List response:

```json
{
  "items": [],
  "total": 0,
  "pageSize": 20,
  "page": 1
}
```

`GET /articles/by-ids` returns only:
- `id`
- `title`
- `publicationSource`
- `articleType`
- `featuredImageUrl`
- `status`

`POST /articles/{id}/duplication` creates a new copied article with:
- `status = DRAFT`
- new Mongo document
- cleared `publishedAt`

### Print Issues

- `GET /print-issues?page=1&pageSize=20`
- `GET /print-issues/latest`
- `POST /print-issues`
- `PUT /print-issues/{id}`
- `DELETE /print-issues/{id}`

List response:

```json
{
  "items": [],
  "total": 0,
  "page": 1,
  "pageSize": 20
}
```

`/print-issues/latest` returns the latest issue by `publicationDate` or `null`.

### Media

- `GET /media?page=1&pageSize=20&kind=IMAGE`
- `POST /media/upload`
- `DELETE /media/{id}`

`POST /media/upload`:
- multipart form-data
- field name: `file`

### Homepage Config

- `GET /homepage-config`
- `PUT /homepage-config`

Both require `homepage:manage`.

### Settings

- `GET /settings`
- `PUT /settings`

Both require:
- `ADMIN`
- `settings:manage`

## Notes

- Mongo document ids are intended to be managed by MongoDB.
- List `total` / `totalCount` values represent the full collection count, not just the current page size.
- Email validation enforces lowercase valid email format.
