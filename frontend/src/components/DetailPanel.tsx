'use client';

import { useEffect, useState } from 'react';
import { Reminder, ReminderList, Priority } from '@/lib/types';

interface DetailPanelProps {
  reminder: Reminder;
  lists: ReminderList[];
  onSave: (data: {
    title: string;
    notes: string | null;
    dueDate: string | null;
    priority: Priority;
    listId: number;
  }) => void;
  onClose: () => void;
}

const priorities: { value: Priority; label: string }[] = [
  { value: 'NONE', label: '없음' },
  { value: 'LOW', label: '낮음' },
  { value: 'MEDIUM', label: '보통' },
  { value: 'HIGH', label: '높음' },
];

export default function DetailPanel({ reminder, lists, onSave, onClose }: DetailPanelProps) {
  const [title, setTitle] = useState(reminder.title);
  const [notes, setNotes] = useState(reminder.notes ?? '');
  const [dueDate, setDueDate] = useState(reminder.dueDate?.slice(0, 16) ?? '');
  const [priority, setPriority] = useState<Priority>(reminder.priority);
  const [listId, setListId] = useState(reminder.listId);

  useEffect(() => {
    setTitle(reminder.title);
    setNotes(reminder.notes ?? '');
    setDueDate(reminder.dueDate?.slice(0, 16) ?? '');
    setPriority(reminder.priority);
    setListId(reminder.listId);
  }, [reminder]);

  const handleSave = () => {
    if (!title.trim()) return;
    onSave({
      title: title.trim(),
      notes: notes || null,
      dueDate: dueDate || null,
      priority,
      listId,
    });
  };

  return (
    <div
      className="flex h-full w-80 flex-col border-l overflow-y-auto"
      style={{ borderColor: 'var(--color-separator)' }}
    >
      <div className="flex items-center justify-between p-4">
        <h2 className="text-sm font-semibold">세부 사항</h2>
        <button
          onClick={onClose}
          className="text-sm"
          style={{ color: 'var(--color-accent)' }}
        >
          완료
        </button>
      </div>

      <div className="flex flex-col gap-3 px-4 pb-4">
        {/* 제목 */}
        <div className="rounded-xl bg-gray-50 p-3">
          <input
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            className="w-full bg-transparent text-sm font-medium outline-none"
            placeholder="제목"
          />
        </div>

        {/* 메모 */}
        <div className="rounded-xl bg-gray-50 p-3">
          <textarea
            value={notes}
            onChange={(e) => setNotes(e.target.value)}
            className="w-full resize-none bg-transparent text-sm outline-none"
            placeholder="메모"
            rows={3}
          />
        </div>

        {/* 마감일 */}
        <div className="rounded-xl bg-gray-50 p-3">
          <label className="mb-1 block text-xs" style={{ color: 'var(--color-secondary)' }}>
            마감일
          </label>
          <input
            type="datetime-local"
            value={dueDate}
            onChange={(e) => setDueDate(e.target.value)}
            className="w-full bg-transparent text-sm outline-none"
          />
        </div>

        {/* 우선순위 */}
        <div className="rounded-xl bg-gray-50 p-3">
          <label className="mb-2 block text-xs" style={{ color: 'var(--color-secondary)' }}>
            우선순위
          </label>
          <div className="flex rounded-lg bg-gray-200 p-0.5">
            {priorities.map((p) => (
              <button
                key={p.value}
                onClick={() => setPriority(p.value)}
                className="flex-1 rounded-md py-1 text-xs font-medium transition-colors"
                style={{
                  backgroundColor: priority === p.value ? 'white' : 'transparent',
                  color: priority === p.value ? 'var(--color-foreground)' : 'var(--color-secondary)',
                  boxShadow: priority === p.value ? '0 1px 3px rgba(0,0,0,0.1)' : 'none',
                }}
              >
                {p.label}
              </button>
            ))}
          </div>
        </div>

        {/* 리스트 선택 */}
        <div className="rounded-xl bg-gray-50 p-3">
          <label className="mb-1 block text-xs" style={{ color: 'var(--color-secondary)' }}>
            목록
          </label>
          <select
            value={listId}
            onChange={(e) => setListId(Number(e.target.value))}
            className="w-full bg-transparent text-sm outline-none"
          >
            {lists.map((l) => (
              <option key={l.id} value={l.id}>{l.name}</option>
            ))}
          </select>
        </div>

        <button
          onClick={handleSave}
          className="mt-2 w-full rounded-xl py-2 text-sm font-semibold text-white"
          style={{ backgroundColor: 'var(--color-accent)' }}
        >
          저장
        </button>
      </div>
    </div>
  );
}
