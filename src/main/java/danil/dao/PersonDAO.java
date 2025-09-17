package danil.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import danil.models.Book;
import danil.models.Person;

@Component
public class PersonDAO {
	
	private final JdbcTemplate jdbcTemplate;
	
	@Autowired
	public PersonDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	public List<Person> index()
	{
		
		return jdbcTemplate.query("SELECT * FROM public.\"Person\"", new BeanPropertyRowMapper<>(Person.class));
	}
	
	public Person show(int id)
	{
		return jdbcTemplate.query("SELECT * FROM public.\"Person\" WHERE id = ?", new Object[] {id}, new BeanPropertyRowMapper<>(Person.class))
				.stream().findAny().orElse(null);
	}
	
	public Optional<Person> getPersonByFullName(String fullName)
	{
		return jdbcTemplate.query("SELECT * FROM public.\"Person\" WHERE \"fullName\" = ?", new Object [] {fullName}, new BeanPropertyRowMapper<>(Person.class))
				.stream()
				.findAny();
	}
	
	public void save(Person person)
	{
		jdbcTemplate.update("INSERT INTO public.\"Person\" (\"fullName\", \"yearOfBirth\") VALUES (?, ?)", person.getFullName(), person.getYearOfBirth());
	}
	public void update(int id, Person updatePerson)
	{
		jdbcTemplate.update("UPDATE public.\"Person\" SET \"fullName\" = ?, \"yearOfBirth\" = ? WHERE id = ?", updatePerson.getFullName(), updatePerson.getYearOfBirth(), id);
	}
	public void delete(int id)
	{
		jdbcTemplate.update("DELETE FROM public.\"Person\" WHERE id = ?", id);
	}
	
	public List<Book> getBooksByPersonId(int id)
	{
		return jdbcTemplate.query("SELECT * FROM public.\"Book\" WHERE person_id = ?", new Object[] {id}, new BeanPropertyRowMapper<>(Book.class));
	}
	
}
