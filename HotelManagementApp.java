import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class HotelManagementApp extends JFrame {
    private JPanel loginPanel;
    private JPanel homePanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private Connection connection;

    public HotelManagementApp() {
        setTitle("Hotel Management System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new CardLayout());
        setLocationRelativeTo(null);  // This centers the window


        // Initialize database connection
        initializeDBConnection();

        // Initialize Panels
        initializeLoginPanel();
        initializeHomePanel();

        // Start with the Login Panel
        add(loginPanel, "Login");
        add(homePanel, "Home");

        // Show the login panel first
        showPanel("Login");
        setVisible(true);
    }

    private void initializeDBConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/final", "postgres", "sanjay2005");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);

        // Set style properties
        button.setBackground(Color.BLUE); // Set background color
        button.setForeground(Color.WHITE); // Set text color
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Set font
        button.setFocusPainted(false); // Remove focus painting for a cleaner look
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Add padding

        return button;
    }


    private void initializeLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        loginPanel.setBackground(new Color(245, 245, 245));

        // Create constraints for positioning components in GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add some padding between components
        gbc.anchor = GridBagConstraints.CENTER;

        // Username Label and Text Field
        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        usernameField.setPreferredSize(new Dimension(200, 30)); // Set preferred size for uniform appearance

        // Password Label and Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 30)); // Set preferred size for uniform appearance

        // Buttons
        loginButton = createStyledButton("Login");
        registerButton = createStyledButton("Register");

        // Ensure buttons are the same size
        Dimension buttonSize = new Dimension(200, 40); // Fixed size for buttons
        loginButton.setPreferredSize(buttonSize);
        registerButton.setPreferredSize(buttonSize);

        // Position elements using GridBagLayout constraints
        gbc.gridx = 0; gbc.gridy = 0;
        loginPanel.add(usernameLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2; // Make the buttons span across two columns
        loginPanel.add(loginButton, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        loginPanel.add(registerButton, gbc);

        // Add action listeners for buttons
        loginButton.addActionListener(this::handleLogin);
        registerButton.addActionListener(this::handleRegister);
    }

    private void initializeHomePanel() {
        homePanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image img = ImageIO.read(new File("hotel.png")); // Path to the image
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), null); // Scale to fit panel
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        homePanel.setLayout(new BorderLayout());

        // Create a JPanel for the title with a background color
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 0, 0, 128)); // Semi-transparent black background
        titlePanel.setLayout(new BorderLayout());

        // Welcome Label
        JLabel welcomeLabel = new JLabel("Welcome to the Hotel Management System", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);

        // Add the welcome label to the title panel
        titlePanel.add(welcomeLabel, BorderLayout.CENTER);

        // Add the titlePanel to the homePanel
        homePanel.add(titlePanel, BorderLayout.NORTH);

        JPanel sideMenuPanel = new JPanel();
        sideMenuPanel.setLayout(new GridLayout(8, 1, 5, 5)); // Updated to 8 rows for the new button
        sideMenuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sideMenuPanel.setBackground(new Color(230, 230, 250));

        // Create buttons for each functionality
        JButton checkAvailabilityButton = createStyledButton("Check Room Availability");
        JButton allotRoomButton = createStyledButton("Allot Room");
        JButton viewCustomerDetailsButton = createStyledButton("View Customer Details");
        JButton viewHistoryLogButton = createStyledButton("View History Log");
        JButton increaseRoomCountButton = createStyledButton("Increase Room Count");
        JButton decreaseRoomCountButton = createStyledButton("Decrease Room Count");
        JButton checkoutButton = createStyledButton("Checkout");
        JButton viewRoomsButton = createStyledButton("View Rooms"); // New button to view room statuses

        // Add buttons to side menu panel
        sideMenuPanel.add(checkAvailabilityButton);
        sideMenuPanel.add(allotRoomButton);
        sideMenuPanel.add(viewCustomerDetailsButton);
        sideMenuPanel.add(viewHistoryLogButton);
        sideMenuPanel.add(increaseRoomCountButton);
        sideMenuPanel.add(decreaseRoomCountButton);
        sideMenuPanel.add(checkoutButton);
        sideMenuPanel.add(viewRoomsButton); // Add the new button to the side menu

        // Add action listeners for each button
        checkAvailabilityButton.addActionListener(e -> showRoomAvailability());
        allotRoomButton.addActionListener(e -> allotRoom());
        viewCustomerDetailsButton.addActionListener(e -> viewCustomerDetails());
        viewHistoryLogButton.addActionListener(e -> viewHistoryLog());
        increaseRoomCountButton.addActionListener(e -> increaseRoomCount());
        decreaseRoomCountButton.addActionListener(e -> decreaseRoomCount());
        checkoutButton.addActionListener(e -> checkoutRoom());
        viewRoomsButton.addActionListener(e -> viewRoomsStatus()); // Add action listener for the new button

        // Add side menu panel and logout button to the home panel
        homePanel.add(sideMenuPanel, BorderLayout.WEST);

        JButton logoutButton = createStyledButton("Logout");
        logoutButton.addActionListener(e -> showPanel("Login"));
        homePanel.add(logoutButton, BorderLayout.SOUTH);
    }



    private void viewRoomsStatus() {
        String query = "SELECT room_id, status FROM rooms ORDER BY room_id";
        try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            StringBuilder roomsStatus = new StringBuilder("Room Status:\n\n");
            while (rs.next()) {
                int roomId = rs.getInt("room_id");
                String status = rs.getString("status");
                roomsStatus.append("Room ID: ").append(roomId).append(" - ").append(status).append("\n");
            }
            JOptionPane.showMessageDialog(this, roomsStatus.toString(), "Room Status", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching room status", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Checkout functionality implementation
    private void checkoutRoom() {
        String roomIdInput = JOptionPane.showInputDialog("Enter Room ID for Checkout:");
        if (roomIdInput != null && !roomIdInput.isEmpty()) {
            try {
                int roomId = Integer.parseInt(roomIdInput); // Parse room ID to integer

                try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/final", "postgres", "sanjay2005");
                     PreparedStatement pstmt = conn.prepareStatement("UPDATE rooms SET status = 'Available' WHERE room_id = ?")) {

                    pstmt.setInt(1, roomId); // Use the integer value for room_id
                    int updatedRows = pstmt.executeUpdate();

                    if (updatedRows > 0) {
                        JOptionPane.showMessageDialog(this, "Checkout successful for Room ID: " + roomId);
                    } else {
                        JOptionPane.showMessageDialog(this, "No room found with ID: " + roomId, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Room ID. Please enter a numeric value.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error during checkout. Please try again.", "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (authenticateUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            showPanel("Home");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (registerUser(username, password)) {
            JOptionPane.showMessageDialog(this, "Registration Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Registration Failed", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean authenticateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // Password should ideally be hashed
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean registerUser(String username, String password) {
        String query = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // Password should ideally be hashed
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showRoomAvailability() {
        String query = "SELECT COUNT(*) FROM rooms WHERE status = 'available'";
        try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int availableRooms = rs.getInt(1);
                JOptionPane.showMessageDialog(this, "Available rooms: " + availableRooms, "Room Availability", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error checking room availability", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void allotRoom() {
        JTextField idField = new JTextField(10);
        JTextField nameField = new JTextField(10);
        JTextField ageField = new JTextField(10);
        JTextField placeField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Customer ID:"));
        panel.add(idField);
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Age:"));
        panel.add(ageField);
        panel.add(new JLabel("Place:"));
        panel.add(placeField);

        int option = JOptionPane.showConfirmDialog(this, panel, "Enter Customer Details", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String checkCustomerQuery = "SELECT COUNT(*) FROM customers WHERE customer_id = ?";
            String insertCustomerQuery = "INSERT INTO customers (customer_id, name, age, place) VALUES (?, ?, ?, ?)";
            String updateRoomQuery = "UPDATE rooms SET status = 'occupied', customer_id = ? WHERE room_id = (SELECT room_id FROM rooms WHERE status = 'available' LIMIT 1)";

            int customerId = Integer.parseInt(idField.getText());

            try (PreparedStatement checkCustomerStmt = connection.prepareStatement(checkCustomerQuery);
                 PreparedStatement insertCustomerStmt = connection.prepareStatement(insertCustomerQuery);
                 PreparedStatement updateRoomStmt = connection.prepareStatement(updateRoomQuery)) {

                // Check if customer ID already exists
                checkCustomerStmt.setInt(1, customerId);
                ResultSet resultSet = checkCustomerStmt.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);

                // Insert new customer if ID doesn't exist
                if (count == 0) {
                    insertCustomerStmt.setInt(1, customerId);
                    insertCustomerStmt.setString(2, nameField.getText());
                    insertCustomerStmt.setInt(3, Integer.parseInt(ageField.getText()));
                    insertCustomerStmt.setString(4, placeField.getText());
                    insertCustomerStmt.executeUpdate();
                }

                // Update room status and assign the customer to the room
                updateRoomStmt.setInt(1, customerId);
                int updated = updateRoomStmt.executeUpdate();

                if (updated > 0) {
                    JOptionPane.showMessageDialog(this, "Room Allotted Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No available rooms", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException | NumberFormatException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error allotting room", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }




    private void viewCustomerDetails() {
        String query = "SELECT r.room_id, c.name AS customer_name, c.age AS customer_age, c.place AS customer_place, c.check_in, c.check_out " +
                "FROM rooms r JOIN customers c ON r.customer_id = c.customer_id WHERE r.status = 'occupied'";
        try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            StringBuilder details = new StringBuilder("Customer Details:\n\n");
            while (rs.next()) {
                details.append("Room: ").append(rs.getInt("room_id"))
                        .append(", Name: ").append(rs.getString("customer_name"))
                        .append(", Age: ").append(rs.getInt("customer_age"))
                        .append(", Place: ").append(rs.getString("customer_place"))
                        .append(", Check-in: ").append(rs.getTimestamp("check_in"))
                        .append(", Check-out: ").append(rs.getTimestamp("check_out"))
                        .append("\n");
            }
            JOptionPane.showMessageDialog(this, details.toString(), "Customer Details", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching customer details", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewHistoryLog() {
        String query = "SELECT * FROM history_log ORDER BY log_date DESC";
        try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            StringBuilder history = new StringBuilder("History Log:\n\n");
            while (rs.next()) {
                history.append("Date: ").append(rs.getTimestamp("log_date"))
                        .append(", Description: ").append(rs.getString("description"))
                        .append("\n");
            }
            JOptionPane.showMessageDialog(this, history.toString(), "History Log", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching history log", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void increaseRoomCount() {
        String query = "INSERT INTO rooms (status) VALUES ('available')";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Room count increased!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error increasing room count", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void decreaseRoomCount() {
        String query = "DELETE FROM rooms WHERE status = 'available' LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Room count decreased!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No available rooms to remove", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error decreasing room count", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showPanel(String panelName) {
        CardLayout layout = (CardLayout) getContentPane().getLayout();
        layout.show(getContentPane(), panelName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomePage().setVisible(true));
    }
}
