package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByTitulo(String titulo);

    List<Libro> findByIdioma(String idioma);

    // Método para contar libros por idioma

    long countByIdioma(String idioma);

    @Query("SELECT l FROM Libro l ORDER BY l.numeroDeDescargas DESC LIMIT 10")
    List<Libro> top10LibrosMasDescargados();
}