<template>
  <v-dialog v-model="dialog" max-width="400">
    <v-card>
      <v-card-title>プロジェクトを追加</v-card-title>
      <v-card-text>
        <v-form ref="form" @submit.prevent="submit">
          <v-text-field
            v-model="name"
            label="プロジェクト名"
            :rules="[v => !!v || 'プロジェクト名は必須です']"
            required
            autofocus
          />
        </v-form>
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn text @click="dialog = false">キャンセル</v-btn>
        <v-btn color="primary" @click="submit">追加</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'

const props = defineProps<{ modelValue: boolean }>()
const emit = defineEmits<{
  'update:modelValue': [value: boolean]
  save: [name: string]
}>()

const form = ref<{ validate: () => Promise<{ valid: boolean }> } | null>(null)
const name = ref('')

const dialog = computed({
  get: () => props.modelValue,
  set: (v: boolean) => emit('update:modelValue', v),
})

watch(() => props.modelValue, (open) => {
  if (open) name.value = ''
})

async function submit() {
  const { valid } = await form.value!.validate()
  if (!valid) return
  emit('save', name.value)
  dialog.value = false
}
</script>
