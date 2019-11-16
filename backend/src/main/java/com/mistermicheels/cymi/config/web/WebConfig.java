package com.mistermicheels.cymi.config.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private ObjectMapper mapper;

    @Autowired
    public WebConfig(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /*
     * required to use Jackson configuration with @EnableWebMvc
     * see also https://stackoverflow.com/a/40711797
     * see also https://stackoverflow.com/a/56630093
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.stream().filter(x -> x instanceof MappingJackson2HttpMessageConverter)
                .forEach(x -> ((MappingJackson2HttpMessageConverter) x).setObjectMapper(mapper));
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        // do nothing instead of configurer.enable();
        // this prevents Spring from using DefaultServletHttpRequestHandler
        // this way, Spring will respect spring.mvc.throwExceptionIfNoHandlerFound=true
        // all of this allows us to handle 404 using @ControllerAdvice
        // see also https://stackoverflow.com/a/54117864
    }

}