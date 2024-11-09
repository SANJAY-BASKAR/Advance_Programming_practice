import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomePage extends JFrame {

    public WelcomePage() {
        setTitle("Welcome to Our Hotel");
        setSize(800, 500);  // Increased size for a more spacious layout
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Center the window on the screen
        getContentPane().setBackground(new Color(255, 255, 255)); // White background for cleanliness

        // Panel for content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(255, 255, 255));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); // Padding for spacing

        // Hotel Image (Responsive Design)
        JLabel imageLabel = new JLabel();
        ImageIcon hotelImage = new ImageIcon("hotel.jpg"); // Ensure hotel.jpg is in the correct directory
        Image image = hotelImage.getImage().getScaledInstance(700, 350, Image.SCALE_SMOOTH);  // Scaled image for better layout
        hotelImage = new ImageIcon(image);
        imageLabel.setIcon(hotelImage);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPanel.add(imageLabel, BorderLayout.CENTER);

        // Enter Button (Styled)
        JButton enterButton = new JButton("Enter");
        enterButton.setFont(new Font("Helvetica Neue", Font.BOLD, 20));
        enterButton.setBackground(new Color(0, 123, 255)); // Blue background
        enterButton.setForeground(Color.WHITE);  // White text
        enterButton.setPreferredSize(new Dimension(250, 60));
        enterButton.setFocusPainted(false);
        enterButton.setBorder(BorderFactory.createEmptyBorder());
        enterButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Hand cursor on hover
        contentPanel.add(enterButton, BorderLayout.SOUTH);

        // Action Listener for Enter Button
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Close the welcome page
                new HotelManagementApp().setVisible(true);  // Open the main system
            }
        });

        // Adding content panel to frame
        add(contentPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomePage().setVisible(true));
    }
}
