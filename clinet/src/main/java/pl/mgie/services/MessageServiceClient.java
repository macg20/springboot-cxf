package pl.mgie.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.mgie.server.messageservice.MessageRequest;
import pl.mgie.server.messageservice.MessageResponse;
import pl.mgie.server.messageservice.SimpleMessageEndpoint;
import pl.mgie.server.messageservice.SimpleMessageEndpoint_Service;


import javax.xml.ws.BindingProvider;

@Service
public class MessageServiceClient {

    @Value("${messageservice.address}")
    private String messageServiceEndpointAddres;


    public String callGetMessage(String firstName, String lastName) {

        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setFirstName(firstName);
        messageRequest.setLastName(lastName);

        SimpleMessageEndpoint_Service messageEndpoint = new SimpleMessageEndpoint_Service();
        SimpleMessageEndpoint port = messageEndpoint.getSimpleMessageEndpointPort();

        BindingProvider provider = (BindingProvider) port;
        provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, messageServiceEndpointAddres);
        MessageResponse response = port.getMessage(messageRequest);
        return response.getResponse();
    }
}
