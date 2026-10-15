package ui;

import java.util.Scanner;

public class ConsoleInput {
    private final Scanner scanner;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readString() {
        return scanner.nextLine();
    }

    public String readText() {
        return readText(false);
    }

    public String readText(boolean allowBlank) {
        String value = readString();
        if (!allowBlank && value.isBlank()) {
            throw new IllegalArgumentException("A value is required.");
        }
        return value;
    }

    public boolean readBoolean() {
        return switch (readText().trim()) {
            case "true" -> true;
            case "false" -> false;
            default -> throw new IllegalArgumentException("Please enter true or false.");
        };
    }

    public int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Try again: ");
            }
        }
    }
}