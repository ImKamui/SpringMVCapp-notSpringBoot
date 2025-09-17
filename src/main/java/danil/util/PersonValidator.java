package danil.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import danil.dao.PersonDAO;
import danil.models.Person;

@Component
public class PersonValidator implements Validator{

	private final PersonDAO dao;
	
	@Autowired
	public PersonValidator(PersonDAO dao) {
		this.dao = dao;
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Person.class.equals(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		Person person = (Person) target;
		if (dao.getPersonByFullName(person.getFullName()).isPresent())
		{
			errors.rejectValue("fullName", "", "Человек с таким ФИО уже существует");
		}
	}
}
