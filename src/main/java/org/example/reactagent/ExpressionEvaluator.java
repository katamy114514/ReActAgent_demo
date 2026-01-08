package org.example.reactagent;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ExpressionEvaluator {
    public double evaluate(String expression) {
        List<Token> tokens = tokenize(expression);
        List<Token> rpn = toRpn(tokens);
        return evalRpn(rpn);
    }

    private List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;
        Token previous = null;
        while (i < input.length()) {
            char c = input.charAt(i);
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }
            if (c == '(' || c == ')') {
                tokens.add(new Token(TokenType.PAREN, String.valueOf(c)));
                previous = tokens.get(tokens.size() - 1);
                i++;
                continue;
            }
            if (isOperatorChar(c)) {
                if (c == '-' && isUnaryPosition(previous)) {
                    int start = i;
                    i++;
                    while (i < input.length() && (Character.isDigit(input.charAt(i)) || input.charAt(i) == '.')) {
                        i++;
                    }
                    tokens.add(new Token(TokenType.NUMBER, input.substring(start, i)));
                    previous = tokens.get(tokens.size() - 1);
                    continue;
                }
                tokens.add(new Token(TokenType.OPERATOR, String.valueOf(c)));
                previous = tokens.get(tokens.size() - 1);
                i++;
                continue;
            }
            if (Character.isDigit(c) || c == '.') {
                int start = i;
                i++;
                while (i < input.length() && (Character.isDigit(input.charAt(i)) || input.charAt(i) == '.')) {
                    i++;
                }
                tokens.add(new Token(TokenType.NUMBER, input.substring(start, i)));
                previous = tokens.get(tokens.size() - 1);
                continue;
            }
            throw new IllegalArgumentException("Unsupported character: " + c);
        }
        return tokens;
    }

    private boolean isUnaryPosition(Token previous) {
        return previous == null || previous.type() == TokenType.OPERATOR || isLeftParen(previous);
    }

    private boolean isLeftParen(Token token) {
        return token.type() == TokenType.PAREN && "(".equals(token.value());
    }

    private boolean isOperatorChar(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private List<Token> toRpn(List<Token> tokens) {
        List<Token> output = new ArrayList<>();
        Deque<Token> operators = new ArrayDeque<>();
        for (Token token : tokens) {
            switch (token.type()) {
                case NUMBER -> output.add(token);
                case OPERATOR -> {
                    while (!operators.isEmpty() && operators.peek().type() == TokenType.OPERATOR
                            && precedence(operators.peek()) >= precedence(token)) {
                        output.add(operators.pop());
                    }
                    operators.push(token);
                }
                case PAREN -> {
                    if ("(".equals(token.value())) {
                        operators.push(token);
                    } else {
                        while (!operators.isEmpty() && !"(".equals(operators.peek().value())) {
                            output.add(operators.pop());
                        }
                        if (operators.isEmpty()) {
                            throw new IllegalArgumentException("Mismatched parentheses");
                        }
                        operators.pop();
                    }
                }
            }
        }
        while (!operators.isEmpty()) {
            Token op = operators.pop();
            if (op.type() == TokenType.PAREN) {
                throw new IllegalArgumentException("Mismatched parentheses");
            }
            output.add(op);
        }
        return output;
    }

    private double evalRpn(List<Token> rpn) {
        Deque<Double> stack = new ArrayDeque<>();
        for (Token token : rpn) {
            if (token.type() == TokenType.NUMBER) {
                stack.push(Double.parseDouble(token.value()));
                continue;
            }
            if (token.type() == TokenType.OPERATOR) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Invalid expression");
                }
                double right = stack.pop();
                double left = stack.pop();
                stack.push(applyOperator(token.value().charAt(0), left, right));
            }
        }
        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression");
        }
        return stack.pop();
    }

    private double applyOperator(char operator, double left, double right) {
        return switch (operator) {
            case '+' -> left + right;
            case '-' -> left - right;
            case '*' -> left * right;
            case '/' -> left / right;
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }

    private int precedence(Token token) {
        return switch (token.value()) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            default -> 0;
        };
    }

    private record Token(TokenType type, String value) {
    }

    private enum TokenType {
        NUMBER,
        OPERATOR,
        PAREN
    }
}
