package org.example.reactagent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AgentState {
    private final String question;
    private final List<AgentStep> steps;

    public AgentState(String question) {
        this.question = question;
        this.steps = new ArrayList<>();
    }

    public String question() {
        return question;
    }

    public List<AgentStep> steps() {
        return Collections.unmodifiableList(steps);
    }

    public void addStep(AgentStep step) {
        steps.add(step);
    }
}
