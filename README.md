# Poustfit

## Descripción

Poustfit es un proyecto desarrollado durante el **HackUDC 2025** que utiliza la API proporcionada por **Inditex Tech Visual Search**. Su objetivo es fusionar las funciones relacionadas con Inditex y las redes sociales, permitiendo que los usuarios suban fotos de sus conjuntos de ropa y obtengan recomendaciones de productos Inditex relacionados con su vestimenta.

### ¿Qué hace?

El usuario podrá subir sus outfits a la plataforma. El sitio mostrará la imagen, información sobre la publicación y una lista de enlaces y fotos de productos Inditex relacionados con la ropa de la imagen. Los demás usuarios podrán ver las publicaciones y también compartir las suyas.

### ¿Cómo lo construimos?

El grupo, conformado por **Lucas Kwak**, **Miguel Marcos Trillo**, **Lucas Redondo** y **Miguel Durán**, dividió el trabajo entre los cuatro miembros, desarrollando tanto el frontend como el backend. Además, trabajamos en la integración de la API de Inditex y algunas APIs adicionales que encontramos en Internet.

### Desafíos encontrados

Uno de los mayores desafíos que enfrentamos fue al tratar de usar la API de Inditex para relacionar las fotos con los productos. Descubrimos que la API no aceptaba directamente el archivo de imagen, sino que necesitábamos proporcionar una URL de la imagen. Por lo tanto, tuvimos que utilizar otra API, **Imgbb**, para poder subir la URL de la imagen y usarla en la solicitud.

Además, tuvimos que realizar un scraping en el sitio web de Inditex para extraer las fotos de los productos y poder mostrarlas en nuestro servicio.

Al principio también tuvimos dificultades para entender la documentación de la API de Inditex, lo que provocó algunas confusiones en el código. Sin embargo, logramos resolverlas y continuar con el desarrollo.

### Logros de los que estamos orgullosos

Nos sentimos muy orgullosos de haber logrado superar los problemas relacionados con la publicación de fotos en la API de Inditex. También conseguimos desarrollar la mayoría de las ideas iniciales que teníamos, algunas de las cuales pensábamos que no podríamos implementar.

### Lo que aprendimos

Aprendimos mucho sobre el uso de APIs externas que nos ayudaron a implementar funcionalidades adicionales. Si hubiéramos tenido que implementar estas funcionalidades por nuestra cuenta, habría sido un proceso mucho más largo y complicado. Además, aprendimos a gestionar la cooperación en grupo en un proyecto full stack, logrando excelentes resultados y una gran comprensión mutua.

## Instalación

Para comenzar con el proyecto, sigue estos pasos:

1. Clona el repositorio:
    ```bash
    git clone https://github.com/usuario/poustfit.git
    cd poustfit
    ```

2. Instala las dependencias necesarias:
    ```bash
    npm install
    ```

3. Ejecuta el proyecto:
    ```bash
    npm start
    ```

4. Configura el `application.yml` en el servidor:
    - Asegúrate de configurar correctamente la conexión a la base de datos en la sección correspondiente del archivo.
    - Configura las rutas para guardar las imágenes en el sistema de archivos local. Debes especificar la ruta donde se almacenarán las imágenes subidas por los usuarios.

    Ejemplo de configuración en `application.yml`:
    ```yaml
    spring:
      datasource:
        url: jdbc:mysql://localhost:3306/tu_base_de_datos
        username: tu_usuario
        password: tu_contraseña
      file:
        upload-dir: /ruta/a/tu/carpeta/de/imagenes
    ```

## Uso

Una vez el proyecto esté corriendo, podrás acceder a la página principal donde podrás subir tus fotos de outfits y ver las recomendaciones de productos Inditex relacionados. También podrás interactuar con las publicaciones de otros usuarios.

## Autores y Reconocimientos

Este proyecto fue desarrollado por el grupo de trabajo conformado por:

- **Lucas Kwak**
- **Miguel Marcos Trillo**
- **Lucas Redondo**
- **Miguel Durán**

Agradecemos a **Inditex Tech** por proporcionar la API que hizo posible la integración de productos Inditex con las fotos de outfits.

## Licencia

Este proyecto está bajo la licencia **Apache 2.0**.

## Estado del Proyecto

El proyecto está completo y funcionando, pero puede recibir futuras mejoras o cambios según sea necesario.
