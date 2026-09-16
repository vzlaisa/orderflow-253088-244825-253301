/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package mx.edu.orderflow.orders;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class OrderControllerTest {
    
    @Test
    void controllerEndpointsWork() {
        OrderService service = new OrderService();
        OrderController controller = new OrderController(service);
        
        CreateOrderRequest request = new CreateOrderRequest("student-1", new BigDecimal(100.00));
        ResponseEntity<?> response = controller.create(request);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        
        assertEquals(1, controller.list().size());
        
        Order createdOrder = (Order) response.getBody();
        assertEquals(HttpStatus.OK, controller.get(createdOrder.id()).getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, controller.get(9999L).getStatusCode());
    }
    
    @Test
    void controllerHandlesBadRequest() {
        OrderService service = new OrderService();
        OrderController controller = new OrderController(service);
        
        CreateOrderRequest invalidRequest = new CreateOrderRequest("", new BigDecimal(100.00));
        ResponseEntity<?> response = controller.create(invalidRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    
}
