package project.kidscafe.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("키즈카페 예약 서비스")
                .description("안녕하세요 !! 키즈카페 예약 서비스입니다.\uD83D\uDE0A  \n\n 키즈카페 예약 서비스의 주요 기능은 다음과 같습니다. \n - 수업 예약 \n - 수업 예약 취소 \n - 사용자 별 예약 이력 조회 \n - 매장, 수업 별 사용자 현황 조회")
                .version("1.0.0");
    }
}
