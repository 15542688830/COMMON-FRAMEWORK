package com.keywordstone.framework.workflow.api.service;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import com.keywordstone.framework.common.basic.dto.ResultDTO;

/**
 * @author ZR
 */
public interface UserService {

    /**
     * 添加用户
     * @param user
     */
    ResultDTO<String> addUser(AbstractDTO user);

    /**
     * 删除用户
     * @param user
     */
    ResultDTO<String> delUser(AbstractDTO user);
}
