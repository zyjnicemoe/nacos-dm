/*
 *   Copyright 1999-2022 Alibaba Group Holding Ltd.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.alibaba.nacos.plugin.datasource.impl.mysql;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.plugin.datasource.constants.DataSourceConstant;
import com.alibaba.nacos.plugin.datasource.constants.FieldConstant;
import com.alibaba.nacos.plugin.datasource.constants.TableConstant;
import com.alibaba.nacos.plugin.datasource.model.MapperContext;
import com.alibaba.nacos.plugin.datasource.model.MapperResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConfigInfoAggrMapperByDmTest {
    
    private ConfigInfoAggrMapperByMySql configInfoAggrMapperByMySql;
    
    @BeforeEach
    void setUp() throws Exception {
        configInfoAggrMapperByMySql = new ConfigInfoAggrMapperByMySql();
    }
    
    @Test
    void testBatchRemoveAggr() {
        List<String> datumList = Arrays.asList("1", "2", "3", "4", "5");
        String dataId = "data-id";
        String groupId = "group-id";
        String tenantId = "tenant-id";
        
        List<String> argList = CollectionUtils.list(dataId, groupId, tenantId);
        argList.addAll(datumList);
        
        MapperContext context = new MapperContext();
        context.putWhereParameter(FieldConstant.DATUM_ID, datumList);
        context.putWhereParameter(FieldConstant.DATA_ID, dataId);
        context.putWhereParameter(FieldConstant.GROUP_ID, groupId);
        context.putWhereParameter(FieldConstant.TENANT_ID, tenantId);
        
        MapperResult mapperResult = configInfoAggrMapperByMySql.batchRemoveAggr(context);
        String sql = mapperResult.getSql();
        List<Object> paramList = mapperResult.getParamList();
        
        assertEquals(sql,
                "DELETE FROM config_info_aggr WHERE data_id = ? AND group_id = ? AND tenant_id = ? " + "AND datum_id IN (?, ?, ?, ?, ?)");
        
        assertEquals(paramList, argList);
    }
    
    @Test
    void testAggrConfigInfoCount() {
        List<String> datumIds = Arrays.asList("1", "2", "3", "4", "5");
        String dataId = "data-id";
        String groupId = "group-id";
        String tenantId = "tenant-id";
        
        List<String> argList = CollectionUtils.list(dataId, groupId, tenantId);
        argList.addAll(datumIds);
        
        MapperContext context = new MapperContext();
        context.putWhereParameter(FieldConstant.DATUM_ID, datumIds);
        context.putWhereParameter(FieldConstant.IS_IN, true);
        context.putWhereParameter(FieldConstant.DATA_ID, dataId);
        context.putWhereParameter(FieldConstant.GROUP_ID, groupId);
        context.putWhereParameter(FieldConstant.TENANT_ID, tenantId);
        MapperResult mapperResult = configInfoAggrMapperByMySql.aggrConfigInfoCount(context);
        String sql = mapperResult.getSql();
        List<Object> paramList = mapperResult.getParamList();
        
        assertEquals(sql, "SELECT count(*) FROM config_info_aggr WHERE data_id = ? AND group_id = ? AND tenant_id = ? "
                + "AND datum_id IN (?, ?, ?, ?, ?)");
        assertEquals(paramList, argList);
    }
    
    @Test
    void testFindConfigInfoAggrIsOrdered() {
        String dataId = "data-id";
        String groupId = "group-id";
        String tenantId = "tenant-id";
        
        MapperContext context = new MapperContext();
        context.putWhereParameter(FieldConstant.DATA_ID, dataId);
        context.putWhereParameter(FieldConstant.GROUP_ID, groupId);
        context.putWhereParameter(FieldConstant.TENANT_ID, tenantId);
        
        MapperResult mapperResult = configInfoAggrMapperByMySql.findConfigInfoAggrIsOrdered(context);
        String sql = mapperResult.getSql();
        List<Object> paramList = mapperResult.getParamList();
        
        assertEquals(sql, "SELECT data_id,group_id,tenant_id,datum_id,app_name,content FROM "
                + "config_info_aggr WHERE data_id = ? AND group_id = ? AND tenant_id = ? ORDER BY datum_id");
        assertEquals(paramList, Arrays.asList(dataId, groupId, tenantId));
    }
    
    @Test
    void testFindConfigInfoAggrByPageFetchRows() {
        String dataId = "data-id";
        String groupId = "group-id";
        String tenantId = "tenant-id";
        Integer startRow = 0;
        Integer pageSize = 5;
        
        MapperContext context = new MapperContext();
        context.putWhereParameter(FieldConstant.DATA_ID, dataId);
        context.putWhereParameter(FieldConstant.GROUP_ID, groupId);
        context.putWhereParameter(FieldConstant.TENANT_ID, tenantId);
        context.setStartRow(startRow);
        context.setPageSize(pageSize);
        
        MapperResult mapperResult = configInfoAggrMapperByMySql.findConfigInfoAggrByPageFetchRows(context);
        String sql = mapperResult.getSql();
        List<Object> paramList = mapperResult.getParamList();
        
        assertEquals(sql, "SELECT data_id,group_id,tenant_id,datum_id,app_name,content FROM config_info_aggr WHERE "
                + "data_id= ? AND group_id= ? AND tenant_id= ? ORDER BY datum_id LIMIT 0,5");
        assertEquals(paramList, Arrays.asList(dataId, groupId, tenantId));
    }
    
    @Test
    void testFindAllAggrGroupByDistinct() {
        MapperResult mapperResult = configInfoAggrMapperByMySql.findAllAggrGroupByDistinct(null);
        assertEquals("SELECT DISTINCT data_id, group_id, tenant_id FROM config_info_aggr", mapperResult.getSql());
    }
    
    @Test
    void testGetTableName() {
        String tableName = configInfoAggrMapperByMySql.getTableName();
        assertEquals(TableConstant.CONFIG_INFO_AGGR, tableName);
    }
    
    @Test
    void testGetDataSource() {
        String dataSource = configInfoAggrMapperByMySql.getDataSource();
        assertEquals(DataSourceConstant.MYSQL, dataSource);
    }
}
