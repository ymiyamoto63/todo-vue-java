import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { projectApi } from '../api/projects'
import type { Project, ProjectRequest, TodoRequest } from '../types/todo'

export const useProjectStore = defineStore('project', () => {
  const projects = ref<Project[]>([])
  const selectedProjectId = ref<number | null>(null)
  const loading = ref(false)
  const filter = ref<'all' | 'active' | 'done'>('all')

  const selectedProject = computed(() =>
    projects.value.find(p => p.id === selectedProjectId.value) ?? null
  )

  const filteredTodos = computed(() => {
    const todos = selectedProject.value?.todos ?? []
    if (filter.value === 'active') return todos.filter(t => !t.completed)
    if (filter.value === 'done') return todos.filter(t => t.completed)
    return todos
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
