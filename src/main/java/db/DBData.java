package db;

import model.Category;
import model.Operation;
import model.User;
import model.Wallet;

import java.io.Serializable;
import java.util.List;

public class DBData implements Serializable {
    private static final long serialVersionUID = 1L; // Уникальный идентификатор версии

    private final List<User> users;
    private final List<Wallet> wallets;
    private final List<Category> categories;
    private final List<Operation> operations;

    public DBData(List<User> users, List<Wallet> wallets, List<Category> categories, List<Operation> operations) {
        this.users = users;
        this.wallets = wallets;
        this.categories = categories;
        this.operations = operations;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Wallet> getWallets() {
        return wallets;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public List<Operation> getOperations() {
        return operations;
    }
}
