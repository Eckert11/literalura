# Literalura - Catálogo de Libros Personal

¡Bienvenido/a a **Literalura**! Esta es una aplicación de consola desarrollada en Java con Spring Boot que permite construir una propia biblioteca digital mediante la API de Gutendex, busca tus libros favoritos y guárdalos en una base de datos PostgreSQL para consultarlos cuando quieras.

Este proyecto es parte del reto LiterAlura del programa ONE (Oracle Next Education) en colaboración con Alura Latam. [citation:1][citation:6][citation:8]

## Funcionalidades Principales

La aplicación ofrece un menú interactivo en la consola con las siguientes opciones:

1.  **🔍 Buscar libro por título**: Consulta la API de Gutendex, muestra los datos del primer resultado y lo guarda automáticamente en tu base de datos local [citation:1][citation:10].
2.  **📋 Listar libros registrados**: Muestra todos los libros que has guardado en tu biblioteca personal [citation:1][citation:2].
3.  **👥 Listar autores registrados**: Muestra todos los autores cuyos libros están en tu base de datos [citation:1][citation:9].
4.  **📅 Listar autores vivos en un determinado año**: Encuentra qué autores estaban vivos en un año específico que ingreses [citation:1][citation:6].
5.  **🌐 Listar libros por idioma**: Filtra y muestra los libros guardados según su idioma (ej. `es`, `en`, `fr`, `pt`) [citation:1][citation:8][citation:10].
6.  **❌ Salir**: Cierra la aplicación.

## 🛠️ Tecnologías Utilizadas

*   **Java** (Versión 17 o 21) [citation:2][citation:10].
*   **Spring Boot**: Para la estructura y configuración de la aplicación.
*   **Spring Data JPA / Hibernate**: Para la capa de persistencia y el mapeo objeto-relacional [citation:9].
*   **PostgreSQL**: Base de datos relacional para almacenar la información de libros y autores [citation:1][citation:2].
*   **Maven**: Herramienta de gestión de dependencias y construcción del proyecto [citation:2][citation:3].
*   **Jackson**: Para el parseo (conversión) de los datos JSON recibidos desde la API [citation:8][citation:9].
*   **API Gutendex**: API pública que proporciona datos de más de 70,000 libros del Proyecto Gutenberg [citation:3][citation:9].

## Configuración y Ejecución

Sigue estos pasos para poner el proyecto en marcha en tu máquina local.

### Prerrequisitos

*   Tener instalado **Java 17** (o superior) y **Maven** [citation:2][citation:10].
*   Tener instalado y corriendo **PostgreSQL** [citation:1].

### Instalación y Puesta en Marcha

1.  **Clona el repositorio** (o descárgalo como ZIP):
    ```bash
    git clone https://github.com/Eckert11/literalura.git
    cd literalura
