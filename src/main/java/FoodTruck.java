import java.util.ArrayList;
import java.util.List;

public class FoodTruck {
    private final String name;
    private final String location;
    private final String cuisine;
    private final List<MenuItem> menu = new ArrayList<>();

    public FoodTruck(String name, String location, String cuisine) {
        this.name = name;
        this.location = location;
        this.cuisine = cuisine;
    }

    public void addItem(String itemName, double price) {
        menu.add(new MenuItem(itemName, price));
    }

    public List<MenuItem> getMenu() {
        return menu;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getCuisine()  { return cuisine; }

    public void printMenu() {
        System.out.println("=== " + name + " (" + location + ") ===");
        for (MenuItem item : menu) {
            System.out.println(item);
        }
    }
}
