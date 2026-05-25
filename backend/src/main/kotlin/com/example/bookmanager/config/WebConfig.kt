package com.example.bookmanager.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    // 本番では ALB 経由で同一オリジンになるため CORS 不要
    // ローカル開発時のみ Astro 開発サーバーからのアクセスを許可
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:4321")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
    }
}
