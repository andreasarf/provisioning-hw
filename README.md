# Simple Provisioning HW

For context, the main purpose it to get provisioning file based on the device model:
- `DESK`: properties file
- `CONFERENCE`: JSON file

## Requirements
1. Java 21
2. Spring Boot 3.4.3
3. Maven

## Implementation
Basically, it's simple REST API/CRUD application.
1. The main logic to handle separate device model is using strategy pattern. Each device model has its own strategy to handle the provisioning file.
2. The JSON file is generated using `Jackson` library.
3. The properties file is generated using custom mapper.

---
&copy; 2025 Andreas
