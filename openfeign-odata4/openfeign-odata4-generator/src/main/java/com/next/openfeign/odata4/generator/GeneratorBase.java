package com.next.openfeign.odata4.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;

import com.next.openfeign.odata4.jaxb.Edmx;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModel;
import freemarker.template.Version;

abstract class GeneratorBase 
{
	Template template;
	
	@Autowired
	Edmx edmx;
	
	@Autowired
	ODataUtil util;

	private String outputPath;
	
	String readResource(String url) throws FileNotFoundException, IOException
	{
		try (InputStream is = ResourceUtils.getURL(url).openStream()) 
		{
			String content = IOUtils.toString(is, StandardCharsets.UTF_8);
			return content;
		}
	}
	
	@Value("${output:./}")
	String outputBase;
	
	public void init(String templateFile,String lastPackage) throws Exception 
	{
		String content = readResource(templateFile);

		Configuration cfg = new Configuration(new Version(2, 3, 20));
		StringTemplateLoader dummyLoader = new StringTemplateLoader();
		dummyLoader.putTemplate("template", content);
		cfg.setTemplateLoader(dummyLoader);
		cfg.setDefaultEncoding("UTF-8");
		cfg.setLocale(Locale.US);
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		
		
		template = cfg.getTemplate("template");
		
		String packName = this.edmx.getDataServices().getSchema().getNamespace().replaceAll("\\.", "/").toLowerCase();
		this.outputPath = outputBase+"/"+packName+"/"+lastPackage;

	}

	protected void process(HashMap<String,Object> data, String outputFileName) throws TemplateException, IOException 
	{
		DefaultObjectWrapper wrapper = new DefaultObjectWrapper(new Version(2,3,20));
		TemplateModel statics = wrapper.getStaticModels();
		data.put("edmx", edmx);
		data.put("util", util);
		data.put("statics", statics);
		StringWriter sw = new StringWriter();
		template.process(data, sw);
		FileUtils.writeStringToFile(new File(outputPath + "/" + outputFileName), sw.toString(), "utf8");
	}
}
