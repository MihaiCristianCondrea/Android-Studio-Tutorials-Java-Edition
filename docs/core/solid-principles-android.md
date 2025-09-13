# SOLID Principles in Android Development with Java

## Introduction
SOLID is an acronym for five design principles that help create maintainable and scalable codebases. Applying these ideas in Android projects keeps features isolated, encourages extension without breaking existing behavior and makes code easier to test.

## Single Responsibility Principle
Each class should have one reason to change. Splitting responsibilities into distinct components improves clarity and testability.

### Violation
```java
class ItemManager {
    private final Context context;
    private final List<Item> items = new ArrayList<>();

    ItemManager(Context context) {
        this.context = context;
    }

    void retrieveAndDisplayItems() {
        List<Item> items = retrieveItemsFromServer();
        RecyclerView recyclerView = new RecyclerView(context);
        ItemListAdapter adapter = new ItemListAdapter(items);
        recyclerView.setAdapter(adapter);
    }

    List<Item> retrieveItemsFromServer() {
        return Collections.emptyList();
    }

    void storeItemsLocally(List<Item> items) {
        // Save items to the local database
    }
}
```

### Adherence
```java
class ItemRepository {
    List<Item> fetchItems() {
        // Fetch items from a server or database
        return Collections.emptyList();
    }

    void saveItems(List<Item> items) {
        // Persist items to a database
    }
}
```

## Open-Closed Principle
Software entities should be open for extension and closed for modification. Favor abstraction so new behavior can be added without altering existing code.

### Violation
```java
class ItemService {
    double calculateTotalPrice(List<Item> cart, double discount) {
        double totalPrice = 0.0;
        for (Item item : cart) {
            totalPrice += item.getPrice();
        }
        totalPrice *= (1.0 - discount);
        return totalPrice;
    }
}
```

### Adherence
```java
interface PriceCalculator {
    double calculateTotalPrice(List<Product> cart);
}

class BasicPriceCalculator implements PriceCalculator {
    @Override
    public double calculateTotalPrice(List<Product> cart) {
        double totalPrice = 0.0;
        for (Product product : cart) {
            totalPrice += product.getPrice();
        }
        return totalPrice;
    }
}

class DiscountedPriceCalculator implements PriceCalculator {
    private final double discount;

    DiscountedPriceCalculator(double discount) {
        this.discount = discount;
    }

    @Override
    public double calculateTotalPrice(List<Product> cart) {
        PriceCalculator basic = new BasicPriceCalculator();
        double total = basic.calculateTotalPrice(cart);
        return total * (1.0 - discount);
    }
}
```

## Liskov Substitution Principle
Subclasses must be replaceable for their base types without altering the correctness of the program.

### Violation
```java
class Bird {
    void fly() {
        // Default flying behavior
    }
}

class Dog extends Bird {
    @Override
    void fly() {
        throw new UnsupportedOperationException("Dogs can't fly");
    }
}
```

### Adherence
```java
class Bird {
    void move() {
        // Default movement behavior
    }
}

class Ostrich extends Bird {
    @Override
    void move() {
        // Ostriches move by running
    }
}
```

## Interface Segregation Principle
Clients should not be forced to implement methods they do not use. Split broad interfaces into focused ones.

### Violation
```java
interface Worker {
    void work();
    void eat();
}

class SuperWorker implements Worker {
    @Override
    public void work() {
        // Working behavior
    }

    @Override
    public void eat() {
        // Eating behavior
    }
}
```

### Adherence
```java
interface Workable {
    void work();
}

interface Eatable {
    void eat();
}

class SuperWorker implements Workable, Eatable {
    @Override
    public void work() {
        // Working behavior
    }

    @Override
    public void eat() {
        // Eating behavior
    }
}
```

## Dependency Inversion Principle
High-level modules should depend on abstractions rather than concrete implementations.

### Violation
```java
class LightBulb {
    void turnOn() {
        // Turn on the light bulb
    }
}

class Switch {
    private final LightBulb bulb = new LightBulb();

    void control() {
        bulb.turnOn();
    }
}
```

### Adherence
```java
interface Switchable {
    void turnOn();
}

class LightBulb implements Switchable {
    @Override
    public void turnOn() {
        // Turn on the light bulb
    }
}

class Switch {
    private final Switchable device;

    Switch(Switchable device) {
        this.device = device;
    }

    void control() {
        device.turnOn();
    }
}
```

## Conclusion
Applying the SOLID principles in Android projects encourages separation of concerns, extensibility and decoupling. These guidelines lead to code that is easier to understand, test and evolve over time.

