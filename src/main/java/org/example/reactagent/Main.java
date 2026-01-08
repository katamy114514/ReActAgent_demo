package org.example.reactagent;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String question = args.length > 0
                ? String.join(" ", args)
                : "ReAct是什么？顺便计算 3 * (7 + 2)";

        ReActAgent agent = new ReActAgent(
                new SimpleRuleModel(),
                List.of(new CalculatorTool(), new SearchTool()),
                3
        );

        AgentResult result = agent.run(question);
        for (AgentStep step : result.steps()) {
            System.out.println("Thought: " + step.thought());
            System.out.println("Action: " + step.actionName() + "(" + step.actionInput() + ")");
            System.out.println("Observation: " + step.observation());
            System.out.println();
        }
        System.out.println("Final: " + result.finalAnswer());
    }
}
