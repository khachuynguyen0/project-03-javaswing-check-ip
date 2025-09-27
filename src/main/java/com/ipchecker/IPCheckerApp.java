package com.ipchecker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * IPCheckerApp - A Java Swing application for checking and validating IP addresses
 * Features:
 * - IP address format validation
 * - IP address type detection (IPv4/IPv6)
 * - Public/Private IP classification
 * - IP address class determination (for IPv4)
 */
public class IPCheckerApp extends JFrame {
    private JTextField ipInputField;
    private JButton checkButton;
    private JButton clearButton;
    private JTextArea resultArea;
    
    // IPv4 regex pattern
    private static final String IPV4_PATTERN = 
        "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    
    // IPv6 regex pattern (simplified)
    private static final String IPV6_PATTERN = 
        "^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$|^::1$|^::$";
    
    private static final Pattern ipv4Pattern = Pattern.compile(IPV4_PATTERN);
    private static final Pattern ipv6Pattern = Pattern.compile(IPV6_PATTERN);
    
    public IPCheckerApp() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setFrameProperties();
    }
    
    private void initializeComponents() {
        ipInputField = new JTextField(20);
        checkButton = new JButton("Check IP");
        clearButton = new JButton("Clear");
        resultArea = new JTextArea(15, 40);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultArea.setBorder(BorderFactory.createLoweredBevelBorder());
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top panel for input
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("IP Address:"));
        inputPanel.add(ipInputField);
        inputPanel.add(checkButton);
        inputPanel.add(clearButton);
        
        // Center panel for results
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("IP Address Information"));
        
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkIPAddress();
            }
        });
        
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
        
        // Allow Enter key to trigger check
        ipInputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkIPAddress();
            }
        });
    }
    
    private void setFrameProperties() {
        setTitle("IP Address Checker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        pack();
        setLocationRelativeTo(null); // Center on screen
        
        // Set minimum size
        setMinimumSize(new Dimension(500, 400));
    }
    
    private void checkIPAddress() {
        String ip = ipInputField.getText().trim();
        
        if (ip.isEmpty()) {
            showResult("Please enter an IP address.");
            return;
        }
        
        StringBuilder result = new StringBuilder();
        result.append("IP Address: ").append(ip).append("\n");
        result.append("==================================================").append("\n\n");
        
        // Validate IP format
        boolean isValidIPv4 = isValidIPv4(ip);
        boolean isValidIPv6 = isValidIPv6(ip);
        
        if (!isValidIPv4 && !isValidIPv6) {
            result.append("❌ Invalid IP Address Format\n");
            result.append("Please enter a valid IPv4 or IPv6 address.\n\n");
            result.append("Examples:\n");
            result.append("IPv4: 192.168.1.1, 8.8.8.8, 127.0.0.1\n");
            result.append("IPv6: 2001:0db8:85a3:0000:0000:8a2e:0370:7334, ::1\n");
            showResult(result.toString());
            return;
        }
        
        result.append("✅ Valid IP Address\n\n");
        
        if (isValidIPv4) {
            analyzeIPv4(ip, result);
        } else if (isValidIPv6) {
            analyzeIPv6(ip, result);
        }
        
        // Try to get hostname
        try {
            InetAddress inetAddr = InetAddress.getByName(ip);
            String hostname = inetAddr.getCanonicalHostName();
            if (!hostname.equals(ip)) {
                result.append("Hostname: ").append(hostname).append("\n");
            }
        } catch (UnknownHostException e) {
            result.append("Hostname: Unable to resolve\n");
        }
        
        showResult(result.toString());
    }
    
    private void analyzeIPv4(String ip, StringBuilder result) {
        result.append("IP Version: IPv4\n");
        
        String[] parts = ip.split("\\.");
        int[] octets = new int[4];
        for (int i = 0; i < 4; i++) {
            octets[i] = Integer.parseInt(parts[i]);
        }
        
        // Determine IP class
        String ipClass = getIPv4Class(octets[0]);
        result.append("IP Class: ").append(ipClass).append("\n");
        
        // Check if private or public
        boolean isPrivate = isPrivateIPv4(octets);
        result.append("Type: ").append(isPrivate ? "Private" : "Public").append("\n");
        
        // Special addresses
        if (ip.equals("127.0.0.1")) {
            result.append("Special: Localhost (Loopback)\n");
        } else if (ip.startsWith("169.254.")) {
            result.append("Special: Link-Local Address (APIPA)\n");
        } else if (ip.startsWith("224.") || ip.startsWith("225.") || ip.startsWith("226.") || 
                  ip.startsWith("227.") || ip.startsWith("228.") || ip.startsWith("229.") ||
                  ip.startsWith("230.") || ip.startsWith("231.") || ip.startsWith("232.") ||
                  ip.startsWith("233.") || ip.startsWith("234.") || ip.startsWith("235.") ||
                  ip.startsWith("236.") || ip.startsWith("237.") || ip.startsWith("238.") ||
                  ip.startsWith("239.")) {
            result.append("Special: Multicast Address\n");
        }
        
        result.append("\nBinary Representation:\n");
        for (int i = 0; i < 4; i++) {
            result.append(String.format("%8s", Integer.toBinaryString(octets[i])).replace(' ', '0'));
            if (i < 3) result.append(".");
        }
        result.append("\n");
    }
    
    private void analyzeIPv6(String ip, StringBuilder result) {
        result.append("IP Version: IPv6\n");
        
        if (ip.equals("::1")) {
            result.append("Special: Localhost (Loopback)\n");
        } else if (ip.startsWith("fe80:")) {
            result.append("Special: Link-Local Address\n");
        } else if (ip.startsWith("fc00:") || ip.startsWith("fd00:")) {
            result.append("Type: Private (Unique Local)\n");
        } else if (ip.startsWith("ff00:")) {
            result.append("Special: Multicast Address\n");
        } else {
            result.append("Type: Global Unicast (Public)\n");
        }
        
        result.append("\n");
    }
    
    private String getIPv4Class(int firstOctet) {
        if (firstOctet >= 1 && firstOctet <= 126) {
            return "A (1.0.0.0 to 126.255.255.255)";
        } else if (firstOctet == 127) {
            return "A (127.0.0.0 to 127.255.255.255) - Loopback";
        } else if (firstOctet >= 128 && firstOctet <= 191) {
            return "B (128.0.0.0 to 191.255.255.255)";
        } else if (firstOctet >= 192 && firstOctet <= 223) {
            return "C (192.0.0.0 to 223.255.255.255)";
        } else if (firstOctet >= 224 && firstOctet <= 239) {
            return "D (224.0.0.0 to 239.255.255.255) - Multicast";
        } else if (firstOctet >= 240 && firstOctet <= 255) {
            return "E (240.0.0.0 to 255.255.255.255) - Reserved";
        } else {
            return "Unknown";
        }
    }
    
    private boolean isPrivateIPv4(int[] octets) {
        // 10.0.0.0/8 - Class A private
        if (octets[0] == 10) {
            return true;
        }
        // 172.16.0.0/12 - Class B private
        if (octets[0] == 172 && octets[1] >= 16 && octets[1] <= 31) {
            return true;
        }
        // 192.168.0.0/16 - Class C private
        if (octets[0] == 192 && octets[1] == 168) {
            return true;
        }
        // 127.0.0.0/8 - Loopback
        if (octets[0] == 127) {
            return true;
        }
        return false;
    }
    
    private boolean isValidIPv4(String ip) {
        return ipv4Pattern.matcher(ip).matches();
    }
    
    private boolean isValidIPv6(String ip) {
        // This is a simplified IPv6 validation
        // For production use, consider using InetAddress.getByName() with try-catch
        return ipv6Pattern.matcher(ip).matches() || ip.equals("::1") || ip.equals("::");
    }
    
    private void showResult(String result) {
        resultArea.setText(result);
        resultArea.setCaretPosition(0); // Scroll to top
    }
    
    private void clearFields() {
        ipInputField.setText("");
        resultArea.setText("");
        ipInputField.requestFocus();
    }
    
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // If system look and feel is not available, use default
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            } catch (Exception ex) {
                // Use default look and feel
            }
        }
        
        // Create and display the application
        SwingUtilities.invokeLater(() -> {
            new IPCheckerApp().setVisible(true);
        });
    }
}