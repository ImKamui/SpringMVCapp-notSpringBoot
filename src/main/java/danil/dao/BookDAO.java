package danil.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import danil.models.Book;
import danil.models.Person;

@Component
public class BookDAO {
	
	private final JdbcTemplate jdbcTemplate;
	
	public BookDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<Book> index()
	{
		
		return jdbcTemplate.query("SELECT * FROM public.\"Book\"", new BeanPropertyRowMapper<>(Book.class));
	}
	
	public Book show(int id)
	{
		return jdbcTemplate.query("SELECT * FROM public.\"Book\" WHERE id = ?", new Object[] {id}, new BeanPropertyRowMapper<>(Book.class))
				.stream().findAny().orElse(null);
	}
	
	public void save(Book book)
	{
		jdbcTemplate.update("INSERT INTO public.\"Book\" (title, author, year) VALUES (?, ?, ?)", book.getTitle(), book.getAuthor(), book.getYear());
	}
	public void update(int id, Book updateBook)
	{
		jdbcTemplate.update("UPDATE public.\"Book\" SET title = ?, author = ?, year = ? WHERE id = ?", updateBook.getTitle(), updateBook.getAuthor(), updateBook.getYear(), id);
	}
	public void delete(int id)
	{
		jdbcTemplate.update("DELETE FROM public.\"Book\" WHERE id = ?", id);
	}
	
	public Optional<Person> getBookOwner(int id)
	{
		return jdbcTemplate.query("SELECT public.\"Person\".* FROM public.\"Book\" JOIN public.\"Person\" ON public.\"Book\".person_id = public.\"Person\".id"
				+ " WHERE public.\"Book\".id = ?", new Object[] {id}, new BeanPropertyRowMapper<>(Person.class))
				.stream()
				.findAny();
	}
	public void release(int id)
	{
		jdbcTemplate.update("UPDATE public.\"Book\" SET person_id=NULL WHERE id=?", id);
	}
	
	public void assign(int id, Person selectedPerson)
	{
		jdbcTemplate.update("UPDATE public.\"Book\" SET person_id=? WHERE id=?", selectedPerson.getId(), id);
	}
}
