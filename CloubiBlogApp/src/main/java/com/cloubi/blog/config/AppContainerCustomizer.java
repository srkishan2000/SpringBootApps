package com.cloubi.blog.config;

import java.net.Socket;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class AppContainerCustomizer
            implements WebServerFactoryCustomizer< ConfigurableWebServerFactory > {
	
	private static final String HOST = "localhost";
	private static final int PORT = 8080;
   
    @Override
    public void customize(ConfigurableWebServerFactory factory) {
    	
    	if(serverListening(HOST,PORT)) {
    		factory.setPort(0);
    	} else {
    		factory.setPort(PORT);
    	}
    }
       
    public static boolean serverListening(String host, int port)
	{
    	Socket s = null;
	    try
	    {
	    	s = new Socket(host, port);
	        return true;
	    }
	    catch (Exception e)
	    {
	    	return false;
	    }
	    finally
	    {
	        if(s != null)
	            try {s.close();}
	            catch(Exception e){}
	    }
	}
    
}
