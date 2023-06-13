package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component("inMemoryMpaStorage")
public class InMemoryMpaStorage implements MpaStorage {
    private MpaDbStorage mpaDbStorage;

    @Autowired
    public InMemoryMpaStorage(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    @Override
    public List<Mpa> getAllMpa() {
        return mpaDbStorage.getAllMpa().stream()
                .sorted(Comparator.comparing(Mpa::getId))
                .collect(Collectors.toList());
    }

    @Override
    public Mpa getMpaById(Integer id) {
        return mpaDbStorage.getMpaById(id);
    }
}
