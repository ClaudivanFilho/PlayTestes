package controllers;

import java.util.List;

import models.Autor;
import models.Livro;
import models.dao.GenericDAO;
import play.Logger;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Controlador Principal do Sistema
 */
public class Application extends Controller {
	private static Form<Livro> bookForm = Form.form(Livro.class);
	private static final GenericDAO dao = new GenericDAO();

	public static Result index() {
		return redirect(routes.Application.books());
	}

	/*
	 * A Anotação transactional é necessária em todas as Actions que
	 * usarem o BD.
	 */
	@Transactional
	public static Result books() {
		// Todos os Livros do Banco de Dados
		List<Livro> result = dao.findAllByClass(Livro.class);
		return ok(views.html.index.render(result));
	}

	@Transactional
	public static Result newBook() {
		// O formulário dos Livros Preenchidos
		Form<Livro> filledForm = bookForm.bindFromRequest();

		if (filledForm.hasErrors()) {
            List<Livro> result = dao.findAllByClass(Livro.class);
            //TODO falta colocar na interface mensagem de erro.
			return badRequest(views.html.index.render(result));
		} else {
            Livro novoLivro = filledForm.get();
            Logger.debug("Criando livro: " + filledForm.data().toString() + " como " + novoLivro.getNome());
			// Persiste o Livro criado
			dao.persist(novoLivro);
			// Espelha no Banco de Dados
			dao.flush();
            /*
             * Usar routes.Application.<uma action> é uma forma de
             * evitar colocar rotas literais (ex: "/books")
             * hard-coded no código. Dessa forma, se mudamos no
             * arquivo routes, continua funcionando.
             */
			return redirect(routes.Application.books());
		}
	}

	@Transactional
	public static Result addAutor(Long id, String nome) {
		criaAutorDoLivro(id, nome);
        return redirect(routes.Application.books());
	}

	private static void criaAutorDoLivro(Long id, String nome) {
		// Cria um novo Autor para um livro de {@code id}
		Autor novoAutor = new Autor(nome);
		// Procura um objeto da classe Livro com o {@code id}
		Livro livroDaListagem = dao.findByEntityId(Livro.class, id);
		// Faz o direcionamento de cada um
		livroDaListagem.addAutor(novoAutor);
		novoAutor.addLivro(livroDaListagem);
		// Persiste o Novo Autor
		dao.persist(novoAutor);

		/* As informações do livro já serão automaticamente atualizadas
		 * no BD no final da transação. Isso porque o livro já existe
		 * no BD, e então já é gerenciado por ele.
		 *
		 * Assim fica opcional fazer dao.merge(livroDaListagem);
		 */
		// Espelha no Banco de Dados
		dao.flush();
	}

	// Notação transactional sempre que o método fizer transação com o Banco de
	// Dados.
	@Transactional
	public static Result deleteBook(Long id) {
		// Remove o Livro pelo Id
		dao.removeById(Livro.class, id);
		// Espelha no banco de dados
		dao.flush();
		return redirect(routes.Application.books());
	}

}
