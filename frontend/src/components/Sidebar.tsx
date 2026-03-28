'use client';

import { useState } from 'react';
import { ReminderList } from '@/lib/types';
import { listApi } from '@/lib/api';
import SmartListGrid from './SmartListGrid';
import ListItem from './ListItem';
import ListFormDialog from './ListFormDialog';
import ContextMenu from './ContextMenu';

interface SidebarProps {
  lists: ReminderList[];
  selectedId: string | null;
  onSelect: (id: string) => void;
  onListsChange: () => void;
}

export default function Sidebar({ lists, selectedId, onSelect, onListsChange }: SidebarProps) {
  const [showCreateDialog, setShowCreateDialog] = useState(false);
  const [editTarget, setEditTarget] = useState<ReminderList | null>(null);
  const [contextMenu, setContextMenu] = useState<{
    list: ReminderList;
    x: number;
    y: number;
  } | null>(null);

  const handleCreate = async (name: string, color: string) => {
    await listApi.create(name, color);
    setShowCreateDialog(false);
    onListsChange();
  };

  const handleEdit = async (name: string, color: string) => {
    if (!editTarget) return;
    await listApi.update(editTarget.id, name, color);
    setEditTarget(null);
    onListsChange();
  };

  const handleDelete = async (list: ReminderList) => {
    if (!confirm(`"${list.name}" 리스트를 삭제하시겠습니까?\n소속 리마인더도 함께 삭제됩니다.`)) return;
    await listApi.delete(list.id);
    setContextMenu(null);
    onListsChange();
  };

  const handleContextMenu = (e: React.MouseEvent, list: ReminderList) => {
    e.preventDefault();
    if (list.defaultList) return;
    setContextMenu({ list, x: e.clientX, y: e.clientY });
  };

  const smartCounts = { today: 0, scheduled: 0, all: 0, completed: 0 };

  return (
    <aside
      className="flex h-screen w-70 flex-col overflow-y-auto"
      style={{ backgroundColor: 'var(--color-sidebar)' }}
    >
      <SmartListGrid
        selectedId={selectedId}
        onSelect={onSelect}
        counts={smartCounts}
      />

      <div
        className="mx-3 my-1"
        style={{ borderTop: '0.5px solid var(--color-separator)' }}
      />

      <div className="px-3 pb-1 pt-2 text-xs font-semibold" style={{ color: 'var(--color-secondary)' }}>
        내 목록
      </div>

      <div className="flex-1 px-1">
        {lists.map((list) => (
          <ListItem
            key={list.id}
            list={list}
            selected={selectedId === `list-${list.id}`}
            onSelect={() => onSelect(`list-${list.id}`)}
            onContextMenu={(e) => handleContextMenu(e, list)}
          />
        ))}
      </div>

      <button
        onClick={() => setShowCreateDialog(true)}
        className="mx-3 mb-4 flex items-center gap-2 rounded-lg px-3 py-2 text-sm transition-colors hover:bg-white/50"
        style={{ color: 'var(--color-accent)' }}
      >
        <span className="text-lg">+</span>
        목록 추가
      </button>

      <ListFormDialog
        open={showCreateDialog}
        title="새로운 목록"
        onSubmit={handleCreate}
        onClose={() => setShowCreateDialog(false)}
      />

      <ListFormDialog
        open={!!editTarget}
        title="목록 수정"
        initialName={editTarget?.name}
        initialColor={editTarget?.color}
        onSubmit={handleEdit}
        onClose={() => setEditTarget(null)}
      />

      {contextMenu && (
        <ContextMenu
          x={contextMenu.x}
          y={contextMenu.y}
          onEdit={() => {
            setEditTarget(contextMenu.list);
            setContextMenu(null);
          }}
          onDelete={() => handleDelete(contextMenu.list)}
          onClose={() => setContextMenu(null)}
        />
      )}
    </aside>
  );
}
