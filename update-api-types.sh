#!/usr/bin/env bash
set -euo pipefail

# nvm 管理の Node.js を PATH に追加
NVM_DIR="/c/Users/$(whoami)/AppData/Local/nvm"
if [[ -d "$NVM_DIR" ]]; then
  NODE_DIR=$(ls -d "$NVM_DIR"/v*/ 2>/dev/null | sort -V | tail -1)
  [[ -n "$NODE_DIR" ]] && export PATH="$NODE_DIR:$PATH"
fi

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BACKEND_URL="http://localhost:8080/v3/api-docs"
OPENAPI_JSON="$SCRIPT_DIR/openapi.json"
BACKEND_PID=""

# バックエンドが既に起動しているか確認
backend_running() {
  curl -sf "$BACKEND_URL" -o /dev/null 2>/dev/null
}

# スクリプト終了時にバックエンドを停止（自分で起動した場合のみ）
cleanup() {
  if [[ -n "$BACKEND_PID" ]]; then
    echo "バックエンドを停止します (PID: $BACKEND_PID)..."
    kill "$BACKEND_PID" 2>/dev/null || true
  fi
}
trap cleanup EXIT

# --- 1. バックエンド起動 ---
if backend_running; then
  echo "バックエンドは既に起動しています。"
else
  echo "バックエンドを起動しています..."
  cd "$SCRIPT_DIR/backend"
  ./mvnw spring-boot:run -q &
  BACKEND_PID=$!

  echo -n "起動待機中"
  for i in $(seq 1 60); do
    if backend_running; then
      echo " 完了"
      break
    fi
    echo -n "."
    sleep 3
    if [[ $i -eq 60 ]]; then
      echo ""
      echo "エラー: バックエンドの起動がタイムアウトしました。" >&2
      exit 1
    fi
  done
fi

# --- 2. openapi.json を取得 ---
echo "openapi.json をエクスポートしています..."
curl -sf "$BACKEND_URL" -o "$OPENAPI_JSON"
echo "保存先: $OPENAPI_JSON"

# --- 3. TypeScript 型を生成 ---
echo "TypeScript 型を生成しています..."
cd "$SCRIPT_DIR/frontend"
npm run generate:api

echo ""
echo "完了: frontend/src/generated/api.ts を更新しました。"
