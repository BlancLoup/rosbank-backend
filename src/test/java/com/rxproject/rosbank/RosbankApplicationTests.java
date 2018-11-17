package com.rxproject.rosbank;

import com.rxproject.rosbank.model.Account;
import com.rxproject.rosbank.model.Card;
import com.rxproject.rosbank.model.User;
import com.rxproject.rosbank.repository.AccountRepository;
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

import javax.persistence.Table;
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

	@Autowired
	AccountRepository accountRepository;

	@Test
	public void customTest(){
		User user = userRepository.findByCardNo("3428429766619371");
		int bp = 42;
	}

	@Test
	public void init(){
		Account account = new Account();
		account.setAccountNumber("564839856472");
		account.setBalance(15000d);
		account.setCurrency(Account.Currency.RUR);
		account.setStartDate(new Date());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 4);
		account.setExpDate(calendar.getTime());
		account.setBic("044525256");
		account.setInn("7730060164");
		account.setInvest(false);

		User user = userRepository.findById(1L).orElse(null);

		Card card = new Card();
		card.setUser(user);
		card.setAccount(account);
		card.setBankName("RxBank");
		card.setCardType(Card.CardType.CREDIT);
		card.setBlocked(false);
		card.setApplePayAllowed(true);
		card.setUserName("MasterCard Black");
		card.setPaySystem("MasterCard");
		card.setCardNumber("3428429766619372");
		card.setHolderName("Mr. Cardholder");
		card.setPercent(8d);
		card.setOverdue(16d);
		card.setFreeLimit(12d);
		card.setCreditLimit(90d);

		accountRepository.save(account);
		cardService.save(card);

	}

}
