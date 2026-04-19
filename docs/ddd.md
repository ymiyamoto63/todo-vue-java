# DDD (ドメイン駆動設計) 学習ノート

## 概念一覧

### Value Object（値オブジェクト）

値の内容によって識別されるオブジェクト。IDは持たない。

**特徴**
- **不変（immutable）**: `final` フィールド、setter なし
- **バリデーションを自身が持つ**: コンストラクタで不正な状態を弾く
- **値で等価比較**: `equals` / `hashCode` を値ベースでオーバーライド

**不変であるべき理由**
- 値が変わったら「別のオブジェクト」であるべきため（変更ではなく新規作成）
- 複数の Entity に共有されたとき、片方の変更が他方に影響しないようにするため

```java
// 不正な状態のオブジェクトが存在できない
public final class TodoTitle {
    private final String value;

    public TodoTitle(String value) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException("タイトルは必須です");
        if (value.length() > 200)            throw new IllegalArgumentException("200文字以内で入力してください");
        this.value = value;
    }
}
```

**実装例**: `TodoTitle`, `TodoDescription`, `TodoId`

---

### Entity（エンティティ）

IDによって識別されるオブジェクト。値が変わっても「同じもの」。

**特徴**
- IDで同一性を判断（`equals` は ID で比較）
- **ドメインロジックを自身が持つ**（貧血ドメインモデルにしない）
- 自分の状態変化（`updatedAt` の更新など）は自分で管理する

```java
// Bad: Service が状態を直接操作する（貧血ドメインモデル）
todo.setCompleted(true);
todo.setUpdatedAt(LocalDateTime.now());

// Good: Entity が振る舞いとして持つ
todo.complete();  // updatedAt の更新も内部で行う
```

**実装例**: `domain/entity/Todo.java`

---

### Aggregate（集約）

関連するオブジェクト群をひとまとめにして一貫性を保つ単位。  
入口となる Entity を **Aggregate Root** と呼ぶ。

**ルール**
- 外部は必ず Aggregate Root を経由してアクセスする
- 集約内部のオブジェクトに直接触ってはいけない
- Repository は集約単位で保存する（子 Entity の Repository を別に作らない）

```
Project（Aggregate Root）
    ├── ProjectId    (Value Object)
    ├── ProjectName  (Value Object)
    └── List<Todo>   ← Project を通してのみ追加・削除できる
```

**Todo の追加は必ず Project 経由**

```java
// NG: Todo を直接リポジトリで保存
todoRepository.save(todo);

// OK: Aggregate Root を通す
project.addTodo(title, description);
projectRepository.save(project); // Project ごと保存
```

**外部には読み取り専用リストを返す**

```java
// Project.java
public List<Todo> getTodos() {
    return Collections.unmodifiableList(todos); // 外から add() できないよう守る
}
```

**一貫性の強制例**

```java
// Aggregate Root がビジネスルールを守る
public void changeTitle(TodoTitle newTitle) {
    if (this.completed) throw new IllegalStateException("完了済みの Todo は編集できません");
    this.title = newTitle;
    this.updatedAt = LocalDateTime.now();
}
```

**複数 Entity を持つ集約の例（このアプリ）**

```
// Project が Aggregate Root、Todo が子 Entity
project.toggleTodo(todoId); // Project が内部の Todo を探して操作
// → TodoRepository は不要。ProjectRepository だけで集約全体を管理する
```

---

### Repository（リポジトリ）

**インターフェースをドメイン層に、実装をインフラ層に置く**ことで、  
ドメインがインフラの詳細（DB・インメモリ等）に依存しないようにする。

```
domain/repository/ProjectRepository.java              ← interface（ドメイン層）
infrastructure/repository/InMemoryProjectRepository.java ← 実装（インフラ層）
```

```java
// Service はインターフェースだけを知る
// → DB に切り替えても Service は一切変更不要
public class ProjectApplicationService {
    private final ProjectRepository repository; // interface
}
```

**子 Entity（Todo）専用の Repository は作らない**

```java
// NG: Todo を直接リポジトリで管理する
public interface TodoRepository { ... } // 集約の境界を壊す

// OK: 集約単位（Project）だけ Repository を持つ
public interface ProjectRepository { ... }
```

---

### Application Service（アプリケーションサービス）

ユースケースの調整役。Aggregate Root のメソッドを呼び出し、Repository を通じて永続化する。  
ビジネスロジック自体は持たない（それは Entity の責務）。

```java
// ProjectApplicationService
public TodoResponse addTodo(Long projectId, TodoRequest request) {
    Project project = findProjectById(projectId);
    project.addTodo(                              // Aggregate Root に委譲
        new TodoTitle(request.getTitle()),
        new TodoDescription(request.getDescription())
    );
    repository.save(project);                     // Project ごと保存
}

public TodoResponse toggleTodo(Long projectId, Long todoId) {
    Project project = findProjectById(projectId);
    project.toggleTodo(new TodoId(todoId));       // Project が Todo を探して操作
    repository.save(project);
}
```

**Application Service が持つべきでないもの**
- ビジネスルールの判断（例：完了済みかどうかのチェック → Entity の責務）
- 子 Entity への直接アクセス（必ず Aggregate Root 経由）

---

## レイヤー構造

```
controller/          プレゼンテーション層  HTTP の入出力
application/service/ アプリケーション層    ユースケースの調整
domain/              ドメイン層            ビジネスの核心（Entity・VO・Repository interface）
infrastructure/      インフラ層            技術的詳細（DB・インメモリ等）
dto/                 DTO                   層をまたぐデータ転送用
```

---

## Before / After 比較

| 責務 | Before（Transaction Script） | After（DDD） |
|---|---|---|
| バリデーション | `TodoRequest` のアノテーション | `TodoTitle` など Value Object |
| 状態変更 | `Service` が `setter` で直接操作 | `Entity` のメソッド経由 |
| 日時更新 | `Service` が `setUpdatedAt()` | `Entity` が自分で管理 |
| ルール強制 | `Service` に散在 | `Entity` に集約 |
