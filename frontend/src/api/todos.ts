import axios from 'axios'
import type { Todo, TodoRequest } from '../types/todo'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
})

export const todoApi = {
  getAll: () => api.get<Todo[]>('/todos'),
  getById: (id: number) => api.get<Todo>(`/todos/${id}`),
  create: (data: TodoRequest) => api.post<Todo>('/todos', data),
  update: (id: number, data: TodoRequest) => api.put<Todo>(`/todos/${id}`, data),
  toggle: (id: number) => api.patch<Todo>(`/todos/${id}/toggle`),
  delete: (id: number) => api.delete<void>(`/todos/${id}`),
}
