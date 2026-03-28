'use client';

import { useState, useEffect } from 'react';

const PRESET_COLORS = [
  '#FF3B30', '#FF9500', '#FFCC00', '#34C759', '#007AFF',
  '#5856D6', '#AF52DE', '#FF2D55', '#A2845E', '#8E8E93',
];

interface ListFormDialogProps {
  open: boolean;
  initialName?: string;
  initialColor?: string;
  title: string;
  onSubmit: (name: string, color: string) => void;
  onClose: () => void;
}

export default function ListFormDialog({
  open, initialName = '', initialColor = '#007AFF', title, onSubmit, onClose,
}: ListFormDialogProps) {
  const [name, setName] = useState(initialName);
  const [color, setColor] = useState(initialColor);

  useEffect(() => {
    setName(initialName);
    setColor(initialColor);
  }, [initialName, initialColor, open]);

  if (!open) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim()) return;
    onSubmit(name.trim(), color);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/30">
      <form
        onSubmit={handleSubmit}
        className="w-80 rounded-2xl bg-white p-6 shadow-xl"
      >
        <h2 className="mb-4 text-lg font-bold">{title}</h2>

        <input
          autoFocus
          value={name}
          onChange={(e) => setName(e.target.value)}
          placeholder="리스트 이름"
          className="mb-4 w-full rounded-lg border border-gray-200 px-3 py-2 text-sm outline-none focus:border-blue-500"
        />

        <div className="mb-4 flex flex-wrap gap-2">
          {PRESET_COLORS.map((c) => (
            <button
              key={c}
              type="button"
              onClick={() => setColor(c)}
              className="h-7 w-7 rounded-full transition-transform"
              style={{
                backgroundColor: c,
                transform: color === c ? 'scale(1.2)' : 'scale(1)',
                outline: color === c ? '2px solid var(--color-accent)' : 'none',
                outlineOffset: '2px',
              }}
            />
          ))}
        </div>

        <div className="flex justify-end gap-2">
          <button
            type="button"
            onClick={onClose}
            className="rounded-lg px-4 py-2 text-sm"
            style={{ color: 'var(--color-accent)' }}
          >
            취소
          </button>
          <button
            type="submit"
            className="rounded-lg px-4 py-2 text-sm font-semibold text-white"
            style={{ backgroundColor: 'var(--color-accent)' }}
          >
            확인
          </button>
        </div>
      </form>
    </div>
  );
}
