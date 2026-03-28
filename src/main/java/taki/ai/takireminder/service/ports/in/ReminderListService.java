package taki.ai.takireminder.service.ports.in;

import taki.ai.takireminder.domain.ReminderList;

import java.util.List;

public interface ReminderListService {

    List<ReminderList> findAll();

    ReminderList findById(Long id);

    ReminderList create(String name, String color);

    ReminderList update(Long id, String name, String color);

    void delete(Long id);
}
