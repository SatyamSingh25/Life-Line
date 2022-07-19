package com.hu22.bloodBankBackendPrivate;

import com.hu22.bloodBankBackendPrivate.dto.BloodBankRequest;
import com.hu22.bloodBankBackendPrivate.dto.JwtRequest;
import com.hu22.bloodBankBackendPrivate.dto.OrderConfirmRequest;
import com.hu22.bloodBankBackendPrivate.entities.BloodBank;
import com.hu22.bloodBankBackendPrivate.entities.Role;
import com.hu22.bloodBankBackendPrivate.entities.User;
import com.hu22.bloodBankBackendPrivate.repositories.BloodBankRepository;
import com.hu22.bloodBankBackendPrivate.repositories.UserRepository;
import com.hu22.bloodBankBackendPrivate.services.BloodBankService;
import com.hu22.bloodBankBackendPrivate.services.JwtService;
import com.hu22.bloodBankBackendPrivate.services.UserService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//unit testing
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BloodBankBackendPrivateApplicationTests {

    @Autowired
    public UserService userService;

    @Autowired
    public JwtService jwtService;

    @Autowired
    public BloodBankService bloodBankService;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public BloodBankRepository bloodBankRepository;

    @Test
    @Order(1)
    public void registerUser(){
        User testUser = new User("raghav@gmail.com", "raghav", "singh", "raghav@pass",
                "27-08-1998", "O+", "gorakhpur", "7905226359");

        Role GeneralRole = new Role();
        GeneralRole.setRoleName("USER");
        GeneralRole.setRoleDescription("USER role");
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(GeneralRole);
        testUser.setRoles(userRoles);

        assertNotNull(userService.registerNewUser(testUser));
    }

    @Test
    @Order(2)
    public void signIn() throws Exception {
        JwtRequest jwtRequest = new JwtRequest();
        jwtRequest.setEmail("raghav@gmail.com");
        jwtRequest.setUserPassword("raghav@pass");

        assertNotNull(jwtService.createJwtToken(jwtRequest));
    }

    @Test
    @Order(3)
    public void addRoleToUser(){
        userService.addRoleToUser("raghav@gmail.com");
        User userResponse = userRepository.findById("raghav@gmail.com").get();
        assertEquals(userResponse.getRoles().size(), 2);
    }


    @Test
    @Order(5)
    public void addBloodBank(){
        BloodBank testBloodBank = new BloodBank("testBank", 1L,2L,3L,4L,5L,6L,7L,8L, "Kanpur", "1919191919", "test@support.com" );
        assertNotNull(bloodBankService.addNewBloodBank(testBloodBank));
        Long id = bloodBankRepository.findByContact("1919191919").getId();
        assertNotNull(bloodBankRepository.findByid(id));

        bloodBankRepository.deleteById(id);
    }

    @Test
    @Order(6)
    public void removeUserRole(){
        userService.removeRole("satyamsingh72@deloitte.com");
        User userResponse = userRepository.findById("satyamsingh72@deloitte.com").get();
        assertEquals(userResponse.getRoles().size(), 1);
    }

    @Test
    @Order(7)
    public void deleteBloodBank(){
        BloodBank testBloodBank = new BloodBank("testBank", 1L,2L,3L,4L,5L,6L,7L,8L, "Kanpur", "1919191919", "test@support.com" );
        bloodBankService.addNewBloodBank(testBloodBank);
        Long t1 = Long.valueOf(bloodBankRepository.findAll().size());

        BloodBankRequest bloodBankRequest = new BloodBankRequest();
        bloodBankRequest.setBloodBankName("testBank");
        bloodBankRequest.setContact("1919191919");

        bloodBankService.deleteBloodBank(bloodBankRequest);
        Long t2 = Long.valueOf(bloodBankRepository.findAll().size());
        assertEquals(t2+1, t1);
    }

    @Test
    @Order(8)
    public void confirmOrder_A(){
        BloodBank testBloodBank = new BloodBank("Shashwat Blood Bank", 100L,221L,123L,443L,556L,612L,718L,890L, "Kanpur", "9857406932", "test@support.com" );
        bloodBankService.addNewBloodBank(testBloodBank);

        OrderConfirmRequest orderConfirmRequest = new OrderConfirmRequest();
        orderConfirmRequest.setBloodBankName("Shashwat Blood Bank");
        orderConfirmRequest.setBloodGroup("A+");
        orderConfirmRequest.setUnit(100L);
        orderConfirmRequest.setPhone("9857406932");

        assertNotNull(userService.confirmOrderAndMakeTransaction("satyamsingh72@deloitte.com" , orderConfirmRequest)); //Accepted
        orderConfirmRequest.setUnit(1200L);
        assertNotNull(userService.confirmOrderAndMakeTransaction("satyamsingh72@deloitte.com" , orderConfirmRequest)); //Failed

        BloodBankRequest bloodBankRequest = new BloodBankRequest();
        bloodBankRequest.setBloodBankName("Shashwat Blood Bank");
        bloodBankRequest.setContact("9857406932");
        bloodBankService.deleteBloodBank(bloodBankRequest);
    }

    @Test
    @Order(9)
    public void confirmOrder_A2(){
        BloodBank testBloodBank = new BloodBank("Shashwat Blood Bank", 100L,221L,123L,443L,556L,612L,718L,890L, "Kanpur", "9857406932", "test@support.com" );
        bloodBankService.addNewBloodBank(testBloodBank);

        OrderConfirmRequest orderConfirmRequest = new OrderConfirmRequest();
        orderConfirmRequest.setBloodBankName("Shashwat Blood Bank");
        orderConfirmRequest.setBloodGroup("A-");
        orderConfirmRequest.setUnit(10L);
        orderConfirmRequest.setPhone("9857406932");

        assertNotNull(userService.confirmOrderAndMakeTransaction("satyamsingh72@deloitte.com" , orderConfirmRequest)); //Accepted
        orderConfirmRequest.setUnit(1200L);
        assertNotNull(userService.confirmOrderAndMakeTransaction("satyamsingh72@deloitte.com" , orderConfirmRequest)); //Failed

        BloodBankRequest bloodBankRequest = new BloodBankRequest();
        bloodBankRequest.setBloodBankName("Shashwat Blood Bank");
        bloodBankRequest.setContact("9857406932");
        bloodBankService.deleteBloodBank(bloodBankRequest);
    }

    @Test
    @Order(10)
    public void confirmOrder_O(){
        BloodBank testBloodBank = new BloodBank("Shashwat Blood Bank", 100L,221L,123L,443L,556L,612L,718L,890L, "Kanpur", "9857406932", "test@support.com" );
        bloodBankService.addNewBloodBank(testBloodBank);

        OrderConfirmRequest orderConfirmRequest = new OrderConfirmRequest();
        orderConfirmRequest.setBloodBankName("Shashwat Blood Bank");
        orderConfirmRequest.setBloodGroup("O+");
        orderConfirmRequest.setUnit(123L);
        orderConfirmRequest.setPhone("9857406932");

        assertNotNull(userService.confirmOrderAndMakeTransaction("satyamsingh72@deloitte.com" , orderConfirmRequest)); //Accepted
        orderConfirmRequest.setUnit(1200L);
        assertNotNull(userService.confirmOrderAndMakeTransaction("satyamsingh72@deloitte.com" , orderConfirmRequest)); //Failed

        BloodBankRequest bloodBankRequest = new BloodBankRequest();
        bloodBankRequest.setBloodBankName("Shashwat Blood Bank");
        bloodBankRequest.setContact("9857406932");
        bloodBankService.deleteBloodBank(bloodBankRequest);
    }

    @Test
    @Order(11)
    public void confirmOrder_O2(){
        BloodBank testBloodBank = new BloodBank("Shashwat Blood Bank", 100L,221L,123L,443L,556L,612L,718L,890L, "Kanpur", "9857406932", "test@support.com" );
        bloodBankService.addNewBloodBank(testBloodBank);

        OrderConfirmRequest orderConfirmRequest = new OrderConfirmRequest();
        orderConfirmRequest.setBloodBankName("Shashwat Blood Bank");
        orderConfirmRequest.setBloodGroup("O-");
        orderConfirmRequest.setUnit(718L);
        orderConfirmRequest.setPhone("9857406932");

        assertNotNull(userService.confirmOrderAndMakeTransaction("satyamsingh72@deloitte.com" , orderConfirmRequest)); //Accepted
        orderConfirmRequest.setUnit(1200L);
        assertNotNull(userService.confirmOrderAndMakeTransaction("satyamsingh72@deloitte.com" , orderConfirmRequest)); //Failed

        BloodBankRequest bloodBankRequest = new BloodBankRequest();
        bloodBankRequest.setBloodBankName("Shashwat Blood Bank");
        bloodBankRequest.setContact("9857406932");
        bloodBankService.deleteBloodBank(bloodBankRequest);
    }

    @Test
    @Order(12)
    public void confirmOrder_B(){
        BloodBank testBloodBank = new BloodBank("Shashwat Blood Bank", 100L,221L,123L,443L,556L,612L,718L,890L, "Kanpur", "9857406932", "test@support.com" );
        bloodBankService.addNewBloodBank(testBloodBank);

        OrderConfirmRequest orderConfirmRequest = new OrderConfirmRequest();
        orderConfirmRequest.setBloodBankName("Shashwat Blood Bank");
        orderConfirmRequest.setBloodGroup("B+");
        orderConfirmRequest.setUnit(443L);
        orderConfirmRequest.setPhone("9857406932");

        assertNotNull(userService.confirmOrderAndMakeTransaction("satyamsingh72@deloitte.com" , orderConfirmRequest)); //Accepted
        orderConfirmRequest.setUnit(1200L);
        assertNotNull(userService.confirmOrderAndMakeTransaction("satyamsingh72@deloitte.com" , orderConfirmRequest)); //Failed

        BloodBankRequest bloodBankRequest = new BloodBankRequest();
        bloodBankRequest.setBloodBankName("Shashwat Blood Bank");
        bloodBankRequest.setContact("9857406932");
        bloodBankService.deleteBloodBank(bloodBankRequest);
    }

    @Test
    @Order(13)
    public void confirmOrder_B2(){
        BloodBank testBloodBank = new BloodBank("Shashwat Blood Bank", 100L,221L,123L,443L,556L,612L,718L,890L, "Kanpur", "9857406932", "test@support.com" );
        bloodBankService.addNewBloodBank(testBloodBank);

        OrderConfirmRequest orderConfirmRequest = new OrderConfirmRequest();
        orderConfirmRequest.setBloodBankName("Shashwat Blood Bank");
        orderConfirmRequest.setBloodGroup("B-");
        orderConfirmRequest.setUnit(890L);
        orderConfirmRequest.setPhone("9857406932");

        assertNotNull(userService.confirmOrderAndMakeTransaction("satyamsingh72@deloitte.com" , orderConfirmRequest)); //Accepted
        orderConfirmRequest.setUnit(1200L);
        assertNotNull(userService.confirmOrderAndMakeTransaction("satyamsingh72@deloitte.com" , orderConfirmRequest)); //Failed

        BloodBankRequest bloodBankRequest = new BloodBankRequest();
        bloodBankRequest.setBloodBankName("Shashwat Blood Bank");
        bloodBankRequest.setContact("9857406932");
        bloodBankService.deleteBloodBank(bloodBankRequest);
    }

    @Test
    @Order(14)
    public void confirmOrder_AB(){
        BloodBank testBloodBank = new BloodBank("Shashwat Blood Bank", 100L,221L,123L,443L,556L,612L,718L,890L, "Kanpur", "9857406932", "test@support.com" );
        bloodBankService.addNewBloodBank(testBloodBank);

        OrderConfirmRequest orderConfirmRequest = new OrderConfirmRequest();
        orderConfirmRequest.setBloodBankName("Shashwat Blood Bank");
        orderConfirmRequest.setBloodGroup("AB+");
        orderConfirmRequest.setUnit(221L);
        orderConfirmRequest.setPhone("9857406932");

        assertNotNull(userService.confirmOrderAndMakeTransaction("satyamsingh72@deloitte.com" , orderConfirmRequest)); //Accepted
        orderConfirmRequest.setUnit(1200L);
        assertNotNull(userService.confirmOrderAndMakeTransaction("satyamsingh72@deloitte.com" , orderConfirmRequest)); //Failed

        BloodBankRequest bloodBankRequest = new BloodBankRequest();
        bloodBankRequest.setBloodBankName("Shashwat Blood Bank");
        bloodBankRequest.setContact("9857406932");
        bloodBankService.deleteBloodBank(bloodBankRequest);
    }

    @Test
    @Order(15)
    public void confirmOrder_AB2(){
        BloodBank testBloodBank = new BloodBank("Shashwat Blood Bank", 100L,221L,123L,443L,556L,612L,718L,890L, "Kanpur", "9857406932", "test@support.com" );
        bloodBankService.addNewBloodBank(testBloodBank);

        OrderConfirmRequest orderConfirmRequest = new OrderConfirmRequest();
        orderConfirmRequest.setBloodBankName("Shashwat Blood Bank");
        orderConfirmRequest.setBloodGroup("AB-");
        orderConfirmRequest.setUnit(612L);
        orderConfirmRequest.setPhone("9857406932");

        assertNotNull(userService.confirmOrderAndMakeTransaction("satyamsingh72@deloitte.com" , orderConfirmRequest)); //Accepted
        orderConfirmRequest.setUnit(1200L);
        assertNotNull(userService.confirmOrderAndMakeTransaction("satyamsingh72@deloitte.com" , orderConfirmRequest)); //Failed

        BloodBankRequest bloodBankRequest = new BloodBankRequest();
        bloodBankRequest.setBloodBankName("Shashwat Blood Bank");
        bloodBankRequest.setContact("9857406932");
        bloodBankService.deleteBloodBank(bloodBankRequest);
    }

    @Test
    @Order(16)
    public void findBloodBank(){
        BloodBank testBloodBank = new BloodBank("Shashwat Blood Bank", 100L,221L,123L,443L,556L,612L,718L,890L, "Kanpur", "9857406932", "test@support.com" );
        bloodBankService.addNewBloodBank(testBloodBank);
        BloodBank bloodBankResponse = bloodBankRepository.findByContact("9857406932");

        assertNotNull(bloodBankService.findBloodBank(bloodBankResponse.getId()));

        BloodBankRequest bloodBankRequest = new BloodBankRequest();
        bloodBankRequest.setBloodBankName("Shashwat Blood Bank");
        bloodBankRequest.setContact("9857406932");
        bloodBankService.deleteBloodBank(bloodBankRequest);
    }

    @Test
    @Order(16)
    public void bloodBankUpdate(){
        BloodBank testBloodBank = new BloodBank("Shashwat Blood Bank", 100L,221L,123L,443L,556L,612L,718L,890L, "Kanpur", "9857406932", "test@support.com" );
        bloodBankService.addNewBloodBank(testBloodBank);
        BloodBank bloodBankResponse = bloodBankRepository.findByContact("9857406932");

        bloodBankResponse.setApositive(10L);
        bloodBankResponse.setAnegative(11L);
        bloodBankResponse.setOpositive(20L);
        bloodBankResponse.setOnegative(21L);
        bloodBankResponse.setBpositive(30L);
        bloodBankResponse.setBnegative(31L);
        bloodBankResponse.setAbpositive(40L);
        bloodBankResponse.setAbnegative(41L);

        assertNotNull(bloodBankService.updateBloodBankInfo(bloodBankResponse));
    }
}
