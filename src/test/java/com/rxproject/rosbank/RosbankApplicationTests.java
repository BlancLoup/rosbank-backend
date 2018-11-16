package com.rxproject.rosbank;

import com.rxproject.rosbank.model.Card;
import com.rxproject.rosbank.model.User;
import com.rxproject.rosbank.repository.UserRepository;
import com.rxproject.rosbank.service.CardService;
import com.rxproject.rosbank.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RosbankApplication.class)
public class RosbankApplicationTests {

	@Autowired
	UserService userService;

	@Autowired
	CardService cardService;

	@Autowired
	UserRepository userRepository;

	@Test
	public void customTest(){
		User user = userRepository.findByCardNo("3428429766619371");
		int bp = 42;
	}

}
