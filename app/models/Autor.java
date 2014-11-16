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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;

import com.google.common.base.Objects;

// Entidade que representa uma Tabela no Banco de Dados
@Entity
public class Autor {

	// Gerador de Sequencia para o Id
	@Id
	@GeneratedValue
	private Long id;

	// Nome do Autor dos Livros
	private String nome;

	// Relação Muitos para Muitos
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable
	private List<Livro> livros;

	// Construtor Vazio para o Hibernate criar os objetos
	public Autor() {
		this.livros = new ArrayList<Livro>();
	}

    public Autor(String nome) {
        this();
        this.nome = nome;
    }

	public List<Livro> getLivros() {
		return Collections.unmodifiableList(livros);
	}

	public String getNome() {
		return nome;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Autor)) {
			return false;
		}
		Autor outroAutor = (Autor) obj;
		return Objects.equal(outroAutor.getNome(), this.getNome());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.nome);
	}

    public void addLivro(Livro livro) {
        this.livros.add(livro);
    }
}
