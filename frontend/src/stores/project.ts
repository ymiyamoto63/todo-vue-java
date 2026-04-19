import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { projectApi } from '../api/projects'
import type { Project, ProjectRequest, Todo, TodoRequest } from '../types/todo'

export type TodoFilter = 'all' | 'not-completed' | 'completed' | 'overdue' | 'high-priority'

export const FILTER_OPTIONS: { value: TodoFilter; label: string }[] = [
  { value: 'all',           label: 'すべて' },
  { value: 'not-completed', label: '未完了' },
  { value: 'completed',     label: '完了' },
  { value: 'overdue',       label: '期限切れ' },
  { value: 'high-priority', label: '優先High' },
]

export const useProjectStore = defineStore('project', () => {
  const projects = ref<Project[]>([])
  const selectedProjectId = ref<number | null>(null)
  const loading = ref(false)
  const filter = ref<TodoFilter>('all')

  const selectedProject = computed(() =>
    projects.value.find(p => p.id === selectedProjectId.value) ?? null
  )

  // クライアントサイドフィルタリング（todos の overdue/priority は backend が付与）
  const filteredTodos = computed((): Todo[] => {
    const todos = selectedProject.value?.todos ?? []
    switch (filter.value) {
      case 'not-completed': return todos.filter(t => !t.completed)
      case 'completed':     return todos.filter(t => t.completed)
      case 'overdue':       return todos.filter(t => !!t.overdue)
      case 'high-priority': return todos.filter(t => t.priority === 'HIGH')
      default:              return todos
    }
  })

  async function fetchAll() {
    loading.value = true
    try {
      const { data } = await projectApi.getAll()
      projects.value = data
    } finally {
      loading.value = false
    }
  }

  async function createProject(fields: ProjectRequest) {
    const { data } = await projectApi.create(fields)
    projects.value.unshift(data)
    selectedProjectId.value = data.id!
    filter.value = 'all'
  }

  async function deleteProject(id: number) {
    await projectApi.delete(id)
    projects.value = projects.value.filter(p => p.id !== id)
    if (selectedProjectId.value === id) {
      selectedProjectId.value = projects.value[0]?.id ?? null
    }
  }

  async function addTodo(fields: TodoRequest) {
    if (selectedProjectId.value == null) return
    await projectApi.addTodo(selectedProjectId.value, fields)
    await refreshSelected()
  }

  async function removeTodo(todoId: number) {
    if (selectedProjectId.value == null) return
    await projectApi.removeTodo(selectedProjectId.value, todoId)
    await refreshSelected()
  }

  async function toggleTodo(todoId: number) {
    if (selectedProjectId.value == null) return
    await projectApi.toggleTodo(selectedProjectId.value, todoId)
    await refreshSelected()
  }

  async function refreshSelected() {
    if (selectedProjectId.value == null) return
    const { data } = await projectApi.getById(selectedProjectId.value)
    const idx = projects.value.findIndex(p => p.id === data.id)
    if (idx !== -1) projects.value[idx] = data
  }

  function selectProject(id: number) {
    selectedProjectId.value = id
    filter.value = 'all'
  }

  return {
    projects, selectedProjectId, selectedProject,
    loading, filter, filteredTodos,
    fetchAll, createProject, deleteProject,
    addTodo, removeTodo, toggleTodo, selectProject,
  }
})
