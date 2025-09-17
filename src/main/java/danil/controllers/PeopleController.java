package danil.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import danil.dao.PersonDAO;
import danil.models.Person;
import danil.util.PersonValidator;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

	private final PersonDAO dao;
	private final PersonValidator validator;
	
	@Autowired
	public PeopleController(PersonDAO dao, PersonValidator validator) {
		this.dao = dao;
		this.validator = validator;
	}
	
	@GetMapping()
	public String index(Model model)
	{
		model.addAttribute("people", dao.index());
		return "people/index";
	}
	@GetMapping("/{id}")
	public String show(@PathVariable("id") int id, Model model)
	{
		model.addAttribute("person", dao.show(id));
		model.addAttribute("books", dao.getBooksByPersonId(id));
		return "people/show";
	}
	
	@GetMapping("/new")
	public String newPerson(@ModelAttribute("person") Person person)
	{
		return "people/new";
	}
	
	@PostMapping()
	public String create(@ModelAttribute("person") @Valid Person person,
			BindingResult bindingResult)
	{
		validator.validate(person, bindingResult);
		if(bindingResult.hasErrors())
		{
			return "people/new";
		}
		
		dao.save(person);
		return "redirect:/people";
	}
	
	
	@GetMapping("/{id}/edit")
	public String edit(Model model, @PathVariable("id") int id)
	{
		model.addAttribute("person", dao.show(id));
		return "people/edit";
	}
	
	@PatchMapping("/{id}")
	public String update(@ModelAttribute("person") Person person,
			BindingResult bindingResult,
			@PathVariable("id") int id)
	{
		//validator.validate(person, bindingResult);
		if (bindingResult.hasErrors())
		{
			return "people/edit";
		}
		
		dao.update(id, person);
		return "redirect:/people";
	}
	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id)
	{
		dao.delete(id);
		return "redirect:/people";
	}
}
