export type Priority = 'NONE' | 'LOW' | 'MEDIUM' | 'HIGH';

export interface ReminderList {
  id: number;
  name: string;
  color: string;
  defaultList: boolean;
  incompleteCount: number;
  createdAt: string;
  updatedAt: string;
}

export interface Reminder {
  id: number;
  title: string;
  notes: string | null;
  dueDate: string | null;
  priority: Priority;
  completed: boolean;
  completedAt: string | null;
  listId: number;
  createdAt: string;
  updatedAt: string;
}
