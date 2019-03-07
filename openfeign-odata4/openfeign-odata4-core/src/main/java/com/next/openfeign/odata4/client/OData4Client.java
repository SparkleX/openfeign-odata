package com.next.openfeign.odata4.client;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalTime;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import feign.Feign;
import feign.Feign.Builder;
import feign.Logger;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
public class OData4Client 
{
	protected String url;
	protected Builder builder;
	public static OData4Client inSecureClient(String url) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException
	{
		CloseableHttpClient httpClients = HttpClients
        .custom()
        .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustSelfSignedStrategy.INSTANCE).build())
        .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
        .build();
		
		Builder builder = Feign.builder().client(new ApacheHttpClient(httpClients))
				.logger(new Slf4jLogger())
				.logLevel(Logger.Level.FULL);
		
		return new OData4Client(builder, url);
	}
	public static OData4Client defaultClient(String url) 
	{
		Builder builder = Feign.builder()
				.client(new ApacheHttpClient())
				.logger(new Slf4jLogger())
				.logLevel(Logger.Level.FULL);
		
		return new OData4Client(builder, url);
	}
	public OData4Client(Builder feignBuilder, String url)
	{
		this.url = url;
		
		ObjectMapper mapper = new ObjectMapper();
		SimpleModule module = new SimpleModule();
		module.addDeserializer(LocalDate.class, new LocalDateDeserializer());
		module.addDeserializer(LocalTime.class, new LocalTimeDeserializer());
		module.addSerializer(LocalDate.class, new LocalDateSerializer());
		module.addSerializer(LocalTime.class, new LocalTimeSerializer());
		
		mapper.registerModule(module);
		mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.setSerializationInclusion(Include.NON_NULL);
	    mapper.setVisibility(PropertyAccessor.ALL, Visibility.NONE);

		builder = feignBuilder
				.decoder(new JacksonDecoder(mapper))
				.encoder(new JacksonEncoder(mapper));
	}
	public <T> T target(Class<T> clazz)
	{
		return builder.target(clazz, url);
	}
}
