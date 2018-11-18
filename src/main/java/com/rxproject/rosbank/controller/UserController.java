package com.rxproject.rosbank.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rxproject.rosbank.authentication.AuthService;
import com.rxproject.rosbank.model.*;
import com.rxproject.rosbank.service.*;
import com.rxproject.rosbank.utils.UserMessageWrapper;
import com.rxproject.rosbank.views.ViewFabric.ViewConstructor;
import com.rxproject.rosbank.views.ViewFabric.ViewModels.DocumentViewModel;
import com.rxproject.rosbank.views.ViewFabric.ViewModels.ImageViewModel;
import com.rxproject.rosbank.views.ViewFabric.ViewModels.UrlViewModel;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
@Transactional
@Log4j
public class UserController {

    private final ObjectMapper objectMapper;

    private final AuthService authService;
    private final UserService userService;
    private final ButtonService buttonService;
    private final DialogStateService dialogStateService;
    private final MessageService messageService;
    private final DocumentService documentService;


    @Autowired
    public UserController(ObjectMapper objectMapper, AuthService authService, UserService userService, ButtonService buttonService, DialogStateService dialogStateService, MessageService messageService, DocumentService documentService) {
        this.objectMapper = objectMapper;
        this.authService = authService;
        this.userService = userService;
        this.buttonService = buttonService;
        this.dialogStateService = dialogStateService;
        this.messageService = messageService;
        this.documentService = documentService;
    }

