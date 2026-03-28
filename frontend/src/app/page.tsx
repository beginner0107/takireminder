'use client';

import { useCallback, useEffect, useState } from 'react';
import { ReminderList } from '@/lib/types';
import { listApi } from '@/lib/api';
import Sidebar from '@/components/Sidebar';

export default function Home() {
  const [lists, setLists] = useState<ReminderList[]>([]);
  const [selectedId, setSelectedId] = useState<string | null>(null);

  const loadLists = useCallback(async () => {
    const data = await listApi.findAll();
    setLists(data);
  }, []);

  useEffect(() => {
    loadLists();
  }, [loadLists]);

  return (
    <div className="flex h-full">
      <Sidebar
        lists={lists}
        selectedId={selectedId}
        onSelect={setSelectedId}
        onListsChange={loadLists}
      />

      <main className="flex-1 overflow-y-auto p-8">
        {selectedId ? (
          <div>
            <h1 className="text-4xl font-bold">
              {selectedId.startsWith('list-')
                ? lists.find((l) => l.id === Number(selectedId.replace('list-', '')))?.name
                : { today: '오늘', scheduled: '예정', all: '전체', completed: '완료됨' }[selectedId]}
            </h1>
          </div>
        ) : (
          <div className="flex h-full items-center justify-center" style={{ color: 'var(--color-secondary)' }}>
            <p className="text-lg">리스트를 선택하세요</p>
          </div>
        )}
      </main>
    </div>
  );
}
