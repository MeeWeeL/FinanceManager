package repository;

import model.OperationType;
import org.jetbrains.annotations.NotNull;

public interface DataRepository {

    void createWallet(int userId, @NotNull String name);

    void createCategory(int userId, int walletId, @NotNull String name);

    void createOperation(int userId, int categoryId, int amount, @NotNull OperationType type);

    void changeWalletName(int userId, int walletId, @NotNull String newName);

    void changeCategoryLimit(int userId, int categoryId, int limit);
}
