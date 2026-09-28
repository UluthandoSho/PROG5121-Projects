/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import com.mycompany.quickchat.Login;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// LoginTest checks username format, password complexity, cell phone regex,
// registration messages, and login authentication with the assignment test data.
/**
 *
 * @author ST10240259
 */
public class LoginTest {

    private Login account;

    @BeforeEach
    public void setUp() {
        account = new Login("Kyle", "Smith");
        account.setUsername("kyl_1");
        account.setPassword("Ch&&sec@ke99!");
        account.setCellPhoneNumber("+27838968976");
    }

    @Test
    public void testUsernameCorrectlyFormattedMessage() {
        account.setTypedUsername("kyl_1");
        account.setTypedPassword("Ch&&sec@ke99!");

        assertEquals("Welcome Kyle, Smith it is great to see you again.", account.returnLoginStatus());
    }

    @Test
    public void testUsernameSuccessfullyCaptured() {
        assertEquals("Username successfully captured.", account.usernameStatus());
    }

    @Test
    public void testUsernameIncorrectlyFormattedMessage() {
        account.setUsername("kyle!!!!!!!");

        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.", account.usernameStatus());
    }

    @Test
    public void testPasswordMeetsComplexityMessage() {
        assertEquals("Password successfully captured.", account.passwordStatus());
    }

    @Test
    public void testPasswordDoesNotMeetComplexityMessage() {
        account.setPassword("password");

        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.", account.passwordStatus());
    }

    @Test
    public void testCellPhoneCorrectlyFormattedMessage() {
        assertEquals("Cell number successfully captured.", account.cellPhoneStatus());
    }

    @Test
    public void testCellPhoneIncorrectlyFormattedMessage() {
        account.setCellPhoneNumber("08966553");

        assertEquals("Cell number is incorrectly formatted or does not contain an international code, please correct the number and try again.", account.cellPhoneStatus());
    }

    @Test
    public void testCellPhoneNumberSuccessfullyAdded() {
        assertEquals("Cell phone number successfully added.", account.cellPhoneReply());
    }

    @Test
    public void testCellPhoneNumberIncorrectlyFormattedRegistrationMessage() {
        account.setCellPhoneNumber("08966553");

        assertEquals("Cell phone number incorrectly formatted or does not contain international code.", account.cellPhoneReply());
    }

    @Test
    public void testLoginSuccessful() {
        account.setTypedUsername("kyl_1");
        account.setTypedPassword("Ch&&sec@ke99!");

        assertTrue(account.loginUser());
    }

    @Test
    public void testLoginFailed() {
        account.setTypedUsername("kyl_1");
        account.setTypedPassword("wrongPass1!");

        assertFalse(account.loginUser());
    }

    @Test
    public void testLoginSuccessfulMessage() {
        account.setTypedUsername("kyl_1");
        account.setTypedPassword("Ch&&sec@ke99!");

        assertEquals("Welcome Kyle, Smith it is great to see you again.", account.returnLoginStatus());
    }

    @Test
    public void testLoginFailedMessage() {
        account.setTypedUsername("kyl_1");
        account.setTypedPassword("wrongPass1!");

        assertEquals("Username or password incorrect, please try again.", account.returnLoginStatus());
    }

    @Test
    public void testUsernameCorrectlyFormatted() {
        assertTrue(account.checkUserName());
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        account.setUsername("kyle!!!!!!!");

        assertFalse(account.checkUserName());
    }

    @Test
    public void testPasswordMeetsComplexityRequirements() {
        assertTrue(account.checkPasswordComplexity());
    }

    @Test
    public void testPasswordDoesNotMeetComplexityRequirements() {
        account.setPassword("password");

        assertFalse(account.checkPasswordComplexity());
    }

    @Test
    public void testCellPhoneNumberCorrectlyFormatted() {
        assertTrue(account.checkCellPhoneNumber());
    }

    @Test
    public void testCellPhoneNumberIncorrectlyFormatted() {
        account.setCellPhoneNumber("08966553");

        assertFalse(account.checkCellPhoneNumber());
    }

    @Test
    public void testRegisterUserUsernameIncorrect() {
        account.setUsername("kyle!!!!!!!");

        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.", account.registerUser());
    }

    @Test
    public void testRegisterUserPasswordIncorrect() {
        account.setPassword("password");

        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.", account.registerUser());
    }

    @Test
    public void testRegisterUserCellPhoneIncorrect() {
        account.setCellPhoneNumber("08966553");

        assertEquals("Cell number is incorrectly formatted or does not contain an international code, please correct the number and try again.", account.registerUser());
    }

    @Test
    public void testRegisterUserSuccess() {
        assertEquals("The user has been registered successfully.", account.registerUser());
    }
}
