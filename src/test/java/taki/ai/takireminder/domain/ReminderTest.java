package taki.ai.takireminder.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReminderTest {

    private final ReminderList sampleList = ReminderList.builder()
            .name("개인")
            .color("#FF6B6B")
            .build();

    @Test
    @DisplayName("Builder로 Reminder를 생성하면 기본값이 설정된다")
    void createWithBuilder() {
        Reminder reminder = Reminder.builder()
                .title("우유 사기")
                .list(sampleList)
                .build();

        assertThat(reminder.getTitle()).isEqualTo("우유 사기");
        assertThat(reminder.getPriority()).isEqualTo(Priority.NONE);
        assertThat(reminder.isCompleted()).isFalse();
        assertThat(reminder.getCompletedAt()).isNull();
        assertThat(reminder.getList()).isEqualTo(sampleList);
        assertThat(reminder.getCreatedAt()).isNotNull();
        assertThat(reminder.getCreatedAt()).isEqualTo(reminder.getUpdatedAt());
    }

    @Test
    @DisplayName("모든 필드를 지정하여 생성할 수 있다")
    void createWithAllFields() {
        LocalDateTime dueDate = LocalDateTime.of(2026, 3, 30, 10, 0);

        Reminder reminder = Reminder.builder()
                .title("보고서 작성")
                .notes("분기 보고서")
                .dueDate(dueDate)
                .priority(Priority.HIGH)
                .list(sampleList)
                .build();

        assertThat(reminder.getTitle()).isEqualTo("보고서 작성");
        assertThat(reminder.getNotes()).isEqualTo("분기 보고서");
        assertThat(reminder.getDueDate()).isEqualTo(dueDate);
        assertThat(reminder.getPriority()).isEqualTo(Priority.HIGH);
    }

    @Test
    @DisplayName("update 호출 시 필드가 변경되고 updatedAt이 갱신된다")
    void update() throws InterruptedException {
        Reminder reminder = Reminder.builder()
                .title("우유 사기")
                .list(sampleList)
                .build();
        LocalDateTime createdAt = reminder.getCreatedAt();

        Thread.sleep(10);

        ReminderList newList = ReminderList.builder().name("쇼핑").color("#34C759").build();
        reminder.update("두유 사기", "무가당", LocalDateTime.of(2026, 4, 1, 9, 0),
                Priority.LOW, newList);

        assertThat(reminder.getTitle()).isEqualTo("두유 사기");
        assertThat(reminder.getNotes()).isEqualTo("무가당");
        assertThat(reminder.getPriority()).isEqualTo(Priority.LOW);
        assertThat(reminder.getList()).isEqualTo(newList);
        assertThat(reminder.getCreatedAt()).isEqualTo(createdAt);
        assertThat(reminder.getUpdatedAt()).isAfter(createdAt);
    }

    @Test
    @DisplayName("toggleComplete로 미완료 → 완료 전환 시 completedAt이 설정된다")
    void toggleCompleteToCompleted() {
        Reminder reminder = Reminder.builder()
                .title("우유 사기")
                .list(sampleList)
                .build();

        reminder.toggleComplete();

        assertThat(reminder.isCompleted()).isTrue();
        assertThat(reminder.getCompletedAt()).isNotNull();
    }

    @Test
    @DisplayName("toggleComplete로 완료 → 미완료 전환 시 completedAt이 null이 된다")
    void toggleCompleteToIncomplete() {
        Reminder reminder = Reminder.builder()
                .title("우유 사기")
                .list(sampleList)
                .build();

        reminder.toggleComplete();
        reminder.toggleComplete();

        assertThat(reminder.isCompleted()).isFalse();
        assertThat(reminder.getCompletedAt()).isNull();
    }
}
