package com.allclear.socialhub.common.config;

import com.allclear.socialhub.common.converter.StringToStatisticTypeConverter;
import com.allclear.socialhub.common.converter.StringToStatisticValueConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConverterConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {

        registry.addConverter(new StringToStatisticValueConverter());
        registry.addConverter(new StringToStatisticTypeConverter());
    }

}
