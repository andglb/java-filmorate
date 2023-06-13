package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureCache
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	private final UserDbStorage userStorage;
	private final FilmDbStorage filmStorage;
	private final FilmService filmService;
	private final UserService userService;
	private User firstUser;
	private User secondUser;
	private User thirdUser;
	private Film firstFilm;
	private Film secondFilm;
	private Film thirdFilm;

	@BeforeEach
	public void beforeEach() {
		firstUser = User.builder()
				.name("User_Name_1")
				.login("User_Login_1")
				.email("User_Email_1@ya.ru")
				.birthday(LocalDate.of(2000, 1, 1))
				.build();

		secondUser = User.builder()
				.name("User_Name_2")
				.login("User_Login_2")
				.email("User_Email_2@ya.ru")
				.birthday(LocalDate.of(2000, 2, 2))
				.build();

		thirdUser = User.builder()
				.name("User_Name_3")
				.login("User_Login_3")
				.email("User_Email_3@ya.ru")
				.birthday(LocalDate.of(2000, 3, 3))
				.build();

		firstFilm = Film.builder()
				.name("Film_Name_1")
				.description("Film_Description_1")
				.releaseDate(LocalDate.of(1961, 10, 5))
				.duration(114)
				.build();
		firstFilm.setMpa(new Mpa(1, "G"));
		firstFilm.setLikes(new HashSet<>());
		firstFilm.setGenres(new HashSet<>(Arrays.asList(new Genre(2, "Драма"),
				new Genre(1, "Комедия"))));

		secondFilm = Film.builder()
				.name("Film_Name_2")
				.description("Film_Description_2")
				.releaseDate(LocalDate.of(2009, 12, 10))
				.duration(162)
				.build();
		secondFilm.setMpa(new Mpa(3, "PG-13"));
		secondFilm.setLikes(new HashSet<>());
		secondFilm.setGenres(new HashSet<>(Arrays.asList(new Genre(6, "Боевик"))));

		thirdFilm = Film.builder()
				.name("Film_Name_3")
				.description("Film_Description_3")
				.releaseDate(LocalDate.of(1975, 11, 19))
				.duration(133)
				.build();
		thirdFilm.setMpa(new Mpa(4, "R"));
		thirdFilm.setLikes(new HashSet<>());
		thirdFilm.setGenres(new HashSet<>(Arrays.asList(new Genre(2, "Драма"))));
	}

	@DisplayName("Проверяет создание и получение пользователя по ID")
	@Test
	public void createUserAndGetUserByIdTest() {
		firstUser = userStorage.create(firstUser);
		Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(firstUser.getId()));
		assertThat(userOptional)
				.hasValueSatisfying(user ->
						assertThat(user)
								.hasFieldOrPropertyWithValue("id", firstUser.getId())
								.hasFieldOrPropertyWithValue("name", "User_Name_1"));
	}

	@DisplayName("Проверяет метод получения всех пользователей")
	@Test
	public void getUsersTest() {
		firstUser = userStorage.create(firstUser);
		secondUser = userStorage.create(secondUser);
		List<User> listUsers = userStorage.getUsers();
		assertThat(listUsers).contains(firstUser);
		assertThat(listUsers).contains(secondUser);
	}

	@DisplayName("Проверяет обновление пользователя")
	@Test
	public void updateUserTest() {
		firstUser = userStorage.create(firstUser);
		User updateUser = User.builder()
				.id(firstUser.getId())
				.name("Update_User_Name_1")
				.login("Update_User_Login_1")
				.email("Update_User_Email_1@ya.ru")
				.birthday(LocalDate.of(1980, 12, 23))
				.build();
		Optional<User> testUpdateUser = Optional.ofNullable(userStorage.update(updateUser));
		assertThat(testUpdateUser)
				.hasValueSatisfying(user -> assertThat(user)
						.hasFieldOrPropertyWithValue("name", "Update_User_Name_1")
				);
	}

	@DisplayName("Проверяет удаление пользователя")
	@Test
	public void deleteUserTest() {
		firstUser = userStorage.create(firstUser);
		secondUser = userStorage.create(secondUser);
		userStorage.delete(firstUser.getId());
		List<User> listUsers = userStorage.getUsers();
		assertThat(listUsers).hasSize(1);
		assertThrows(UserNotFoundException.class, () -> userStorage.getUserById(firstUser.getId()));
	}

	@DisplayName("Проверяет создание и получение фильма по ID")
	@Test
	public void createFilmAndGetFilmByIdTest() {
		firstFilm = filmStorage.create(firstFilm);
		Optional<Film> filmOptional = Optional.ofNullable(filmStorage.getFilmById(firstFilm.getId()));
		assertThat(filmOptional)
				.hasValueSatisfying(film -> assertThat(film)
						.hasFieldOrPropertyWithValue("id", firstFilm.getId())
						.hasFieldOrPropertyWithValue("name", "Film_Name_1")
				);
	}

	@DisplayName("Проверяет метод получения всех фильмов")
	@Test
	public void getFilmsTest() {
		firstFilm = filmStorage.create(firstFilm);
		secondFilm = filmStorage.create(secondFilm);
		thirdFilm = filmStorage.create(thirdFilm);
		List<Film> listFilms = filmStorage.getFilms();
		assertThat(listFilms).contains(firstFilm);
		assertThat(listFilms).contains(secondFilm);
		assertThat(listFilms).contains(thirdFilm);
	}

	@DisplayName("Проверяет обновление фильма")
	@Test
	public void updateFilmTest() {
		firstFilm = filmStorage.create(firstFilm);
		Film updateFilm = Film.builder()
				.id(firstFilm.getId())
				.name("Update_Film_Name_1")
				.description("Update_Film_Description_1")
				.releaseDate(LocalDate.of(1975, 11, 19))
				.duration(133)
				.build();
		updateFilm.setMpa(new Mpa(1, "G"));
		Optional<Film> testUpdateFilm = Optional.ofNullable(filmStorage.update(updateFilm));
		assertThat(testUpdateFilm)
				.hasValueSatisfying(film ->
						assertThat(film)
								.hasFieldOrPropertyWithValue("name", "Update_Film_Name_1")
								.hasFieldOrPropertyWithValue("description", "Update_Film_Description_1")
				);
	}

	@DisplayName("Проверяет удаление фильма")
	@Test
	public void deleteFilmTest() {
		firstFilm = filmStorage.create(firstFilm);
		secondFilm = filmStorage.create(secondFilm);
		filmStorage.delete(firstFilm.getId());
		List<Film> listFilms = filmStorage.getFilms();
		assertThat(listFilms).hasSize(1);
		assertThrows(FilmNotFoundException.class, () -> filmStorage.getFilmById(firstFilm.getId()));
		assertThat(Optional.of(listFilms.get(0)))
				.hasValueSatisfying(film ->
						AssertionsForClassTypes.assertThat(film)
								.hasFieldOrPropertyWithValue("name", "Film_Name_2"));
	}

	@DisplayName("Проверяет добавление лайка к фильму")
	@Test
	public void addLikeTest() {
		firstUser = userStorage.create(firstUser);
		firstFilm = filmStorage.create(firstFilm);
		filmService.addLike(firstFilm.getId(), firstUser.getId());
		firstFilm = filmStorage.getFilmById(firstFilm.getId());
		assertThat(firstFilm.getLikes()).hasSize(1);
		assertThat(firstFilm.getLikes()).contains(firstUser.getId());
	}

	@DisplayName("Проверяет удаление лайка")
	@Test
	public void deleteLikeTest() {
		firstUser = userStorage.create(firstUser);
		secondUser = userStorage.create(secondUser);
		firstFilm = filmStorage.create(firstFilm);
		filmService.addLike(firstFilm.getId(), firstUser.getId());
		filmService.addLike(firstFilm.getId(), secondUser.getId());
		filmService.deleteLike(firstFilm.getId(), firstUser.getId());
		firstFilm = filmStorage.getFilmById(firstFilm.getId());
		assertThat(firstFilm.getLikes()).hasSize(1);
		assertThat(firstFilm.getLikes()).contains(secondUser.getId());
	}

	@DisplayName("Проверяет метод просмотра самых популярных фильмов")
	@Test
	public void getPopularFilmsTest() {

		firstUser = userStorage.create(firstUser);
		secondUser = userStorage.create(secondUser);
		thirdUser = userStorage.create(thirdUser);

		firstFilm = filmStorage.create(firstFilm);
		filmService.addLike(firstFilm.getId(), firstUser.getId());

		secondFilm = filmStorage.create(secondFilm);
		filmService.addLike(secondFilm.getId(), firstUser.getId());
		filmService.addLike(secondFilm.getId(), secondUser.getId());
		filmService.addLike(secondFilm.getId(), thirdUser.getId());

		thirdFilm = filmStorage.create(thirdFilm);
		filmService.addLike(thirdFilm.getId(), firstUser.getId());
		filmService.addLike(thirdFilm.getId(), secondUser.getId());

		List<Film> listFilms = filmService.getPopular(5);

		assertThat(listFilms).hasSize(3);

		assertThat(Optional.of(listFilms.get(0)))
				.hasValueSatisfying(film ->
						AssertionsForClassTypes.assertThat(film)
								.hasFieldOrPropertyWithValue("name", "Film_Name_2"));

		assertThat(Optional.of(listFilms.get(1)))
				.hasValueSatisfying(film ->
						AssertionsForClassTypes.assertThat(film)
								.hasFieldOrPropertyWithValue("name", "Film_Name_3"));

		assertThat(Optional.of(listFilms.get(2)))
				.hasValueSatisfying(film ->
						AssertionsForClassTypes.assertThat(film)
								.hasFieldOrPropertyWithValue("name", "Film_Name_1"));
	}

	@DisplayName("Проверяет метод для добавление в друзья")
	@Test
	public void addFriendTest() {
		firstUser = userStorage.create(firstUser);
		secondUser = userStorage.create(secondUser);
		userService.addFriend(firstUser.getId(), secondUser.getId());
		assertThat(userService.getFriends(firstUser.getId())).hasSize(1);
		assertThat(userService.getFriends(firstUser.getId())).contains(secondUser);
	}

	@DisplayName("Проверяет метод для удаления из друзей")
	@Test
	public void deleteFriendTest() {
		firstUser = userStorage.create(firstUser);
		secondUser = userStorage.create(secondUser);
		thirdUser = userStorage.create(thirdUser);
		userService.addFriend(firstUser.getId(), secondUser.getId());
		userService.addFriend(firstUser.getId(), thirdUser.getId());
		userService.deleteFriend(firstUser.getId(), secondUser.getId());
		assertThat(userService.getFriends(firstUser.getId())).hasSize(1);
		assertThat(userService.getFriends(firstUser.getId())).contains(thirdUser);
	}

	@DisplayName("Проверяет метод для получения списка друзей пользователя")
	@Test
	public void getFriendsTest() {
		firstUser = userStorage.create(firstUser);
		secondUser = userStorage.create(secondUser);
		thirdUser = userStorage.create(thirdUser);
		userService.addFriend(firstUser.getId(), secondUser.getId());
		userService.addFriend(firstUser.getId(), thirdUser.getId());
		assertThat(userService.getFriends(firstUser.getId())).hasSize(2);
		assertThat(userService.getFriends(firstUser.getId())).contains(secondUser, thirdUser);
	}

	@DisplayName("Проверяет метод для получений общих друзей пользователей")
	@Test
	public void getCommonFriendsTest() {
		firstUser = userStorage.create(firstUser);
		secondUser = userStorage.create(secondUser);
		thirdUser = userStorage.create(thirdUser);
		userService.addFriend(firstUser.getId(), secondUser.getId());
		userService.addFriend(firstUser.getId(), thirdUser.getId());
		userService.addFriend(secondUser.getId(), firstUser.getId());
		userService.addFriend(secondUser.getId(), thirdUser.getId());
		assertThat(userService.getCommonFriends(firstUser.getId(), secondUser.getId())).hasSize(1);
		assertThat(userService.getCommonFriends(firstUser.getId(), secondUser.getId()))
				.contains(thirdUser);
	}
}
