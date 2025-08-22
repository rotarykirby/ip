public class LebronException extends Exception {
    public LebronException(String message) {
        super(message);
    }

    public static class MissingDescriptionException extends LebronException {
        public MissingDescriptionException(String keyword) {
            super("☹ OOPS!!! The description of a " + keyword + " cannot be empty.");
        }
    }
}
