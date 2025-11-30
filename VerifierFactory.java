package sudoku;

public class VerifierFactory {
    private final NumberValidator numberValidator;
    
    public VerifierFactory(NumberValidator numberValidator) {
        this.numberValidator = numberValidator;
    }
    
    public VerifierFactory() {
        this(new CompleteSetValidator());
    }
    
    public VerifierStrategy createVerifier(int mode) {
        switch (mode) {
            case 0:
                return new SequentialVerifier(numberValidator);
            case 3:
                // Will be implemented by Member 2
                throw new UnsupportedOperationException("3-thread mode not yet implemented");
            case 27:
                // Will be implemented by Member 3  
                throw new UnsupportedOperationException("27-thread mode not yet implemented");
            default:
                throw new IllegalArgumentException("Invalid mode: " + mode);
        }
    }
}
