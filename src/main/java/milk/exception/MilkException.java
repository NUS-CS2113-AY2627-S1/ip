package milk.exception;

import milk.ui.Ui;

public class MilkException extends Exception {
    Ui ui = new Ui();
    public MilkException(String exceptionMessage) {
        ui.printResponse(exceptionMessage);
    }
}
