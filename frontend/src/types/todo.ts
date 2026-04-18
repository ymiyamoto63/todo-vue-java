// Backend の Todo モデル / TodoRequest DTO に対応する型定義。
// npm run generate:api 実行後は src/generated/api からインポートするよう更新できる。

export interface Todo {
  id: number
  title: string
  description?: string
  completed: boolean
  createdAt: string
  updatedAt: string
}

export interface TodoRequest {
  title: string
  description?: string
  completed?: boolean
}
