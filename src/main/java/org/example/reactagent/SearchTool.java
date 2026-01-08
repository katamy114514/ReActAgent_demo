package org.example.reactagent;

import java.util.Map;

public class SearchTool implements Tool {
    private final Map<String, String> knowledgeBase = Map.of(
            "react agent", "ReAct是一种将推理(Reasoning)与行动(Action)交替结合的智能体方法，通过工具调用得到观察结果后再继续推理。",
            "react", "ReAct是一种将推理与行动结合的提示范式，常用于多步骤任务。"
    );

    @Override
    public String name() {
        return "search";
    }

    @Override
    public String description() {
        return "从内置知识库中检索ReAct相关信息。";
    }

    @Override
    public String execute(String input) {
        String key = input.toLowerCase();
        return knowledgeBase.getOrDefault(key, "未找到相关信息。你可以换个关键词。");
    }
}
