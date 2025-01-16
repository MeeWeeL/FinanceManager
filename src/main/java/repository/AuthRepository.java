package repository;

import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.StateFlow;
import model.User;
import org.jetbrains.annotations.NotNull;

public interface AuthRepository {

    @NotNull
    StateFlow<User> getUserData();

    @NotNull
    Flow<String> getMessages();

    void createUser(@NotNull String login, @NotNull String password);

    void auth(@NotNull String login, @NotNull String password);

    void logOut();
}
