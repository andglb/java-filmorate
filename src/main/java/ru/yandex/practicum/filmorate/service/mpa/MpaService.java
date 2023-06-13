package ru.yandex.practicum.filmorate.service.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.InMemoryMpaStorage;

import java.util.Collection;

@Service
public class MpaService {

    private InMemoryMpaStorage inMemoryMpaStorage;

    @Autowired
    public MpaService(InMemoryMpaStorage inMemoryMpaStorage) {
        this.inMemoryMpaStorage = inMemoryMpaStorage;
    }

    public Collection<Mpa> getAllMpa() {
        return inMemoryMpaStorage.getAllMpa();
    }

    public Mpa getMpaById(Integer id) {
        return inMemoryMpaStorage.getMpaById(id);
    }
}

