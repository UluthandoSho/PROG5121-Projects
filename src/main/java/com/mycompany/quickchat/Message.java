package com.mycompany.quickchat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Message represents one chat message in the QuickChat app.
 * It creates the message ID and hash, checks the recipient and length,
 * sends, disregards or stores the message, and (Part 3) keeps the arrays
 * that let the user view, search, delete and report on stored messages.
 */
public class Message {

    // The longest message text allowed (in characters)
    private static final int MAX_LENGTH = 250;

    // The file where stored messages are saved and read from
    private static final String JSON_FILE = "messages.json";

    // The name shown as the sender when displaying stored messages
    private static final String SENDER = "You";

    // Used to generate random message IDs
    private static final Random RANDOM = new Random();

    // Regular expression used to read each message object from the JSON file.
    // It captures four values: messageID, messageHash, recipient and message.
    // Oracle. n.d. Pattern (Java SE documentation).
    // Available at: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html
    // [Accessed 14 November 2026].
    private static final Pattern STORED_ENTRY = Pattern.compile(
            "\"messageID\":\\s*\"((?:[^\"\\\\]|\\\\.)*)\",\\s*"
            + "\"messageHash\":\\s*\"((?:[^\"\\\\]|\\\\.)*)\",\\s*"
            + "\"recipient\":\\s*\"((?:[^\"\\\\]|\\\\.)*)\",\\s*"
            + "\"message\":\\s*\"((?:[^\"\\\\]|\\\\.)*)\"");

    // ---- The five Part 3 arrays (filled while the program runs, no hard-coding) ----

    // Sent Messages array: every message the user chose to send
    private static final List<Message> sentMessages = new ArrayList<>();

    // Disregarded Messages array: every message the user chose to disregard
    private static final List<Message> disregardedMessages = new ArrayList<>();

    // Stored Messages array: every message the user chose to store (also saved to JSON)
    private static final List<Message> storedMessages = new ArrayList<>();

    // Message Hash array: the hash of every message
    private static final List<String> messageHashes = new ArrayList<>();

    // Message ID array: the ID of every message
    private static final List<String> messageIDs = new ArrayList<>();

    // Counts how many messages have been sent so far
    private static int totalMessages = 0;

    // ---- The details of one message ----
    private final String messageID;      // ten-digit ID
    private final int messageNumber;     // the number of the message in the session
    private final String recipient;      // the cell phone number it is sent to
    private final String messageText;    // what the message says
    private final String messageHash;    // short code built from the ID, number and words

    /**
     * Creates a new message with a random message ID.
     * The message number is the number of messages sent so far.
     */
    public Message(String recipient, String messageText) {
        this(generateMessageID(), totalMessages, recipient, messageText);
    }

