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
                return new ThreeThreadVerifier(numberValidator);
            case 27:
                
                return new TwentySevenThreadVerifier(numberValidator);
            default:
                throw new IllegalArgumentException("Invalid mode: " + mode);
        }
    }
}
