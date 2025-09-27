# IP Address Checker - Java Swing Application

A comprehensive Java Swing application for checking, validating, and analyzing IP addresses (both IPv4 and IPv6).

## Features

- **IP Address Validation**: Validates both IPv4 and IPv6 address formats
- **IP Classification**: Determines IP address class (A, B, C, D, E) for IPv4 addresses
- **Network Type Detection**: Identifies whether an IP is public, private, loopback, or special-use
- **Binary Representation**: Shows binary representation of IPv4 addresses
- **Hostname Resolution**: Attempts to resolve IP addresses to hostnames
- **User-Friendly GUI**: Clean Swing interface with input validation and clear results display
- **Command Line Interface**: CLI version available for testing and automation

## Supported IP Address Types

### IPv4
- **Class A**: 1.0.0.0 to 126.255.255.255
- **Class B**: 128.0.0.0 to 191.255.255.255  
- **Class C**: 192.0.0.0 to 223.255.255.255
- **Class D**: 224.0.0.0 to 239.255.255.255 (Multicast)
- **Class E**: 240.0.0.0 to 255.255.255.255 (Reserved)

### IPv6
- Global Unicast (Public)
- Link-Local (fe80::/10)
- Unique Local (fc00::/7, fd00::/8)
- Multicast (ff00::/8)
- Loopback (::1)

### Special Addresses
- **Private IPv4**: 10.0.0.0/8, 172.16.0.0/12, 192.168.0.0/16
- **Loopback**: 127.0.0.1 (IPv4), ::1 (IPv6)
- **Link-Local**: 169.254.0.0/16 (IPv4), fe80::/10 (IPv6)
- **Multicast**: 224.0.0.0/4 (IPv4), ff00::/8 (IPv6)

## Requirements

- Java 8 or higher
- For GUI: Display environment (X11 on Linux, Windows/macOS native)

## Building and Running

### Build the Application
```bash
./build.sh
```

### Run GUI Application
```bash
./run.sh
```

### Run Command Line Interface
```bash
# Test a specific IP address
java -cp classes com.ipchecker.IPCheckerCLI <ip_address>

# Examples:
java -cp classes com.ipchecker.IPCheckerCLI 192.168.1.1
java -cp classes com.ipchecker.IPCheckerCLI 8.8.8.8
java -cp classes com.ipchecker.IPCheckerCLI ::1
```

## Manual Compilation and Execution

### Compile
```bash
mkdir -p classes
javac -d classes -sourcepath src/main/java src/main/java/com/ipchecker/IPCheckerApp.java
javac -d classes -sourcepath src/main/java src/main/java/com/ipchecker/IPCheckerCLI.java
```

### Run GUI
```bash
java -cp classes com.ipchecker.IPCheckerApp
```

### Run CLI
```bash
java -cp classes com.ipchecker.IPCheckerCLI <ip_address>
```

## Usage Examples

### Valid IPv4 Addresses
- `192.168.1.1` - Private Class C
- `8.8.8.8` - Public Class A (Google DNS)
- `127.0.0.1` - Localhost
- `10.0.0.1` - Private Class A

### Valid IPv6 Addresses  
- `::1` - IPv6 Localhost
- `2001:4860:4860::8888` - Public IPv6 (Google DNS)
- `fe80::1` - Link-Local

### Invalid Examples
- `256.256.256.256` - Invalid IPv4 (octets > 255)
- `192.168.1` - Incomplete IPv4
- `not.an.ip.address` - Invalid format

## GUI Features

- **Input Field**: Enter IP address to check
- **Check Button**: Validate and analyze the entered IP
- **Clear Button**: Clear input and results
- **Results Panel**: Displays comprehensive IP analysis including:
  - Validation status
  - IP version (IPv4/IPv6)
  - IP class (for IPv4)
  - Network type (Public/Private)
  - Special address types
  - Binary representation (for IPv4)
  - Hostname resolution (if available)

## Project Structure

```
project-03-javaswing-check-ip/
├── src/main/java/com/ipchecker/
│   ├── IPCheckerApp.java      # Main GUI application
│   └── IPCheckerCLI.java      # Command line interface
├── classes/                   # Compiled Java classes
├── build.sh                   # Build script
├── run.sh                     # Run script for GUI
└── README.md                  # This file
```

## Error Handling

The application includes comprehensive error handling for:
- Invalid IP address formats
- Network resolution failures
- GUI display issues
- Input validation

## Testing

The application has been tested with various IP address types:
- ✅ Valid IPv4 addresses (public, private, special)
- ✅ Valid IPv6 addresses (various types)
- ✅ Invalid IP address formats
- ✅ Edge cases and boundary conditions

## Notes

- In headless environments (servers without display), use the CLI version
- The GUI automatically attempts to use the system look and feel
- Binary representation is shown for IPv4 addresses in dotted binary format
- Hostname resolution may timeout or fail depending on network configuration
