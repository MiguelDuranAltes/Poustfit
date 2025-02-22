package com.hackudc.poustfit_server.remote.playwright;

import com.hackudc.poustfit_server.config.MyProperties;
import com.hackudc.poustfit_server.dto.out.common.OkDTO;
import com.hackudc.poustfit_server.dto.out.image.ImageDTO;
import com.hackudc.poustfit_server.exceptions.ModelException;
import com.hackudc.poustfit_server.persistence.service.post.PostService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/playwright")
public class PlaywrightController {
    private final PlaywrightService playwrightService;
    private final String outputPath;

    @Autowired
    public PlaywrightController(PlaywrightService playwrightService) {
        this.playwrightService = playwrightService;
        this.outputPath = "producto.jpeg";
    }

    @PostMapping()
    public ResponseEntity<OkDTO> runPlaywright(@RequestParam String url, @RequestParam String name, HttpServletResponse response)  throws ModelException {
        playwrightService.captureProductImage(url, name, 0, outputPath);

        ImageDTO imageDTO=playwrightService.getProductImage(outputPath);

        try {
            response.setHeader("Content-disposition", "filename=" + imageDTO.getFilename());
            response.setContentType(imageDTO.getMimeType());
            IOUtils.copy(imageDTO.getInputStream(), response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(new OkDTO("Imagen recuperada correctamente"));
    }
}
