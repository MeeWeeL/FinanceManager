package model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Operation implements Serializable {
    private final int id;
    private final int categoryId;
    private final int amount;
    @NotNull
    private final OperationType type;

    public Operation(
        int id,
        int categoryId,
        int amount,
        @NotNull OperationType type
    ) {
        this.id = id;
        this.categoryId = categoryId;
        this.amount = amount;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getAmount() {
        return amount;
    }

    @NotNull
    public OperationType getType() {
        return type;
    }
}
