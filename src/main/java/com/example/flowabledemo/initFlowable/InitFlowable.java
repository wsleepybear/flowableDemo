package com.example.flowabledemo.initFlowable;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;

/**
 * @author wangkai
 * @description:
 * @date 2024/5/8 16:09
 */

public class InitFlowable {
    private static final ProcessEngine processEngine;
    static {
        // 单机处理引擎配置信息实例
        StandaloneProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration();
        // 连接数据库的信息,将xxx替换为自己的数据库信息
        cfg.setJdbcUrl("jdbc:mysql://xxx.xx.xxx.xxx:3306/flowable?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        cfg.setJdbcUsername("xxxx");
        cfg.setJdbcPassword("xxxxxx");
        cfg.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        // 设置了true，确保在JDBC参数连接的数据库中，数据库表结构不存在时，会创建相应的表结构。
        cfg.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        // 通过配置获取执行引擎
        processEngine = cfg.buildProcessEngine();
    }

    public static void main(String[] args) {
        System.out.println(processEngine);
    }

}
