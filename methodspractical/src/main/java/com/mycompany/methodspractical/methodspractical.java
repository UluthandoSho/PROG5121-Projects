/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.methodspractical;


/**
 *
 * @author Uluthando
 */
import java.util.Scanner;

public class methodspractical {

    // Method to calculate and return the sum of two numbers
    public static int calculateSum(int firstNumber, int secondNumber) {
        return firstNumber + secondNumber;
    }

    // Method to calculate and return the average of two numbers
    public static double calculateAverage(int firstNumber, int secondNumber) {
        return (firstNumber + secondNumber) / 2.0;
    }

    public static void main(String[] args) {

        // Prompt the user to enter the first number
        try ( // Create Scanner object to get input from the user
                Scanner input = new Scanner(System.in)) {
            // Prompt the user to enter the first number
            System.out.print("Enter the first number: ");
            int firstNumber = input.nextInt();
            // Prompt the user to enter the second number
            System.out.print("Enter the second number: ");
            int secondNumber = input.nextInt();
            // Call the calculateSum method
            int sum = calculateSum(firstNumber, secondNumber);
            // Display the sum
            System.out.println("The sum of the two numbers is: " + sum);
            // Call the calculateAverage method
            double average = calculateAverage(firstNumber, secondNumber);
            // Display the average
            System.out.println("The average of the two numbers is: " + average);
            // Close the Scanner
        }
    }
}
