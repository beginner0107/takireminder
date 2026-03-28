package taki.ai.takireminder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import taki.ai.takireminder.domain.ReminderList;

public interface ReminderListRepository extends JpaRepository<ReminderList, Long> {
}
