import axios from 'axios'
import type { Project, ProjectRequest, Todo, TodoRequest } from '../types/todo'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
})

export type TodoFilter = 'all' | 'not-completed' | 'completed' | 'overdue' | 'high-priority'

export const projectApi = {
  getAll: () => api.get<Project[]>('/projects'),
  getById: (id: number) => api.get<Project>(`/projects/${id}`),
  create: (data: ProjectRequest) => api.post<Project>('/projects', data),
  delete: (id: number) => api.delete<void>(`/projects/${id}`),

  filterTodos: (projectId: number, filters: string[]) =>
    api.get<Todo[]>(`/projects/${projectId}/todos`, {
      params: filters.length ? { filter: filters } : {},
      paramsSerializer: { indexes: null },
    }),

  addTodo: (projectId: number, data: TodoRequest) =>
    api.post<Todo>(`/projects/${projectId}/todos`, data),
  removeTodo: (projectId: number, todoId: number) =>
    api.delete<void>(`/projects/${projectId}/todos/${todoId}`),
  toggleTodo: (projectId: number, todoId: number) =>
    api.patch<Todo>(`/projects/${projectId}/todos/${todoId}/toggle`),
}
