package kento;

public class Ui {

    public Ui() {

    }

    /**
     * prints out Kento Greeting
     */
    public void showKentoGreeting() {
        String banner = " _  __         ____  ___      \n"
                + "| |/ /___ _ __|_  _|/   \\\n"
                + "| ' // _ \\ '_ \\| | / (_) |\n"
                + "|   \\  __/ | | | | \\    |\n"
                + "|_|\\_\\___|_| |_|_|\\_\\___/\n";

        String greeting = """
                ____________________________________________________________
                What is the service you are willing to pay the most for?
                ____________________________________________________________
                """;

        System.out.println("Welcome to");
        System.out.println(banner);
        System.out.println(greeting);
    }

}
