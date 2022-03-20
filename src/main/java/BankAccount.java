import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class BankAccount {
    private String cardNumber;
    private String pin;

    public BankAccount() {
        cardNumber = createCardNumber();
        pin = createPin();
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    private String createCardNumber() {
        ArrayList<Integer> card = new ArrayList<>();
        card.add(4);
        card.add(0);
        card.add(0);
        card.add(0);
        card.add(0);
        card.add(0);

        // append random numbers
        Random random = new Random();
        for (int i = 0; i < 9; i++) {
            card.add((random.nextInt(10)));
        }

        // add a checksum
        card.add(calculateLuhn(card));

        return card.toString().replaceAll("[\\[,\\] ]", "");
    }

    static ArrayList<Integer> cardToInt(String cardAsString) {
        ArrayList<Integer> cardAsInt = new ArrayList<>();
        for (int i = 0; i < cardAsString.length() - 1; i++) {
            cardAsInt.add(Character.getNumericValue(cardAsString.charAt(i)));
        }
        return cardAsInt;
    }

    static int calculateLuhn(ArrayList<Integer> card) {
        int cardSum = 0;
        for (int i = 0; i < card.size(); i++) {
            int numberToAdd = card.get(i);

            if (i % 2 == 0) {
                numberToAdd *= 2;
            }

            if (numberToAdd > 9) {
                numberToAdd -= 9;
            }

            cardSum += numberToAdd;
        }
        return cardSum % 10 == 0 ? 0 : 10 - cardSum % 10;
    }

    private String createPin() {
        StringBuilder pin = new StringBuilder();

        // append random numbers
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            pin.append((random.nextInt(10)));
        }

        return pin.toString();
    }
}
