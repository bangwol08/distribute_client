package UI;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UI
{
    public int MainMenu()
    {
        Scanner scan = new Scanner(System.in);
        int choose = -1;

        System.out.println("Please Select Menu");
        System.out.println("0. Exit the Program");
        System.out.println("1. Show Server List");
        System.out.println("2. Distribute Storage");
        System.out.println("3. WordCounting Strart");
        System.out.println("4. Show Result");
        System.out.print("Select : ");

        try
        {
            choose = scan.nextInt();
        }
        catch (InputMismatchException e)
        {
            System.out.println("Pleasu input Integer");
        }

        return choose;
    }
}

