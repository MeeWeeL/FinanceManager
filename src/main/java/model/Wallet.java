package model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public class Wallet implements Serializable {
    private static final long serialVersionUID = 1L; // Уникальный идентификатор версии

    private final int id;
    private final int userId;
    private final int balance;
    @NotNull
    private final String name;
    @NotNull
    private final List<Category> categories;

    public Wallet(
        int id,
        int userId,
        int balance,
        @NotNull String name,
        @NotNull List<Category> categories
    ) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
        this.name = name;
        this.categories = categories;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getBalance() {
        return balance;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public List<Category> getCategories() {
        return categories;
    }

    @NotNull
    public Wallet copy(@NotNull String walletName) {
        return new Wallet(this.id, this.userId, this.balance, walletName, this.categories);
    }

    @NotNull
    public Wallet copy(int balance) {
        return new Wallet(this.id, this.userId, balance, this.name, this.categories);
    }
}
