package com.alibaba.nacos.plugin.auth.impl.persistence.handler.support;

import com.alibaba.nacos.persistence.constants.PersistenceConstant;
import com.alibaba.nacos.plugin.auth.impl.constant.AuthPageConstant;
import com.alibaba.nacos.plugin.auth.impl.model.OffsetFetchResult;
import com.alibaba.nacos.plugin.auth.impl.persistence.handler.PageHandlerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DmPageHandlerAdapter implements PageHandlerAdapter {
  
    @Override  
    public boolean supports(String dataSourceType) {  
        return PersistenceConstant.DM.equals(dataSourceType);
    }  
  
    @Override  
    public OffsetFetchResult addOffsetAndFetchNext(String fetchSql, Object[] arg, int pageNo, int pageSize) {
        if (!fetchSql.contains(AuthPageConstant.LIMIT)) {
            fetchSql += " " + AuthPageConstant.LIMIT_SIZE;  
            List<Object> newArgsList = new ArrayList<>(Arrays.asList(arg));
            newArgsList.add((pageNo - 1) * pageSize);  
            newArgsList.add(pageSize);  
  
            Object[] newArgs = newArgsList.toArray(new Object[0]);  
            return new OffsetFetchResult(fetchSql, newArgs);  
        }  
  
        return new OffsetFetchResult(fetchSql, arg);  
    }  
  
}