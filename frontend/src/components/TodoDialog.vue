<template>
  <v-dialog v-model="dialog" max-width="500">
    <v-card>
      <v-card-title>{{ isEdit ? 'TODOを編集' : 'TODOを追加' }}</v-card-title>
      <v-card-text>
        <v-form ref="form" @submit.prevent="submit">
          <v-text-field
            v-model="fields.title"
            label="タイトル"
            :rules="[v => !!v || 'タイトルは必須です']"
            required
            autofocus
          />
          <v-textarea
            v-model="fields.description"
            label="説明"
            rows="3"
          />
          <v-text-field
            v-model="fields.dueDate"
            label="締め切り日"
            type="date"
            clearable
          />
          <v-select
            v-model="fields.priority"
            label="優先度"
            :items="priorityItems"
            item-title="label"
            item-value="value"
          />
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn text @click="dialog = false">キャンセル</v-btn>
        <v-btn color="primary" @click="submit">{{ isEdit ? '更新' : '追加' }}</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import type { Todo, TodoRequest } from '../types/todo'

const props = defineProps<{
  modelValue: boolean
  todo: Todo | null
}>()
const emit = defineEmits<{
  'update:modelValue': [value: boolean]
  save: [fields: TodoRequest]
}>()

const priorityItems = [
  { value: 'HIGH',   label: '高 (HIGH)' },
  { value: 'MEDIUM', label: '中 (MEDIUM)' },
  { value: 'LOW',    label: '低 (LOW)' },
]

const form = ref<{ validate: () => Promise<{ valid: boolean }> } | null>(null)
const fields = ref<TodoRequest>({ title: '', description: '', priority: 'MEDIUM' })
const isEdit = computed(() => !!props.todo)

const dialog = computed({
  get: () => props.modelValue,
  set: (v: boolean) => emit('update:modelValue', v),
})

watch(() => props.modelValue, (open) => {
  if (open) {
    fields.value = {
      title: props.todo?.title ?? '',
      description: props.todo?.description ?? '',
      dueDate: props.todo?.dueDate ?? undefined,
      priority: props.todo?.priority ?? 'MEDIUM',
    }
  }
})

async function submit() {
  const { valid } = await form.value!.validate()
  if (!valid) return
  const payload: TodoRequest = {
    ...fields.value,
    dueDate: fields.value.dueDate || undefined,
  }
  emit('save', payload)
  dialog.value = false
}
</script>
