package com.cybercloud.sprbotfreedom.platform.mongodb;

import com.cybercloud.sprbotfreedom.platform.base.entity.PageResult;
import lombok.Data;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

/**
 * mongodb分页工具
 * @author liuyutang
 * @date 2023/8/30
 */
@Data
public class MongoPage<T> {

    private Integer pageSize;
    private Integer currentPage;

    public void initPage(Integer currentPage, Integer pageSize, Query query){
        pageSize = pageSize == 0 ? 20 : pageSize;
        query.limit(pageSize);
        query.skip((currentPage - 1) * pageSize);
        this.pageSize = pageSize;
        this.currentPage = currentPage;
    }

    public PageResult<T> pageHelper(int total , List<T> data){
        return PageResult.create(data,total,this.pageSize,this.currentPage);
    }

}
