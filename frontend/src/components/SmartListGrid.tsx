'use client';

interface SmartListGridProps {
  selectedId: string | null;
  onSelect: (id: string) => void;
  counts: { today: number; scheduled: number; all: number; completed: number };
}

const smartLists = [
  { id: 'today', label: '오늘', icon: '📅', colorVar: 'var(--smart-today)' },
  { id: 'scheduled', label: '예정', icon: '📆', colorVar: 'var(--smart-scheduled)' },
  { id: 'all', label: '전체', icon: '📋', colorVar: 'var(--smart-all)' },
  { id: 'completed', label: '완료됨', icon: '✓', colorVar: 'var(--smart-completed)' },
];

export default function SmartListGrid({ selectedId, onSelect, counts }: SmartListGridProps) {
  const countMap: Record<string, number> = counts;

  return (
    <div className="grid grid-cols-2 gap-2 p-3">
      {smartLists.map((item) => (
        <button
          key={item.id}
          onClick={() => onSelect(item.id)}
          className="rounded-xl p-3 text-left transition-all"
          style={{
            backgroundColor: selectedId === item.id ? 'var(--color-hover)' : 'white',
          }}
        >
          <div className="flex items-start justify-between">
            <span
              className="flex h-7 w-7 items-center justify-center rounded-full text-sm text-white"
              style={{ backgroundColor: item.colorVar }}
            >
              {item.icon}
            </span>
            <span className="text-xl font-bold" style={{ color: item.colorVar }}>
              {countMap[item.id] ?? 0}
            </span>
          </div>
          <div className="mt-1 text-sm font-semibold" style={{ color: 'var(--color-secondary)' }}>
            {item.label}
          </div>
        </button>
      ))}
    </div>
  );
}