    /**
     * Creates a message with a known ID and number.
     * This lets the unit tests use the exact test data from the task.
     */
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    /**
     * Creates a message that is read back from the JSON file.
     * It keeps the hash that was saved instead of building a new one.
     */
    private Message(String messageID, int messageNumber, String recipient, String messageText, String messageHash) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = messageHash;
    }

    /** Generates a random ten-digit number (padded with zeros) to use as the message ID. */
    private static String generateMessageID() {
        long number = Math.floorMod(RANDOM.nextLong(), 10_000_000_000L);
        return String.format("%010d", number);
    }

    /** Returns true if the message ID is not more than ten characters long. */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    /** Returns the message telling the user which ID was generated. */
    public String messageIDGenerated() {
        return "Message ID generated: " + messageID;
    }

    /**
     * Returns true if the cell number starts with + (the international code)
     * followed by only digits.
     */
    public static boolean isValidCell(String cell) {
        return cell != null && cell.matches("^\\+\\d{2,13}$");
    }

    /** Returns a success or error message about the recipient's cell number. */
    public String checkRecipientCell() {
        if (isValidCell(recipient)) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain "
                + "an international code. Please correct the number and try again.";
    }

    /** Checks the message is not longer than 250 characters and returns the matching message. */
    public String checkMessageLength() {
        int length = messageText.length();
        if (length <= MAX_LENGTH) {
            return "Message ready to send.";
        }
        // Tell the user how many characters they must remove
        return "Message exceeds 250 characters by " + (length - MAX_LENGTH)
                + "; please reduce the size.";
    }

    /**
     * Builds the message hash: the first 2 digits of the ID, then the message
     * number, then the first and last word of the message, all in capital letters.
     */
    public String createMessageHash() {
        // Split the message into words
        String[] words = messageText.trim().split("\\s+");
        // Remove punctuation from the first and last word
        String first = words[0].replaceAll("[^A-Za-z0-9]", "");
        String last = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");
        return (messageID.substring(0, 2) + ":" + messageNumber + ":" + first + last)
                .toUpperCase();
    }

    /**
     * Handles what the user wants to do with the message:
     * 1 = send, 2 = disregard, 3 = store.
     * The message is added to the matching array and its hash and ID are recorded.
     */
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                // Send: add to the Sent Messages array
                sentMessages.add(this);
                recordHashAndID();
                totalMessages++;
                return "Message successfully sent.";
            case 2:
                // Disregard: add to the Disregarded Messages array
                disregardedMessages.add(this);
                recordHashAndID();
                return "Press 0 to delete the message.";
            case 3:
                // Store: add to the Stored Messages array and save to the JSON file
                storedMessages.add(this);
                recordHashAndID();
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    /** Adds this message's hash and ID to the Message Hash and Message ID arrays (no duplicates). */
    private void recordHashAndID() {
        if (!messageHashes.contains(messageHash)) {
            messageHashes.add(messageHash);
        }
        if (!messageIDs.contains(messageID)) {
            messageIDs.add(messageID);
        }
    }

    /** Returns the full details of this message in the order: ID, hash, recipient, message. */
    public String details() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText;
    }

    /** Returns the details of all messages sent while the program is running. */
    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append(m.details()).append("\n\n");
        }
        return sb.toString();
    }

    /** Returns how many messages have been sent. */
    public static int returnTotalMessages() {
        return totalMessages;
    }

    // ---------------------------------------------------------------
    // Part 3: Stored Messages features
    // ---------------------------------------------------------------

    /** Returns the text of every message in the Sent Messages array. */
    public static List<String> getSentMessageTexts() {
        List<String> texts = new ArrayList<>();
        for (Message m : sentMessages) {
            texts.add(m.messageText);
        }
        return texts;
    }

    /** Returns the text of every message in the Disregarded Messages array. */
    public static List<String> getDisregardedMessageTexts() {
        List<String> texts = new ArrayList<>();
        for (Message m : disregardedMessages) {
            texts.add(m.messageText);
        }
        return texts;
    }

    /** Returns a copy of the Message Hash array. */
    public static List<String> getMessageHashes() {
        return new ArrayList<>(messageHashes);
    }

    /** Returns a copy of the Message ID array. */
    public static List<String> getMessageIDs() {
        return new ArrayList<>(messageIDs);
    }

    /**
     * Reads messages.json and fills the Stored Messages array with
     * the messages saved in the file.
     */
    public static void loadStoredMessages() {
        // Start with an empty array so messages are not loaded twice
        storedMessages.clear();
        File file = new File(JSON_FILE);
        if (!file.exists()) {
            return; // nothing has been stored yet
        }
        try {
            // Read the whole file into one text value
            String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
            // Find every message object in the text
            Matcher matcher = STORED_ENTRY.matcher(content);
            while (matcher.find()) {
                String id = unescape(matcher.group(1));
                String hash = unescape(matcher.group(2));
                String recipient = unescape(matcher.group(3));
                String text = unescape(matcher.group(4));
                // Rebuild the message and add it to the Stored Messages array
                Message loaded = new Message(id, numberFromHash(hash), recipient, text, hash);
                storedMessages.add(loaded);
                loaded.recordHashAndID();
            }
        } catch (IOException e) {
            System.out.println("Could not read JSON file: " + e.getMessage());
        }
    }

    /** a. Returns the sender and recipient of all stored messages. */
    public static String storedSenderAndRecipient() {
        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : storedMessages) {
            sb.append("Sender: ").append(SENDER)
                    .append(" | Recipient: ").append(m.recipient).append("\n");
        }
        return sb.toString();
    }

    /** b. Returns the longest stored message (the one with the most characters). */
    public static String longestStoredMessage() {
        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }
        // Start with the first message, then replace it whenever a longer one is found
        Message longest = storedMessages.get(0);
        for (Message m : storedMessages) {
            if (m.messageText.length() > longest.messageText.length()) {
                longest = m;
            }
        }
        return longest.messageText;
    }

    /** Looks for a sent or stored message with this ID. Returns null if there is none. */
    public static Message findByID(String id) {
        for (Message m : sentMessages) {
            if (m.messageID.equals(id)) {
                return m;
            }
        }
        for (Message m : storedMessages) {
            if (m.messageID.equals(id)) {
                return m;
            }
        }
        return null;
    }

    /** c. Searches for a message ID and returns the matching message text. */
    public static String searchByMessageID(String id) {
        Message found = findByID(id);
        if (found == null) {
            return "Message ID not found.";
        }
        return found.messageText;
    }

    /** d. Returns the text of all sent and stored messages for one recipient. */
    public static List<String> messagesForRecipient(String recipient) {
        List<String> results = new ArrayList<>();
        // Check the sent messages first
        for (Message m : sentMessages) {
            if (m.recipient.equals(recipient)) {
                results.add(m.messageText);
            }
        }
        // Then check the stored messages
        for (Message m : storedMessages) {
            if (m.recipient.equals(recipient)) {
                results.add(m.messageText);
            }
        }
        return results;
    }

    /**
     * e. Deletes a message using its message hash.
     * The message is removed from the arrays and the JSON file is updated.
     */
    public static String deleteByHash(String hash) {
        // Look for the message in the stored messages, then the sent messages
        Message target = null;
        for (Message m : storedMessages) {
            if (m.messageHash.equals(hash)) {
                target = m;
            }
        }
        if (target == null) {
            for (Message m : sentMessages) {
                if (m.messageHash.equals(hash)) {
                    target = m;
                }
            }
        }
        if (target == null) {
            return "Message hash not found.";
        }

        // Remove the message from every array it is in
        boolean wasStored = storedMessages.remove(target);
        sentMessages.remove(target);
        messageHashes.remove(target.messageHash);
        messageIDs.remove(target.messageID);
        if (wasStored) {
            storeMessage(); // update messages.json so the deleted message is gone
        }

        // The task's expected output shows the text without its final full stop
        String shown = target.messageText;
        if (shown.endsWith(".")) {
            shown = shown.substring(0, shown.length() - 1);
        }
        return "Message: \"" + shown + "\" successfully deleted.";
    }

    /** f. Returns a report with the full details of all stored messages. */
    public static String storedReport() {
        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : storedMessages) {
            sb.append(m.details()).append("\n\n");
        }
        return sb.toString();
    }

    /**
     * Main menu option 4 ("Stored Messages").
     * Shows the Stored Messages menu and keeps running until the user enters 0.
     */
    public static void storedMessagesMenu(Scanner input) {
        // Read the saved messages from the JSON file into the array first
        loadStoredMessages();
        int choice = -1;
        while (choice != 0) {
            System.out.println("\n=== STORED MESSAGES ===");
            System.out.println("1. Display sender and recipient of all stored messages");
            System.out.println("2. Display the longest stored message");
            System.out.println("3. Search for a message ID");
            System.out.println("4. Search for all messages for a recipient");
            System.out.println("5. Delete a message using its hash");
            System.out.println("6. Display report of all stored messages");
            System.out.println("0. Back to main menu");
            System.out.print("Choose an option: ");

            // Read the choice; if the user did not type a number, treat it as invalid
            try {
                choice = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                choice = -1;
            }

            switch (choice) {
                case 1:
                    System.out.println(storedSenderAndRecipient());
                    break;
                case 2:
                    System.out.println(longestStoredMessage());
                    break;
                case 3:
                    // Search by ID and show both the recipient and the message
                    System.out.print("Enter the message ID: ");
                    String id = input.nextLine().trim();
                    Message found = findByID(id);
                    if (found == null) {
                        System.out.println("Message ID not found.");
                    } else {
                        System.out.println("Recipient: " + found.recipient);
                        System.out.println("Message: " + found.messageText);
                    }
                    break;
                case 4:
                    // Show every message for the recipient the user types in
                    System.out.print("Enter the recipient number: ");
                    List<String> results = messagesForRecipient(input.nextLine().trim());
                    if (results.isEmpty()) {
                        System.out.println("No messages found for that recipient.");
                    } else {
                        for (String text : results) {
                            System.out.println(text);
                        }
                    }
                    break;
                case 5:
                    // Hashes are stored in capital letters, so convert the input
                    System.out.print("Enter the message hash: ");
                    System.out.println(deleteByHash(input.nextLine().trim().toUpperCase()));
                    break;
                case 6:
                    System.out.println(storedReport());
                    break;
                case 0:
                    break; // leave the menu
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ---------------------------------------------------------------
    // JSON storage
    // ---------------------------------------------------------------

    /** Saves all stored messages to messages.json. Returns true if the file was written. */
    public static boolean storeMessage() {
        // Build the JSON text: a list of message objects separated by commas
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            json.append(storedMessages.get(i).toJson());
            json.append(i < storedMessages.size() - 1 ? ",\n" : "\n");
        }
        json.append("]");
        // Write the text to the file (it is closed automatically)
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            writer.write(json.toString());
            return true;
        } catch (IOException e) {
            System.out.println("Could not write JSON file: " + e.getMessage());
            return false;
        }
    }

    /** Converts this message into one JSON object. */
    private String toJson() {
        return "  {\"messageID\": \"" + escape(messageID) + "\", "
                + "\"messageHash\": \"" + escape(messageHash) + "\", "
                + "\"recipient\": \"" + escape(recipient) + "\", "
                + "\"message\": \"" + escape(messageText) + "\"}";
    }

    /** Makes text safe for JSON by escaping backslashes, quotes and new lines. */
    private static String escape(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }

    /** Reverses escape(): turns \" back into " and \n back into a new line. */
    private static String unescape(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\\' && i + 1 < text.length()) {
                // A backslash means the next character was escaped
                char next = text.charAt(++i);
                sb.append(next == 'n' ? '\n' : next);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /** Gets the message number from the middle part of the hash (e.g. 00:1:HITONIGHT gives 1). */
    private static int numberFromHash(String hash) {
        String[] parts = hash.split(":");
        if (parts.length >= 2) {
            try {
                return Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    /** Clears all the arrays and the counter. Used by the unit tests between runs. */
    public static void resetAll() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        totalMessages = 0;
    }

    // Getters so other classes can read a message's details
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
}