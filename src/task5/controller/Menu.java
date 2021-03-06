package task5.controller;

import task5.input.MyScanner;
import task5.model.Student;
import task5.exceptions.BreakException;
import task5.views.MenuView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Menu {
    private List<Student> curatorBook = new ArrayList<>();
    private MenuView menuView;
    private MyScanner scanner;
    private boolean menu;
    private enum MenuOption{ADD_RECORD, SHOW_RECORDS, CHANGE_LANGUAGE, EXIT}
    private enum LanguageMenuOption{ENGLISH, UKRAINIAN, RUSSIAN}

    public Menu( MenuView menuView, MyScanner scanner) {
        this.menuView = menuView;
        this.scanner = scanner;
    }

    public void run() {
        chooseLanguage();
        menu = true;
        while (menu) {
            menuView.menuText();
            try {
                processInput(inputValue(MenuView.MAIN_MENU_REQUEST, MenuView.MAIN_MENU_PATTERN));
            }catch (BreakException e){
                exit();
            }
        }
    }

    private void chooseLocal(String locale){
        LanguageMenuOption languageMenuOption = LanguageMenuOption.values()[Integer.parseInt(locale)-1];
        switch (languageMenuOption){
            case ENGLISH:
                menuView.changeLocal(Locale.getDefault());
                break;
            case UKRAINIAN:
                menuView.changeLocal(new Locale("uk","UA"));
                break;
            case RUSSIAN:
                menuView.changeLocal(new Locale("ru","RU"));
                break;
        }
    }

    private void processInput(String statement) {
        MenuOption menuOption = MenuOption.values()[Integer.parseInt(statement)-1];
        switch (menuOption) {
            case ADD_RECORD:
                addStudent();
                break;
            case SHOW_RECORDS:
                showAll();
                break;
            case CHANGE_LANGUAGE:
                chooseLanguage();
                break;
            case EXIT:
                exit();
                break;
            default:
                break;
        }
    }

    private void addStudent(){
        Student student = new Student();
        try {
            String surname = inputValue(MenuView.REQUEST_SURNAME, MenuView.SURNAME_PATTERN);
            student.setSurname(surname);
            String name = inputValue(MenuView.REQUEST_NAME, MenuView.NAME_PATTERN);
            student.setName(name);
            String dateBirth = inputValue(MenuView.REQUEST_BIRTH_DATE, MenuView.BIRTH_DAY_PATTERN);
            student.setDate(dateBirth);
            String phone = inputValue(MenuView.REQUEST_PHONE, MenuView.PHONE_PATTERN);
            student.setPhonenumber(phone);
            String address = inputValue(MenuView.REQUEST_ADDRESS, MenuView.ADDRESS_PATTERN);
            student.setAddress(address);
            curatorBook.add(student);
        }catch (BreakException e){
            menuView.displayMessage(e.getMessage());
        }
    }

    private String inputValue(String request, String patt) throws BreakException{
        request = menuView.getLocaleString(request);
        patt = menuView.getLocaleString(patt);
        while(true){
            menuView.displayMessage(request);
            String input = scanner.readInput();
            if (input.equals("break")){
                throw new BreakException();
            }
            Pattern pattern = Pattern.compile(patt);
            Matcher m = pattern.matcher(input);
            if (m.find()){
                return input;
            }
            menuView.displayMessage(menuView.getLocaleString(MenuView.INVALID_INPUT));
        }
    }


    private void showAll() {
        menuView.displayMessage(menuView.getLocaleString(MenuView.ALL_RECORDS));
        menuView.showCuratorBook(curatorBook);
    }

    private void chooseLanguage(){
        menuView.chooseLanguageMenu();
        try {
            String lan = inputValue(MenuView.REQUEST_LANGUAGE, MenuView.MENU_PATTERN);
            chooseLocal(lan);
        }catch (BreakException e){
            menuView.displayMessage(e.getMessage());
        }
    }


    private void exit() {
        menu = false;
    }
}




