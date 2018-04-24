package pl.mgie.server.configuration;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.io.CachedOutputStream;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import pl.mgie.server.endpoint.SimpleMessageEndpoint;

import javax.xml.ws.Endpoint;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public ServletRegistrationBean wsDispatcherServlet() {
        CXFServlet cxfServlet = new CXFServlet();
        return new ServletRegistrationBean(cxfServlet, "/services/*");
    }

    @Bean(name = "cxf")
    public SpringBus springBus() {
        SpringBus bus = new SpringBus();

        bus.setApplicationContext(applicationContext);
        bus.setInInterceptors(Arrays.asList(new AbstractPhaseInterceptor(Phase.RECEIVE) {
            @Override
            public void handleMessage(Message message) throws Fault {
                message.put(Message.ENCODING, "UTF-8");
                InputStream is = message.getContent(InputStream.class);
                if (is != null) {
                    CachedOutputStream bos = new CachedOutputStream();
                    try {
                        IOUtils.copy(is, bos);
                        String soapMessage = new String(bos.getBytes());
                        System.out.println("-------------------------------------------");
                        System.out.println("Inbound message is " + soapMessage);
                        System.out.println("-------------------------------------------");
                        bos.flush();
                        message.setContent(InputStream.class, is);

                        is.close();
                        InputStream inputStream = new ByteArrayInputStream(soapMessage.getBytes());
                        message.setContent(InputStream.class, inputStream);
                        bos.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        }));


        bus.setOutInterceptors(Arrays.asList(new AbstractPhaseInterceptor(Phase.RECEIVE) {
            @Override
            public void handleMessage(Message message) throws Fault {
                System.out.println("message " + message);
                message.put(Message.ENCODING, "UTF-8");
                InputStream is = message.getContent(InputStream.class);
                if (is != null) {
                    CachedOutputStream bos = new CachedOutputStream();
                    try {
                        IOUtils.copy(is, bos);
                        String soapMessage = new String(bos.getBytes());
                        System.out.println("-------------------------------------------");
                        System.out.println("OutBound message is " + soapMessage);
                        System.out.println("-------------------------------------------");
                        bos.flush();
                        message.setContent(InputStream.class, is);

                        is.close();
                        InputStream inputStream = new ByteArrayInputStream(soapMessage.getBytes());
                        message.setContent(InputStream.class, inputStream);
                        bos.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        }));

        return bus;
    }

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), new SimpleMessageEndpoint());
        endpoint.publish("/SimpleMessageEndpoint");
        return endpoint;
    }

//    public static class MessageInterceptor extends AbstractPhaseInterceptor {
//        public MessageInterceptor() {
//        }
//
//        @Override
//        public void handleMessage(Message message) throws Fault {
//
//        }
//    }

}