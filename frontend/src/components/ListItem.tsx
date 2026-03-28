'use client';

import { ReminderList } from '@/lib/types';

interface ListItemProps {
  list: ReminderList;
  selected: boolean;
  onSelect: () => void;
  onContextMenu: (e: React.MouseEvent) => void;
}

export default function ListItem({ list, selected, onSelect, onContextMenu }: ListItemProps) {
  return (
    <button
      onClick={onSelect}
      onContextMenu={onContextMenu}
      className="flex w-full items-center gap-3 rounded-lg px-3 py-2 text-left transition-colors"
      style={{
        backgroundColor: selected ? 'var(--color-hover)' : 'transparent',
      }}
    >
      <span
        className="flex h-7 w-7 items-center justify-center rounded-full text-xs text-white"
        style={{ backgroundColor: list.color }}
      >
        ●
      </span>
      <span className="flex-1 text-sm">{list.name}</span>
      <span className="text-sm" style={{ color: 'var(--color-secondary)' }}>
        {list.incompleteCount}
      </span>
    </button>
  );
}
