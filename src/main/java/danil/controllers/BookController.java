package danil.controllers;

import java.util.Optional;

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

import danil.dao.BookDAO;
import danil.dao.PersonDAO;
import danil.models.Book;
import danil.models.Person;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {

	private PersonDAO pdao;
	private BookDAO bdao;
	
	public BookController(PersonDAO pdao, BookDAO bdao) {
		this.pdao = pdao;
		this.bdao = bdao;
	}
	
	@GetMapping()
	public String index(Model model)
	{
		model.addAttribute("books", bdao.index());
		return "books/index";
	}
	
	@GetMapping("/{id}")
	public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person)
	{
		model.addAttribute("books", bdao.show(id));
		
		Optional<Person> bookOwner = bdao.getBookOwner(id);
		if (bookOwner.isPresent())
		{
			model.addAttribute("owner", bookOwner.get());
		}
		else
		{
			model.addAttribute("people", pdao.index());
		}
		return "books/show";
	}
	
	@GetMapping("/new")
	public String newBook(@ModelAttribute("book") Book book)
	{
		return "books/new";
	}
	
	@PostMapping()
	public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult)
	{
		if (bindingResult.hasErrors())
		{
			return "books/new";
		}
		bdao.save(book);
		return "redirect:/books";
	}
	
	@GetMapping("/{id}/edit")
	public String edit(Model model, @PathVariable("id") int id)
	{
		model.addAttribute("book", bdao.show(id));
		return "books/edit";
	}
	
	@PatchMapping("/{id}")
	public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
			@PathVariable("id") int id)
	{
		if (bindingResult.hasErrors())
		{
			return "books/edit";
		}
		bdao.update(id, book);
		return "redirect:/books";
	}
	
	@DeleteMapping("/{id}")
	public String delete(@PathVariable("id") int id)
	{
		bdao.delete(id);
		return "redirect:/books";
	}
	
	@PatchMapping("/{id}/release")
	public String release(@PathVariable("id") int id)
	{
		bdao.release(id);
		return "redirect:/books/" + id;
	}
	
	@PatchMapping("/{id}/assign")
	public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person selectedPerson)
	{
		bdao.assign(id, selectedPerson);
		return "redirect:/books/" + id;
	}
}
