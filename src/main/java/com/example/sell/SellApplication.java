package com.example.sell;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.example.sell.converter.Date2Second;
import com.example.sell.converter.Date2SecondFilter;
import com.example.sell.converter.Enum2CodeFilter;
import com.example.sell.exception.ExceptionCode;
import com.example.sell.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EnableConfigurationProperties
@SpringBootApplication
@EnableCaching
@Slf4j
public class SellApplication {

	public static void main(String[] args) {
		SpringApplication.run(SellApplication.class, args);
	}

	@Bean
	public HttpMessageConverters fastjsonHttpMessageConverter(){
		//定义一个转换消息的对象
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

		//添加fastjson的配置信息 比如 ：是否要格式化返回的json数据
		FastJsonConfig fastJsonConfig = new FastJsonConfig();

		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		fastJsonConfig.setSerializeFilters(new Date2SecondFilter(),new Enum2CodeFilter());
		fastJsonConfig.setCharset(Charset.forName("utf-8"));
		//在转换器中添加配置信息
		fastConverter.setFastJsonConfig(fastJsonConfig);

		//防止中文乱码
		List<MediaType> fastMediaTypes = new ArrayList<MediaType>();
		fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
		fastConverter.setSupportedMediaTypes(fastMediaTypes);

		HttpMessageConverter<?> converter = fastConverter;

		return new HttpMessageConverters(converter);
	}
}
