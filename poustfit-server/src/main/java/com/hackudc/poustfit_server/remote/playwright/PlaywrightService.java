package com.hackudc.poustfit_server.remote.playwright;

import com.hackudc.poustfit_server.config.MyProperties;
import com.hackudc.poustfit_server.dto.out.image.ImageDTO;
import com.hackudc.poustfit_server.exceptions.MultimediaException;
import com.hackudc.poustfit_server.persistence.service.image.ImageService;
import com.microsoft.playwright.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.file.Paths;

@Service
public class PlaywrightService {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;
    private final MyProperties properties;
    private final ImageService imageService;

    @Autowired
    public PlaywrightService(MyProperties properties, ImageService imageService) {
        this.properties = properties;
        this.imageService = imageService;
    }

    // Inicialización del navegador bajo demanda
    public void initializeBrowser() {
        if (playwright == null) {
            this.playwright = Playwright.create();
        }
        if (browser == null) {
            this.browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        }
        if (context == null) {
            this.context = browser.newContext();
        }
        if (page == null) {
            this.page = context.newPage();
        }
    }

    public void captureProductImage(String productUrl, String productName, int imageIndex, String outputPath) {
        // Asegúrate de que el navegador está inicializado antes de capturar la imagen
        initializeBrowser();

        page.navigate(productUrl);

        // Buscar imágenes con alt que contenga el nombre del producto
        Locator images = page.locator("img[alt*='" + productName + "']");

        // Seleccionar la imagen en la posición indicada
        Locator selectedImage = images.nth(imageIndex);

        // Esperar a que la imagen esté visible
        selectedImage.waitFor();

        // Capturar la imagen seleccionada
        selectedImage.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get(properties.getMediaPathPost() + outputPath)));

        System.out.println("Captura de pantalla guardada: " + outputPath);
    }

    // Método para cerrar correctamente el navegador y liberar recursos
    public void closeBrowser() {
        if (browser != null) {
            browser.close();
            playwright.close();
            browser = null;
            playwright = null;
        }
    }

    public ImageDTO getProductImage(String nombreImagen) throws MultimediaException {
        return imageService.getProductImage(nombreImagen);
    }
}
