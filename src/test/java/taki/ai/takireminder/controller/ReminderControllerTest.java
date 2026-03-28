package taki.ai.takireminder.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import taki.ai.takireminder.domain.Reminder;
import taki.ai.takireminder.domain.ReminderList;
import taki.ai.takireminder.repository.ReminderListRepository;
import taki.ai.takireminder.repository.ReminderRepository;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReminderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReminderListRepository reminderListRepository;

    @Autowired
    private ReminderRepository reminderRepository;

    private ReminderList savedList;
    private Reminder savedReminder;

    @BeforeEach
    void setUp() {
        savedList = reminderListRepository.save(
                ReminderList.builder().name("개인").color("#FF6B6B").build());
        savedReminder = reminderRepository.save(
                Reminder.builder().title("우유 사기").list(savedList).build());
    }

    @Test
    @DisplayName("GET /api/lists/{listId}/reminders — 리스트별 리마인더를 조회한다")
    void findByListId() throws Exception {
        reminderRepository.save(Reminder.builder().title("빵 사기").list(savedList).build());

        mockMvc.perform(get("/api/lists/{listId}/reminders", savedList.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("POST /api/lists/{listId}/reminders — 리마인더를 생성한다")
    void create() throws Exception {
        mockMvc.perform(post("/api/lists/{listId}/reminders", savedList.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "보고서 작성",
                                  "notes": "분기 보고서",
                                  "dueDate": "2026-03-30T10:00:00",
                                  "priority": "HIGH"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("보고서 작성")))
                .andExpect(jsonPath("$.notes", is("분기 보고서")))
                .andExpect(jsonPath("$.priority", is("HIGH")))
                .andExpect(jsonPath("$.listId", is(savedList.getId().intValue())));
    }

    @Test
    @DisplayName("POST — title이 빈 값이면 400을 반환한다")
    void createWithBlankTitle() throws Exception {
        mockMvc.perform(post("/api/lists/{listId}/reminders", savedList.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": ""}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/reminders/{id} — 리마인더를 수정한다")
    void update() throws Exception {
        mockMvc.perform(put("/api/reminders/{id}", savedReminder.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "title": "두유 사기",
                                  "notes": "무가당",
                                  "priority": "LOW",
                                  "listId": %d
                                }
                                """.formatted(savedList.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("두유 사기")))
                .andExpect(jsonPath("$.notes", is("무가당")))
                .andExpect(jsonPath("$.priority", is("LOW")));
    }

    @Test
    @DisplayName("PATCH /api/reminders/{id}/complete — 완료 토글한다")
    void toggleComplete() throws Exception {
        mockMvc.perform(patch("/api/reminders/{id}/complete", savedReminder.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed", is(true)))
                .andExpect(jsonPath("$.completedAt").isNotEmpty());
    }

    @Test
    @DisplayName("DELETE /api/reminders/{id} — 리마인더를 삭제한다")
    void deleteReminder() throws Exception {
        mockMvc.perform(delete("/api/reminders/{id}", savedReminder.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET — 존재하지 않는 리스트의 리마인더 조회 시 404")
    void findByInvalidListId() throws Exception {
        mockMvc.perform(get("/api/lists/{listId}/reminders", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE — 존재하지 않는 리마인더 삭제 시 404")
    void deleteNotFound() throws Exception {
        mockMvc.perform(delete("/api/reminders/{id}", 999))
                .andExpect(status().isNotFound());
    }
}
