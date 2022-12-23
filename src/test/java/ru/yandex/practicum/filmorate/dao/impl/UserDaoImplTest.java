package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.FriendStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDaoImplTest {
    @Qualifier("userDaoImpl")
    private final UserDao userDao;
    @Qualifier("friendDaoImpl")
    private final FriendDao friendDao;

    @Test
    @DisplayName("Проверка сохранения пользователя в БД")
    void createUserTest() {
        User originalUser = createUserForTest();
        User userFromDatabase = userDao.createUser(originalUser);
        originalUser.setId(userFromDatabase.getId());
        assertEquals(originalUser, userFromDatabase);
    }

    @Test
    @DisplayName("Проверка получения пользователя по id")
    void getUserByIdTest() {
        User originalUser = createUserForTest();
        Long userId = userDao.createUser(originalUser).getId();
        User userFromDatabase = userDao.getUserById(userId);
        originalUser.setId(userId);
        assertEquals(originalUser, userFromDatabase);
    }

    @Test
    @DisplayName("Проверка получения списка всех пользователей")
    void getAllUsersTest() {
        int oldNumberOfUsersInDatabase = userDao.getAllUsers().size();
        User newUser = createUserForTest();
        userDao.createUser(newUser);
        Integer newNumberOfUsersInDatabase = userDao.getAllUsers().size();
        assertEquals(oldNumberOfUsersInDatabase + 1, newNumberOfUsersInDatabase);
    }

    @Test
    @DisplayName("Проверка обновления пользователя")
    void updateUserTest() {
        User user = createUserForTest();
        Long userId = userDao.createUser(user).getId();
        user.setId(userId);
        user.setName("Updated");
        userDao.updateUser(user);
        assertEquals("Updated", userDao.getUserById(userId).getName());
    }

    @Test
    @DisplayName("Проверка наличия приглашения в дружбу")
    void userHasActiveInvitationFromFriendTest() {
        User user = createUserForTest();
        User friend = createUserForTest();
        Long userId = userDao.createUser(user).getId();
        Long friendId = userDao.createUser(friend).getId();
        userDao.sendFriendInvitation(userId, friendId);
        assertTrue(userDao.userHasActiveInvitationFromFriend(friendId, userId));
    }

    @Test
    @DisplayName("Проверка отправки приглашения в дружбу")
    void sendFriendInvitationTest() {
        User user = createUserForTest();
        User friend = createUserForTest();
        Long userId = userDao.createUser(user).getId();
        Long friendId = userDao.createUser(friend).getId();
        userDao.sendFriendInvitation(userId, friendId);
        assertEquals(List.of(friendId), userDao.getFriendIds(userId));
    }

    @Test
    @DisplayName("Проверка принятия дружбы")
    void acceptFriendInvitationTest() {
        User user = createUserForTest();
        User friend = createUserForTest();
        Long userId = userDao.createUser(user).getId();
        Long friendId = userDao.createUser(friend).getId();
        userDao.sendFriendInvitation(userId, friendId);
        assertEquals(FriendStatus.UNCONFIRMED, friendDao.getFriendsForUser(userId).get(0).getStatus());
        userDao.acceptFriendInvitation(friendId, userId);
        assertEquals(FriendStatus.CONFIRMED, friendDao.getFriendsForUser(userId).get(0).getStatus());
    }

    @Test
    @DisplayName("Проверка удаления друга")
    void deleteFriendTest() {
        User user = createUserForTest();
        User friend = createUserForTest();
        Long userId = userDao.createUser(user).getId();
        Long friendId = userDao.createUser(friend).getId();
        userDao.sendFriendInvitation(userId, friendId);
        assertEquals(1, friendDao.getFriendsForUser(userId).size());
        userDao.deleteFriend(userId, friendId);
        assertEquals(0, friendDao.getFriendsForUser(userId).size());
    }

    @Test
    @DisplayName("Проверка получения всех друзей")
    void getAllFriendsTest() {
        User user = createUserForTest();
        User friend = createUserForTest();
        Long userId = userDao.createUser(user).getId();
        Long friendId = userDao.createUser(friend).getId();
        userDao.sendFriendInvitation(userId, friendId);
        assertEquals(1, userDao.getAllFriends(userId).size());
    }

    @Test
    @DisplayName("Проверка получения id друзей для пользователя ")
    void getFriendIdsTest() {
        User user = createUserForTest();
        User friend = createUserForTest();
        Long userId = userDao.createUser(user).getId();
        Long friendId = userDao.createUser(friend).getId();
        userDao.sendFriendInvitation(userId, friendId);
        assertEquals(List.of(friendId),userDao.getFriendIds(userId));
    }

    private User createUserForTest() {
        return User.builder()
                .email("mail@mail.ru")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(2000, 1, 1))
                .friends(new ArrayList<>())
                .build();
    }
}