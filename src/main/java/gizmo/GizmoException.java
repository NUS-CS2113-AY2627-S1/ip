package gizmo;

/**
 * Represents an invalid command or input entered for Gizmo.
 */
public class GizmoException extends Exception {
    private static final long serialVersionUID = 1L;

    /** Creates an exception with the supplied user-facing message. */
    public GizmoException(String message) {
        super(message);
    }
}
