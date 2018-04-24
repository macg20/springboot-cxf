package pl.mgie.server.endpoint;

import org.springframework.stereotype.Service;
import pl.mgie.server.domian.MessageRequest;
import pl.mgie.server.domian.MessageResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.time.LocalDate;

@Service
@WebService(serviceName = "SimpleMessageEndpoint", targetNamespace = "http://server.mgie.pl/MessageService")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public class SimpleMessageEndpoint {

    @WebMethod
    public MessageResponse getMessage(@WebParam(name = "request") MessageRequest request) {
        MessageResponse response = new MessageResponse();
        response.setResponse(generateMessage(request.getFirstName(), request.getLastName()));
        return response;
    }

    private String generateMessage(String firstName, String lastName) {
        return String.format("Hello %s %s! Today is %s", firstName, lastName, LocalDate.now().toString());
    }


}
