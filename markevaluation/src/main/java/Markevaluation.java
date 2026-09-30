java
import java.util.Scanner;

public class StudentMarkEvaluation {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // 1.1 Prompt for the student mark (0-100)
        System.out.print("Enter Student Mark: ");
        int studentMark = input.nextInt();

        System.out.println();

        // 1.2 Distinction check (75 or higher)
        if (studentMark >= 75) {
            System.out.println("Student qualifies for a distinction.");
        }

        // 1.3 + 1.4 Pass/fail check with multiple statements in each branch
        if (studentMark >= 50) {
            System.out.println("Congratulations, you passed!");
            System.out.println("You may proceed to the next module.");
        } else {
            System.out.println("Unfortunately, you failed.");
            System.out.println("Please speak to your lecturer about rewriting the module.");
        }

        input.close();
    }
}