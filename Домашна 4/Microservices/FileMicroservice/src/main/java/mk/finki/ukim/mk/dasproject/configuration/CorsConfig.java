//package mk.finki.ukim.mk.dasproject.configuration;
//import org.springframework.web.filter.CorsFilter;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class CorsConfig {
//
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedOrigin("http://localhost:8082");  // Allow frontend
//        config.addAllowedMethod("*");  // Allow all HTTP methods (GET, POST, etc.)
//        config.addAllowedHeader("*");  // Allow all headers
//        config.setAllowCredentials(true);  // Allow cookies and credentials if needed
//        source.registerCorsConfiguration("/api/**", config);
//        return new CorsFilter(source);
//    }
//}
