'use client';

import { useCallback, useEffect, useState } from 'react';
import { Reminder, ReminderList } from '@/lib/types';
import { reminderApi } from '@/lib/api';
import ReminderItem from './ReminderItem';
import AddReminder from './AddReminder';
import DetailPanel from './DetailPanel';

interface ReminderListViewProps {
  list: ReminderList;
  allLists: ReminderList[];
  onListsChange: () => void;
}

export default function ReminderListView({ list, allLists, onListsChange }: ReminderListViewProps) {
  const [reminders, setReminders] = useState<Reminder[]>([]);
  const [selectedReminder, setSelectedReminder] = useState<Reminder | null>(null);

  const loadReminders = useCallback(async () => {
    const data = await reminderApi.findByListId(list.id);
    setReminders(data);
  }, [list.id]);

  useEffect(() => {
    loadReminders();
    setSelectedReminder(null);
  }, [loadReminders]);

  const handleAdd = async (title: string) => {
    await reminderApi.create(list.id, { title });
    loadReminders();
    onListsChange();
  };

  const handleToggleComplete = async (id: number) => {
    await reminderApi.toggleComplete(id);
    loadReminders();
    onListsChange();
  };

  const handleDelete = async (id: number) => {
    await reminderApi.delete(id);
    if (selectedReminder?.id === id) setSelectedReminder(null);
    loadReminders();
    onListsChange();
  };

  const handleSave = async (data: Parameters<typeof reminderApi.update>[1]) => {
    if (!selectedReminder) return;
    const updated = await reminderApi.update(selectedReminder.id, data);
    setSelectedReminder(updated);
    loadReminders();
    onListsChange();
  };

  const incompleteReminders = reminders.filter((r) => !r.completed);
  const completedReminders = reminders.filter((r) => r.completed);

  return (
    <div className="flex h-full">
      <div className="flex-1 overflow-y-auto p-8">
        <h1 className="mb-6 text-4xl font-bold" style={{ color: list.color }}>
          {list.name}
        </h1>

        <div className="space-y-0.5">
          {incompleteReminders.map((r) => (
            <ReminderItem
              key={r.id}
              reminder={r}
              listColor={list.color}
              onToggleComplete={handleToggleComplete}
              onClick={setSelectedReminder}
              onDelete={handleDelete}
            />
          ))}
        </div>

        <div className="mt-2">
          <AddReminder accentColor={list.color} onAdd={handleAdd} />
        </div>

        {completedReminders.length > 0 && (
          <div className="mt-6">
            <div className="mb-2 px-3 text-xs font-semibold" style={{ color: 'var(--color-secondary)' }}>
              완료됨 ({completedReminders.length})
            </div>
            <div className="space-y-0.5">
              {completedReminders.map((r) => (
                <ReminderItem
                  key={r.id}
                  reminder={r}
                  listColor={list.color}
                  onToggleComplete={handleToggleComplete}
                  onClick={setSelectedReminder}
                  onDelete={handleDelete}
                />
              ))}
            </div>
          </div>
        )}
      </div>

      {selectedReminder && (
        <DetailPanel
          reminder={selectedReminder}
          lists={allLists}
          onSave={handleSave}
          onClose={() => setSelectedReminder(null)}
        />
      )}
    </div>
  );
}
