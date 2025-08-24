
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class NonScrollingPasswordCheckerUI extends JFrame {

    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckbox;
    private JLabel strengthLabel, entropyLabel;
    private JTextArea adviceArea, breachStatusArea;
    private JPanel strengthIndicator;

    // Custom Rounded Panel Class
    class RoundedPanel extends JPanel {

        private int arcWidth = 20;
        private int arcHeight = 20;

        public RoundedPanel(LayoutManager layout) {
            super(layout);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight));
            g2.dispose();
        }
    }

    public NonScrollingPasswordCheckerUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle(" Password Checker");
        setSize(900, 650);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(new Color(240, 240, 240));
        setContentPane(contentPane);

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(25, 25, 10, 25));
        JLabel title = new JLabel("Password Strength & Security Checker");
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(new Color(60, 60, 60));
        topPanel.add(title);
        contentPane.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        RoundedPanel inputPanel = new RoundedPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(255, 255, 255, 220));
        inputPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(new JLabel("<html><b style='font-size:14px;'>Enter your password below to check its strength and see if it has been compromised.</b></html>", JLabel.CENTER), gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        inputPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        passwordField = new JPasswordField(25);
        inputPanel.add(passwordField, gbc);

        gbc.gridx = 3;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        showPasswordCheckbox = new JCheckBox("Show");
        showPasswordCheckbox.addActionListener(e -> togglePasswordVisibility());
        inputPanel.add(showPasswordCheckbox, gbc);

        gbc.gridx = 4;
        JButton checkBtn = new JButton("Check");
        checkBtn.addActionListener(e -> checkPassword());
        checkBtn.setBackground(new Color(0, 150, 136));
        checkBtn.setForeground(Color.BLACK);
        checkBtn.setFocusPainted(false);
        inputPanel.add(checkBtn, gbc);

        gbc.gridx = 5;
        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> clearResults());
        clearBtn.setBackground(new Color(244, 67, 54));
        clearBtn.setForeground(Color.BLACK);
        clearBtn.setFocusPainted(false);
        inputPanel.add(clearBtn, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(inputPanel, gbc);

        JPanel mainContentPanel = new JPanel(new GridLayout(1, 2, 25, 25));
        mainContentPanel.setOpaque(false);

        RoundedPanel leftPanel = new RoundedPanel(new BorderLayout(15, 15));
        leftPanel.setBackground(new Color(255, 255, 255, 220));
        leftPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel strengthAndEntropyPanel = new JPanel(new BorderLayout(10, 10));
        strengthAndEntropyPanel.setOpaque(false);

        JPanel strengthIndicatorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        strengthIndicatorPanel.setOpaque(false);
        strengthIndicator = new JPanel();
        strengthIndicator.setPreferredSize(new Dimension(30, 30));
        strengthIndicator.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        strengthIndicator.setOpaque(true);
        strengthIndicator.setBackground(Color.LIGHT_GRAY);
        strengthIndicatorPanel.add(strengthIndicator);

        strengthLabel = new JLabel("Strength:", JLabel.LEFT);
        strengthLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        entropyLabel = new JLabel("Entropy:", JLabel.LEFT);
        entropyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        textPanel.add(strengthLabel);
        textPanel.add(entropyLabel);

        strengthAndEntropyPanel.add(strengthIndicatorPanel, BorderLayout.WEST);
        strengthAndEntropyPanel.add(textPanel, BorderLayout.CENTER);

        // Advice Area without ScrollPane
        adviceArea = new JTextArea();
        adviceArea.setEditable(false);
        adviceArea.setLineWrap(true);
        adviceArea.setWrapStyleWord(true);
        adviceArea.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        adviceArea.setOpaque(false);

        JPanel advicePanel = new JPanel(new BorderLayout());
        advicePanel.setOpaque(false);
        advicePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        advicePanel.add(adviceArea, BorderLayout.CENTER);

        leftPanel.add(strengthAndEntropyPanel, BorderLayout.NORTH);
        leftPanel.add(advicePanel, BorderLayout.CENTER);

        RoundedPanel rightPanel = new RoundedPanel(new BorderLayout(15, 15));
        rightPanel.setBackground(new Color(255, 255, 255, 220));
        rightPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel breachTitle = new JLabel("Breach Status (Have I Been Pwned)", JLabel.CENTER);
        breachTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        rightPanel.add(breachTitle, BorderLayout.NORTH);

        // Breach Status Area without ScrollPane
        breachStatusArea = new JTextArea();
        breachStatusArea.setEditable(false);
        breachStatusArea.setLineWrap(true);
        breachStatusArea.setWrapStyleWord(true);
        breachStatusArea.setFont(new Font("Segoe UI", Font.BOLD, 14));
        breachStatusArea.setOpaque(false);

        JPanel breachPanel = new JPanel(new BorderLayout());
        breachPanel.setOpaque(false);
        breachPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        breachPanel.add(breachStatusArea, BorderLayout.CENTER);

        rightPanel.add(breachPanel, BorderLayout.CENTER);

        mainContentPanel.add(leftPanel);
        mainContentPanel.add(rightPanel);

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weighty = 1.0;
        centerPanel.add(mainContentPanel, gbc);

        contentPane.add(centerPanel, BorderLayout.CENTER);
    }

    private void togglePasswordVisibility() {
        if (showPasswordCheckbox.isSelected()) {
            passwordField.setEchoChar((char) 0);
        } else {
            passwordField.setEchoChar('•');
        }
    }

    private String unescapeUnicode(String input) {
        Pattern pattern = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
        Matcher matcher = pattern.matcher(input);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            try {
                int hex = Integer.parseInt(matcher.group(1), 16);
                matcher.appendReplacement(sb, new String(Character.toChars(hex)));
            } catch (NumberFormatException e) {
                // Ignore invalid unicode escapes
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private void checkPassword() {
        String password = new String(passwordField.getPassword());
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a password to check.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                ProcessBuilder pb = new ProcessBuilder(
                        "C:\\Users\\amans\\AppData\\Local\\Programs\\Python\\Python312\\python.exe",
                        "C:\\Password_Checker_Project\\PasswordChecker.py",
                        password
                );
                pb.redirectErrorStream(true);
                Process process = pb.start();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    StringBuilder output = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line);
                    }
                    return output.toString();
                }
            }

            @Override
            protected void done() {
                try {
                    String jsonStr = get();
                    if (jsonStr.contains("Error")) {
                        JOptionPane.showMessageDialog(NonScrollingPasswordCheckerUI.this,
                                "An error occurred with the Python script. Check the console.",
                                "Script Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    String strength = extractValue(jsonStr, "strength");
                    String entropy = extractValue(jsonStr, "entropy");
                    String adviceRaw = extractValue(jsonStr, "advice");
                    String advice = unescapeUnicode(adviceRaw.replace("\\n", "\n"));
                    String breached = jsonStr.contains("\"breached\": true") ? "true" : "false";

                    strengthLabel.setText("Strength: " + strength);
                    entropyLabel.setText("Entropy: " + entropy + " bits");
                    adviceArea.setText(advice);

                    Color targetColor;
                    if (breached.equals("true")) {
                        breachStatusArea.setText(" WARNING: This password was found in known breach databases.");
                        breachStatusArea.setForeground(new Color(244, 67, 54));
                        targetColor = new Color(244, 67, 54);
                    } else if (strength.equals("Weak")) {
                        breachStatusArea.setText(" This password is very weak. Consider a stronger one.");
                        breachStatusArea.setForeground(new Color(255, 152, 0));
                        targetColor = new Color(255, 152, 0);
                    } else if (strength.equals("Medium")) {
                        breachStatusArea.setText(" This password is not found in breaches and has a medium strength.");
                        breachStatusArea.setForeground(new Color(0, 150, 136));
                        targetColor = new Color(0, 150, 136);
                    } else { // Strong or Very Strong
                        breachStatusArea.setText(" This password is not found in breaches and is very strong.");
                        breachStatusArea.setForeground(new Color(76, 175, 80));
                        targetColor = new Color(76, 175, 80);
                    }

                    animateColorChange(targetColor);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(NonScrollingPasswordCheckerUI.this,
                            "An unexpected error occurred: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private String extractValue(String json, String key) {
        Pattern p = Pattern.compile("\"" + key + "\":\\s*\"?([^\",]+)\"?");
        Matcher m = p.matcher(json);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }

    private void animateColorChange(Color targetColor) {
        Timer timer = new Timer(10, null);
        timer.addActionListener(e -> {
            Color currentColor = strengthIndicator.getBackground();
            int r = (int) (currentColor.getRed() + (targetColor.getRed() - currentColor.getRed()) * 0.1);
            int g = (int) (currentColor.getGreen() + (targetColor.getGreen() - currentColor.getGreen()) * 0.1);
            int b = (int) (currentColor.getBlue() + (targetColor.getBlue() - currentColor.getBlue()) * 0.1);

            strengthIndicator.setBackground(new Color(r, g, b));
            strengthIndicator.repaint();

            if (currentColor.equals(targetColor)) {
                timer.stop();
            }
        });
        timer.start();
    }

    private void clearResults() {
        passwordField.setText("");
        strengthLabel.setText("Strength: ");
        entropyLabel.setText("Entropy: ");
        adviceArea.setText("");
        breachStatusArea.setText("");
        strengthIndicator.setBackground(Color.LIGHT_GRAY);
        breachStatusArea.setForeground(Color.BLACK);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NonScrollingPasswordCheckerUI app = new NonScrollingPasswordCheckerUI();
            app.setVisible(true);
        });
    }
}