    //информация по авторизованному юзеру
    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getUser(){
        User user = authService.getUser();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // текущее состояние диалога
    @RequestMapping(value = "ds", method = RequestMethod.GET)
    public ResponseEntity getUserDialogState(){
        User user = authService.getUser();
        return new ResponseEntity<>(user.getState(), HttpStatus.OK);
    }

    @RequestMapping(value = "style", method = RequestMethod.POST)
    public ResponseEntity setStyle(@RequestBody JsonNode json){
        if (!json.hasNonNull("style"))
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        User.Style style = User.Style.valueOf(json.get("style").asText());
        User user = authService.getUser();
        user.setStyle(style);
        userService.save(user);
        return new ResponseEntity(HttpStatus.OK);
    }

    //полная история диалога
    @RequestMapping(value = "history", method = RequestMethod.GET)
    public ResponseEntity getMessageHistory() throws JsonProcessingException {
        User user = authService.getUser();
        List<Message> history = messageService.findByUser(user);
        if (history.isEmpty()){
            Message message = new Message(user);
            DialogState dialogState = dialogStateService.getById(1L);
            user.setState(dialogState);
            userService.save(user);
            message.addBotMessage(dialogState);
            messageService.save(message);
        }
        history = messageService.findByUser(user);
        return new ResponseEntity<>(history, HttpStatus.OK);
    }

    //получение документа по id
    @RequestMapping(value = "document", method = RequestMethod.GET)
    public ResponseEntity getDocumentById(@RequestParam("id") Long id){
        Document document = documentService.getById(id);
        if (document == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        User user = authService.getUser();
        if (document.getUser().equals(user))
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        File file = new File(document.getPath());
        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            return new ResponseEntity<>(resource,headers,HttpStatus.OK);
        } catch (FileNotFoundException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    //обработка шаблонного нажатия конпки
    @RequestMapping(value = "buttons", method = RequestMethod.POST)
    public ResponseEntity buttonHandler(@RequestBody Button bodyBtn){
        Button button = buttonService.getById(bodyBtn.getId());
        if (button == null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        User user = authService.getUser();
        DialogState currentState = user.getState();
        Set<Button> buttons = currentState.getButtons();
        if (!buttons.contains(button))
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        DialogState targetState = button.getTargetState();
        if (targetState == null)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        Message message = messageService.findLastByUser(user);
        UserMessageWrapper umw = new UserMessageWrapper();
        umw.setType(currentState.getType());
        umw.setTextNode(button.getText());
        message.addUserAnswer(umw);
        messageService.save(message);
        user.setState(targetState);
        log.info(String.format("from %d to %d",  currentState.getId(), targetState.getId()));
        userService.save(user);
        message = new Message(user);
        message.addBotMessage(targetState);
        messageService.save(message);
        return new ResponseEntity<>(targetState, HttpStatus.OK);
    }

    // --------------------------------------------------------------------------

    @RequestMapping(value = "docState10", method = RequestMethod.POST)
    public ResponseEntity docState10(){
        User user = authService.getUser();
        if (user.getState().getId() != 8L)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        Message message = messageService.findLastByUser(user);
        UserMessageWrapper umw = new UserMessageWrapper();
        DialogState currentState = user.getState();
        umw.setType(currentState.getType());
        umw.setTextNode("Открыть расчетный счет");
        message.addUserAnswer(umw);
        messageService.save(message);

        ViewConstructor viewConstructor = new ViewConstructor();
        UrlViewModel uvm = new UrlViewModel();
        uvm.setUrl("https://www.rosbank.ru/");
        uvm.setText("Тарифы на расчетные счета");
        uvm.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT0z7029YrMN61lmE4U-x1TfDRi5g9_NdVp39rMtDe8O_ATGPxS");
        viewConstructor.addElement(uvm);

        DialogState targetState = dialogStateService.getById(10L);
        targetState.setView(viewConstructor.toJSON());
        message = new Message(user);
        message.addBotMessage(targetState);
        messageService.save(message);
        user.setState(targetState);
        userService.save(user);
        return new ResponseEntity<>(targetState, HttpStatus.OK);
    }

    @RequestMapping(value = "docState11", method = RequestMethod.POST)
    public ResponseEntity docState11(){
        User user = authService.getUser();
        if (user.getState().getId() != 8L)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        Message message = messageService.findLastByUser(user);
        UserMessageWrapper umw = new UserMessageWrapper();
        DialogState currentState = user.getState();
        umw.setType(currentState.getType());
        umw.setTextNode("Открыть расчетный счет");
        message.addUserAnswer(umw);
        messageService.save(message);

        ViewConstructor viewConstructor = new ViewConstructor();
        UrlViewModel uvm = new UrlViewModel();
        uvm.setUrl("https://www.rosbank.ru/");
        uvm.setText("Тарифы на эквайрин");
        uvm.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT0z7029YrMN61lmE4U-x1TfDRi5g9_NdVp39rMtDe8O_ATGPxS");
        viewConstructor.addElement(uvm);

        DialogState targetState = dialogStateService.getById(11L);
        targetState.setView(viewConstructor.toJSON());
        message = new Message(user);
        message.addBotMessage(targetState);
        messageService.save(message);
        user.setState(targetState);
        userService.save(user);
        return new ResponseEntity<>(targetState, HttpStatus.OK);
    }

    @RequestMapping(value = "docState12", method = RequestMethod.POST)
    public ResponseEntity docState12(){
        User user = authService.getUser();
        if (user.getState().getId() != 8L)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        Message message = messageService.findLastByUser(user);
        UserMessageWrapper umw = new UserMessageWrapper();
        DialogState currentState = user.getState();
        umw.setType(currentState.getType());
        umw.setTextNode("Открыть расчетный счет");
        message.addUserAnswer(umw);
        messageService.save(message);

        ViewConstructor viewConstructor = new ViewConstructor();
        UrlViewModel uvm = new UrlViewModel();
        uvm.setUrl("https://www.rosbank.ru/");
        uvm.setText("Тарифы на корпоративные карты");
        uvm.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT0z7029YrMN61lmE4U-x1TfDRi5g9_NdVp39rMtDe8O_ATGPxS");
        viewConstructor.addElement(uvm);

        DialogState targetState = dialogStateService.getById(12L);
        targetState.setView(viewConstructor.toJSON());
        message = new Message(user);
        message.addBotMessage(targetState);
        messageService.save(message);
        user.setState(targetState);
        userService.save(user);
        return new ResponseEntity<>(targetState, HttpStatus.OK);
    }

    @RequestMapping(value = "docState15", method = RequestMethod.POST)
    public ResponseEntity docState15(){
        User user = authService.getUser();
        if (user.getState().getId() != 13L)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        Message message = messageService.findLastByUser(user);
        UserMessageWrapper umw = new UserMessageWrapper();
        DialogState currentState = user.getState();
        umw.setType(currentState.getType());
        umw.setTextNode("Открыть расчетный счет");
        message.addUserAnswer(umw);
        messageService.save(message);

        ViewConstructor viewConstructor = new ViewConstructor();
        UrlViewModel uvm = new UrlViewModel();
        uvm.setUrl("https://www.rosbank.ru/");
        uvm.setText("Тарифы на расчетные счета");
        uvm.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT0z7029YrMN61lmE4U-x1TfDRi5g9_NdVp39rMtDe8O_ATGPxS");
        viewConstructor.addElement(uvm);

        DialogState targetState = dialogStateService.getById(15L);
        targetState.setView(viewConstructor.toJSON());
        message = new Message(user);
        message.addBotMessage(targetState);
        messageService.save(message);
        user.setState(targetState);
        userService.save(user);
        return new ResponseEntity<>(targetState, HttpStatus.OK);
    }

    @RequestMapping(value = "docState16", method = RequestMethod.POST)
    public ResponseEntity docState16(){
        User user = authService.getUser();
        if (user.getState().getId() != 13L)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        Message message = messageService.findLastByUser(user);
        UserMessageWrapper umw = new UserMessageWrapper();
        DialogState currentState = user.getState();
        umw.setType(currentState.getType());
        umw.setTextNode("Открыть расчетный счет");
        message.addUserAnswer(umw);
        messageService.save(message);

        ViewConstructor viewConstructor = new ViewConstructor();
        UrlViewModel uvm = new UrlViewModel();
        uvm.setUrl("https://www.rosbank.ru/");
        uvm.setText("Тарифы на экваринг");
        uvm.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT0z7029YrMN61lmE4U-x1TfDRi5g9_NdVp39rMtDe8O_ATGPxS");
        viewConstructor.addElement(uvm);

        DialogState targetState = dialogStateService.getById(16L);
        targetState.setView(viewConstructor.toJSON());
        message = new Message(user);
        message.addBotMessage(targetState);
        messageService.save(message);
        user.setState(targetState);
        userService.save(user);
        return new ResponseEntity<>(targetState, HttpStatus.OK);
    }

    @RequestMapping(value = "docState17", method = RequestMethod.POST)
    public ResponseEntity docState17(){
        User user = authService.getUser();
        if (user.getState().getId() != 8L)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        Message message = messageService.findLastByUser(user);
        UserMessageWrapper umw = new UserMessageWrapper();
        DialogState currentState = user.getState();
        umw.setType(currentState.getType());
        umw.setTextNode("Открыть расчетный счет");
        message.addUserAnswer(umw);
        messageService.save(message);

        ViewConstructor viewConstructor = new ViewConstructor();
        UrlViewModel uvm = new UrlViewModel();
        uvm.setUrl("https://www.rosbank.ru/");
        uvm.setText("Тфрифы на расчетные счета");
        uvm.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT0z7029YrMN61lmE4U-x1TfDRi5g9_NdVp39rMtDe8O_ATGPxS");
        viewConstructor.addElement(uvm);

        DialogState targetState = dialogStateService.getById(17L);
        targetState.setView(viewConstructor.toJSON());
        message = new Message(user);
        message.addBotMessage(targetState);
        messageService.save(message);
        user.setState(targetState);
        userService.save(user);
        return new ResponseEntity<>(targetState, HttpStatus.OK);
    }

    @RequestMapping(value ="photo27", method = RequestMethod.POST)
    public ResponseEntity photo27(@RequestParam("file") MultipartFile file){
        User user = authService.getUser();
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String originalFilename = file.getOriginalFilename();
                String fileName = String.format("user_files/photos/%d-%s",new Date().getTime(), originalFilename);
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(fileName)));
                stream.write(bytes);
                stream.close();
                Document document = new Document();
                document.setPath(fileName);
                document.setUser(user);
                documentService.save(document);
                Message message = messageService.findLastByUser(user);
                UserMessageWrapper umw = new UserMessageWrapper();
                umw.setType(user.getState().getType());
                umw.setTextNode("Фото отправлено");
                message.addUserAnswer(umw);
                messageService.save(message);

                DialogState targetState = dialogStateService.getById(1L);
                message = new Message(user);
                message.addBotMessage(targetState);
                messageService.save(message);
                user.setState(targetState);
                userService.save(user);

                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value ="doc29", method = RequestMethod.POST)
    public ResponseEntity doc29(@RequestParam("file") MultipartFile file){
        User user = authService.getUser();
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String originalFilename = file.getOriginalFilename();
                String fileName = String.format("user_files/docs/%d-%s",new Date().getTime(), originalFilename);
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(fileName)));
                stream.write(bytes);
                stream.close();
                Document document = new Document();
                document.setPath(fileName);
                document.setUser(user);
                documentService.save(document);
                Message message = messageService.findLastByUser(user);
                UserMessageWrapper umw = new UserMessageWrapper();
                umw.setType(user.getState().getType());
                umw.setTextNode("Документ загружен");
                message.addUserAnswer(umw);
                messageService.save(message);

                DialogState targetState = dialogStateService.getById(1L);
                message = new Message(user);
                message.addBotMessage(targetState);
                messageService.save(message);
                user.setState(targetState);
                userService.save(user);

                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }


