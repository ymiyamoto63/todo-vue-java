# Todo App

Spring Boot + Vue 3 で構築した TODO 管理アプリケーションです。

## 技術スタック

| 層 | 技術 |
|---|---|
| バックエンド | Java / Spring Boot / SpringDoc (OpenAPI) |
| フロントエンド | Vue 3 / TypeScript / Vite / Vuetify / axios |

## API・型定義の連携

バックエンドの型定義をフロントエンドで安全に利用するため、OpenAPI を介した自動生成フローを採用しています。

```
Backend (Java/Spring Boot)
        │
        │ @Schema アノテーション付き Model/DTO
        │ SpringDoc が自動生成
        ▼
http://localhost:8080/v3/api-docs  (OpenAPI JSON)
        │
        │ npm run generate:api
        │ (openapi-typescript)
        ▼
frontend/src/generated/api.ts      (自動生成・編集禁止)
        │
        │ import type { components }
        ▼
frontend/src/types/todo.ts         (Todo / TodoRequest を再エクスポート)
        │
        │ import type { Todo, TodoRequest }
        ▼
frontend/src/api/todos.ts          (axios で HTTP 呼び出し、型付き)
```

### 各層の役割

| 層 | ファイル | 役割 |
|---|---|---|
| Backend | `backend/.../model/Todo.java` | `@Schema` アノテーションで型情報を定義 |
| Backend | `backend/.../controller/TodoController.java` | `@Operation`/`@ApiResponse` でエンドポイントを文書化 |
| 自動生成 | `frontend/src/generated/api.ts` | OpenAPI JSON から `openapi-typescript` が生成。**直接編集禁止** |
| 型ラッパー | `frontend/src/types/todo.ts` | `generated/api.ts` から `Todo`/`TodoRequest` を再エクスポート |
| API クライアント | `frontend/src/api/todos.ts` | axios + 上記型でリクエスト関数を定義 |

### 型の更新手順

バックエンドのモデルを変更したら、バックエンドを起動した状態で以下を実行します。

```bash
cd frontend
npm run generate:api
```

`http://localhost:8080/v3/api-docs` から最新の OpenAPI 定義を取得し `src/generated/api.ts` を再生成します。型の不一致はコンパイルエラーとして検出できます。

## 起動方法

### バックエンド

```bash
cd backend
./mvnw spring-boot:run
```

### フロントエンド

```bash
cd frontend
npm install
npm run dev
```
