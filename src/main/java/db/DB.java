package db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Category;
import model.Operation;
import model.OperationType;
import model.User;
import model.Wallet;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class DB {
    private final List<User> users = new ArrayList<>();
    private final List<Wallet> wallets = new ArrayList<>();
    private final List<Category> categories = new ArrayList<>();
    private final List<Operation> operations = new ArrayList<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public DB() {
        loadFromFile();
    }

    private void saveToFile() {
        File directory = new File(dataFilePath()).getParentFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }
        DBData dbData = new DBData(users, wallets, categories, operations);
        try (FileWriter writer = new FileWriter(dataFilePath())) {
            gson.toJson(dbData, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        File file = new File(dataFilePath());
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                DBData loadedDBData = gson.fromJson(reader, DBData.class);
                users.clear();
                users.addAll(loadedDBData.getUsers());
                wallets.clear();
                wallets.addAll(loadedDBData.getWallets());
                categories.clear();
                categories.addAll(loadedDBData.getCategories());
                operations.clear();
                operations.addAll(loadedDBData.getOperations());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String dataFilePath() {
        String userHome = System.getProperty("user.home");
        return Paths.get(userHome, "AppData", "Local", "MyApp", "FinanceManager.json").toString();
    }

    public User getUserByLogin(String login) {
        return users.stream().filter(user -> user.getLogin().equals(login)).findFirst().orElse(null);
    }

    public User getUserById(int userId) {
        Optional<User> optionalUser = users.stream().filter(user -> user.getId() == userId).findFirst();
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<Wallet> userWallets = new ArrayList<>();
            for (Wallet wallet : wallets) {
                if (wallet.getUserId() == userId) {
                    List<Category> walletCategories = new ArrayList<>();
                    for (Category category : categories) {
                        if (category.getWalletId() == wallet.getId()) {
                            List<Operation> categoryOperations = new ArrayList<>();
                            for (Operation operation : operations) {
                                if (operation.getCategoryId() == category.getId()) {
                                    categoryOperations.add(operation);
                                }
                            }
                            walletCategories.add(new Category(
                                category.getId(),
                                category.getWalletId(),
                                category.getName(),
                                category.getLimit(),
                                categoryOperations
                            ));
                        }
                    }
                    userWallets.add(new Wallet(
                        wallet.getId(),
                        wallet.getUserId(),
                        walletCategories.stream().mapToInt(cat -> cat.getOperations().stream()
                            .mapToInt(op -> op.getType() == OperationType.INCOME ? op.getAmount() : -op.getAmount())
                            .sum()).sum(),
                        wallet.getName(),
                        walletCategories
                    ));
                }
            }
            return user.copy(userWallets);
        }
        throw new RuntimeException("Can't find user by id");
    }

    public User createUser(String login, String password) {
        User newUser = new User(generateNewUserId(), login, password, new ArrayList<>());
        users.add(newUser);
        saveToFile();
        return newUser;
    }

    public void createWallet(int userId, String walletName) {
        wallets.add(new Wallet(generateNewWalletId(), userId, 0, walletName, new ArrayList<>()));
        saveToFile();
    }

    public void createCategory(int walletId, String categoryName, Integer limit) {
        categories.add(new Category(generateNewCategoryId(), walletId, categoryName, limit, new ArrayList<>()));
        saveToFile();
    }

    public void changeCategoryLimit(int categoryId, int newLimit) {
        Optional<Category> optionalCategory = categories.stream().filter(cat -> cat.getId() == categoryId).findFirst();
        optionalCategory.ifPresent(cat -> {
            categories.remove(cat);
            categories.add(new Category(cat.getId(), cat.getWalletId(), cat.getName(), newLimit, cat.getOperations()));
        });
        saveToFile();
    }

    public void createOperation(int amount, int categoryId, OperationType type) {
        operations.add(new Operation(generateNewOperationId(), categoryId, amount, type));
        Optional<Category> optionalCategory = categories.stream().filter(cat -> cat.getId() == categoryId).findFirst();
        optionalCategory.ifPresent(category -> {
            if (category.getLimit() != null && type == OperationType.EXPENSE) {
                categories.remove(category);
                categories.add(new Category(category.getId(), category.getWalletId(), category.getName(), category.getLimit() - amount, category.getOperations()));
            }
        });
        saveToFile();
    }

    public Category getCategoryById(int categoryId) {
        return categories.stream().filter(cat -> cat.getId() == categoryId).findFirst().orElse(null);
    }

    public void changeWalletName(int walletId, String newName) {
        Optional<Wallet> optionalWallet = wallets.stream().filter(wallet -> wallet.getId() == walletId).findFirst();
        optionalWallet.ifPresent(wallet -> {
            wallets.remove(wallet);
            wallets.add(wallet.copy(newName));
            saveToFile();
        });
    }

    private int generateNewUserId() {
        int newUserId;
        while (true) {
            newUserId = generateId();
            int finalNewUserId = newUserId;
            if (users.stream().noneMatch(user -> user.getId() == finalNewUserId)) break;
        }
        return newUserId;
    }

    private int generateNewWalletId() {
        int newWalletId;
        while (true) {
            newWalletId = generateId();
            int finalNewWalletId = newWalletId;
            if (wallets.stream().noneMatch(wallet -> wallet.getId() == finalNewWalletId)) break;
        }
        return newWalletId;
    }

    private int generateNewCategoryId() {
        int newCategoryId;
        while (true) {
            newCategoryId = generateId();
            int finalNewCategoryId = newCategoryId;
            if (categories.stream().noneMatch(category -> category.getId() == finalNewCategoryId)) break;
        }
        return newCategoryId;
    }

    private int generateNewOperationId() {
        int newOperationId;
        while (true) {
            newOperationId = generateId();
            int finalNewOperationId = newOperationId;
            if (operations.stream().noneMatch(operation -> operation.getId() == finalNewOperationId)) break;
        }
        return newOperationId;
    }

    private int generateId() {
        return new Random().nextInt(1, 1000001);
    }
}