    @RequestMapping(value ="handleForm1", method = RequestMethod.POST)
    public ResponseEntity handleForm1(@RequestBody JsonNode json){
        User user = authService.getUser();
        DialogState currentState = user.getState();
        if (currentState.getId() != 20L)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        Message message = messageService.findLastByUser(user);
        UserMessageWrapper umv = new UserMessageWrapper();
        umv.setType(currentState.getType());
        umv.setObjectNode((ObjectNode) json);
        message.addUserAnswer(umv);
        messageService.save(message);

        DialogState targetState = dialogStateService.getById(30L);
        ViewConstructor viewConstructor = new ViewConstructor();
        DocumentViewModel dvm = new DocumentViewModel();
        dvm.setFileId(1L);
        dvm.setText("Документ на ООО");
        viewConstructor.addElement(dvm);
        targetState.setView(viewConstructor.toJSON());
        message = new Message(user);
        message.addBotMessage(targetState);
        messageService.save(message);
        user.setState(targetState);
        userService.save(user);
        return new ResponseEntity<>(targetState,HttpStatus.OK);
    }

    @RequestMapping(value ="handleForm2", method = RequestMethod.POST)
    public ResponseEntity handleForm2(@RequestBody JsonNode json){
        User user = authService.getUser();
        DialogState currentState = user.getState();
        if (currentState.getId() != 21L)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        Message message = messageService.findLastByUser(user);
        UserMessageWrapper umv = new UserMessageWrapper();
        umv.setType(currentState.getType());
        umv.setObjectNode((ObjectNode) json);
        message.addUserAnswer(umv);
        messageService.save(message);

        DialogState targetState = dialogStateService.getById(31L);
        ViewConstructor viewConstructor = new ViewConstructor();
        DocumentViewModel dvm = new DocumentViewModel();
        dvm.setFileId(1L);
        dvm.setText("Документ на ИП");
        viewConstructor.addElement(dvm);
        targetState.setView(viewConstructor.toJSON());
        message = new Message(user);
        message.addBotMessage(targetState);
        messageService.save(message);
        user.setState(targetState);
        userService.save(user);
        return new ResponseEntity<>(targetState,HttpStatus.OK);
    }


