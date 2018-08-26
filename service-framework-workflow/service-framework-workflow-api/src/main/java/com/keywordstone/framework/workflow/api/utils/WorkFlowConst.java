package com.keywordstone.framework.workflow.api.utils;

/**
 * @author k
 */
public class WorkFlowConst {

    /**
     * 工作流实体主键
     */
    public final static String ID = "id";

    /**
     * 工作流操作名称
     */
    public final static String OPERATE_NAME = "operateName";

    /**
     * 连接符
     */
    public final static String FIX = "#";

    /**
     * 工作流日志表名
     */
    public final static String WORK_FLOW_LOG_TABLE = "work_flow_log";

    public final static String INITIATOR = "${INITIATOR}";
    public final static String PREFIX = "processes/";
    public final static String SUFFIX = ".bpmn20.xml";
    /**
     *  新建合同流程
     */
    public final static String[] CONTRACT_AUDIT = {
        "contract-audit-1","contract-audit-2",
        "contract-audit-3","contract-audit-4",
        "contract-audit-5","contract-audit-6",
        "contract-audit-7","contract-audit-8",
        "contract-audit-9"
     };

    /**
     * 合同借阅processId
     */
    public final static String CONTRACT_BORROW_KEY = "contract-borrow";
    /**
     * 合同借阅流程xml
     */
    public final static String BPMN_XML = "contract-borrow";
    /**
     * 合同借阅流程任务ID
     */
    public final static String SID_APPROVE = "SID-APPROVE-1";
    public final static String SID_RETURN = "SID-RETURN";
    /**
     * 合同借阅合同主键
     */
    public final static String CONTRACT_ID = "contractId";
    /**
     * 借阅人主键
     */
    public final static String BORROW_PERSON_ID = "borrowPersonId";
    /**
     * 审核状态
     */
    public final static String TASK_REVIEW = "taskReview";
    /**
     * 归还状态
     */
    public final static String RETURN_STATUS = "returnStatus";
    /**
     * 任务主键
     */
    public final static String TASK_ID = "taskId";
    /**
     * 日期格式
     */
    public final static String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 预计归还日期
     */
    public final static String EXPECT_DATE = "expectDate";
    /**
     * 合同借阅到预计归还日期前一天做提醒
     */
    public final static long DURATION_DAY = 1L;
    /**
     * 提醒内容
     */
    public final static String NOTICE_CONTENT = "请尽快归还合同附件！";
}
