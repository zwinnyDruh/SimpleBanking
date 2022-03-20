import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // path to db and instance creation
        String dbPath = "jdbc:sqlite:" + args[1];
        Query db = new Query(dbPath);

        // Creating table - sqlite db
        String query = "CREATE TABLE IF NOT EXISTS card (" +
                "id INTEGER PRIMARY KEY, " +
                "number TEXT, " +
                "pin TEXT, " +
                "balance INTEGER DEFAULT 0);";
        db.processUpdate(query);

        Scanner input = new Scanner(System.in);

        // main loop
        while (true) {

            boolean outerBreak = false;

            int operation;
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");

            operation = input.nextInt();

            switch (operation) {

                case 1:
                    // creating account and saving to db
                    BankAccount account = new BankAccount();
                    query = "INSERT INTO card(number, pin, balance) VALUES (\"" +
                            account.getCardNumber() + "\", \"" +
                            account.getPin() + "\", 0);";
                    db.processUpdate(query);

                    System.out.println();
                    System.out.println("Your card has been created");
                    System.out.println("Your card number:");
                    System.out.println(account.getCardNumber());
                    System.out.println("Your card PIN:");
                    System.out.println(account.getPin());
                    System.out.println();
                    break;

                case 2: // logging in
                    System.out.println("Enter your card number:");
                    String userCard = input.next();
                    System.out.println("Enter your PIN:");
                    String userPin = input.next();

                    // check if card is in db
                    // todo create a BankAccount object instead of direct data from db
                    query = "SELECT * FROM card WHERE " +
                            "number='" + userCard +
                            "' AND pin='" + userPin + "';";
                    ArrayList<String[]> result = db.processQuery(query);

                    // logging in
                    boolean isLogged = false;
                    if (result.size() > 0) {
                        isLogged = true;
                        System.out.println("You have successfully logged in!");
                    } else {
                        System.out.println("Wrong card number or PIN!");
                    }

                    // flow control
                    if (!isLogged) {
                        break;
                    }

                    // preparing variables for further usage
                    int id = Integer.parseInt(result.get(0)[0]);
                    int balance = Integer.parseInt(result.get(0)[3]);

                    while (true) {

                        // checking balance, logging out
                        System.out.println("1. Balance");
                        System.out.println("2. Add income");
                        System.out.println("3. Do transfer");
                        System.out.println("4. Close account");
                        System.out.println("5. Log out");
                        System.out.println("0. Exit");
                        int option = input.nextInt();

                        // behavior based on input
                        switch (option) {
                            case 1:
                                System.out.println("Balance: " + balance);
                                break;
                            case 2: // Add income
                                System.out.println("Enter income:");
                                int addIncome = input.nextInt();
                                query = "UPDATE card SET balance = balance + " + addIncome + " WHERE id = " + id + ";";
                                System.out.println(query);
                                if (db.processUpdate(query) > 0) {
                                    query = "SELECT * FROM card WHERE id = " + id + ";";
                                    result = db.processQuery(query);
                                    balance = Integer.parseInt(result.get(0)[3]);
                                    System.out.println("Income was added!");
                                }
                                break;
                            case 3: // Do transfer
                                System.out.println("Enter card number:");
                                String cardToTransfer = input.next();

                                // luhn check
                                int lastDigit1 =
                                        Character.getNumericValue(cardToTransfer.charAt(cardToTransfer.length() - 1));
                                int lastDigit2 = BankAccount.calculateLuhn(BankAccount.cardToInt(cardToTransfer));
                                if (lastDigit1 != lastDigit2) {
                                    System.out.println("Probably you made a mistake in the card number. Please try " +
                                            "again!\n");
                                    break;
                                }

                                // does the card exists
                                query = "SELECT * FROM card WHERE " + "number='" + cardToTransfer + "';";
                                result = db.processQuery(query);
                                if (!(result.size() > 0)) {
                                    System.out.println("Such a card does not exist.");
                                    break;
                                } else {
                                    System.out.println("jest taki numer");
                                }

                                // how enough money
                                System.out.println("Enter how much money you want to transfer:");
                                int moneyToTransfer = input.nextInt();
                                if (balance < moneyToTransfer) {
                                    System.out.println("Not enough money!");
                                    break;
                                }

                                // make transfer
                                // todo change to prepared statement and make a transaction
                                query =
                                        "UPDATE card SET balance = " + (balance - moneyToTransfer) + " WHERE id = " + id + "; ";
                                query += "UPDATE card SET balance = balance + " + moneyToTransfer + " " +
                                        "WHERE id = " + result.get(0)[0] + "; ";
                                System.out.println("query " + query);
                                db.processUpdate(query);
                                System.out.println("Success!");
                                break;
                            case 4: // Close account
                                query = "DELETE FROM card WHERE id = " + id + ";";
                                db.processUpdate(query);
                                isLogged = false;
                                outerBreak = true;
                                break;
                            case 5: // Log out
                                isLogged = false;
                                outerBreak = true;
                                break;
                            case 0:
                                System.out.println("Bye!");
                                System.exit(0);
                        }
                        if (outerBreak) {
                            break;
                        }
                    }
                    break;
                case 0: // exit
                    System.out.println("Bye!");
                    System.exit(0);
            }
        }
    }
}