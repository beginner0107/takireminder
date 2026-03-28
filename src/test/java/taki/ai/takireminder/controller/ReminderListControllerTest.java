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
import taki.ai.takireminder.domain.ReminderList;
import taki.ai.takireminder.repository.ReminderListRepository;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ReminderListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReminderListRepository reminderListRepository;

    private ReminderList savedList;

    @BeforeEach
    void setUp() {
        savedList = reminderListRepository.save(
                ReminderList.builder().name("개인").color("#FF6B6B").build());
    }

    @Test
    @DisplayName("GET /api/lists — 전체 리스트를 조회한다")
    void findAll() throws Exception {
        reminderListRepository.save(ReminderList.builder().name("업무").color("#007AFF").build());

        mockMvc.perform(get("/api/lists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("개인")))
                .andExpect(jsonPath("$[1].name", is("업무")));
    }

    @Test
    @DisplayName("GET /api/lists/{id} — ID로 리스트를 조회한다")
    void findById() throws Exception {
        mockMvc.perform(get("/api/lists/{id}", savedList.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("개인")))
                .andExpect(jsonPath("$.color", is("#FF6B6B")));
    }

    @Test
    @DisplayName("GET /api/lists/{id} — 존재하지 않으면 404를 반환한다")
    void findByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/lists/{id}", 999))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/lists — 새 리스트를 생성한다")
    void create() throws Exception {
        mockMvc.perform(post("/api/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "쇼핑", "color": "#34C759"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("쇼핑")))
                .andExpect(jsonPath("$.color", is("#34C759")))
                .andExpect(jsonPath("$.id").isNumber());
    }

    @Test
    @DisplayName("POST /api/lists — name이 빈 값이면 400을 반환한다")
    void createWithBlankName() throws Exception {
        mockMvc.perform(post("/api/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "", "color": "#34C759"}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /api/lists/{id} — 리스트를 수정한다")
    void update() throws Exception {
        mockMvc.perform(put("/api/lists/{id}", savedList.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"name": "업무", "color": "#007AFF"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("업무")))
                .andExpect(jsonPath("$.color", is("#007AFF")));
    }

    @Test
    @DisplayName("DELETE /api/lists/{id} — 리스트를 삭제한다")
    void deleteList() throws Exception {
        mockMvc.perform(delete("/api/lists/{id}", savedList.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/lists/{id} — 기본 목록은 삭제할 수 없다")
    void deleteDefaultList() throws Exception {
        ReminderList defaultList = reminderListRepository.save(
                ReminderList.builder().name("미리 알림").color("#007AFF").defaultList(true).build());

        mockMvc.perform(delete("/api/lists/{id}", defaultList.getId()))
                .andExpect(status().isBadRequest());
    }
}
