package org.example.reactagent;

import java.util.List;
import java.util.Optional;

public class ReActAgent {
    private final ReasoningModel model;
    private final List<Tool> tools;
    private final int maxSteps;

    public ReActAgent(ReasoningModel model, List<Tool> tools, int maxSteps) {
        this.model = model;
        this.tools = tools;
        this.maxSteps = maxSteps;
    }

    public AgentResult run(String question) {
        AgentState state = new AgentState(question);
        for (int i = 0; i < maxSteps; i++) {
            AgentDecision decision = model.decide(state, tools);
            if (decision.finalAnswer() != null) {
                return new AgentResult(decision.finalAnswer(), state.steps());
            }
            Tool tool = findTool(decision.actionName())
                    .orElseThrow(() -> new IllegalStateException("Unknown tool: " + decision.actionName()));
            String observation = tool.execute(decision.actionInput());
            state.addStep(new AgentStep(decision.thought(), decision.actionName(), decision.actionInput(), observation));
        }
        return new AgentResult("无法在限制步数内得到答案。", state.steps());
    }

    private Optional<Tool> findTool(String name) {
        return tools.stream().filter(tool -> tool.name().equalsIgnoreCase(name)).findFirst();
    }
}
