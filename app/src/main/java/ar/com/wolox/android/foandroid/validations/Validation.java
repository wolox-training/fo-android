package ar.com.wolox.android.foandroid.validations;

/**
 * Represents a generic validation on a value of type T. Implementing classes are expected to be
 * objects that can validate data format, consistency, integrity, etc. against a specific set of
 * rules. For example, implementing class {@link EmailFormatValidation} is used to validate that a
 *  given is a valid email address.
 * @param <T>   The type of object that this Validation validates.
 */
@FunctionalInterface
public interface Validation<T> {

    /**
     * Validates the given object. Implementing classes are encouraged, but not required, to
     * handle a <code>null</code> parameter.
     * @param objectToValidate  The object to be validated
     * @return  A {@link ValidationResult} object containing the result of the validation.
     * @See ValidationResult
     */
    ValidationResult validate(T objectToValidate);
}