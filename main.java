import java.util.*;

/* ================= BASE USER ================= */
abstract class User {
    protected int id;
    protected String username;
    protected String password;

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public abstract void dashboard(Platform platform, Scanner sc);

    public String getUsername() { return username; }
    public String getPassword() { return password; }
}

/* ================= COUPON ================= */
class Coupon {
    String code;
    double discount;

    public Coupon(String code, double discount) {
        this.code = code;
        this.discount = discount;
    }
}

/* ================= ORDER ================= */
class Order {
    String customerName;
    String restaurantName;
    String foodName;
    double amount;

    public Order(String customerName, String restaurantName,
                 String foodName, double amount) {
        this.customerName = customerName;
        this.restaurantName = restaurantName;
        this.foodName = foodName;
        this.amount = amount;
    }
}

/* ================= FOOD ================= */
class FoodItem {
    int id;
    String name;
    double price;

    public FoodItem(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

/* ================= RESTAURANT ================= */
class Restaurant {
    int id;
    String name;
    int ownerId;
    List<FoodItem> menu = new ArrayList<>();
    List<Coupon> coupons = new ArrayList<>();
    double revenue = 0;

    public Restaurant(int id, String name, int ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
    }

    public void showMenu() {
        if (menu.isEmpty()) {
            System.out.println("No food available.");
            return;
        }

        for (FoodItem f : menu) {
            System.out.println(f.id + ". " + f.name + " ₹" + f.price);
        }
    }
}

/* ================= PLATFORM ================= */
class Platform {
    List<User> users = new ArrayList<>();
    List<Restaurant> restaurants = new ArrayList<>();
    List<Order> orders = new ArrayList<>();

    int nextUserId = 1;
    int nextRestaurantId = 1;
    int nextFoodId = 1;

    public Restaurant getRestaurantById(int id) {
        for (Restaurant r : restaurants)
            if (r.id == id)
                return r;
        return null;
    }

    public Restaurant getRestaurantByOwner(int ownerId) {
        for (Restaurant r : restaurants)
            if (r.ownerId == ownerId)
                return r;
        return null;
    }
}

/* ================= ADMIN ================= */
class Admin extends User {

    public Admin(int id, String username, String password) {
        super(id, username, password);
    }

    public void dashboard(Platform platform, Scanner sc) {

        while (true) {
            System.out.println("\n--- Admin Dashboard ---");
            System.out.println("1. Add Restaurant");
            System.out.println("2. View Restaurants");
            System.out.println("3. View All Orders");
            System.out.println("4. Logout");
            System.out.print("Choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Restaurant Name: ");
                    String rName = sc.nextLine();

                    System.out.print("Set Password: ");
                    String rPass = sc.nextLine();

                    for (User u : platform.users) {
                        if (u.getUsername().equals(rName)) {
                            System.out.println("Username already exists.");
                            break;
                        }
                    }

                    int ownerId = platform.nextUserId++;

                    RestaurantOwner newOwner =
                            new RestaurantOwner(ownerId, rName, rPass);

                    platform.users.add(newOwner);

                    platform.restaurants.add(
                            new Restaurant(platform.nextRestaurantId++, rName, ownerId)
                    );

                    System.out.println("Restaurant created successfully.");
                    break;

                case 2:
                    for (Restaurant r : platform.restaurants) {
                        System.out.println(r.id + ". " + r.name +
                                " | Revenue ₹" + r.revenue);
                    }
                    break;

                case 3:
                    System.out.println("\n--- All Orders ---");
                    for (Order o : platform.orders) {
                        System.out.println("Customer: " + o.customerName +
                                " | Restaurant: " + o.restaurantName +
                                " | Food: " + o.foodName +
                                " | ₹" + o.amount);
                    }
                    break;

                case 4:
                    return;
            }
        }
    }
}

/* ================= RESTAURANT OWNER ================= */
class RestaurantOwner extends User {

    public RestaurantOwner(int id, String username, String password) {
        super(id, username, password);
    }

