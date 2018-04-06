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
     * An error message explaining why the object was not valid. If <code>{@link #ok} == true</code>,
     * this field should be ignored.
     */
    public String errorMessage;

    public ValidationResult(boolean ok, String errorMessage) {
        this.ok = ok;
        this.errorMessage = errorMessage;
    }
}