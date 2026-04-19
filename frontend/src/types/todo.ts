// openapi-typescript で生成した src/generated/api.ts からの再エクスポート。
// 型の単一ソースは generated/api.ts であり、このファイルは直接編集しない。
// Backend 変更時は npm run generate:api を再実行すること。

import type { components } from '../generated/api'

export type Todo = components['schemas']['TodoResponse']
export type TodoRequest = components['schemas']['TodoRequest']
export type Project = components['schemas']['ProjectResponse']
export type ProjectRequest = components['schemas']['ProjectRequest']
