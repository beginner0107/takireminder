package taki.ai.takireminder.dto;

import jakarta.validation.constraints.NotBlank;
import taki.ai.takireminder.domain.Priority;

import java.time.LocalDateTime;

public record ReminderRequest(
        @NotBlank String title,
        String notes,
        LocalDateTime dueDate,
        Priority priority,
        Long listId
) {
}
