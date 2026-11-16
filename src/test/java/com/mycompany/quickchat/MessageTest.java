package com.mycompany.quickchat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Part 3 unit tests (JUnit 5).
 * Each test uses the exact test data from the task and checks the result with assertEquals.
 */
public class MessageTest {

    // The five test messages from the task
    private Message m1;
    private Message m2;
    private Message m3;
    private Message m4;
    private Message m5;

    /**
     * Runs before every test. It clears the arrays and then creates
     * the five test messages with the flags given in the task.
     */
    @BeforeEach
    public void setUp() {
        Message.resetAll();

        // Test Data Messages 1 to 5 (ID, message number, recipient, message)
        m1 = new Message("1000000001", 1, "+27834557896", "Did you get the cake?");
        m2 = new Message("2000000002", 2, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        m3 = new Message("3000000003", 3, "+27834484567", "Yohoooo, I am at your gate.");
        m4 = new Message("0838884567", 4, "0838884567", "It is dinner time!");
        m5 = new Message("5000000005", 5, "+27838884567", "Ok, I am leaving without you.");

        // Apply each message's flag: 1 = Sent, 2 = Disregard, 3 = Stored
        m1.sentMessage(1);  // Sent
        m2.sentMessage(3);  // Stored
        m3.sentMessage(2);  // Disregard
        m4.sentMessage(1);  // Sent
        m5.sentMessage(3);  // Stored
    }

    /** The Sent Messages array must contain only messages 1 and 4. */
    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        assertEquals(Arrays.asList("Did you get the cake?", "It is dinner time!"),
                Message.getSentMessageTexts());
    }

    /** The Disregarded Messages array must contain only message 3. */
    @Test
    public void testDisregardedMessagesArrayCorrectlyPopulated() {
        assertEquals(Arrays.asList("Yohoooo, I am at your gate."),
                Message.getDisregardedMessageTexts());
    }

    /** The Message Hash and Message ID arrays must each hold all 5 messages. */
    @Test
    public void testMessageHashAndIDArraysPopulated() {
        assertEquals(5, Message.getMessageHashes().size());
        assertEquals(5, Message.getMessageIDs().size());
    }

    /** The longest stored message must be message 2. */
    @Test
    public void testDisplayLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                Message.longestStoredMessage());
    }

    /** Searching for message 4's ID must return "It is dinner time!". */
    @Test
    public void testSearchForMessageID() {
        assertEquals("It is dinner time!", Message.searchByMessageID("0838884567"));
    }

    /** Searching for +27838884567 must return messages 2 and 5. */
    @Test
    public void testSearchAllMessagesForRecipient() {
        assertEquals(Arrays.asList("Where are you? You are late! I have asked you to be on time.",
                "Ok, I am leaving without you."),
                Message.messagesForRecipient("+27838884567"));
    }

    /** Deleting message 2 by its hash must return the success message. */
    @Test
    public void testDeleteMessageUsingHash() {
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time\" successfully deleted.",
                Message.deleteByHash(m2.getMessageHash()));
    }

    /** The report must list the hash, recipient and message of every sent message. */
    @Test
    public void testDisplayReport() {
        // Build the report we expect for the two sent messages (1 and 4)
        String expected = "Message ID: 1000000001"
                + "\nMessage Hash: " + m1.getMessageHash()
                + "\nRecipient: +27834557896"
                + "\nMessage: Did you get the cake?\n\n"
                + "Message ID: 0838884567"
                + "\nMessage Hash: " + m4.getMessageHash()
                + "\nRecipient: 0838884567"
                + "\nMessage: It is dinner time!\n\n";
        assertEquals(expected, Message.printMessages());
    }
}