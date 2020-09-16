import java.util.*;

public final class Calc {

    private boolean manageNegative(final String exp, final int index,
                                   final Stack<Character> operators, final Stack<Float> operands) {
        boolean isNegative = false;

        if (index == 0)
            isNegative = true;
        else {
            final char prev = exp.charAt(index - 1);
            isNegative = (prev == '(' || (prev != ')' && !Character.isDigit(prev)));
        }

        if (isNegative) {
            operators.push('*');
            operands.push(-1.0f);
        }

        return isNegative;
    }

    private void compute(final Stack<Character> operators, final Stack<Float> operands) {
        final char action = operators.pop();
        final float right = operands.pop();
        final float left = operands.pop();

        if (action == '-')
            operands.push(left - right);
        else if (action == '+')
            operands.push(left + right);
        else if (action == '/')
            operands.push(left / right);
        else if (action == '*')
            operands.push(left * right);
    }

    public float solve(final String source) {

        final String exp = "(" + source.trim().replaceAll("\\s+", "") + ")";

        final Map<Character, Integer> precedenceMap = new HashMap<>();
        precedenceMap.put('-', 1);
        precedenceMap.put('+', 1);
        precedenceMap.put('/', 2);
        precedenceMap.put('*', 2);

        final Stack<Character> operators = new Stack<>();
        final Stack<Float> operands = new Stack<>();

        for (int i = 0; i < exp.length(); i++) {
            final char current = exp.charAt(i);

            if (current == '(') {
                operators.push(current);
            }
            else if (current == ')') {
                while (!operators.isEmpty() && operators.peek() != '(')
                    this.compute(operators, operands);

                operators.pop();
            }
            else if (current == '-' && this.manageNegative(exp, i, operators, operands)) {
                continue;
            }
            else if (precedenceMap.containsKey(current)) {
                final int precedence = precedenceMap.get(current);

                while (!operators.isEmpty() && precedenceMap.getOrDefault(operators.peek(), 0) >= precedence)
                    this.compute(operators, operands);

                operators.push(current);
            }
            else {
                int num = 0;

                while (Character.isDigit(exp.charAt(i))) {
                    num = (num * 10) + (exp.charAt(i) - '0');
                    i++;
                }

                operands.push((float)num);
                i--;
            }
        }

        return operands.pop();
    }

    public static void main(final String[] args) {

        System.out.println(new Calc().solve("1+(-2*-7)*((-4/2)+-3)*-1"));
    }
}
