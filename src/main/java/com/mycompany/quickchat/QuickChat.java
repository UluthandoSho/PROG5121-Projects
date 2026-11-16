/* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.quickchat;

import java.util.Scanner;

/**
 *
 * @author ST10240259
 */
public class QuickChat {

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        Login account = new Login();

        System.out.println("QuickChat registration");
        System.out.println();

        System.out.print("Please enter your first name: ");
        account.setFirstName(keyboard.nextLine());

        System.out.print("Please enter your last name: ");
        account.setLastName(keyboard.nextLine());

        // Username must contain an underscore and be no more than five characters long
        boolean usernameValid = false;
        while (usernameValid == false) {
            System.out.print("Please enter a username: ");
            account.setUsername(keyboard.nextLine());

            if (account.checkUserName() == true) {
                System.out.println(account.usernameStatus());
                usernameValid = true;
            } else {
                System.out.println(account.usernameStatus());
            }
        }

        // Password must be eight characters long and include a capital letter, a number, and a special character
        boolean passwordValid = false;
        while (passwordValid == false) {
            System.out.print("Please enter a password: ");
            account.setPassword(keyboard.nextLine());

            if (account.checkPasswordComplexity() == true) {
                System.out.println(account.passwordStatus());
                passwordValid = true;
            } else {
                System.out.println(account.passwordStatus());
            }
        }

        // Cell phone number must include the +27 country code and a number no more than ten digits long
        boolean cellValid = false;
        while (cellValid == false) {
            System.out.print("Please enter a South African cell phone number, for example +27838968976: ");
            account.setCellPhoneNumber(keyboard.nextLine());

            if (account.checkCellPhoneNumber() == true) {
                System.out.println(account.cellPhoneReply());
                cellValid = true;
            } else {
                System.out.println(account.cellPhoneReply());
            }
        }

        System.out.println();
        System.out.println(account.registerUser());
        System.out.println();
        System.out.println("QuickChat login");
        System.out.println();

        // Decision structure that verifies authentication before allowing access
        boolean accessGranted = false;
        while (accessGranted == false) {
            System.out.print("Please enter your username: ");
            account.setTypedUsername(keyboard.nextLine());

            System.out.print("Please enter your password: ");
            account.setTypedPassword(keyboard.nextLine());

            if (account.loginUser() == true) {
                System.out.println(account.returnLoginStatus());
                accessGranted = true;
            } else {
                System.out.println(account.returnLoginStatus());
            }
        }
        runMessaging(keyboard);
        keyboard.close();
    }
    // Part 2: Sending messages. Part 3: Stored Messages (option 4)
    private static void runMessaging(Scanner keyboard) {
        System.out.println("Welcome to QuickChat.");
        int maxMessages = readInt(keyboard, "How many messages do you wish to enter? ");
        int entered = 0;
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            int option = readInt(keyboard, "Choose an option: ");

            if (option == 1) {
                if (entered >= maxMessages) {
                    System.out.println("You have reached your limit of " + maxMessages + " messages.");
                } else if (captureMessage(keyboard)) {
                    entered++;
                    if (entered == maxMessages) {
                        System.out.println("Total messages sent: " + Message.returnTotalMessages());
                    }
                }
            } else if (option == 2) {
                System.out.println("Coming Soon.");
            } else if (option == 3) {
                running = false;
            } else if (option == 4) {
                // Part 3: opens the Stored Messages menu (display, search, delete and report)
                Message.storedMessagesMenu(keyboard);
            } else {
                System.out.println("Please choose 1, 2, 3 or 4.");
            }
        }

        System.out.println();
        System.out.println("Total messages sent: " + Message.returnTotalMessages());
        System.out.println(Message.printMessages());
    }

    // Captures one message. Returns true if it was sent or stored.
    private static boolean captureMessage(Scanner keyboard) {
        String recipient;
        do {
            System.out.print("Recipient cell number (e.g. +27718693002): ");
            recipient = keyboard.nextLine().trim();
            if (!Message.isValidCell(recipient)) {
                System.out.println("Cell phone number is incorrectly formatted or does not contain "
                        + "an international code. Please correct the number and try again.");
            }
        } while (!Message.isValidCell(recipient));
        System.out.println("Cell phone number successfully captured.");

        String text;
        while (true) {
            System.out.print("Enter your message: ");
            text = keyboard.nextLine().trim();
            if (text.isEmpty()) {
                System.out.println("Message cannot be empty.");
            } else if (text.length() > 250) {
                System.out.println("Please enter a message of less than 250 characters.");
            } else {
                break;
            }
        }
        System.out.println("Message ready to send.");

        Message message = new Message(recipient, text);
        System.out.println(message.messageIDGenerated());

        System.out.println();
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        int choice = readInt(keyboard, "Choose an option: ");
        System.out.println(message.sentMessage(choice));

        switch (choice) {
            case 1:
                System.out.println();
                System.out.println(message.details());
                return true;
            case 2:
                int del = readInt(keyboard, "");
                System.out.println(del == 0 ? "Message deleted." : "Message was not sent.");
                return false;
            case 3:
                return true;
            default:
                break;
        }
        return false;
    }

    // Keeps asking until the user types a whole number
    private static int readInt(Scanner keyboard, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(keyboard.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }
}