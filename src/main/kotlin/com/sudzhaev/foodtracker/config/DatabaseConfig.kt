package com.sudzhaev.foodtracker.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

@Configuration
class DatabaseConfig {

    @Bean
    fun dataSource(
            @Value("\${db.url}") url: String,
            @Value("\${db.username}") username: String,
            @Value("\${db.password}") password: String,
    ): DataSource {
        return DriverManagerDataSource().apply {
            setDriverClassName("org.postgresql.Driver")
            setUrl(url)
            setUsername(username)
            setPassword(password)
        }
    }

    @Bean
    fun jdbcTemplate(dataSource: DataSource): NamedParameterJdbcTemplate {
        return NamedParameterJdbcTemplate(dataSource)
    }
}
