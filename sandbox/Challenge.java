public class Challenge {
    public String solve(String input) {
        int n = Integer.parseInt(input.trim());
        if (n < 0)
            throw new IllegalArgumentException("Input must be a non-negative integer");
        int result = 1;
        for (int i = 2; i <= n; i++)
            result *= i;
        return String.valueOf(result);
    }
}