package com.keywordstone.framework.workflow.api.module;

import com.keywordstone.framework.common.basic.entity.BasicEntity;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 工作流载体
 *
 * @author k
 */
@Data
public class Work extends BasicEntity {

    /**
     * 名称
     */
    private String name;

    /**
     * 工作流ID
     */
    private String flowId;

    /**
     * 操作者
     */
    private String operator;

    /**
     * 工作流挂载数据
     */
    private Map<String, Object> dataMap;

    /**
     * 动作参数
     */
    private String operateKey;

    /**
     * 动作名称
     */
    private String operateName;

    /**
     * 下路分支
     */
    private List<Line> lineList;

    /**
     * 备忘
     */
    private String memo;

    /**
     * 流程示意图（base64）
     */
    private String img;

    /**
     * 实例ID
     */
    private String instanceId;

    public Work() {
    }

    public Work(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    public Work(String flowId,
                Map<String, Object> dataMap) {
        this.flowId = flowId;
        this.dataMap = dataMap;
    }
}
