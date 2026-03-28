package taki.ai.takireminder.service.ports.in;

import taki.ai.takireminder.domain.Priority;
import taki.ai.takireminder.domain.Reminder;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderService {

    List<Reminder> findByListId(Long listId);

    Reminder findById(Long id);

    Reminder create(Long listId, String title, String notes, LocalDateTime dueDate, Priority priority);

    Reminder update(Long id, String title, String notes, LocalDateTime dueDate,
                    Priority priority, Long listId);

    Reminder toggleComplete(Long id);

    void delete(Long id);
}
