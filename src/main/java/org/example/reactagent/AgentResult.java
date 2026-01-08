package org.example.reactagent;

import java.util.List;

public record AgentResult(String finalAnswer, List<AgentStep> steps) {
}
