package simplecalculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class CalculatorPostFixExpression {

    static BigDecimal evaluatePostFixExpression(String postfixExpression) {
        String expression = postfixExpression.replaceAll("\\s+", " ");
        List<String> arrayOfString = Arrays
                .stream(expression.split(" "))
                .collect(Collectors.toList());
        Stack<BigDecimal> numStack = new Stack<>();
        for (String s : arrayOfString) {
            if (isOperation(s.charAt(0))) {
                BigDecimal num1 = numStack.pop();
                BigDecimal num2 = numStack.pop();
                numStack.push(calcValueOfTwoNum(num2, num1, s));
            } else {
                numStack.push(new BigDecimal(s));
            }
        }
        return numStack.peek();
    }

    private static int precedence(char c) {
        switch (c) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
        }
        return -1;
    }

    public static String infixToPostFix(String expression) {
        StringBuilder result = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (precedence(c) > 0) {
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(c)) {
                    result.append(stack.pop());
                }
                stack.push(c);
            } else if (c == ')') {
                char x = stack.pop();
                while (x != '(') {
                    result.append(x);
                    x = stack.pop();
                }
            } else if (c == '(') {
                stack.push(c);
            } else {
                result.append(c);
            }
        }
        for (int i = 0; i <= stack.size(); i++) {
            result.append(stack.pop()).append(" ");
        }
        return result.toString();
    }

    public static boolean isOperation(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    public static BigDecimal calcValueOfTwoNum(BigDecimal num1, BigDecimal num2, String op) {
        switch (op) {
            case "+":
                return num1.add(num2);
            case "-":
                return num1.subtract(num2);
            case "*":
                return num1.multiply(num2);
            case "/":
                if (num2.signum() == 0) {
                    throw new RuntimeException("Divisor cannot be 0");
                }
                return num1.divide(num2, 2, RoundingMode.HALF_UP);// The division retains 2 decimal places and rounds up
        }
        throw new RuntimeException("No such operator");
    }
}