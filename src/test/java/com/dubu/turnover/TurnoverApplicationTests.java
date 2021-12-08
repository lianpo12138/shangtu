package com.dubu.turnover;

import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;
import org.junit.Test;

import java.util.Properties;

public class TurnoverApplicationTests {

    @Test
    public void generator() throws Exception {
        //表名，为空则全部
        String[] tableNames = new String[]{};
        //输出目录
        String outRoot = "code\\";

        String template = "src\\test\\resources\\templates";
        Properties properties = new Properties();
        properties.setProperty("outRoot", outRoot);
        properties.setProperty("basepackage", "com.dubu.turnover");
        properties.setProperty("jdbc.username", "root");
        properties.setProperty("jdbc.password", "lenovo123");
        properties.setProperty("jdbc.url", "jdbc:mysql://27.115.115.222:3308/shangtu?useSSL=false&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull");
        properties.setProperty("jdbc.driver", "com.mysql.jdbc.Driver");
        properties.setProperty("java_typemapping.java.sql.Timestamp", "java.util.Date");
        properties.setProperty("java_typemapping.java.sql.Date", "java.util.Date");
        properties.setProperty("java_typemapping.java.sql.Time", "java.util.Date");
        GeneratorProperties.setProperties(properties);
        GeneratorFacade generatorFacade = new GeneratorFacade();
        generatorFacade.generateByTable("card_user", template);

        
        
        
    }

}
