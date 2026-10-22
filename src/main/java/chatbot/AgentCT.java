package chatbot;

/** Runs the AgentCT command-line chatbot. */
public class AgentCT {
    /** Runs the command-line chatbot.
     *
     * @param args command-line arguments, which are unused
     */
    public static void main(String[] args) {
        new AgentCTApplication("data/agentct.txt").run();
    }
}
