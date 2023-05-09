package com.yassir.logger;

import ch.qos.logback.classic.spi.ILoggingEvent;

import com.google.cloud.logging.LogEntry.Builder;
import com.google.cloud.logging.logback.LoggingEventEnhancer;
import com.google.cloud.logging.Payload;
import com.google.cloud.logging.Severity;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.MDC;

public class YassirCustomEnhancer implements LoggingEventEnhancer {
	
	@Override
    public void enhanceLogEntry(Builder builder, ILoggingEvent e) {
		
		builder.clearLabels();

        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        if (mdcContext != null) {
        	
        	if(mdcContext.containsKey("environment")) {
        		String environment = mdcContext.get("environment");
                builder.addLabel("environment", environment);
        	}
        	
        	if(mdcContext.containsKey("squad")) {
        		String squad = mdcContext.get("squad");
                builder.addLabel("squad", squad);
        	}
        	
        	if(mdcContext.containsKey("domain")) {
        		String domain = mdcContext.get("domain");
                builder.addLabel("domain", domain);
        	}
        	
        	
            
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("message", e.getMessage());
        if(builder.build().getSeverity() == Severity.ERROR){

            HashMap<String, Object> source = new HashMap<>();
            source.put("lineNumber", e.getCallerData()[0].getLineNumber());
            source.put("methodName", e.getCallerData()[0].getMethodName());
            source.put("className", e.getCallerData()[0].getClassName());
            source.put("fileName", e.getCallerData()[0].getFileName());

            map.put("stack_trace", builder.build().getPayload().getData());
            map.put("source", source);
        }
        
        
        builder.setPayload(
            Payload.JsonPayload.of(map)
        );
        
    }

}
