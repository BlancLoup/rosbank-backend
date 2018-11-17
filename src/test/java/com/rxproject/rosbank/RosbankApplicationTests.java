package com.rxproject.rosbank;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rxproject.rosbank.model.*;
import com.rxproject.rosbank.repository.*;
import com.rxproject.rosbank.views.FormFabrics.FieldElements.DatePickerField;
import com.rxproject.rosbank.views.FormFabrics.FieldElements.LabelField;
import com.rxproject.rosbank.views.FormFabrics.FieldElements.PickerField;
import com.rxproject.rosbank.views.FormFabrics.FieldElements.SwitchField;
import com.rxproject.rosbank.views.FormFabrics.FormConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RosbankApplication.class)
public class RosbankApplicationTests {

	@Autowired
	AccountRepository accountRepository;
	@Autowired
	ButtonRepository buttonRepository;
	@Autowired
	CardRepository cardRepository;
	@Autowired
	CreditRepository creditRepository;
	@Autowired
	DepositRepository depositRepository;
	@Autowired
	DialogStateRepository dialogStateRepository;
	@Autowired
	FormRepository formRepository;
	@Autowired
	MessageRepository messageRepository;
	@Autowired
	PartnerOfferRepository partnerOfferRepository;
	@Autowired
	TransactionRepository transactionRepository;
	@Autowired
	UserRepository userRepository;

	@Test
	public void customTest(){
		JsonNodeFactory jnf = JsonNodeFactory.instance;
		ObjectNode on = jnf.objectNode();
		on.put("text","asdasdasdasd");
		on.put("photo", "asdasdasdasdasd");
		Form form = new Form();
		form.setJson(on.toString());
		formRepository.save(form);
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
		cardRepository.save(card);

	}

	@Test
	public void stateBuilder(){

		List<DialogState> dialogStateList = new ArrayList<>();

		Form form = formRepository.getOne(1L);

		DialogState dialogState = new DialogState();
		dialogState.setType(DialogState.Type.PHOTO);
		dialogState.setForm(form);
		dialogState.setEndpoint("/api/user/photostate");
		dialogState.setMessage("photo input");
		dialogStateList.add(dialogState);


		dialogStateRepository.saveAll(dialogStateList);
		//buttonRepository.saveAll(buttonList);
	}

	@Test
	public void formBuilder(){

		FormConstructor formContructor = new FormConstructor();

		DatePickerField datePickerField = new DatePickerField();
		datePickerField.setValue(new Date());
		datePickerField.setLabel("Выберите дату");
		datePickerField.setName("date_picker");
		formContructor.addElement(datePickerField);

		LabelField labelField = new LabelField();
		labelField.setName("label_field");
		labelField.setLabel("какой то лейбл");
		formContructor.addElement(labelField);

		PickerField pickerField = new PickerField();
		pickerField.setName("picker");
		pickerField.setLabel("picker");
		List<String> values = new ArrayList<>();
		values.add("value1");
		values.add("value2");
		values.add("value3");
		pickerField.setVariants(values);
		formContructor.addElement(pickerField);

		SwitchField switchField = new SwitchField();
		switchField.setName("sw");
		switchField.setLabel("switch");
		switchField.setValue(true);
		formContructor.addElement(switchField);

		String result = formContructor.toJSON();
		Form form = formRepository.getOne(1L);
		form.setJson(result);
		formRepository.save(form);
	}

	@Test
	public void refactorMessages(){
		JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
		List<DialogState> dialogStates = dialogStateRepository.findAll();
		dialogStates.forEach(ds -> {
			ObjectNode on = jsonNodeFactory.objectNode();
			on.put("normal", ds.getMessage());
			ds.setMessage(on.toString());
		});
		int bp = 42;
		dialogStateRepository.saveAll(dialogStates);
	}
}
