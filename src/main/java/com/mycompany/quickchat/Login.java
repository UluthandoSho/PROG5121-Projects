/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.quickchat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Login stores one user's registration details and checks the username format,
// password complexity, South African cell phone number, and login authentication.
/**
 *
 * @author ST10240259
 */
public class Login {

    // Details saved when the user registers
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellPhoneNumber;

    // Details typed when the user logs in
    private String typedUsername;
    private String typedPassword;

    // Regular expression compiled once and reused.
    // W3Schools. n.d. Java Regular Expressions.
    // Available at: https://www.w3schools.com/java/java_regex.asp
    // [Accessed 10 September 2026].
    // The pattern requires the South African country code (+27) followed by
    // a number that is no more than ten digits long.
    private static final Pattern SA_CELL_PATTERN = Pattern.compile("^\\+27[0-9]{1,10}$");

    public Login() {
    }

    public Login(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCellPhoneNumber(String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
    }

    public void setTypedUsername(String typedUsername) {
        this.typedUsername = typedUsername;
    }

    public void setTypedPassword(String typedPassword) {
        this.typedPassword = typedPassword;
    }

    // Returns true when the username contains an underscore and is no more than five characters long
    public boolean checkUserName() {
        if (username == null) {
            return false;
        }

        // indexOf() and length() are String methods from Farrell (2023, Chapter 7)
        if (username.indexOf('_') >= 0 && username.length() <= 5) {
            return true;
        } else {
            return false;
        }
    }

    // Returns true when the password is at least eight characters and has a capital letter, a number, and a special character
    public boolean checkPasswordComplexity() {
        if (password == null) {
            return false;
        }

        if (password.length() < 8) {
            return false;
        }

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        int position = 0;

        // Character methods from Farrell (2023, Chapter 7, Table 7-1)
        while (position < password.length()) {
            char current = password.charAt(position);

            if (Character.isUpperCase(current) == true) {
                hasCapital = true;
            }

            if (Character.isDigit(current) == true) {
                hasNumber = true;
            }

            if (Character.isLetterOrDigit(current) == false) {
                hasSpecial = true;
            }

            position = position + 1;
        }

        if (hasCapital == true && hasNumber == true && hasSpecial == true) {
            return true;
        } else {
            return false;
        }
    }

    // Returns true when the cell phone number matches the attributed South African regex
    public boolean checkCellPhoneNumber() {
        if (cellPhoneNumber == null) {
            return false;
        }

        Matcher matcher = SA_CELL_PATTERN.matcher(cellPhoneNumber);

        if (matcher.matches() == true) {
            return true;
        } else {
            return false;
        }
    }

    // Returns the username confirmation or error message
    public String usernameStatus() {
        if (checkUserName() == true) {
            return "Username successfully captured.";
        } else {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
    }

    // Returns the password confirmation or error message
    public String passwordStatus() {
        if (checkPasswordComplexity() == true) {
            return "Password successfully captured.";
        } else {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
    }

    // Returns the cell phone confirmation or error message used by the unit tests
    public String cellPhoneStatus() {
        if (checkCellPhoneNumber() == true) {
            return "Cell number successfully captured.";
        } else {
            return "Cell number is incorrectly formatted or does not contain an international code, please correct the number and try again.";
        }
    }

    // Returns the cell phone confirmation or error message used during registration
    public String cellPhoneReply() {
        if (checkCellPhoneNumber() == true) {
            return "Cell phone number successfully added.";
        } else {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
    }

    // Returns registration messaging for a bad username, a bad password, a bad cell number, or a successful registration
    public String registerUser() {
        if (checkUserName() == false) {
            return usernameStatus();
        } else if (checkPasswordComplexity() == false) {
            return passwordStatus();
        } else if (checkCellPhoneNumber() == false) {
            return cellPhoneStatus();
        } else {
            return "The user has been registered successfully.";
        }
    }

    // Returns true only when the typed username and password match the registered details
    public boolean loginUser() {
        if (username == null || password == null || typedUsername == null || typedPassword == null) {
            return false;
        }

        if (username.equals(typedUsername) && password.equals(typedPassword)) {
            return true;
        } else {
            return false;
        }
    }

    // Returns the login confirmation message or the login error message
    public String returnLoginStatus() {
        if (loginUser() == true) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}
