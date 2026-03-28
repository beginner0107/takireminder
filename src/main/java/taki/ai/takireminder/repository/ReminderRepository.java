package taki.ai.takireminder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taki.ai.takireminder.domain.Reminder;
import taki.ai.takireminder.domain.ReminderList;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByListOrderByCreatedAtDesc(ReminderList list);

    List<Reminder> findByCompletedFalseAndDueDateBetween(LocalDateTime start, LocalDateTime end);

    List<Reminder> findByCompletedFalseAndDueDateAfter(LocalDateTime dateTime);

    List<Reminder> findByCompletedFalse();

    List<Reminder> findByCompletedTrue();

    int countByListAndCompletedFalse(ReminderList list);
}
