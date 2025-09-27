package com.ipchecker;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * IPCheckerCLI - Command line interface for testing the IP checking functionality
 */
public class IPCheckerCLI {
    // IPv4 regex pattern
    private static final String IPV4_PATTERN = 
        "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    
    // IPv6 regex pattern (simplified)
    private static final String IPV6_PATTERN = 
        "^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$|^::1$|^::$";
    
    private static final Pattern ipv4Pattern = Pattern.compile(IPV4_PATTERN);
    private static final Pattern ipv6Pattern = Pattern.compile(IPV6_PATTERN);
    
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java com.ipchecker.IPCheckerCLI <ip_address>");
            System.out.println("Example: java com.ipchecker.IPCheckerCLI 192.168.1.1");
            return;
        }
        
        String ip = args[0];
        checkIPAddress(ip);
    }
    
    public static void checkIPAddress(String ip) {
        System.out.println("IP Address: " + ip);
        System.out.println("==================================================\n");
        
        // Validate IP format
        boolean isValidIPv4 = isValidIPv4(ip);
        boolean isValidIPv6 = isValidIPv6(ip);
        
        if (!isValidIPv4 && !isValidIPv6) {
            System.out.println("❌ Invalid IP Address Format");
            System.out.println("Please enter a valid IPv4 or IPv6 address.\n");
            System.out.println("Examples:");
            System.out.println("IPv4: 192.168.1.1, 8.8.8.8, 127.0.0.1");
            System.out.println("IPv6: 2001:0db8:85a3:0000:0000:8a2e:0370:7334, ::1");
            return;
        }
        
        System.out.println("✅ Valid IP Address\n");
        
        if (isValidIPv4) {
            analyzeIPv4(ip);
        } else if (isValidIPv6) {
            analyzeIPv6(ip);
        }
        
        // Try to get hostname
        try {
            InetAddress inetAddr = InetAddress.getByName(ip);
            String hostname = inetAddr.getCanonicalHostName();
            if (!hostname.equals(ip)) {
                System.out.println("Hostname: " + hostname);
            }
        } catch (UnknownHostException e) {
            System.out.println("Hostname: Unable to resolve");
        }
    }
    
    private static void analyzeIPv4(String ip) {
        System.out.println("IP Version: IPv4");
        
        String[] parts = ip.split("\\.");
        int[] octets = new int[4];
        for (int i = 0; i < 4; i++) {
            octets[i] = Integer.parseInt(parts[i]);
        }
        
        // Determine IP class
        String ipClass = getIPv4Class(octets[0]);
        System.out.println("IP Class: " + ipClass);
        
        // Check if private or public
        boolean isPrivate = isPrivateIPv4(octets);
        System.out.println("Type: " + (isPrivate ? "Private" : "Public"));
        
        // Special addresses
        if (ip.equals("127.0.0.1")) {
            System.out.println("Special: Localhost (Loopback)");
        } else if (ip.startsWith("169.254.")) {
            System.out.println("Special: Link-Local Address (APIPA)");
        } else if (ip.startsWith("224.") || ip.startsWith("225.") || ip.startsWith("226.") || 
                  ip.startsWith("227.") || ip.startsWith("228.") || ip.startsWith("229.") ||
                  ip.startsWith("230.") || ip.startsWith("231.") || ip.startsWith("232.") ||
                  ip.startsWith("233.") || ip.startsWith("234.") || ip.startsWith("235.") ||
                  ip.startsWith("236.") || ip.startsWith("237.") || ip.startsWith("238.") ||
                  ip.startsWith("239.")) {
            System.out.println("Special: Multicast Address");
        }
        
        System.out.println("\nBinary Representation:");
        for (int i = 0; i < 4; i++) {
            System.out.print(String.format("%8s", Integer.toBinaryString(octets[i])).replace(' ', '0'));
            if (i < 3) System.out.print(".");
        }
        System.out.println();
    }
    
    private static void analyzeIPv6(String ip) {
        System.out.println("IP Version: IPv6");
        
        if (ip.equals("::1")) {
            System.out.println("Special: Localhost (Loopback)");
        } else if (ip.startsWith("fe80:")) {
            System.out.println("Special: Link-Local Address");
        } else if (ip.startsWith("fc00:") || ip.startsWith("fd00:")) {
            System.out.println("Type: Private (Unique Local)");
        } else if (ip.startsWith("ff00:")) {
            System.out.println("Special: Multicast Address");
        } else {
            System.out.println("Type: Global Unicast (Public)");
        }
        
        System.out.println();
    }
    
    private static String getIPv4Class(int firstOctet) {
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
    
    private static boolean isPrivateIPv4(int[] octets) {
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
    
    private static boolean isValidIPv4(String ip) {
        return ipv4Pattern.matcher(ip).matches();
    }
    
    private static boolean isValidIPv6(String ip) {
        // This is a simplified IPv6 validation
        // For production use, consider using InetAddress.getByName() with try-catch
        return ipv6Pattern.matcher(ip).matches() || ip.equals("::1") || ip.equals("::");
    }
}