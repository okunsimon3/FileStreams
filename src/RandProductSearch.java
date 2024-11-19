import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandProductSearch extends JFrame {
    private final JTextField searchField;
    private JButton searchButton;
    private final JTextArea resultArea;

    private static final int RECORD_LENGTH = Product.ID_LENGTH + Product.NAME_LENGTH + Product.DESCRIPTION_LENGTH + Product.COST_LENGTH;

    public RandProductSearch() {
        setTitle("Random Product Search");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("Enter partial product name:"));
        searchField = new JTextField(20);
        searchPanel.add(searchField);

        searchButton = new JButton("Search");
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);

        resultArea = new JTextArea(15, 50);
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        searchButton.addActionListener(e -> handleSearchButton());

        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void handleSearchButton() {
        String searchQuery = searchField.getText().trim();

        if (searchQuery.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a product name to search.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        resultArea.setText("");

        try (RandomAccessFile randomFile = new RandomAccessFile("products.dat", "r")) {
            int recordCount = (int) (randomFile.length() / RECORD_LENGTH);
            boolean found = false;

            for (int i = 0; i < recordCount; i++) {
                randomFile.seek(i * RECORD_LENGTH);
                byte[] recordBytes = new byte[Product.ID_LENGTH + Product.NAME_LENGTH + Product.DESCRIPTION_LENGTH + Product.COST_LENGTH];
                randomFile.read(recordBytes);

                String record = new String(recordBytes);
                String id = record.substring(0, Product.ID_LENGTH).trim();
                String name = record.substring(Product.ID_LENGTH, Product.ID_LENGTH + Product.NAME_LENGTH).trim();
                String description = record.substring(Product.ID_LENGTH + Product.NAME_LENGTH,
                        Product.ID_LENGTH + Product.NAME_LENGTH + Product.DESCRIPTION_LENGTH).trim();
                String cost = record.substring(Product.ID_LENGTH + Product.NAME_LENGTH + Product.DESCRIPTION_LENGTH).trim();

                if (name.toLowerCase().contains(searchQuery.toLowerCase())) {
                    found = true;
                    resultArea.append("ID: " + id + "\n");
                    resultArea.append("Name: " + name + "\n");
                    resultArea.append("Description: " + description + "\n");
                    resultArea.append("Cost: $" + cost + "\n");
                    resultArea.append("-----------------------------------------\n");
                }
            }

            if (!found) {
                resultArea.setText("No products found matching the name: " + searchQuery);
            }

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading the file: " + ex.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RandProductSearch::new);
    }
}
