package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.*;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal implements org.springframework.boot.CommandLineRunner {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final Scanner teclado = new Scanner(System.in);
    private static final String URL_BASE = "https://gutendex.com/books/";

    @Override
    public void run(String... args) throws Exception {
        mostrarMenu();
    }

    public void mostrarMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    \n*** LiterAlura - Biblioteca Digital ***
                    
                    1. Buscar libro por título
                    2. Listar libros registrados
                    3. Listar autores registrados
                    4. Listar autores vivos en un año
                    5. Listar libros por idioma
                    6. Estadísticas por idioma
                    0. Salir
                    
                    Elige una opción:
                    """;
            System.out.println(menu);

            try {
                opcion = Integer.parseInt(teclado.nextLine());

                switch (opcion) {
                    case 1 -> buscarLibroYTitulo();
                    case 2 -> listarLibrosRegistrados();
                    case 3 -> listarAutoresRegistrados();
                    case 4 -> listarAutoresVivosEnAnio();
                    case 5 -> listarLibrosPorIdioma();
                    case 6 -> mostrarEstadisticasPorIdioma();
                    case 0 -> System.out.println("¡Hasta luego!");
                    default -> System.out.println("Opción no válida. Intenta de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingresa un número válido.");
            }
        }
    }

    @Transactional
    private void buscarLibroYTitulo() {
        System.out.println("Ingresa el título del libro que deseas buscar:");
        String titulo = teclado.nextLine();

        String tituloFormateado = titulo.replace(" ", "+");
        String url = URL_BASE + "?search=" + tituloFormateado;

        try {
            String jsonResponse = consumoAPI.obtenerDatos(url);
            DatosRespuesta respuesta = conversor.obtenerDatos(jsonResponse, DatosRespuesta.class);

            if (respuesta.resultados() != null && !respuesta.resultados().isEmpty()) {
                DatosLibro datosLibro = respuesta.resultados().get(0);

                // Verificar si el libro ya existe
                Optional<Libro> libroExistente = libroRepository.findByTitulo(datosLibro.titulo());

                if (libroExistente.isPresent()) {
                    System.out.println("El libro ya está registrado:");
                    mostrarLibro(libroExistente.get());
                    return;
                }

                // Crear libro (sin autor aún)
                Libro libro = new Libro(datosLibro);

                // Procesar autor
                if (datosLibro.autores() != null && !datosLibro.autores().isEmpty()) {
                    DatosAutor datosAutor = datosLibro.autores().get(0);

                    // Buscar autor existente
                    Optional<Autor> autorExistente = autorRepository.findByNombre(datosAutor.nombre());
                    Autor autor;

                    if (autorExistente.isPresent()) {
                        autor = autorExistente.get();
                        System.out.println("Autor ya existe, usando existente: " + autor.getNombre());
                    } else {
                        autor = new Autor(datosAutor);
                        autor = autorRepository.save(autor);
                        System.out.println("Nuevo autor guardado: " + autor.getNombre());
                    }

                    // Asignar autor al libro
                    libro.setAutor(autor);
                }

                // Guardar libro
                Libro libroGuardado = libroRepository.save(libro);
                System.out.println("✅ Libro guardado exitosamente:");
                mostrarLibro(libroGuardado);

            } else {
                System.out.println("No se encontraron libros con el título: " + titulo);
            }

        } catch (Exception e) {
            System.out.println("Error al buscar el libro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("📭 No hay libros registrados en la base de datos.");
        } else {
            System.out.println("\n📚 Libros registrados (" + libros.size() + "):");
            libros.stream()
                    .sorted(Comparator.comparing(Libro::getNumeroDeDescargas).reversed())
                    .forEach(this::mostrarLibro);
        }
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()) {
            System.out.println("📭 No hay autores registrados en la base de datos.");
        } else {
            System.out.println("\n👥 Autores registrados (" + autores.size() + "):");
            autores.stream()
                    .sorted(Comparator.comparing(Autor::getNombre))
                    .forEach(this::mostrarAutor);
        }
    }

    private void listarAutoresVivosEnAnio() {
        System.out.println("Ingresa el año para buscar autores vivos:");
        try {
            int anio = Integer.parseInt(teclado.nextLine());
            List<Autor> autores = autorRepository.autoresVivosEnAnio(anio);

            if (autores.isEmpty()) {
                System.out.println("📭 No se encontraron autores vivos en el año " + anio);
            } else {
                System.out.println("\n👥 Autores vivos en " + anio + ":");
                autores.forEach(this::mostrarAutor);
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Por favor, ingresa un año válido.");
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("""
                Ingresa el idioma para buscar libros:
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugués
                """);
        String idioma = teclado.nextLine().toLowerCase();

        List<Libro> libros = libroRepository.findByIdioma(idioma);
        if (libros.isEmpty()) {
            System.out.println("📭 No se encontraron libros en el idioma: " + idioma);
        } else {
            System.out.println("\n📚 Libros en " + idioma + " (" + libros.size() + "):");
            libros.forEach(this::mostrarLibro);
        }
    }

    private void mostrarEstadisticasPorIdioma() {
        System.out.println("""
                \n📊 ESTADÍSTICAS POR IDIOMA
                Elige un idioma:
                es - Español
                en - Inglés
                fr - Francés
                pt - Portugués
                """);

        String idioma = teclado.nextLine().toLowerCase();
        long cantidad = libroRepository.countByIdioma(idioma);

        String nombreIdioma = switch (idioma) {
            case "es" -> "Español";
            case "en" -> "Inglés";
            case "fr" -> "Francés";
            case "pt" -> "Portugués";
            default -> "Desconocido";
        };

        System.out.println("\n📚 Libros en " + nombreIdioma + ": " + cantidad);
    }

    private void mostrarLibro(Libro libro) {
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("📖 Título: " + libro.getTitulo());
        if (libro.getAutor() != null) {
            System.out.println("✍️ Autor: " + libro.getAutor().getNombre());
        }
        System.out.println("🌐 Idioma: " + libro.getIdioma());
        System.out.println("📥 Descargas: " + String.format("%.0f", libro.getNumeroDeDescargas()));
        System.out.println("═══════════════════════════════════════");
    }

    private void mostrarAutor(Autor autor) {
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("✍️ Nombre: " + autor.getNombre());
        if (autor.getFechaDeNacimiento() != null) {
            System.out.println("   🎂 Nacimiento: " + autor.getFechaDeNacimiento());
        }
        if (autor.getFechaDeFallecimiento() != null) {
            System.out.println("   ⚰️ Fallecimiento: " + autor.getFechaDeFallecimiento());
        }
        System.out.println("   📚 Libros registrados: " + autor.getLibros().size());
        System.out.println("═══════════════════════════════════════");
    }
}