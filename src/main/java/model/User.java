package model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    @NotNull
    private final String login;
    @NotNull
    private final String password;
    @NotNull
    private final List<Wallet> wallets;

    public User(
        int id,
        @NotNull String login,
        @NotNull String password,
        @NotNull List<Wallet> wallets
    ) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.wallets = wallets;
    }

    public int getId() {
        return id;
    }

    @NotNull
    public String getLogin() {
        return login;
    }

    @NotNull
    public List<Wallet> getWallets() {
        return wallets;
    }

    public boolean checkPassword(@NotNull String curPassword) {
        return password.equals(curPassword);
    }

    @NotNull
    public User copy(@NotNull List<Wallet> wallets) {
        return new User(this.id, this.login, this.password, wallets);
    }

    public User copy() {
        return new User(this.id, this.login, this.password, this.wallets);
    }
}
