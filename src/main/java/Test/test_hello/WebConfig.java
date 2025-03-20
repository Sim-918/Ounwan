package Test.test_hello;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // ✅ 게시글 이미지 & 동영상 서빙 (이미 정상 동작 중)
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + System.getProperty("user.home") + "/uploads/");

        // ✅ 프로필 이미지 서빙 추가
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + System.getProperty("user.home") + "/upload/");
    }
}