package taki.ai.takireminder.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import taki.ai.takireminder.domain.Priority;
import taki.ai.takireminder.domain.Reminder;
import taki.ai.takireminder.domain.ReminderList;
import taki.ai.takireminder.repository.ReminderListRepository;
import taki.ai.takireminder.repository.ReminderRepository;
import taki.ai.takireminder.service.ports.in.ReminderService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ReminderServiceTest {

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private ReminderListRepository reminderListRepository;

    private ReminderList savedList;

    @BeforeEach
    void setUp() {
        savedList = reminderListRepository.save(
                ReminderList.builder().name("개인").color("#FF6B6B").build());
    }

    @Test
    @DisplayName("리스트별 리마인더를 조회한다")
    void findByListId() {
        reminderRepository.save(Reminder.builder().title("우유 사기").list(savedList).build());
        reminderRepository.save(Reminder.builder().title("빵 사기").list(savedList).build());

        List<Reminder> result = reminderService.findByListId(savedList.getId());

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("ID로 리마인더를 조회한다")
    void findById() {
        Reminder saved = reminderRepository.save(
                Reminder.builder().title("우유 사기").list(savedList).build());

        Reminder result = reminderService.findById(saved.getId());

        assertThat(result.getTitle()).isEqualTo("우유 사기");
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회하면 예외가 발생한다")
    void findByIdNotFound() {
        assertThatThrownBy(() -> reminderService.findById(999L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("리마인더를 생성한다")
    void create() {
        LocalDateTime dueDate = LocalDateTime.of(2026, 3, 30, 10, 0);

        Reminder result = reminderService.create(
                savedList.getId(), "보고서 작성", "분기 보고서", dueDate, Priority.HIGH);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getTitle()).isEqualTo("보고서 작성");
        assertThat(result.getNotes()).isEqualTo("분기 보고서");
        assertThat(result.getDueDate()).isEqualTo(dueDate);
        assertThat(result.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(result.isCompleted()).isFalse();
        assertThat(result.getList().getId()).isEqualTo(savedList.getId());
    }

    @Test
    @DisplayName("리마인더를 수정한다")
    void update() {
        Reminder saved = reminderRepository.save(
                Reminder.builder().title("우유 사기").list(savedList).build());
        ReminderList newList = reminderListRepository.save(
                ReminderList.builder().name("쇼핑").color("#34C759").build());

        Reminder result = reminderService.update(
                saved.getId(), "두유 사기", "무가당",
                LocalDateTime.of(2026, 4, 1, 9, 0), Priority.LOW, newList.getId());

        assertThat(result.getTitle()).isEqualTo("두유 사기");
        assertThat(result.getNotes()).isEqualTo("무가당");
        assertThat(result.getPriority()).isEqualTo(Priority.LOW);
        assertThat(result.getList().getId()).isEqualTo(newList.getId());
    }

    @Test
    @DisplayName("완료 토글: 미완료 → 완료")
    void toggleCompleteToCompleted() {
        Reminder saved = reminderRepository.save(
                Reminder.builder().title("우유 사기").list(savedList).build());

        Reminder result = reminderService.toggleComplete(saved.getId());

        assertThat(result.isCompleted()).isTrue();
        assertThat(result.getCompletedAt()).isNotNull();
    }

    @Test
    @DisplayName("완료 토글: 완료 → 미완료")
    void toggleCompleteToIncomplete() {
        Reminder saved = reminderRepository.save(
                Reminder.builder().title("우유 사기").list(savedList).build());
        reminderService.toggleComplete(saved.getId());

        Reminder result = reminderService.toggleComplete(saved.getId());

        assertThat(result.isCompleted()).isFalse();
        assertThat(result.getCompletedAt()).isNull();
    }

    @Test
    @DisplayName("리마인더를 삭제한다")
    void delete() {
        Reminder saved = reminderRepository.save(
                Reminder.builder().title("우유 사기").list(savedList).build());

        reminderService.delete(saved.getId());

        assertThat(reminderRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 리마인더를 삭제하면 예외가 발생한다")
    void deleteNotFound() {
        assertThatThrownBy(() -> reminderService.delete(999L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