    public void dashboard(Platform platform, Scanner sc) {

        Restaurant r = platform.getRestaurantByOwner(id);

        if (r == null) {
            System.out.println("No restaurant assigned.");
            return;
        }

        while (true) {
            System.out.println("\n--- Restaurant Dashboard (" + r.name + ") ---");
            System.out.println("1. Add Food");
            System.out.println("2. Add Coupon");
            System.out.println("3. View Orders");
            System.out.println("4. View Revenue");
            System.out.println("5. Logout");
            System.out.print("Choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.print("Food Name: ");
                    String fname = sc.nextLine();

                    System.out.print("Price: ");
                    double price = sc.nextDouble();
                    sc.nextLine();

                    r.menu.add(
                            new FoodItem(platform.nextFoodId++, fname, price)
                    );

                    System.out.println("Food added.");
                    break;

                case 2:
                    System.out.print("Coupon Code: ");
                    String code = sc.nextLine();

                    System.out.print("Discount %: ");
                    double disc = sc.nextDouble();
                    sc.nextLine();

                    r.coupons.add(new Coupon(code, disc));
                    System.out.println("Coupon created.");
                    break;

                case 3:
                    System.out.println("\n--- Orders ---");
                    for (Order o : platform.orders) {
                        if (o.restaurantName.equals(r.name)) {
                            System.out.println(o.customerName +
                                    " ordered " + o.foodName +
                                    " ₹" + o.amount);
                        }
                    }
                    break;

                case 4:
                    System.out.println("Total Revenue: ₹" + r.revenue);
                    break;

                case 5:
                    return;
            }
        }
    }
}

/* ================= CUSTOMER ================= */
class Customer {

    String name;

    public Customer(String name) {
        this.name = name;
    }

    public void dashboard(Platform platform, Scanner sc) {

        while (true) {
            System.out.println("\n--- Customer Dashboard ---");
            System.out.println("1. View Restaurants");
            System.out.println("2. Order Food");
            System.out.println("3. Exit");
            System.out.print("Choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                for (Restaurant r : platform.restaurants)
                    System.out.println(r.id + ". " + r.name);
            }

            else if (choice == 2) {

                System.out.print("Restaurant ID: ");
                int rid = sc.nextInt();
                sc.nextLine();

                Restaurant r = platform.getRestaurantById(rid);
                if (r == null) {
                    System.out.println("Invalid restaurant.");
                    continue;
                }

                r.showMenu();

                System.out.print("Food ID: ");
                int fid = sc.nextInt();
                sc.nextLine();

                FoodItem selected = null;
                for (FoodItem f : r.menu)
                    if (f.id == fid)
                        selected = f;

                if (selected == null) {
                    System.out.println("Invalid food.");
                    continue;
                }

                System.out.print("Enter Coupon Code or NONE: ");
                String couponInput = sc.nextLine();

                double amount = selected.price;
                boolean applied = false;

                for (Coupon c : r.coupons) {
                    if (c.code.equalsIgnoreCase(couponInput)) {
                        amount = selected.price -
                                (selected.price * c.discount / 100);
                        applied = true;
                        break;
                    }
                }

                if (applied)
                    System.out.println("Coupon Applied!");
                else if (!couponInput.equalsIgnoreCase("NONE"))
                    System.out.println("Invalid Coupon.");

                r.revenue += amount;

                platform.orders.add(
                        new Order(name, r.name, selected.name, amount)
                );

                System.out.println("Order placed. Paid ₹" + amount);
            }

            else if (choice == 3)
                return;
        }
    }
}

/* ================= MAIN ================= */
public class main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Platform platform = new Platform();

        platform.users.add(new Admin(platform.nextUserId++, "admin", "1234"));

        while (true) {
            System.out.println("\n===== Food Delivery Backend =====");
            System.out.println("1. Admin Login");
            System.out.println("2. Restaurant Login");
            System.out.println("3. Customer");
            System.out.println("4. Exit");
            System.out.print("Choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1 || choice == 2) {

                System.out.print("Username: ");
                String username = sc.nextLine();

                System.out.print("Password: ");
                String password = sc.nextLine();

                boolean found = false;

                for (User u : platform.users) {
                    if (u.getUsername().equals(username) &&
                            u.getPassword().equals(password)) {
                        u.dashboard(platform, sc);
                        found = true;
                        break;
                    }
                }

                if (!found)
                    System.out.println("Invalid credentials.");
            }

            else if (choice == 3) {
                System.out.print("Enter Your Name: ");
                String cname = sc.nextLine();

                Customer c = new Customer(cname);
                c.dashboard(platform, sc);
            }

            else if (choice == 4)
                break;
        }

        sc.close();
    }
}