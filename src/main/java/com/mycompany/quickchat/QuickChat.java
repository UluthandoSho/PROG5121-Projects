/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
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

        keyboard.close();
    }
}
