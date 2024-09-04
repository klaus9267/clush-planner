package com.clush.planner.domain.common.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Operation(responses = {@ApiResponse(responseCode = "200", useReturnTypeSchema = true)})
@ResponseStatus(HttpStatus.OK)
public @interface SwaggerOK {
  String summary() default "";

  String description() default "";
}
