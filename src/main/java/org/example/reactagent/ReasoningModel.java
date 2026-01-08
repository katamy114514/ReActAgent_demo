package org.example.reactagent;

import java.util.List;

public interface ReasoningModel {
    AgentDecision decide(AgentState state, List<Tool> tools);
}
