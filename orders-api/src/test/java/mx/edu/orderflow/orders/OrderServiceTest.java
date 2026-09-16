package mx.edu.orderflow.orders; 

import static org.junit.jupiter.api.Assertions.*; 
import java.math.BigDecimal; 
import org.junit.jupiter.api.Test;

class OrderServiceTest { 
    @Test 
    void createsValidOrder(){
        var s=new OrderService(); 
        var o=s.create("student-1",new BigDecimal("150.00")); 
        assertEquals(OrderStatus.CREATED,o.status()); 
        assertEquals("student-1",o.customerId());
    } 
    
    @Test
    void rejectsNegativeTotal(){
        var s=new OrderService(); 
        assertThrows(IllegalArgumentException.class,()->s.create("student-1",new BigDecimal("-1")));
    } 
    
    @Test
    void listAndFindOthers() {
        var s=new OrderService(); 
        var o=s.create("student-2",new BigDecimal("200.00")); 
        
        assertEquals(1, s.list().size());
        assertTrue(s.find(o.id()).isPresent());
    }
    
}