    // --------------------------------------------------------------------------

    @RequestMapping(value = "testview", method = RequestMethod.POST)
    public ResponseEntity testView(){
        User user = authService.getUser();
        if (user.getState().getId() != 1L)
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        ViewConstructor viewConstructor = new ViewConstructor();

        DocumentViewModel dvm = new DocumentViewModel();
        dvm.setFileId(1L);
        dvm.setImgUrl("https://www.openkm.com/resources/images/icon/document-management-big.png");
        dvm.setText("Документ");
        viewConstructor.addElement(dvm);

        ImageViewModel ivm = new ImageViewModel();
        ivm.setFileId(1L);
        ivm.setText("Картинка");
        viewConstructor.addElement(ivm);

        UrlViewModel uvm = new UrlViewModel();
        uvm.setUrl("https://www.rosbank.ru/");
        uvm.setText("Сайт росбанка");
        uvm.setImage("https://www.rosbank.ru/content/images/sg_rb_brand_block_2_l_light_bkgd-rgb.svg");
        viewConstructor.addElement(uvm);

        DialogState targetState = dialogStateService.getById(4L);
        targetState.setView(viewConstructor.toJSON());
        user.setState(targetState);
        userService.save(user);
        return new ResponseEntity<>(targetState, HttpStatus.OK);
    }


    @RequestMapping(value ="textstate", method = RequestMethod.POST)
    public ResponseEntity textHandler(@RequestBody JsonNode json){
        String text = json.get("text").asText();
        User user = authService.getUser();
        DialogState targetState = dialogStateService.getById(1L);
        user.setState(targetState);
        userService.save(user);
        return new ResponseEntity<>(targetState,HttpStatus.OK);
    }

    @RequestMapping(value ="photo", method = RequestMethod.POST)
    public ResponseEntity photoHandler(@RequestParam("file") MultipartFile file){
        User user = authService.getUser();
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String originalFilename = file.getOriginalFilename();
                String fileName = String.format("user_files/photos/%d-%s",new Date().getTime(), originalFilename);
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(fileName)));
                stream.write(bytes);
                stream.close();
                Document document = new Document();
                document.setPath(fileName);
                document.setUser(user);
                documentService.save(document);
                log.info("Success");
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                log.info(e.getMessage());
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }

    @RequestMapping(value ="doc", method = RequestMethod.POST)
    public ResponseEntity docHandler(@RequestParam("file") MultipartFile file){
        User user = authService.getUser();
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String originalFilename = file.getOriginalFilename();
                String fileName = String.format("user_files/docs/%d-%s",new Date().getTime(), originalFilename);
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(fileName)));
                stream.write(bytes);
                stream.close();
                Document document = new Document();
                document.setPath(fileName);
                document.setUser(user);
                documentService.save(document);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
}
