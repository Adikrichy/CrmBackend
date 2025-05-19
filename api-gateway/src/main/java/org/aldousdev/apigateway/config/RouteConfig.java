// RouteConfig.java
package org.aldousdev.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Staff Service Routes
                .route("staff-service", r -> r
                        .path("/api/v1/staff/**")
                        .filters(f -> f
                                .stripPrefix(0)
                                .addRequestHeader("X-Service-Name", "staff-service"))
                        .uri("lb://STAFF-SERVICE"))

                // Notification Service Routes
                .route("notification-service", r -> r
                        .path("/api/v1/notifications/**")
                        .filters(f -> f
                                .stripPrefix(0)
                                .addRequestHeader("X-Service-Name", "notification-service"))
                        .uri("lb://NOTIFICATION-SERVICE"))

                // Auth Service Routes
                .route("auth-service", r -> r
                        .path("/api/v1/auth/**")
                        .filters(f -> f
                                .stripPrefix(0)
                                .addRequestHeader("X-Service-Name", "auth-service"))
                        .uri("lb://AUTH-SERVICE"))

                .build();
    }
}