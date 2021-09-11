package com.dongfeng.test;

import org.checkerframework.checker.units.qual.A;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 动态数据源
 *
 * AbstractRoutingDataSource : 进行数据源路由的
 *
 * @author eastFeng
 * @date 2020-11-03 13:58
 */
public class DataSourceTest extends AbstractRoutingDataSource {

    ////
    AtomicInteger atomicInteger = new AtomicInteger(0);

    /**
     * 返回一个数据库的名称
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return null;
    }
}
