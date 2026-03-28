package taki.ai.takireminder.dto;

import taki.ai.takireminder.domain.ReminderList;

import java.time.LocalDateTime;

public record ReminderListResponse(
        Long id,
        String name,
        String color,
        boolean defaultList,
        int incompleteCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ReminderListResponse from(ReminderList list) {
        return new ReminderListResponse(
                list.getId(),
                list.getName(),
                list.getColor(),
                list.isDefaultList(),
                0,
                list.getCreatedAt(),
                list.getUpdatedAt()
        );
    }

    public static ReminderListResponse from(ReminderList list, int incompleteCount) {
        return new ReminderListResponse(
                list.getId(),
                list.getName(),
                list.getColor(),
                list.isDefaultList(),
                incompleteCount,
                list.getCreatedAt(),
                list.getUpdatedAt()
        );
    }
}
