package com.equabli.collectprism.config;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.equabli.domain.entity.PmUtil;
//import com.equabli.domain.requestInterceptor.AuthInterceptor;
import com.equabli.domain.requestInterceptor.TraceInterceptor;
import com.equabli.eqauthservice.auth.ProjectAuthKeys;
import com.equabli.eqauthservice.interceptor.AuthInterceptor;
import com.equabli.eqauthservice.interceptor.PartnerInterceptor;
//import com.equabli.domain.security.ProjectAuthKeys;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer  {

	public WebConfig(DataSource dataSource) {
		ProjectAuthKeys.loadAuthKeyPair(dataSource);
		PmUtil.loadData(dataSource);
		PmUtil.loadRegulatoryBodyData(dataSource);
	}

	@Override 
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) { 
		 converters.add(new MappingJackson2HttpMessageConverter());
		for(HttpMessageConverter<?> converter: converters) {
            if(converter instanceof MappingJackson2HttpMessageConverter) {
                ObjectMapper mapper = ((MappingJackson2HttpMessageConverter)converter).getObjectMapper();
                mapper.setSerializationInclusion(Include.NON_NULL);
            }
        }
    }

	@Autowired
	AuthInterceptor authInerceptor;

	@Autowired
	TraceInterceptor traceInterceptor;

	@Autowired
	PartnerInterceptor partnerInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(traceInterceptor);
		registry.addInterceptor(authInerceptor);

		List<String> partnerExcludePathList  = new ArrayList<String>();
		partnerExcludePathList.add("/getErrorWarnMessageDetailsForClient");
		partnerExcludePathList.add("/data-scrubbing/getErrorWarnMessageDetailsForClient");
		registry.addInterceptor(partnerInterceptor).addPathPatterns(partnerExcludePathList);
	}
}
