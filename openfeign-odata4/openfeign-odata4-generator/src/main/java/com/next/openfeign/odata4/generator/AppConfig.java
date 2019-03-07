package com.next.openfeign.odata4.generator;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import com.next.openfeign.odata4.jaxb.Edmx;

@Configuration
public class AppConfig 
{
	
	@Value("${metadata:../openfeign-odata4-example/src/main/resources/$metadata.xml}")
	String metadataFileName;
	
	@Bean Edmx edmx() throws Exception
	{
		JAXBContext jaxbContext = JAXBContext.newInstance(Edmx.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		
		InputStream is = ResourceUtils.getURL(metadataFileName).openStream();
		XMLStreamReader xsr = XMLInputFactory.newFactory().createXMLStreamReader(is);
		XMLReaderWithoutNamespace xr = new XMLReaderWithoutNamespace(xsr);
		Edmx edmx = (Edmx) jaxbUnmarshaller.unmarshal(xr);		
		return edmx;		
	}

	@Bean FunctionManager functionManager(Edmx edmx)
	{
		FunctionManager functionManager = new FunctionManager(edmx);
		return functionManager;
	}
	
	@Bean BindableFunctionImport bindableFunction(Edmx edmx)
	{
		BindableFunctionImport rt = new BindableFunctionImport(edmx);
		return rt;
	}
}
