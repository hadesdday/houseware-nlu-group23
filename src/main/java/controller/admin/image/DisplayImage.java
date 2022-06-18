package controller.admin.image;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;

@WebServlet(name = "DisplayImage", value = "/img/*")
public class DisplayImage extends HttpServlet {
    private String imagePath;

    public void init() throws ServletException {
        this.imagePath = "";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        File root = new File((String) request.getServletContext().getAttribute("FILES_DIR") + File.separator);
        imagePath = root.getAbsolutePath();
        String requestedImage = request.getPathInfo();
        if (requestedImage == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        File image = new File(imagePath, URLDecoder.decode(requestedImage, "UTF-8"));
        if (!image.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String contentType = getServletContext().getMimeType(image.getName());
        if (contentType == null || !contentType.startsWith("image")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        response.reset();
        response.setContentType(contentType);
        response.setHeader("Content-Length", String.valueOf(image.length()));
        Files.copy(image.toPath(), response.getOutputStream());
    }
}
