import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { todoApi } from '../api/todos'
import type { Todo, TodoRequest } from '../types/todo'

export const useTodoStore = defineStore('todo', () => {
  const todos = ref<Todo[]>([])
  const loading = ref(false)
  const filter = ref<'all' | 'active' | 'done'>('all')

  const filteredTodos = computed(() => {
    if (filter.value === 'active') return todos.value.filter(t => !t.completed)
    if (filter.value === 'done') return todos.value.filter(t => t.completed)
    return todos.value
  })

  async function fetchAll() {
    loading.value = true
    try {
      const { data } = await todoApi.getAll()
      todos.value = data
    } finally {
      loading.value = false
    }
  }

  async function create(fields: TodoRequest) {
    const { data } = await todoApi.create(fields)
    todos.value.unshift(data)
  }

  async function update(id: number, fields: TodoRequest) {
    const { data } = await todoApi.update(id, fields)
    const idx = todos.value.findIndex(t => t.id === data.id)
    if (idx !== -1) todos.value[idx] = data
  }

  async function toggle(todo: Todo) {
    const { data } = await todoApi.toggle(todo.id)
    const idx = todos.value.findIndex(t => t.id === data.id)
    if (idx !== -1) todos.value[idx] = data
  }

  async function remove(id: number) {
    await todoApi.delete(id)
    todos.value = todos.value.filter(t => t.id !== id)
  }

  return { todos, loading, filter, filteredTodos, fetchAll, create, update, toggle, remove }
})
