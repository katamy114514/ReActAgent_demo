package org.example.reactagent;

public record AgentStep(String thought, String actionName, String actionInput, String observation) {
}
