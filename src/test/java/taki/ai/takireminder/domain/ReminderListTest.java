package taki.ai.takireminder.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReminderListTest {

    @Test
    @DisplayName("Builder로 ReminderList를 생성하면 name과 color가 설정된다")
    void createWithBuilder() {
        ReminderList list = ReminderList.builder()
                .name("개인")
                .color("#FF6B6B")
                .build();

        assertThat(list.getName()).isEqualTo("개인");
        assertThat(list.getColor()).isEqualTo("#FF6B6B");
    }

    @Test
    @DisplayName("생성 시 createdAt과 updatedAt이 자동 설정된다")
    void creationSetsTimestamps() {
        LocalDateTime before = LocalDateTime.now();

        ReminderList list = ReminderList.builder()
                .name("업무")
                .color("#007AFF")
                .build();

        LocalDateTime after = LocalDateTime.now();

        assertThat(list.getCreatedAt()).isNotNull();
        assertThat(list.getUpdatedAt()).isNotNull();
        assertThat(list.getCreatedAt()).isEqualTo(list.getUpdatedAt());
        assertThat(list.getCreatedAt()).isBetween(before, after);
    }

    @Test
    @DisplayName("update 호출 시 name, color가 변경되고 updatedAt이 갱신된다")
    void updateChangesFieldsAndUpdatedAt() throws InterruptedException {
        ReminderList list = ReminderList.builder()
                .name("쇼핑")
                .color("#34C759")
                .build();
        LocalDateTime createdAt = list.getCreatedAt();

        Thread.sleep(10);

        list.update("장보기", "#FF9500");

        assertThat(list.getName()).isEqualTo("장보기");
        assertThat(list.getColor()).isEqualTo("#FF9500");
        assertThat(list.getCreatedAt()).isEqualTo(createdAt);
        assertThat(list.getUpdatedAt()).isAfter(createdAt);
    }
}
