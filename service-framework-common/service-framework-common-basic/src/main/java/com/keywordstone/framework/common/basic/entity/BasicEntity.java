package com.keywordstone.framework.common.basic.entity;

import com.keywordstone.framework.common.basic.enums.DataFlagEnum;
import com.keywordstone.framework.common.basic.utils.TokenUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * @author k
 */
@Data
public abstract class BasicEntity implements Serializable {

    private String id;

    private String createUser;

    private Date createTime;

    private String updateUser;

    private Date updateTime;

    private Integer dataFlag;

    private Integer verid;

    public BasicEntity() {
    }

    public boolean preInsert() {
        this.setVerid(0);
        if (preUpdate()) {
            if(StringUtils.isEmpty(this.getCreateUser())) {
                this.setCreateUser(this.getUpdateUser());
            }
            this.setCreateTime(this.getUpdateTime());
            this.setId(UUID.randomUUID().toString().replace("-", StringUtils.EMPTY));
            this.setDataFlag(DataFlagEnum.FUNCTIONAL.getValue());
            return true;
        }
        return false;
    }

    public boolean preUpdate() {
        String userId = TokenUtils.getUserId();
        if (StringUtils.isNotEmpty(userId)) {
            this.setUpdateUser(userId);
            this.setUpdateTime(new Date());
            if (null != this.getVerid()
                    && 0 != this.getVerid().intValue()) {
                this.setVerid(this.getVerid() + 1);
            }
            return true;
        }
        return false;
    }

    public boolean remove() {
        if (preUpdate()) {
            this.setDataFlag(DataFlagEnum.REMOVED.getValue());
            return true;
        }
        return false;
    }

    public boolean everRemoved() {
        return dataFlag == DataFlagEnum.REMOVED.getValue();
    }
}
