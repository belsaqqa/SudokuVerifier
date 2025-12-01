package sudokulab9;

import java.util.*;
import java.util.concurrent.*;

public class TwentySevenThreadVerifier implements VerifierStrategy {
    private final NumberValidator numberValidator;
    
    public TwentySevenThreadVerifier(NumberValidator numberValidator) {
        this.numberValidator = numberValidator;
    }
    
    @Override
    public VerificationResult verify(int[][] board) {
        ExecutorService executor = Executors.newFixedThreadPool(27);
        
        // Use a thread-safe list for error collection
        List<ValidationError> allErrors = Collections.synchronizedList(new ArrayList<>());
        
        List<Future<?>> futures = new ArrayList<>();
        
        // Create 9 threads for rows
        for (int i = 0; i < 9; i++) {
            final int rowIndex = i;
            futures.add(executor.submit(() -> {
                RowVerifier rowVerifier = new RowVerifier(numberValidator);
                ValidationError error = rowVerifier.validate(board, rowIndex);
                if (error != null) {
                    synchronized(allErrors) {
                        allErrors.add(error);
                    }
                }
            }));
        }
        
        // Create 9 threads for columns
        for (int i = 0; i < 9; i++) {
            final int colIndex = i;
            futures.add(executor.submit(() -> {
                ColumnVerifier colVerifier = new ColumnVerifier(numberValidator);
                ValidationError error = colVerifier.validate(board, colIndex);
                if (error != null) {
                    synchronized(allErrors) {
                        allErrors.add(error);
                    }
                }
            }));
        }
        
        // Create 9 threads for boxes
        for (int i = 0; i < 9; i++) {
            final int boxIndex = i;
            futures.add(executor.submit(() -> {
                BoxVerifier boxVerifier = new BoxVerifier(numberValidator);
                ValidationError error = boxVerifier.validate(board, boxIndex);
                if (error != null) {
                    synchronized(allErrors) {
                        allErrors.add(error);
                    }
                }
            }));
        }
        
        // Wait for all threads to complete
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread execution interrupted", e);
            }
        }
        
        executor.shutdown();
        
        // Sort errors by type and index for clean output
        List<ValidationError> sortedErrors = sortErrorsByTypeAndIndex(allErrors);
        
        boolean isValid = sortedErrors.isEmpty();
        return new VerificationResult(isValid, sortedErrors);
    }
    
    private List<ValidationError> sortErrorsByTypeAndIndex(List<ValidationError> errors) {
        List<ValidationError> sorted = new ArrayList<>(errors);
        
        Collections.sort(sorted, (error1, error2) -> {
            // First sort by type: ROW, then COL, then BOX
            String type1 = getTypeFromError(error1);
            String type2 = getTypeFromError(error2);
            
            int typeComparison = getTypePriority(type1) - getTypePriority(type2);
            if (typeComparison != 0) {
                return typeComparison;
            }
            
            // Then sort by index within the same type
            int index1 = getIndexFromError(error1);
            int index2 = getIndexFromError(error2);
            return Integer.compare(index1, index2);
        });
        
        return sorted;
    }
    
    private String getTypeFromError(ValidationError error) {
        // Extract type from the error's toString() - it starts with the type
        String errorStr = error.toString();
        if (errorStr.startsWith("ROW")) return "ROW";
        if (errorStr.startsWith("COL")) return "COL"; 
        if (errorStr.startsWith("BOX")) return "BOX";
        return "UNKNOWN";
    }
    
    private int getIndexFromError(ValidationError error) {
        // Extract index from the error's toString()
        String errorStr = error.toString();
        try {
            // Look for patterns like "ROW 1," "COL 3," "BOX 5,"
            String[] parts = errorStr.split(" ");
            for (int i = 0; i < parts.length - 1; i++) {
                if (parts[i].equals("ROW") || parts[i].equals("COL") || parts[i].equals("BOX")) {
                    // The next part should be the index (might have comma)
                    String indexStr = parts[i + 1].replace(",", "");
                    return Integer.parseInt(indexStr);
                }
            }
        } catch (Exception e) {
            // If parsing fails, return 0
        }
        return 0;
    }
    
    private int getTypePriority(String type) {
        switch (type) {
            case "ROW": return 1;
            case "COL": return 2;
            case "BOX": return 3;
            default: return 4;
        }
    }
}
