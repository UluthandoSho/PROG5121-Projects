# QuickChat

QuickChat is a Java console application that allows registered users to log in, send messages and manage stored messages. The application was developed for the PROG5121 POE (Parts 1–3) using Apache NetBeans and Maven.

**Student number:** ST10240259

## Features

- User registration and login (Part 1). Users must log in successfully before they can send messages.
- Username validation: must contain an underscore and be no longer than five characters.
- Password validation: must contain at least eight characters, including a capital letter, a number and a special character.
- Cell phone validation: the number must start with the South African country code (+27) and is checked using a regular expression.
- Menu options:
  1. Send Messages
  2. Show recently sent messages (displays "Coming Soon.")
  3. Quit
  4. Stored Messages (Part 3)
- Each message includes a randomly generated ten-digit message ID, an incrementing message number, a recipient cell number, message text limited to 250 characters and an automatically generated message hash (for example, `00:0:HITONIGHT`).
- After creating a message, users can choose to send, disregard or store it.
- Stored messages are saved in a `messages.json` file.

## Part 3: Arrays and Stored Messages

The application uses five arrays to manage message information while it runs. The arrays are populated dynamically rather than using hard-coded message data.

| Array | Contents |
|---|---|
| Sent Messages | All messages that have been sent |
| Disregarded Messages | All messages that have been disregarded |
| Stored Messages | Messages loaded from the `messages.json` file |
| Message Hash | All generated message hashes |
| Message ID | All generated message IDs |

Menu option 4 (**Stored Messages**) allows users to:

1. Display the sender and recipient of all stored messages.
2. Display the longest stored message.
3. Search for a message using its message ID and display the recipient and message.
4. Search for all messages stored for a specific recipient.
5. Delete a stored message using its message hash.
6. Display a report containing the full details of all stored messages.

## Project Structure

| File | Purpose |
|---|---|
| `Login.java` | Handles user registration and login (Part 1). |
| `QuickChat.java` | Main application containing registration, login and menu options. |
| `Message.java` | Handles message validation, hash generation, sending, storing, disregarding, arrays, JSON storage and Stored Messages functionality. |
| `LoginTest.java` | JUnit tests for the Login class (Part 1). |
| `MessageTest.java` | JUnit tests for sending messages (Part 2). |
| `MessagePart3Test.java` | JUnit tests for the arrays and Stored Messages functionality (Part 3). |
| `.github/workflows/maven.yml` | GitHub Actions workflow that automatically runs the tests. |

## How to Run

1. Open the project in Apache NetBeans.
2. Locate `QuickChat.java`.
3. Right-click the file and select **Run File**.
4. Register a new user.
5. Log in using the same registration details.
6. Follow the menu instructions in the Output window.

## How to Run the Unit Tests

1. Open the project in Apache NetBeans.
2. Right-click the **QuickChat** project.
3. Select **Test**.
4. Wait for Maven to execute the tests.

**Expected result:** All 40 unit tests should pass.

## Part 3 Test Data

The following test data is used to demonstrate the different message statuses:

| Message | Recipient | Message Text | Flag |
|---|---|---|---|
| 1 | +27834557896 | Did you get the cake? | Sent |
| 2 | +27838884567 | Where are you? You are late! I have asked you to be on time. | Stored |
| 3 | +27834484567 | Yohoooo, I am at your gate. | Disregard |
| 4 | 0838884567 | It is dinner time! | Sent |
| 5 | +27838884567 | Ok, I am leaving without you. | Stored |

## Continuous Integration

QuickChat uses GitHub Actions to automatically run the unit tests whenever code is pushed to the repository.

To check the test results:

1. Open the project repository on GitHub.
2. Select the **Actions** tab.
3. Open the latest workflow run.
4. Check whether the tests completed successfully.

## Requirements

- Java 21
- Apache Maven
- JUnit 5
