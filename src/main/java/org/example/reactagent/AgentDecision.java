package org.example.reactagent;

public record AgentDecision(String thought, String actionName, String actionInput, String finalAnswer) {
    public static AgentDecision action(String thought, String actionName, String actionInput) {
        return new AgentDecision(thought, actionName, actionInput, null);
    }

    public static AgentDecision finish(String thought, String finalAnswer) {
        return new AgentDecision(thought, null, null, finalAnswer);
    }
}
