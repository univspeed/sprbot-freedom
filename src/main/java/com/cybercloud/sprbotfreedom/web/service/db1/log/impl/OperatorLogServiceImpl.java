package com.cybercloud.sprbotfreedom.web.service.db1.log.impl;

import com.cybercloud.sprbotfreedom.platform.annotation.log.PrintFunctionLog;
import com.cybercloud.sprbotfreedom.platform.base.entity.PageResult;
import com.cybercloud.sprbotfreedom.platform.base.service.impl.BaseServiceImpl;
import com.cybercloud.sprbotfreedom.platform.datasource.annotation.DB1DataSource;
import com.cybercloud.sprbotfreedom.platform.mongodb.MongoPage;
import com.cybercloud.sprbotfreedom.platform.util.DateUtil;
import com.cybercloud.sprbotfreedom.platform.util.genid.IdWorker;
import com.cybercloud.sprbotfreedom.web.entity.po.db1.log.OperateLogEntity;
import com.cybercloud.sprbotfreedom.web.service.db1.log.OperatorLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 操作记录业务接口
 * @author liuyutang
 * @date 2023/8/30
 */
@Service
@PrintFunctionLog
@DB1DataSource
public class OperatorLogServiceImpl extends BaseServiceImpl implements OperatorLogService {

    private final MongoTemplate mongoTemplate;
    private final IdWorker idWorker;

    public OperatorLogServiceImpl(MongoTemplate mongoTemplate, IdWorker idWorker) {
        this.mongoTemplate = mongoTemplate;
        this.idWorker = idWorker;
    }

    @Override
    public PageResult<OperateLogEntity> page(Integer page, Integer pageSize, String username, String keyword, String startTime, String endTime, String loginIp) {
        MongoPage<OperateLogEntity> result = new MongoPage<>();
        Query query = getQuery(username, startTime, endTime, loginIp);
        result.initPage(page,pageSize,query);

        List<OperateLogEntity> logs = mongoTemplate.find(query, OperateLogEntity.class);
        long count = mongoTemplate.count(query, OperateLogEntity.class);
        return result.pageHelper((int)count,logs);
    }

    private Query getQuery(String username, String startTime, String endTime, String loginIp) {
        Criteria where = new Criteria();
        if(StringUtils.isNotBlank(username)){
            where.and("operatorName").regex(username,"i");
        }
        if(StringUtils.isNotBlank(loginIp)){
            where.and("loginIp").regex(loginIp,"i");
        }
        if(StringUtils.isNotBlank(endTime) && StringUtils.isNotBlank(startTime)){
            where.and("operateTime").lte(DateUtil.parseDate(endTime,DateUtil.FMT_3)).gte(DateUtil.parseDate(startTime,DateUtil.FMT_3));
        }
        Query query = new Query(where);
        query.with(Sort.by("operateTime"));
        return query;
    }

    @Override
    public boolean insert(String description) {
        String operatorName = "";
        if (getUserInfo() != null) {
            operatorName = getUserInfo().getUsername();
        }
        String requestClientIp = getRequestClientIp();
        if (StringUtils.isBlank(operatorName)) {
            operatorName = "非法用户";
        }
        OperateLogEntity save = mongoTemplate.save(OperateLogEntity.builder()
                .id(idWorker.nextId())
                .logInfo(description)
                .operateTime(new Date())
                .operatorName(operatorName)
                .loginIp(requestClientIp)
                .build(),COLLECTION_NAME);
        return save != null;
    }


    @Override
    public boolean del(String username, String keyword, String startTime, String endTime, String loginIp) {

        return mongoTemplate.remove(getQuery( username,  startTime,  endTime,  loginIp),OperateLogEntity.class).getDeletedCount() > 0;
    }
}
