'use client';

import { useState } from 'react';
import { Reminder } from '@/lib/types';

interface ReminderItemProps {
  reminder: Reminder;
  listColor: string;
  onToggleComplete: (id: number) => void;
  onClick: (reminder: Reminder) => void;
  onDelete: (id: number) => void;
}

const priorityLabels: Record<string, { text: string; color: string } | null> = {
  HIGH: { text: '!!!', color: '#FF3B30' },
  MEDIUM: { text: '!!', color: '#FF9500' },
  LOW: { text: '!', color: '#007AFF' },
  NONE: null,
};

function formatDate(dateStr: string | null): string | null {
  if (!dateStr) return null;
  const date = new Date(dateStr);
  return `${date.getMonth() + 1}/${date.getDate()}`;
}

export default function ReminderItem({
  reminder, listColor, onToggleComplete, onClick, onDelete,
}: ReminderItemProps) {
  const [fading, setFading] = useState(false);
  const [hovered, setHovered] = useState(false);
  const priority = priorityLabels[reminder.priority];

  const handleToggle = () => {
    if (!reminder.completed) {
      setFading(true);
      setTimeout(() => onToggleComplete(reminder.id), 500);
    } else {
      onToggleComplete(reminder.id);
    }
  };

  return (
    <div
      className="group flex items-start gap-3 rounded-lg px-3 py-2 transition-all"
      style={{
        opacity: fading || reminder.completed ? 0.5 : 1,
        transition: 'opacity 0.5s ease',
      }}
      onMouseEnter={() => setHovered(true)}
      onMouseLeave={() => setHovered(false)}
    >
      <button
        onClick={handleToggle}
        className="mt-0.5 flex h-5 w-5 flex-shrink-0 items-center justify-center rounded-full border-2 transition-colors"
        style={{
          borderColor: listColor,
          backgroundColor: reminder.completed ? listColor : 'transparent',
        }}
      >
        {reminder.completed && (
          <svg width="10" height="8" viewBox="0 0 10 8" fill="none">
            <path d="M1 4L3.5 6.5L9 1" stroke="white" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
          </svg>
        )}
      </button>

      <div className="flex-1 cursor-pointer" onClick={() => onClick(reminder)}>
        <div
          className="text-sm"
          style={{
            textDecoration: reminder.completed ? 'line-through' : 'none',
            color: reminder.completed ? 'var(--color-secondary)' : 'var(--color-foreground)',
          }}
        >
          {reminder.title}
        </div>
        {reminder.notes && !reminder.completed && (
          <div className="text-xs" style={{ color: 'var(--color-secondary)' }}>
            {reminder.notes}
          </div>
        )}
        <div className="flex items-center gap-2">
          {reminder.dueDate && (
            <span className="text-xs" style={{ color: 'var(--color-secondary)' }}>
              {formatDate(reminder.dueDate)}
            </span>
          )}
          {priority && (
            <span className="text-xs font-bold" style={{ color: priority.color }}>
              {priority.text}
            </span>
          )}
        </div>
      </div>

      {hovered && !reminder.completed && (
        <button
          onClick={() => onDelete(reminder.id)}
          className="mt-0.5 flex h-5 w-5 flex-shrink-0 items-center justify-center rounded-full text-xs text-gray-400 transition-colors hover:bg-gray-100 hover:text-red-500"
        >
          ✕
        </button>
      )}
    </div>
  );
}
