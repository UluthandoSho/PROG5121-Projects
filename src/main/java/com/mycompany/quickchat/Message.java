package com.mycompany.quickchat;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Message {

    private static final int MAX_LENGTH = 250;
    private static final String JSON_FILE = "messages.json";
    private static final Random RANDOM = new Random();

    // Shared lists and counter (live while the program runs)
    private static final List<Message> sentMessages = new ArrayList<>();
    private static final List<Message> storedMessages = new ArrayList<>();
    private static int totalMessages = 0;

    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;

    
    public Message(String recipient, String messageText) {
        this(generateMessageID(), totalMessages, recipient, messageText);
    }

    // Used by unit tests (lets us supply a known ID and number)
    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    // Random ten-digit number
    private static String generateMessageID() {
        long number = Math.floorMod(RANDOM.nextLong(), 10_000_000_000L);
        return String.format("%010d", number);
    }

    // Message ID must not be more than ten characters
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    public String messageIDGenerated() {
        return "Message ID generated: " + messageID;
    }

   
    public static boolean isValidCell(String cell) {
        return cell != null && cell.matches("^\\+\\d{2,13}$");
    }

    public String checkRecipientCell() {
        if (isValidCell(recipient)) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain "
                + "an international code. Please correct the number and try again.";
    }

    public String checkMessageLength() {
        int length = messageText.length();
        if (length <= MAX_LENGTH) {
            return "Message ready to send.";
        }
        return "Message exceeds 250 characters by " + (length - MAX_LENGTH)
                + "; please reduce the size.";
    }

    // First 2 digits of ID : message number : FIRSTLAST (all caps)
    public String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");
        String first = words[0].replaceAll("[^A-Za-z0-9]", "");
        String last = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");
        return (messageID.substring(0, 2) + ":" + messageNumber + ":" + first + last)
                .toUpperCase();
    }

    // 1 = send, 2 = disregard, 3 = store
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                totalMessages++;
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                storedMessages.add(this);
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    // Order: Message ID, Message Hash, Recipient, Message
    public String details() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText;
    }

    // All messages sent while the program is running
    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append(m.details()).append("\n\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

    // Saves all stored messages to messages.json
    public boolean storeMessage() {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            json.append(storedMessages.get(i).toJson());
            json.append(i < storedMessages.size() - 1 ? ",\n" : "\n");
        }
        json.append("]");
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            writer.write(json.toString());
            return true;
        } catch (IOException e) {
            System.out.println("Could not write JSON file: " + e.getMessage());
            return false;
        }
    }

    private String toJson() {
        return "  {\"messageID\": \"" + escape(messageID) + "\", "
                + "\"messageHash\": \"" + escape(messageHash) + "\", "
                + "\"recipient\": \"" + escape(recipient) + "\", "
                + "\"message\": \"" + escape(messageText) + "\"}";
    }

    private static String escape(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }

    // For tests: clear everything between runs
    public static void resetAll() {
        sentMessages.clear();
        storedMessages.clear();
        totalMessages = 0;
    }

    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
}