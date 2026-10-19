# QuickChat

QuickChat is a Java console application where a registered user can log in and send short messages. It was built for the PROG5121 POE (Part 2: Sending Messages) using Apache NetBeans and Maven.

**Student number:** ST10240259

## Features

- User registration and login (from Part 1). Messages can only be sent after a successful login.
- Welcome message: "Welcome to QuickChat."
- Numeric menu:
  1. Send Messages
  2. Show recently sent messages (displays "Coming Soon.")
  3. Quit
- The user chooses how many messages they want to enter when the app starts. The app only allows that many messages.
- Each message has:
  - A randomly generated ten-digit message ID
  - A message number that increases as messages are sent
  - A recipient cell number (must start with an international code, for example `+27718693002`)
  - A message of no more than 250 characters
  - An automatically generated message hash, for example `00:0:HITONIGHT`
- After writing a message, the user can **send** it, **disregard** it, or **store** it to send later.
- Stored messages are saved in a JSON file (`messages.json`).
- The total number of messages sent is shown at the end.

## Project structure

| File | Purpose |
|------|---------|
| `Login.java` | Registration and login logic (Part 1) |
| `QuickChat.java` | Main program: registration, login, menu and message flow |
| `Message.java` | Message class: validation, hash, send/store/disregard, JSON storage |
| `MessageTest.java` | JUnit unit tests |
| `.github/workflows/maven.yml` | GitHub Actions workflow that runs the tests automatically |

## Message class methods

| Method | What it does |
|--------|--------------|
| `checkMessageID()` | Checks the message ID is not more than ten characters |
| `checkRecipientCell()` | Checks the cell number has an international code and is correctly formatted |
| `createMessageHash()` | Creates the hash: first two digits of the ID, the message number, and the first and last words of the message in capitals |
| `sentMessage()` | Lets the user send, disregard or store the message |
| `printMessages()` | Returns all messages sent while the program is running |
| `returnTotalMessages()` | Returns the total number of messages sent |
| `storeMessage()` | Saves stored messages to a JSON file |

## How to run

1. Open the project in Apache NetBeans.
2. Run `QuickChat.java` (right-click the file and choose **Run File**).
3. Register, then log in with the same details.
4. Follow the menu in the Output window.

## How to run the unit tests

In NetBeans, right-click the **QuickChat** project and choose **Test**.

The tests cover:
- Message length (success and failure)
- Recipient number format (success and failure)
- Message hash (Test Case 1 and the remaining hashes in a loop)
- Message ID creation
- Send, disregard and store options
- Total number of messages sent

## Test data

| | Message 1 | Message 2 |
|---|---|---|
| Recipient | +27718693002 | 08575975889 |
| Message | Hi Mike, can you join us for dinner tonight? | Hi Keegan, did you receive the payment? |
| Option chosen | Send | Disregard |

## Continuous Integration

GitHub Actions runs the unit tests every time code is pushed. Open the **Actions** tab on GitHub to see the results.

## Requirements

- Java 21
- Apache Maven
- JUnit 5
