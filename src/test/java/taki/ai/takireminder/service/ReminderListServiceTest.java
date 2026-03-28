package taki.ai.takireminder.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import taki.ai.takireminder.domain.ReminderList;
import taki.ai.takireminder.service.ports.in.ReminderListService;
import taki.ai.takireminder.repository.ReminderListRepository;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ReminderListServiceTest {

    @Autowired
    private ReminderListService reminderListService;

    @Autowired
    private ReminderListRepository reminderListRepository;

    private ReminderList savedList;

    @BeforeEach
    void setUp() {
        savedList = reminderListRepository.save(
                ReminderList.builder().name("개인").color("#FF6B6B").build());
    }

    @Test
    @DisplayName("전체 리스트를 조회한다")
    void findAll() {
        reminderListRepository.save(ReminderList.builder().name("업무").color("#007AFF").build());

        List<ReminderList> result = reminderListService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("ID로 리스트를 조회한다")
    void findById() {
        ReminderList result = reminderListService.findById(savedList.getId());

        assertThat(result.getName()).isEqualTo("개인");
        assertThat(result.getColor()).isEqualTo("#FF6B6B");
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회하면 예외가 발생한다")
    void findByIdNotFound() {
        assertThatThrownBy(() -> reminderListService.findById(999L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("새 리스트를 생성한다")
    void create() {
        ReminderList result = reminderListService.create("개인", "#FF6B6B");

        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("개인");
        assertThat(result.getColor()).isEqualTo("#FF6B6B");
        assertThat(result.isDefaultList()).isFalse();
    }

    @Test
    @DisplayName("리스트를 수정한다")
    void update() {
        ReminderList result = reminderListService.update(savedList.getId(), "업무", "#007AFF");

        assertThat(result.getName()).isEqualTo("업무");
        assertThat(result.getColor()).isEqualTo("#007AFF");
    }

    @Test
    @DisplayName("리스트를 삭제한다")
    void delete() {
        reminderListService.delete(savedList.getId());

        assertThat(reminderListRepository.findById(savedList.getId())).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 리스트를 삭제하면 예외가 발생한다")
    void deleteNotFound() {
        assertThatThrownBy(() -> reminderListService.delete(999L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("기본 목록은 삭제할 수 없다")
    void deleteDefaultListThrows() {
        ReminderList defaultList = reminderListRepository.save(
                ReminderList.builder().name("미리 알림").color("#007AFF").defaultList(true).build());

        assertThatThrownBy(() -> reminderListService.delete(defaultList.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("기본 목록은 삭제할 수 없습니다.");
        assertThat(reminderListRepository.findById(defaultList.getId())).isPresent();
    }
}
