package repository;

import db.DB;
import handler.DataHandler;
import model.OperationType;
import model.User;
import org.jetbrains.annotations.NotNull;

public class DataRepositoryImpl implements DataRepository {
    @NotNull
    private final DB db;
    @NotNull
    private final DataHandler dataHandler;

    public DataRepositoryImpl(@NotNull DB db, @NotNull DataHandler dataHandler) {
        this.db = db;
        this.dataHandler = dataHandler;
    }

    @Override
    public void createWallet(int userId, @NotNull String name) {
        db.createWallet(userId, name);
        updateData(userId);
    }

    @Override
    public void createCategory(int userId, int walletId, @NotNull String name) {
        db.createCategory(walletId, name, null);
        updateData(userId);
    }

    @Override
    public void createOperation(int userId, int categoryId, int amount, @NotNull OperationType type) {
        db.createOperation(amount, categoryId, type);
        updateData(userId);
    }

    @Override
    public void changeCategoryLimit(int userId, int categoryId, int limit) {
        db.changeCategoryLimit(categoryId, limit);
        updateData(userId);
    }

    @Override
    public void changeWalletName(int userId, int walletId, @NotNull String newName) {
        db.changeWalletName(walletId, newName);
        updateData(userId);
    }

    private void updateData(int userId) {
        User user = db.getUserById(userId);
        dataHandler.updateUserData(user);
    }
}
