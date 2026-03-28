'use client';

import { useState } from 'react';

interface AddReminderProps {
  accentColor: string;
  onAdd: (title: string) => void;
}

export default function AddReminder({ accentColor, onAdd }: AddReminderProps) {
  const [editing, setEditing] = useState(false);
  const [title, setTitle] = useState('');

  const handleSubmit = () => {
    if (title.trim()) {
      onAdd(title.trim());
      setTitle('');
    }
    setEditing(false);
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter') handleSubmit();
    if (e.key === 'Escape') {
      setTitle('');
      setEditing(false);
    }
  };

  if (!editing) {
    return (
      <button
        onClick={() => setEditing(true)}
        className="flex items-center gap-2 px-3 py-2 text-sm"
        style={{ color: accentColor }}
      >
        <span className="text-lg">+</span>
        새로운 리마인더
      </button>
    );
  }

  return (
    <div className="flex items-center gap-3 px-3 py-2">
      <span
        className="flex h-5 w-5 flex-shrink-0 items-center justify-center rounded-full border-2"
        style={{ borderColor: accentColor }}
      />
      <input
        autoFocus
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        onKeyDown={handleKeyDown}
        onBlur={handleSubmit}
        placeholder="새로운 리마인더"
        className="flex-1 bg-transparent text-sm outline-none"
      />
    </div>
  );
}
