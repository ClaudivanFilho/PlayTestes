package models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

import com.google.common.base.Objects;

// Entidade que representa um Livro
// Referenciar a uma tabela
@Entity(name = "Livro")
public class Livro {

	// Todo Id tem que ter o GeneratedValue a não ser que ele seja setado
	@Id
	@SequenceGenerator(name = "LIVRO_SEQUENCE", sequenceName = "LIVRO_SEQUENCE", allocationSize = 1, initialValue = 0)
	@GeneratedValue(strategy = GenerationType.TABLE)
	// Usar Id sempre Long
	private Long id;

    @ManyToMany(mappedBy = "livros", cascade = CascadeType.ALL)
	private List<Autor> autores;

	@Column
	private String nome;

	// Construtor vazio para o Hibernate criar os objetos
	public Livro() {
		this.autores = new ArrayList<Autor>();
	}

    public Livro(String nome) {
        this();
        this.nome = nome;
    }

    public Livro(String nome, Autor... autores) {
        this.autores = Arrays.asList(autores);
        this.nome = nome;
    }

    public String getNome() {
		return nome;
	}

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Livro)) {
			return false;
		}
		Livro outroLivro = (Livro) obj;
		return Objects.equal(outroLivro.getNome(), this.getNome());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.getNome());
	}

    public List<Autor> getAutores() {
        return Collections.unmodifiableList(autores);
    }

    public void addAutor(Autor autor) {
        autores.add(autor);
    }

    public void setId(long id) {
        this.id = id;
    }
}
