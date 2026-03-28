package taki.ai.takireminder.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import taki.ai.takireminder.dto.ReminderRequest;
import taki.ai.takireminder.dto.ReminderResponse;
import taki.ai.takireminder.service.ports.in.ReminderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;

    @GetMapping("/api/lists/{listId}/reminders")
    public List<ReminderResponse> findByListId(@PathVariable Long listId) {
        return reminderService.findByListId(listId).stream()
                .map(ReminderResponse::from)
                .toList();
    }

    @PostMapping("/api/lists/{listId}/reminders")
    @ResponseStatus(HttpStatus.CREATED)
    public ReminderResponse create(@PathVariable Long listId,
                                   @Valid @RequestBody ReminderRequest request) {
        return ReminderResponse.from(
                reminderService.create(listId, request.title(), request.notes(),
                        request.dueDate(), request.priority()));
    }

    @PutMapping("/api/reminders/{id}")
    public ReminderResponse update(@PathVariable Long id,
                                   @Valid @RequestBody ReminderRequest request) {
        return ReminderResponse.from(
                reminderService.update(id, request.title(), request.notes(),
                        request.dueDate(), request.priority(), request.listId()));
    }

    @PatchMapping("/api/reminders/{id}/complete")
    public ReminderResponse toggleComplete(@PathVariable Long id) {
        return ReminderResponse.from(reminderService.toggleComplete(id));
    }

    @DeleteMapping("/api/reminders/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reminderService.delete(id);
    }
}
