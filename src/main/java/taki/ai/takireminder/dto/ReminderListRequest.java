package taki.ai.takireminder.dto;

import jakarta.validation.constraints.NotBlank;

public record ReminderListRequest(
        @NotBlank String name,
        @NotBlank String color
) {
}
