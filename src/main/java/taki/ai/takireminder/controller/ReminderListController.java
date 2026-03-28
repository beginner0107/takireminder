package taki.ai.takireminder.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import taki.ai.takireminder.domain.ReminderList;
import taki.ai.takireminder.dto.ReminderListRequest;
import taki.ai.takireminder.dto.ReminderListResponse;
import taki.ai.takireminder.service.ports.in.ReminderListService;

import java.util.List;

@RestController
@RequestMapping("/api/lists")
@RequiredArgsConstructor
public class ReminderListController {

    private final ReminderListService reminderListService;

    @GetMapping
    public List<ReminderListResponse> findAll() {
        return reminderListService.findAll().stream()
                .map(ReminderListResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ReminderListResponse findById(@PathVariable Long id) {
        return ReminderListResponse.from(reminderListService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReminderListResponse create(@Valid @RequestBody ReminderListRequest request) {
        ReminderList created = reminderListService.create(request.name(), request.color());
        return ReminderListResponse.from(created);
    }

    @PutMapping("/{id}")
    public ReminderListResponse update(@PathVariable Long id,
                                       @Valid @RequestBody ReminderListRequest request) {
        ReminderList updated = reminderListService.update(id, request.name(), request.color());
        return ReminderListResponse.from(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        reminderListService.delete(id);
    }
}
