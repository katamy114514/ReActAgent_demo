package org.example.reactagent;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleRuleModel implements ReasoningModel {
    private static final Pattern EXPRESSION_PATTERN = Pattern.compile("[-+*/().\\d\\s]+");

    @Override
    public AgentDecision decide(AgentState state, List<Tool> tools) {
        Optional<String> expression = extractExpression(state.question());
        boolean wantsSearch = containsKeyword(state.question(), List.of("react", "ReAct", "智能体", "agent"));

        if (state.steps().isEmpty()) {
            if (wantsSearch) {
                return AgentDecision.action("需要检索ReAct的解释。", "search", "ReAct Agent");
            }
            if (expression.isPresent()) {
                return AgentDecision.action("需要先计算表达式。", "calculator", expression.get());
            }
            return AgentDecision.finish("无需使用工具。", "我已了解问题，但没有合适的工具可以调用。");
        }

        AgentStep lastStep = state.steps().get(state.steps().size() - 1);
        if ("search".equalsIgnoreCase(lastStep.actionName())) {
            if (expression.isPresent()) {
                return AgentDecision.action("需要补充计算结果。", "calculator", expression.get());
            }
            return AgentDecision.finish("根据检索结果回答。", lastStep.observation());
        }
        if ("calculator".equalsIgnoreCase(lastStep.actionName())) {
            return AgentDecision.finish("根据工具结果组织答案。", buildFinalAnswer(state, wantsSearch));
        }
        return AgentDecision.finish("未匹配到合适的工具输出。", "已完成。\n" + lastStep.observation());
    }

    private Optional<String> extractExpression(String question) {
        Matcher matcher = EXPRESSION_PATTERN.matcher(question);
        while (matcher.find()) {
            String candidate = matcher.group().trim();
            if (candidate.chars().anyMatch(Character::isDigit)) {
                return Optional.of(candidate);
            }
        }
        return Optional.empty();
    }

    private boolean containsKeyword(String text, List<String> keywords) {
        String normalized = text.toLowerCase();
        return keywords.stream().anyMatch(keyword -> normalized.contains(keyword.toLowerCase()));
    }

    private String buildFinalAnswer(AgentState state, boolean wantsSearch) {
        String calculation = state.steps().stream()
                .filter(step -> "calculator".equalsIgnoreCase(step.actionName()))
                .map(AgentStep::observation)
                .findFirst()
                .orElse("暂无计算结果");
        if (!wantsSearch) {
            return "计算结果是: " + calculation;
        }
        String search = state.steps().stream()
                .filter(step -> "search".equalsIgnoreCase(step.actionName()))
                .map(AgentStep::observation)
                .findFirst()
                .orElse("暂无检索结果");
        return "ReAct解释: " + search + System.lineSeparator() + "计算结果: " + calculation;
    }
}
