package servlets.dictionary.wordSet;

import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MsgRemoveWordsFromWordSet;
import servlets.NonAbonentServlet;
import util.AddressService;
import util.SessionCache;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сервлет, обрабатывающий запрос на удаление слов из набора
 * На данный момент, word's ids лежат в заголовке:
 * wordId: 1,2,3,4,5
 */

public class RemoveWordsFromWordSetServlet extends HttpServlet implements NonAbonentServlet {
    private Integer wordIds[];
    private int wordSetId;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String sessionId = req.getSession().getId();

        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }

        try{
            initParams(req);
        }catch (Exception e){
            Logger.getLogger(this.getClass().toString()).log(Level.SEVERE, "Wrong parameters\n" +
                    "wordSetId: " + wordSetId +
                    "\nwordIds: " + Arrays.toString(wordIds), e);
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
        MessageSystem.INSTANCE.sendMessageForService(new MsgRemoveWordsFromWordSet(null, AddressService.INSTANCE.getDictionaryServiceAddress(),
                wordIds,wordSetId));
    }

    private void initParams(HttpServletRequest request){
        wordSetId = Integer.parseInt(request.getParameter("wordSetId"));
        wordIds = Arrays.stream(request.getParameter("wordId").split(",")).
                map(Integer::parseInt).toArray(Integer[]::new);
    }
}
