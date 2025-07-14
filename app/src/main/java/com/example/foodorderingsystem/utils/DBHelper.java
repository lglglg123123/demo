package com.example.foodorderingsystem.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.foodorderingsystem.models.CartItem;
import com.example.foodorderingsystem.models.FoodItem;
import com.example.foodorderingsystem.models.Order;
import com.example.foodorderingsystem.models.User;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "FoodOrderingDB";
    private static final int DATABASE_VERSION = 1;

    // 用户表
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PHONE = "phone";

    // 食品表
    private static final String TABLE_FOODS = "foods";
    private static final String COLUMN_FOOD_ID = "food_id";
    private static final String COLUMN_FOOD_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_CATEGORY = "category";

    // 购物车表
    private static final String TABLE_CART = "cart";
    private static final String COLUMN_CART_ID = "cart_id";
    private static final String COLUMN_QUANTITY = "quantity";

    // 订单表
    private static final String TABLE_ORDERS = "orders";
    private static final String COLUMN_ORDER_ID = "order_id";
    private static final String COLUMN_ORDER_DATE = "order_date";
    private static final String COLUMN_TOTAL_AMOUNT = "total_amount";
    private static final String COLUMN_STATUS = "status";

    // 订单详情表
    private static final String TABLE_ORDER_ITEMS = "order_items";
    private static final String COLUMN_ORDER_ITEM_ID = "order_item_id";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建用户表
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT UNIQUE,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PHONE + " TEXT"
                + ")";
        db.execSQL(CREATE_USERS_TABLE);

        // 创建食品表
        String CREATE_FOODS_TABLE = "CREATE TABLE " + TABLE_FOODS + "("
                + COLUMN_FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FOOD_NAME + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_PRICE + " REAL,"
                + COLUMN_IMAGE + " TEXT,"
                + COLUMN_CATEGORY + " TEXT"
                + ")";
        db.execSQL(CREATE_FOODS_TABLE);

        // 创建购物车表
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_FOOD_ID + " INTEGER,"
                + COLUMN_QUANTITY + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "),"
                + "FOREIGN KEY(" + COLUMN_FOOD_ID + ") REFERENCES " + TABLE_FOODS + "(" + COLUMN_FOOD_ID + ")"
                + ")";
        db.execSQL(CREATE_CART_TABLE);

        // 创建订单表
        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + "("
                + COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_ORDER_DATE + " INTEGER,"
                + COLUMN_TOTAL_AMOUNT + " REAL,"
                + COLUMN_STATUS + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")"
                + ")";
        db.execSQL(CREATE_ORDERS_TABLE);

        // 创建订单详情表
        String CREATE_ORDER_ITEMS_TABLE = "CREATE TABLE " + TABLE_ORDER_ITEMS + "("
                + COLUMN_ORDER_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ORDER_ID + " INTEGER,"
                + COLUMN_FOOD_ID + " INTEGER,"
                + COLUMN_QUANTITY + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_ORDER_ID + ") REFERENCES " + TABLE_ORDERS + "(" + COLUMN_ORDER_ID + "),"
                + "FOREIGN KEY(" + COLUMN_FOOD_ID + ") REFERENCES " + TABLE_FOODS + "(" + COLUMN_FOOD_ID + ")"
                + ")";
        db.execSQL(CREATE_ORDER_ITEMS_TABLE);

        // 初始化数据
        initializeData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // 初始化数据
    private void initializeData(SQLiteDatabase db) {
        // 添加测试用户
        addUser(db, "admin", "admin123", "admin@example.com", "1234567890");
        addUser(db, "user1", "user123", "user1@example.com", "0987654321");

        // 添加食品
        addFood(db, "汉堡", "经典汉堡，美味多汁", 15.99, "burger", "快餐");
        addFood(db, "披萨", "意大利风味披萨", 29.99, "pizza", "主食");
        addFood(db, "沙拉", "新鲜蔬菜沙拉", 12.99, "salad", "健康餐");
        addFood(db, "炸鸡", "香脆炸鸡", 18.99, "fried_chicken", "快餐");
        addFood(db, "意面", "番茄肉酱意面", 22.99, "pasta", "主食");
        addFood(db, "寿司", "新鲜三文鱼寿司", 24.99, "sushi", "日式料理");
    }

    // 用户操作方法
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, user.getUsername());
        values.put(COLUMN_PASSWORD, user.getPassword());
        values.put(COLUMN_EMAIL, user.getEmail());
        values.put(COLUMN_PHONE, user.getPhone());
        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    private void addUser(SQLiteDatabase db, String username, String password, String email, String phone) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PHONE, phone);
        db.insert(TABLE_USERS, null, values);
    }

    public User getUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_USER_ID, COLUMN_USERNAME, COLUMN_EMAIL, COLUMN_PHONE},
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getInt(0),
                    cursor.getString(1),
                    password,
                    cursor.getString(2),
                    cursor.getString(3)
            );
            cursor.close();
            return user;
        }
        return null;
    }

    // 食品操作方法
    private void addFood(SQLiteDatabase db, String name, String description, double price, String image, String category) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOOD_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_CATEGORY, category);
        db.insert(TABLE_FOODS, null, values);
    }

    public List<FoodItem> getAllFoods() {
        List<FoodItem> foodList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FOODS,
                new String[]{COLUMN_FOOD_ID, COLUMN_FOOD_NAME, COLUMN_DESCRIPTION, COLUMN_PRICE, COLUMN_IMAGE, COLUMN_CATEGORY},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                FoodItem food = new FoodItem(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );
                foodList.add(food);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return foodList;
    }

    public List<FoodItem> getFoodsByCategory(String category) {
        List<FoodItem> foodList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FOODS,
                new String[]{COLUMN_FOOD_ID, COLUMN_FOOD_NAME, COLUMN_DESCRIPTION, COLUMN_PRICE, COLUMN_IMAGE, COLUMN_CATEGORY},
                COLUMN_CATEGORY + "=?",
                new String[]{category}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                FoodItem food = new FoodItem(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );
                foodList.add(food);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return foodList;
    }

    public FoodItem getFoodById(int foodId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FOODS,
                new String[]{COLUMN_FOOD_ID, COLUMN_FOOD_NAME, COLUMN_DESCRIPTION, COLUMN_PRICE, COLUMN_IMAGE, COLUMN_CATEGORY},
                COLUMN_FOOD_ID + "=?",
                new String[]{String.valueOf(foodId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            FoodItem food = new FoodItem(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
            cursor.close();
            return food;
        }
        return null;
    }

    // 购物车操作方法
    public void addToCart(int userId, int foodId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        // 检查是否已存在
        Cursor cursor = db.query(TABLE_CART,
                new String[]{COLUMN_CART_ID, COLUMN_QUANTITY},
                COLUMN_USER_ID + "=? AND " + COLUMN_FOOD_ID + "=?",
                new String[]{String.valueOf(userId), String.valueOf(foodId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // 已存在，更新数量
            int existingQuantity = cursor.getInt(1);
            int newQuantity = existingQuantity + quantity;
            updateCartItemQuantity(userId, foodId, newQuantity);
            cursor.close();
        } else {
            // 不存在，添加新记录
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_ID, userId);
            values.put(COLUMN_FOOD_ID, foodId);
            values.put(COLUMN_QUANTITY, quantity);
            db.insert(TABLE_CART, null, values);
        }
        db.close();
    }

    public void updateCartItemQuantity(int userId, int foodId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUANTITY, quantity);
        db.update(TABLE_CART, values,
                COLUMN_USER_ID + "=? AND " + COLUMN_FOOD_ID + "=?",
                new String[]{String.valueOf(userId), String.valueOf(foodId)});
        db.close();
    }

    public void removeFromCart(int userId, int foodId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART,
                COLUMN_USER_ID + "=? AND " + COLUMN_FOOD_ID + "=?",
                new String[]{String.valueOf(userId), String.valueOf(foodId)});
        db.close();
    }

    public void clearCart(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)});
        db.close();
    }

    public List<CartItem> getCartItems(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT c." + COLUMN_CART_ID + ", c." + COLUMN_FOOD_ID + ", c." + COLUMN_QUANTITY + ", " +
                "f." + COLUMN_FOOD_NAME + ", f." + COLUMN_PRICE + ", f." + COLUMN_IMAGE + " " +
                "FROM " + TABLE_CART + " c " +
                "INNER JOIN " + TABLE_FOODS + " f ON c." + COLUMN_FOOD_ID + " = f." + COLUMN_FOOD_ID + " " +
                "WHERE c." + COLUMN_USER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                CartItem cartItem = new CartItem(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getDouble(4),
                        cursor.getString(5)
                );
                cartItems.add(cartItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartItems;
    }

    public double getCartTotal(int userId) {
        double total = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT SUM(f." + COLUMN_PRICE + " * c." + COLUMN_QUANTITY + ") as total " +
                "FROM " + TABLE_CART + " c " +
                "INNER JOIN " + TABLE_FOODS + " f ON c." + COLUMN_FOOD_ID + " = f." + COLUMN_FOOD_ID + " " +
                "WHERE c." + COLUMN_USER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }
        cursor.close();
        return total;
    }

    // 订单操作方法
    public long placeOrder(int userId, double totalAmount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_ORDER_DATE, System.currentTimeMillis());
        values.put(COLUMN_TOTAL_AMOUNT, totalAmount);
        values.put(COLUMN_STATUS, "待处理");
        long orderId = db.insert(TABLE_ORDERS, null, values);
        db.close();
        return orderId;
    }

    public void addOrderItems(long orderId, List<CartItem> cartItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (CartItem item : cartItems) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ORDER_ID, orderId);
            values.put(COLUMN_FOOD_ID, item.getFoodId());
            values.put(COLUMN_QUANTITY, item.getQuantity());
            db.insert(TABLE_ORDER_ITEMS, null, values);
        }
        db.close();
    }

    public List<Order> getUserOrders(int userId) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ORDERS,
                new String[]{COLUMN_ORDER_ID, COLUMN_USER_ID, COLUMN_ORDER_DATE, COLUMN_TOTAL_AMOUNT, COLUMN_STATUS},
                COLUMN_USER_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, COLUMN_ORDER_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Order order = new Order(
                        cursor.getLong(0),
                        cursor.getInt(1),
                        cursor.getLong(2),
                        cursor.getDouble(3),
                        cursor.getString(4)
                );
                orders.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orders;
    }

    public Order getOrderById(long orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDERS,
                new String[]{COLUMN_ORDER_ID, COLUMN_USER_ID, COLUMN_ORDER_DATE, COLUMN_TOTAL_AMOUNT, COLUMN_STATUS},
                COLUMN_ORDER_ID + "=?",
                new String[]{String.valueOf(orderId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Order order = new Order(
                    cursor.getLong(0),
                    cursor.getInt(1),
                    cursor.getLong(2),
                    cursor.getDouble(3),
                    cursor.getString(4)
            );
            cursor.close();
            return order;
        }
        return null;
    }

    public List<CartItem> getOrderItems(long orderId) {
        List<CartItem> orderItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT oi." + COLUMN_ORDER_ITEM_ID + ", oi." + COLUMN_FOOD_ID + ", oi." + COLUMN_QUANTITY + ", " +
                "f." + COLUMN_FOOD_NAME + ", f." + COLUMN_PRICE + ", f." + COLUMN_IMAGE + " " +
                "FROM " + TABLE_ORDER_ITEMS + " oi " +
                "INNER JOIN " + TABLE_FOODS + " f ON oi." + COLUMN_FOOD_ID + " = f." + COLUMN_FOOD_ID + " " +
                "WHERE oi." + COLUMN_ORDER_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(orderId)});

        if (cursor.moveToFirst()) {
            do {
                CartItem item = new CartItem(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getDouble(4),
                        cursor.getString(5)
                );
                orderItems.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orderItems;
    }
}