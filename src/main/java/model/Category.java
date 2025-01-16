package model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;

public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int id;
    private final int walletId;
    @NotNull
    private final String name;
    @Nullable
    private final Integer limit;
    @NotNull
    private final List<Operation> operations;

    public Category(
        int id,
        int walletId,
        @NotNull String name,
        @Nullable Integer limit,
        @NotNull List<Operation> operations
    ) {
        this.id = id;
        this.walletId = walletId;
        this.name = name;
        this.limit = limit;
        this.operations = operations;
    }

    public int getId() {
        return id;
    }

    public int getWalletId() {
        return walletId;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @Nullable
    public Integer getLimit() {
        return limit;
    }

    @NotNull
    public List<Operation> getOperations() {
        return operations;
    }

    @NotNull
    public Category copy(List<Operation> operations) {
        return new Category(this.id, this.walletId, this.name, this.limit, operations);
    }
}
