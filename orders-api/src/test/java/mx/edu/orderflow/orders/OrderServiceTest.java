package mx.edu.orderflow.orders; 

import java.math.BigDecimal; 
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderServiceTest { 
    @Test 
    void createsValidOrder(){
        // Arrange
        var service = new OrderService();
        String customerId = "student-1";
        BigDecimal total = new BigDecimal("150.00");
        // Act
        var order = service.create(customerId, total);
        // Assert
        assertEquals(OrderStatus.CREATED, order.status());
        assertEquals("student-1", order.customerId());
        assertEquals(total, order.total());
        assertEquals(1001, order.id());
    }
    
    @Test
    void listAndFindOthers() {
        // Arrange
        var service = new OrderService();
        // Act
        var order = service.create("student-2",new BigDecimal("200.00")); 
        // Assert
        assertEquals(1, service.list().size());
        assertTrue(service.find(order.id()).isPresent());
    }
    
    @Test
    void rejectsNegativeTotal() {
        // Arrange
        var service = new OrderService();
        BigDecimal negativeTotal = new BigDecimal("-1");
        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> service.create("student-1", negativeTotal));
        // Assert
        assertEquals("total must be positive", exception.getMessage());
    } 
    
    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void rejectBlankCustomerId(String customerId) {
        // Arrange
        var service = new OrderService();
        BigDecimal total = new BigDecimal("100.00");
        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> service.create(customerId, total));
        // Assert
        assertEquals("customerId required", exception.getMessage());
    }
    
    @Test
    void rejectNullCostumerId() {
        // Arrange
        var service = new OrderService();
        BigDecimal total = new BigDecimal("100.00");
        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> service.create(null, total));
        // Assert
        assertEquals("customerId required", exception.getMessage());
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"0", "-0.01", "-100"})
    void rejectZeroOrNegativeTotal(String totalInvalid) {
        // Arrange
        var service = new OrderService();
        BigDecimal total = new BigDecimal(totalInvalid);
        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> service.create("student-1", total));
        // Assert
        assertEquals("total must be positive", exception.getMessage());
    }
    
    @Test
    void listsCreatedOrders() {
        // Arrange
        var service =new OrderService();
        service.create("student-1", new BigDecimal("100.00"));
        service.create("student-2", new BigDecimal("150.00"));
        // Act
        var orders = service.list();
        // Assert
        assertEquals(2, orders.size());
        assertEquals("student-1", orders.get(0).customerId());
        assertEquals("student-2", orders.get(1).customerId());
    }
    
    @Test
    void findExistingOrder() {
        // Arrange
        var service = new OrderService();
        var existingOrder = service.create("student-1", new BigDecimal("100.00"));
        // Act
        Optional<Order> result = service.find(existingOrder.id());
        // Assert
        assertTrue(result.isPresent());
        assertEquals(existingOrder, result.get());
    }
    
    @Test
    void emptyWhenOrderDoesNotExist() {
        // Arrange
        var service = new OrderService();
        long nonexistentId = 999L;
        // Act
        Optional<Order> result = service.find(nonexistentId);
        // Assert
        assertTrue(result.isEmpty());
    }
    
    @Test
    void rejectsNullTotal() {
        // Arrange
        var service = new OrderService();
        // Act
        var exception = assertThrows(IllegalArgumentException.class, () -> service.create("student-1", null));
        // Assert
        assertEquals("total must be positive", exception.getMessage());
    }    
}
