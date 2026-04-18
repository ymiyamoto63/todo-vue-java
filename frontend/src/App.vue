<template>
  <v-app>
    <v-app-bar color="primary" elevation="2">
      <v-app-bar-title>TODO アプリ</v-app-bar-title>
      <template #append>
        <v-btn icon="mdi-plus" @click="openCreate" />
      </template>
    </v-app-bar>

    <v-main>
      <v-container max-width="800">
        <!-- フィルター -->
        <v-btn-toggle v-model="filter" mandatory class="mb-4" density="compact">
          <v-btn value="all">すべて</v-btn>
          <v-btn value="active">未完了</v-btn>
          <v-btn value="done">完了</v-btn>
        </v-btn-toggle>

        <!-- ローディング -->
        <v-progress-linear v-if="loading" indeterminate color="primary" class="mb-4" />

        <!-- リスト -->
        <v-list v-if="filteredTodos.length" lines="two" rounded="lg" elevation="1">
          <template v-for="(todo, i) in filteredTodos" :key="todo.id">
            <v-divider v-if="i > 0" />
            <v-list-item :class="{ 'text-disabled': todo.completed }">
              <template #prepend>
                <v-checkbox-btn
                  :model-value="todo.completed"
                  color="primary"
                  @change="toggle(todo)"
                />
              </template>
              <v-list-item-title :class="{ 'text-decoration-line-through': todo.completed }">
                {{ todo.title }}
              </v-list-item-title>
              <v-list-item-subtitle v-if="todo.description">
                {{ todo.description }}
              </v-list-item-subtitle>
              <template #append>
                <v-btn icon="mdi-pencil" variant="text" size="small" @click="openEdit(todo)" />
                <v-btn icon="mdi-delete" variant="text" size="small" color="error" @click="remove(todo)" />
              </template>
            </v-list-item>
          </template>
        </v-list>

        <v-empty-state
          v-else-if="!loading"
          icon="mdi-check-circle-outline"
          title="TODOがありません"
          text="右上の + ボタンから追加してください"
        />
      </v-container>
    </v-main>

    <!-- ダイアログ -->
    <TodoDialog
      v-model="dialogOpen"
      :todo="editingTodo"
      @save="onSave"
    />

    <!-- スナックバー -->
    <v-snackbar v-model="snack.show" :color="snack.color" timeout="3000">
      {{ snack.text }}
    </v-snackbar>
  </v-app>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { useTodoStore } from './stores/todo'
import TodoDialog from './components/TodoDialog.vue'
import type { Todo, TodoRequest } from './types/todo'

const store = useTodoStore()
const { filteredTodos, loading, filter } = storeToRefs(store)

const dialogOpen = ref(false)
const editingTodo = ref<Todo | null>(null)
const snack = ref({ show: false, text: '', color: 'success' })

function openCreate() {
  editingTodo.value = null
  dialogOpen.value = true
}

function openEdit(todo: Todo) {
  editingTodo.value = todo
  dialogOpen.value = true
}

async function onSave(fields: TodoRequest) {
  try {
    if (editingTodo.value) {
      await store.update(editingTodo.value.id!, fields)
      notify('更新しました')
    } else {
      await store.create(fields)
      notify('追加しました')
    }
  } catch {
    notify('保存に失敗しました', 'error')
  }
}

async function toggle(todo: Todo) {
  try {
    await store.toggle(todo)
  } catch {
    notify('更新に失敗しました', 'error')
  }
}

async function remove(todo: Todo) {
  try {
    await store.remove(todo.id!)
    notify('削除しました')
  } catch {
    notify('削除に失敗しました', 'error')
  }
}

function notify(text: string, color = 'success') {
  snack.value = { show: true, text, color }
}

onMounted(async () => {
  try {
    await store.fetchAll()
  } catch {
    notify('取得に失敗しました', 'error')
  }
})
</script>
