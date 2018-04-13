package ar.com.wolox.android.foandroid.validations;

/**
 * The result of a validation. Fields are public for convenience.
 * @See {@link Validation}
 */
public class ValidationResult {

    /**
     * Whether the validated object was valid or not
     */
    public boolean ok;
    /**
     * The id of an error message (as in R.string) explaining why the object was not valid.
     * If <code>{@link #ok} == true</code>, this field should be ignored.
     */
    public int errorMessageID;

    public ValidationResult() {
        this(true, 0);
    }

    public ValidationResult(boolean ok, int errorMessageID) {
        this.ok = ok;
        this.errorMessageID = errorMessageID;
    }

    @Override
    public int hashCode() {
        return errorMessageID;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ValidationResult &&
                ((ValidationResult) o).ok == this.ok &&
                ((ValidationResult) o).errorMessageID == this.errorMessageID;
    }
}