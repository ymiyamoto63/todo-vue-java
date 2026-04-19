<template>
  <v-app>
    <v-app-bar color="primary" elevation="2">
      <v-app-bar-title>TODO アプリ</v-app-bar-title>
    </v-app-bar>

    <v-main>
      <v-container fluid class="pa-0" style="height: calc(100vh - 64px);">
        <v-row no-gutters style="height: 100%;">

          <!-- 左ペイン: プロジェクト一覧 -->
          <v-col cols="3" style="border-right: 1px solid rgba(0,0,0,0.12); overflow-y: auto;">
            <v-list density="compact">
              <v-list-subheader>
                プロジェクト
                <v-btn
                  icon="mdi-plus"
                  size="x-small"
                  variant="text"
                  class="ml-1"
                  @click="projectDialog = true"
                />
              </v-list-subheader>

              <v-progress-linear v-if="loading" indeterminate color="primary" />

              <v-list-item
                v-for="project in projects"
                :key="project.id"
                :value="project.id"
                :active="selectedProjectId === project.id"
                active-color="primary"
                rounded="lg"
                @click="store.selectProject(project.id!)"
              >
                <v-list-item-title>{{ project.name }}</v-list-item-title>
                <v-list-item-subtitle>
                  {{ project.todos?.length ?? 0 }} 件
                </v-list-item-subtitle>
                <template #append>
                  <v-btn
                    icon="mdi-delete"
                    size="x-small"
                    variant="text"
                    color="error"
                    @click.stop="removeProject(project)"
                  />
                </template>
              </v-list-item>

              <v-list-item v-if="!loading && projects.length === 0">
                <v-list-item-subtitle class="text-center text-disabled">
                  プロジェクトがありません
                </v-list-item-subtitle>
              </v-list-item>
            </v-list>
          </v-col>

          <!-- 右ペイン: TODO一覧 -->
          <v-col cols="9" class="pa-4" style="overflow-y: auto;">
            <template v-if="selectedProject">
              <div class="d-flex align-center mb-4 flex-wrap gap-2">
                <span class="text-h6">{{ selectedProject.name }}</span>
                <v-spacer />
                <v-btn-toggle v-model="filter" mandatory density="compact" class="mr-2">
                  <v-btn value="all" size="small">すべて</v-btn>
                  <v-btn value="not-completed" size="small">未完了</v-btn>
                  <v-btn value="completed" size="small">完了</v-btn>
                  <v-btn value="overdue" size="small" color="error">
                    <v-icon start size="small">mdi-alert-circle</v-icon>期限切れ
                  </v-btn>
                  <v-btn value="high-priority" size="small" color="deep-orange">
                    <v-icon start size="small">mdi-fire</v-icon>優先High
                  </v-btn>
                </v-btn-toggle>
                <v-btn icon="mdi-plus" color="primary" variant="tonal" size="small" @click="openAddTodo" />
              </div>

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
                    <v-list-item-subtitle class="d-flex align-center flex-wrap gap-1 mt-1">
                      <span v-if="todo.description">{{ todo.description }}</span>
                      <!-- 優先度チップ -->
                      <v-chip
                        :color="priorityColor(todo.priority)"
                        size="x-small"
                        :prepend-icon="priorityIcon(todo.priority)"
                        variant="tonal"
                      >
                        {{ priorityLabel(todo.priority) }}
                      </v-chip>
                      <!-- 締め切り日チップ -->
                      <v-chip
                        v-if="todo.dueDate"
                        :color="todo.overdue ? 'error' : 'default'"
                        size="x-small"
                        :prepend-icon="todo.overdue ? 'mdi-alert-circle' : 'mdi-calendar'"
                        variant="tonal"
                      >
                        {{ todo.dueDate }}
                      </v-chip>
                    </v-list-item-subtitle>
                    <template #append>
                      <v-btn
                        icon="mdi-delete"
                        variant="text"
                        size="small"
                        color="error"
                        @click="removeTodo(todo)"
                      />
                    </template>
                  </v-list-item>
                </template>
              </v-list>

              <v-empty-state
                v-else
                icon="mdi-check-circle-outline"
                title="TODOがありません"
                text="右上の + ボタンから追加してください"
              />
            </template>

            <v-empty-state
              v-else
              icon="mdi-folder-outline"
              title="プロジェクトを選択してください"
              text="左のパネルからプロジェクトを選択するか、新しく作成してください"
            />
          </v-col>
        </v-row>
      </v-container>
    </v-main>

    <ProjectDialog v-model="projectDialog" @save="onCreateProject" />
    <TodoDialog v-model="todoDialog" :todo="null" @save="onAddTodo" />

    <v-snackbar v-model="snack.show" :color="snack.color" timeout="3000">
      {{ snack.text }}
    </v-snackbar>
  </v-app>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { storeToRefs } from 'pinia'
import { useProjectStore } from './stores/project'
import ProjectDialog from './components/ProjectDialog.vue'
import TodoDialog from './components/TodoDialog.vue'
import type { Todo, TodoRequest } from './types/todo'

const store = useProjectStore()
const { projects, selectedProjectId, selectedProject, loading, filter, filteredTodos } = storeToRefs(store)

const projectDialog = ref(false)
const todoDialog = ref(false)
const snack = ref({ show: false, text: '', color: 'success' })

type Priority = 'HIGH' | 'MEDIUM' | 'LOW' | undefined

function priorityColor(p: Priority) {
  if (p === 'HIGH') return 'deep-orange'
  if (p === 'LOW') return 'blue-grey'
  return 'primary'
}

function priorityIcon(p: Priority) {
  if (p === 'HIGH') return 'mdi-fire'
  if (p === 'LOW') return 'mdi-arrow-down'
  return 'mdi-minus'
}

function priorityLabel(p: Priority) {
  if (p === 'HIGH') return 'High'
  if (p === 'LOW') return 'Low'
  return 'Medium'
}

function openAddTodo() {
  todoDialog.value = true
}

async function onCreateProject(name: string) {
  try {
    await store.createProject({ name })
    notify('プロジェクトを作成しました')
  } catch {
    notify('作成に失敗しました', 'error')
  }
}

async function onAddTodo(fields: TodoRequest) {
  try {
    await store.addTodo(fields)
    notify('追加しました')
  } catch {
    notify('追加に失敗しました', 'error')
  }
}

async function toggle(todo: Todo) {
  try {
    await store.toggleTodo(todo.id!)
  } catch {
    notify('更新に失敗しました', 'error')
  }
}

async function removeTodo(todo: Todo) {
  try {
    await store.removeTodo(todo.id!)
    notify('削除しました')
  } catch {
    notify('削除に失敗しました', 'error')
  }
}

async function removeProject(project: { id?: number; name?: string }) {
  try {
    await store.deleteProject(project.id!)
    notify(`"${project.name}" を削除しました`)
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
