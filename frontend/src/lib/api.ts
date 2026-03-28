import { Reminder, ReminderList, Priority } from './types';

const API_BASE = 'http://localhost:8080/api';

async function request<T>(url: string, options?: RequestInit): Promise<T> {
  const res = await fetch(`${API_BASE}${url}`, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  });
  if (!res.ok) {
    throw new Error(`API error: ${res.status}`);
  }
  if (res.status === 204) return undefined as T;
  return res.json();
}

// ReminderList API
export const listApi = {
  findAll: () => request<ReminderList[]>('/lists'),

  findById: (id: number) => request<ReminderList>(`/lists/${id}`),

  create: (name: string, color: string) =>
    request<ReminderList>('/lists', {
      method: 'POST',
      body: JSON.stringify({ name, color }),
    }),

  update: (id: number, name: string, color: string) =>
    request<ReminderList>(`/lists/${id}`, {
      method: 'PUT',
      body: JSON.stringify({ name, color }),
    }),

  delete: (id: number) =>
    request<void>(`/lists/${id}`, { method: 'DELETE' }),
};

// Reminder API
export const reminderApi = {
  findByListId: (listId: number) =>
    request<Reminder[]>(`/lists/${listId}/reminders`),

  create: (listId: number, data: {
    title: string;
    notes?: string;
    dueDate?: string;
    priority?: Priority;
  }) =>
    request<Reminder>(`/lists/${listId}/reminders`, {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  update: (id: number, data: {
    title: string;
    notes?: string | null;
    dueDate?: string | null;
    priority?: Priority;
    listId: number;
  }) =>
    request<Reminder>(`/reminders/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    }),

  toggleComplete: (id: number) =>
    request<Reminder>(`/reminders/${id}/complete`, { method: 'PATCH' }),

  delete: (id: number) =>
    request<void>(`/reminders/${id}`, { method: 'DELETE' }),
};
