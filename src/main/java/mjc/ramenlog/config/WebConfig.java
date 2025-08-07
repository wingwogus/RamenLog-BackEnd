package mjc.ramenlog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("https://preview--ramen-korea-radar.lovable.app", "https://ramenlog.kro.kr", "http://localhost:8081")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true); // 중요!
    }
}