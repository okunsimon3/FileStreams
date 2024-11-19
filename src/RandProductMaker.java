import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandProductMaker extends JFrame {
    private final JTextField idField;
    private final JTextField nameField;
    private final JTextField descriptionField;
    private final JTextField costField;
    private final JTextField recordCountField;
    private JButton addButton;
    private JButton quitButton;
    private RandomAccessFile randomFile;
    private int recordCount = 0;

    private static final int RECORD_LENGTH = Product.ID_LENGTH + Product.NAME_LENGTH + Product.DESCRIPTION_LENGTH + Product.COST_LENGTH;

    public RandProductMaker() {
        // Set up the JFrame
        setTitle("Random Product Maker");
        setLayout(new GridLayout(6, 2, 10, 10));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // GUI Components
        add(new JLabel("Product ID (6 chars):"));
        idField = new JTextField();
        add(idField);

        add(new JLabel("Name (35 chars):"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Description (75 chars):"));
        descriptionField = new JTextField();
        add(descriptionField);

        add(new JLabel("Cost (e.g., 19.99):"));
        costField = new JTextField();
        add(costField);

        add(new JLabel("Record Count:"));
        recordCountField = new JTextField("0");
        recordCountField.setEditable(false);
        add(recordCountField);

        addButton = new JButton("Add");
        add(addButton);

        JButton quitButton = new JButton("Quit");
        add(quitButton);

        quitButton.addActionListener(e -> {
            int confirmed = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to quit?", "Quit Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (confirmed == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        addButton.addActionListener(e -> handleAddButton());

        try {
            randomFile = new RandomAccessFile("products.dat", "rw");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error initializing file: " + ex.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        setSize(500, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleAddButton() {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String description = descriptionField.getText().trim();
            String costText = costField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || description.isEmpty() || costText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (id.length() > Product.ID_LENGTH) {
                JOptionPane.showMessageDialog(this, "ID must be at most " + Product.ID_LENGTH + " characters.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (name.length() > Product.NAME_LENGTH) {
                JOptionPane.showMessageDialog(this, "Name must be at most " + Product.NAME_LENGTH + " characters.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (description.length() > Product.DESCRIPTION_LENGTH) {
                JOptionPane.showMessageDialog(this, "Description must be at most " + Product.DESCRIPTION_LENGTH + " characters.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double cost;
            try {
                cost = Double.parseDouble(costText);
                if (cost < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cost must be a positive numeric value.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Product product = new Product(id, name, description, cost);
            writeToRandomFile(product);

            recordCount++;
            recordCountField.setText(String.valueOf(recordCount));

            clearFields();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Application Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void writeToRandomFile(Product product) throws IOException {
        randomFile.seek(recordCount * RECORD_LENGTH);
        randomFile.writeBytes(product.getFormattedRecord());
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        descriptionField.setText("");
        costField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RandProductMaker::new);
    }
}
