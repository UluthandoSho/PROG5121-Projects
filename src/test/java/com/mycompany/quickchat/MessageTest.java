package com.mycompany.quickchat;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageTest {

    private static final String MSG1 = "Hi Mike, can you join us for dinner tonight?";
    private static final String MSG2 = "Hi Keegan, did you receive the payment?";

    @BeforeEach
    void setUp() {
        Message.resetAll();
    }

    @Test
    void messageLengthSuccess() {
        Message m = new Message("0012345678", 0, "+27718693002", MSG1);
        assertEquals("Message ready to send.", m.checkMessageLength());
    }

    @Test
    void messageLengthFailure() {
        String longText = "a".repeat(260);
        Message m = new Message("0012345678", 0, "+27718693002", longText);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                m.checkMessageLength());
    }

    @Test
    void recipientSuccess() {
        Message m = new Message("0012345678", 0, "+27718693002", MSG1);
        assertEquals("Cell phone number successfully captured.", m.checkRecipientCell());
    }

    @Test
    void recipientFailure() {
        Message m = new Message("0412345678", 1, "08575975889", MSG2);
        assertEquals("Cell phone number is incorrectly formatted or does not contain "
                + "an international code. Please correct the number and try again.",
                m.checkRecipientCell());
    }

    @Test
    void messageHashTestCase1() {
        Message m = new Message("0012345678", 0, "+27718693002", MSG1);
        assertEquals("00:0:HITONIGHT", m.createMessageHash());
    }

    @Test
    void messageHashesInLoop() {
        String[] ids = {"0012345678", "0412345678"};
        int[] numbers = {0, 1};
        String[] texts = {MSG1, MSG2};
        String[] expected = {"00:0:HITONIGHT", "04:1:HIPAYMENT"};

        for (int i = 0; i < texts.length; i++) {
            Message m = new Message(ids[i], numbers[i], "+27718693002", texts[i]);
            assertEquals(expected[i], m.createMessageHash());
        }
    }

    @Test
    void messageIDCreated() {
        Message m = new Message("+27718693002", MSG1);
        assertTrue(m.checkMessageID());
        assertEquals(10, m.getMessageID().length());
        assertEquals("Message ID generated: " + m.getMessageID(), m.messageIDGenerated());
    }

    @Test
    void messageSentOptions() {
        Message m = new Message("0012345678", 0, "+27718693002", MSG1);
        assertEquals("Message successfully sent.", m.sentMessage(1));
        assertEquals("Press 0 to delete the message.", m.sentMessage(2));
        assertEquals("Message successfully stored.", m.sentMessage(3));
    }

    @Test
    void totalMessagesSent() {
        Message m1 = new Message("0012345678", 0, "+27718693002", MSG1);
        Message m2 = new Message("0412345678", 1, "08575975889", MSG2);
        m1.sentMessage(1);   // Test data 1: Send
        m2.sentMessage(2);   // Test data 2: Discard
        assertEquals(1, Message.returnTotalMessages());
    }
}