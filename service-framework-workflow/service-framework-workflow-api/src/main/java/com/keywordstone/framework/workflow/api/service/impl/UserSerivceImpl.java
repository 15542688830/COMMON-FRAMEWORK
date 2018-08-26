package com.keywordstone.framework.workflow.api.service.impl;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import com.keywordstone.framework.common.basic.dto.ResultDTO;
import com.keywordstone.framework.workflow.api.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.IdentityService;
import org.flowable.idm.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 9:39 2018/5/29
 * @Modified By:
 */
@Service
public class UserSerivceImpl implements UserService{

    @Autowired
    private IdentityService identityService;

    /**
     * 添加用户
     * @param user
     */
    @Override
    public ResultDTO addUser(AbstractDTO user) {

        if (user != null && StringUtils.isNotEmpty(user.getId())) {
            User user1 = identityService.newUser(user.getId());
            return ResultDTO.success(user1.getId());
        } else {
            return ResultDTO.failure();
        }
    }

    /**
     * 删除用户
     * @param user
     */
    @Override
    public ResultDTO delUser(AbstractDTO user) {

        if (user != null && StringUtils.isNotEmpty(user.getId())) {
//            identityService.create
//            identityService.deleteMembership(user.getId());
            identityService.deleteUser(user.getId());
            return ResultDTO.success();
        } else {
            return ResultDTO.failure();
        }
    }
}
