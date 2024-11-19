public class Product {
    private final String ID; // Immutable, fixed length
    private String name; // Fixed length
    private String description; // Fixed length
    private double cost; // Numeric field

    // Fixed lengths for fields
    public static final int ID_LENGTH = 6;
    public static final int NAME_LENGTH = 35;
    public static final int DESCRIPTION_LENGTH = 75;
    public static final int COST_LENGTH = 10; // For formatted cost (e.g., "12345.67")

    // Constructor
    public Product(String ID, String name, String description, double cost) {
        if (ID == null || ID.trim().isEmpty() || ID.length() > ID_LENGTH) {
            throw new IllegalArgumentException("ID must be non-empty and at most " + ID_LENGTH + " characters.");
        }
        this.ID = padString(ID, ID_LENGTH); // ID is immutable and padded
        setName(name);
        setDescription(description);
        setCost(cost);
    }

    // Getters
    public String getID() {
        return ID.trim(); // Remove padding when accessed
    }

    public String getName() {
        return name.trim();
    }

    public String getDescription() {
        return description.trim();
    }

    public double getCost() {
        return cost;
    }

    // Setters
    public void setName(String name) {
        if (name == null || name.trim().isEmpty() || name.length() > NAME_LENGTH) {
            throw new IllegalArgumentException("Name must be non-empty and at most " + NAME_LENGTH + " characters.");
        }
        this.name = padString(name, NAME_LENGTH);
    }

    public void setDescription(String description) {
        if (description == null || description.length() > DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("Description must be at most " + DESCRIPTION_LENGTH + " characters.");
        }
        this.description = padString(description, DESCRIPTION_LENGTH);
    }

    public void setCost(double cost) {
        if (cost < 0) {
            throw new IllegalArgumentException("Cost cannot be negative.");
        }
        this.cost = cost;
    }

    // Utility: Format Product as a fixed-length record for RandomAccessFile
    public String getFormattedRecord() {
        String paddedCost = padString(String.format("%.2f", cost), COST_LENGTH); // Format and pad cost
        return ID + name + description + paddedCost; // Concatenate all fields
    }

    // Static utility: Pad or truncate strings
    private static String padString(String text, int length) {
        if (text == null) {
            text = ""; // Handle null input
        }
        if (text.length() > length) {
            return text.substring(0, length); // Truncate if too long
        }
        return String.format("%-" + length + "s", text); // Pad with spaces
    }

    // Override toString for debugging or display
    @Override
    public String toString() {
        return "Product{" +
                "ID='" + getID() + '\'' +
                ", Name='" + getName() + '\'' +
                ", Description='" + getDescription() + '\'' +
                ", Cost=" + cost +
                '}';
    }
}
