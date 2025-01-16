package repository;

import db.DB;
import handler.DataHandler;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.StateFlow;
import model.User;
import org.jetbrains.annotations.NotNull;

public class AuthRepositoryImpl implements AuthRepository {
    @NotNull
    private final DB db;
    @NotNull
    private final DataHandler dataHandler;

    public AuthRepositoryImpl(@NotNull DB db, @NotNull DataHandler dataHandler) {
        this.db = db;
        this.dataHandler = dataHandler;
    }

    @NotNull
    @Override
    public StateFlow<User> getUserData() {
        return dataHandler.getUserData();
    }

    @NotNull
    @Override
    public Flow<String> getMessages() {
        return dataHandler.getMessages();
    }

    @Override
    public void createUser(@NotNull String login, @NotNull String password) {
        User user = db.createUser(login, password);
        dataHandler.updateUserData(user);
    }

    @Override
    public void auth(@NotNull String login, @NotNull String password) {
        User user = db.getUserByLogin(login);
        if (user == null) {
            dataHandler.sendMessage("Пользователь с таким логином не найден");
        } else if (user.checkPassword(password)) {
            dataHandler.updateUserData(db.getUserById(user.getId()));
        } else {
            dataHandler.sendMessage("Пароль не подходит");
        }
    }

    @Override
    public void logOut() {
        dataHandler.updateUserData(null);
    }
}
