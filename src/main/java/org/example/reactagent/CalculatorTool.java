package org.example.reactagent;

import java.text.DecimalFormat;

public class CalculatorTool implements Tool {
    private final ExpressionEvaluator evaluator;
    private final DecimalFormat formatter;

    public CalculatorTool() {
        this.evaluator = new ExpressionEvaluator();
        this.formatter = new DecimalFormat("0.########");
    }

    @Override
    public String name() {
        return "calculator";
    }

    @Override
    public String description() {
        return "计算四则运算表达式，支持括号。";
    }

    @Override
    public String execute(String input) {
        double result = evaluator.evaluate(input);
        return formatter.format(result);
    }
}
