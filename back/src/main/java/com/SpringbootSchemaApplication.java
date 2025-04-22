package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication//声明这是一个 Spring Boot 应用程序的主类（包含了 @Configuration、@EnableAutoConfiguration、@ComponentScan）
@MapperScan(basePackages = {"com.dao"})//告诉 MyBatis 扫描 com.dao 包下的接口，并将其注册为 Mapper（用于操作数据库）
public class SpringbootSchemaApplication extends SpringBootServletInitializer{//允许将 Spring Boot 应用部署为传统的 WAR 包到 Tomcat 或其他 Servlet 容器中

	public static void main(String[] args) {
		SpringApplication.run(SpringbootSchemaApplication.class, args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {
        return applicationBuilder.sources(SpringbootSchemaApplication.class);//用于 Servlet 容器启动时配置应用上下文（支持外部容器）
    }
}
