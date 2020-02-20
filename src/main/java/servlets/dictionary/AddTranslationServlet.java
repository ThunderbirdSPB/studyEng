package servlets.dictionary;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Word;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MessageAddTranslation;
import servlets.NonAbonentServlet;
import util.AddressService;
import util.SessionCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Вставляет новый перевод
 * в ответе необходимо отправить id
 */
public class AddTranslationServlet extends HttpServlet implements NonAbonentServlet {
    private HttpServletResponse response;
    private String sessionId;
    private String translation;
    private int wordId;
    private int userId;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        response = resp;
        sessionId = req.getSession().getId();
        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }

        try{
            initParams(req);
        }catch (Exception e){
            System.out.println("Wrong parameters in AddTranslationServlet");
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.flushBuffer();
            return;
        }

        createMessage();
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.flushBuffer();
    }

    @Override
    public void createMessage() {
        MessageSystem.INSTANCE.sendMessageForService(new MessageAddTranslation(null, AddressService.INSTANCE.getDictionaryServiceAddress(),
                translation, wordId, userId));
    }

    private void initParams(HttpServletRequest request) throws Exception {
        userId = SessionCache.INSTANCE.getUserIdBySessionId(sessionId);
        ObjectMapper mapper = new ObjectMapper();
        Word obj = mapper.readValue(request.getReader().readLine(), Word.class);

        wordId = obj.getId();
        translation = obj.getTranslations().get(0);

        if (translation == null || translation.isBlank() || wordId <= 0)
            throw new Exception();
    }
}