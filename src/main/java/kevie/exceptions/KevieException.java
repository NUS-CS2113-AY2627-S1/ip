package kevie.exceptions;

import kevie.Kevie;
import kevie.UserInterface;

/**
 * Abstract exception that Kevie throws.
 */
public class KevieException extends Exception{

    protected String msg;
    protected String type;
    protected UserInterface ui;

    /**
     * Creates a Kevie exception.
     *
     * @param msg Main reason of exception thrown.
     * @param type Smaller insight of why exception is thrown.
     * @param ui User interface to print messages in
     */
    public KevieException(String msg, String type, UserInterface ui){
        this.msg = msg;
        this.type = type;
        this.ui = ui;
    }

    @Override
    public String toString() {
        return msg + ": " + type + ".";
    }

    /**
     * Prints reason why the exception was thrown.
     */
    public void printMessage(){
        ui.botSpeak(this.toString());
    }
}
