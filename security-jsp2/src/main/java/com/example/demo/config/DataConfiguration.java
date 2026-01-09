package com.example.demo.config;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.log4j.Log4j2;
/*
@Configuration
:스프링 애플리케이션을 만들 때 필요한 객체들을 직접(위임->스프링 컨테이너) new통해서 생성하지 않고
스프링 컨테이너가 대신 만들어서 관리해줌.-> 객체에 대한 라이프사이클 관리를 해줌.
-> init() -> service() -> destroy()
즉 @Configuration은(는) 빈(객체) 만드는 규칙을 모아 놓은 설정 관련 클래스 이다.

*/
@Log4j2
@Configuration
public class DatabaseConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Bean
    public DataSource dataSource() {
        DataSource dataSource = new HikariDataSource(hikariConfig());
        log.info("datasource : {}", dataSource);
        return dataSource;
    }
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        //classpath는 src/main/resourcs이고 해당 쿼리가 있는 xml 위치는 본인의 취향대로 위치키시고 그에 맞도록 설정해주면 된다.
        try {
            sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/mapper/**/*.xml"));
        } catch (Exception e) {
            log.warn("매퍼 파일을 로드할 수 없습니다: {}", e.getMessage());
            // 테스트 환경 등에서 매퍼가 없을 수 있으므로 계속 진행
        }
        return sqlSessionFactoryBean.getObject();
    }
    //mybatis-spring.jar -> SqlSessionTemplate -> 쿼리문을 요청하기 -> selectList, insert,
    //update, delete
    //객체 생성이 안된 상태에서 호출하면 NPE(NullPointerException)
    //필요할 때 스프링 컨테이너가 대신(자동관리) 필요한 객체를 주입해줌.
    //스프링에서는 빈을 관리하는 클래스가 2 가지 있다.
    //BeanFactory(spring-bean.jar),< ApplicationContext(spring-context.jar)
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
