package taki.ai.takireminder.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import taki.ai.takireminder.domain.ReminderList;
import taki.ai.takireminder.service.ports.in.ReminderListService;
import taki.ai.takireminder.repository.ReminderListRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultReminderListService implements ReminderListService {

    private final ReminderListRepository reminderListRepository;

    @Override
    public List<ReminderList> findAll() {
        return reminderListRepository.findAll();
    }

    @Override
    public ReminderList findById(Long id) {
        return reminderListRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("리스트를 찾을 수 없습니다. id=" + id));
    }

    @Override
    @Transactional
    public ReminderList create(String name, String color) {
        ReminderList list = ReminderList.builder()
                .name(name)
                .color(color)
                .build();
        return reminderListRepository.save(list);
    }

    @Override
    @Transactional
    public ReminderList update(Long id, String name, String color) {
        ReminderList list = findById(id);
        list.update(name, color);
        return list;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ReminderList list = findById(id);
        if (list.isDefaultList()) {
            throw new IllegalStateException("기본 목록은 삭제할 수 없습니다.");
        }
        reminderListRepository.delete(list);
    }
}
