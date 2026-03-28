package taki.ai.takireminder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taki.ai.takireminder.domain.Priority;
import taki.ai.takireminder.domain.Reminder;
import taki.ai.takireminder.domain.ReminderList;
import taki.ai.takireminder.repository.ReminderRepository;
import taki.ai.takireminder.service.ports.in.ReminderListService;
import taki.ai.takireminder.service.ports.in.ReminderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultReminderService implements ReminderService {

    private final ReminderRepository reminderRepository;
    private final ReminderListService reminderListService;

    @Override
    public List<Reminder> findByListId(Long listId) {
        ReminderList list = reminderListService.findById(listId);
        return reminderRepository.findByListOrderByCreatedAtDesc(list);
    }

    @Override
    public Reminder findById(Long id) {
        return reminderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("리마인더를 찾을 수 없습니다. id=" + id));
    }

    @Override
    @Transactional
    public Reminder create(Long listId, String title, String notes,
                           LocalDateTime dueDate, Priority priority) {
        ReminderList list = reminderListService.findById(listId);
        Reminder reminder = Reminder.builder()
                .title(title)
                .notes(notes)
                .dueDate(dueDate)
                .priority(priority)
                .list(list)
                .build();
        return reminderRepository.save(reminder);
    }

    @Override
    @Transactional
    public Reminder update(Long id, String title, String notes,
                           LocalDateTime dueDate, Priority priority, Long listId) {
        Reminder reminder = findById(id);
        ReminderList list = reminderListService.findById(listId);
        reminder.update(title, notes, dueDate, priority, list);
        return reminder;
    }

    @Override
    @Transactional
    public Reminder toggleComplete(Long id) {
        Reminder reminder = findById(id);
        reminder.toggleComplete();
        return reminder;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Reminder reminder = findById(id);
        reminderRepository.delete(reminder);
    }
}
