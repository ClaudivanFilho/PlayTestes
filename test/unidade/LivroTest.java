package unidade;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import models.Autor;
import models.Livro;
import models.dao.GenericDAO;

import org.junit.Test;

import base.AbstractTest;

public class LivroTest extends AbstractTest {

    GenericDAO dao = new GenericDAO();
	List<Livro> livros;
	List<Autor> autores;
	
	@Test
	public void deveSalvarLivroSemAutor () {
		livros = dao.findAllByClass(Livro.class); //consulta o bd
		assertThat(livros.size()).isEqualTo(0);
		
		Livro l1 = new Livro("Biblia Sagrada");
		dao.persist(l1);
		
		livros = dao.findAllByClass(Livro.class); //consulta o bd
		assertThat(livros.size()).isEqualTo(1);
		assertThat(livros.get(0).getNome()).isEqualTo("Biblia Sagrada");
	}
	
	@Test
	public void deveSalvarLivroComAutor() {
		Autor a1 = new Autor("George Martin");
		Livro l1 = new Livro("A Game of Thrones", a1); // cria o livro com autor
		a1.addLivro(l1); // add o livro ao autor
		
		dao.persist(l1); // salva tudo junto
		
		livros = dao.findAllByClass(Livro.class); // carrega os livros com seu autor
		assertThat(livros.size()).isEqualTo(1);
		assertThat(livros.get(0).getNome()).isEqualTo("A Game of Thrones");
		assertThat(livros.get(0).getAutores().size()).isEqualTo(1);
		
		autores = dao.findAllByClass(Autor.class); // carrega os autores com seus livros
		assertThat(autores.size()).isEqualTo(1);
		assertThat(autores.get(0).getNome()).isEqualTo("George Martin");
		assertThat(autores.get(0).getLivros().size()).isEqualTo(1);
	}
}
