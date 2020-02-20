package servlets.dictionary.wordSet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Word;
import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.dictionary.toService.MessageGetWordsFromWordSet;
import servlets.BaseServlet;
import util.AddressService;
import util.SessionCache;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class GetWordsFromWordSetServlet extends HttpServlet implements BaseServlet {
    private static final Address address = new Address();
    private static final int N_WORDS = 30; //максимальное количество слов в response
    private String sessionId;
    private HttpServletResponse response;
    private int index;                      //С какого индекса необходимо формировать слова для response, соответствует заголовку request'a
    private int wordSetId;                  // Из какого набора требуются слова, соответствует заголовку request'a
    private int userId;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        response = resp;
        sessionId = req.getSession().getId();
        if (!SessionCache.INSTANCE.isAuthorized(sessionId)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.flushBuffer();
            return;
        }

        //First request
        if (req.getHeader("handling") == null) {
            try{
                initParams(req);
                System.out.println("words for wordSetId=" + wordSetId);
            }catch (Exception e){
                System.out.println("Wrong parameters in GetWordFromWordSet");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.flushBuffer();
                return;
            }
            createMessage();

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.flushBuffer();
        }
        //Not the first request
        else
            checkServiceResult();
    }

    @Override
    public void createMessage() {
        MessageSystem.INSTANCE.sendMessageForService(new MessageGetWordsFromWordSet(getAddress(), AddressService.INSTANCE.getDictionaryServiceAddress(),
                userId, wordSetId, index, N_WORDS, sessionId));
    }


    public void handle(Collection<Word> words) {
        System.out.println(words);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(READY, "true");

        try{
            if (words == null || words.isEmpty())
                response.flushBuffer();
            else {
                ObjectMapper objectMapper = new ObjectMapper();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().write(objectMapper.writeValueAsString(words));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void notReady() {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader(READY, "false");
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getSessionId() {
        return sessionId;
    }

    @Override
    public Address getAddress() {
        return getAdr();
    }

    public static Address getAdr(){return address;}

    private void initParams(HttpServletRequest request){
        index = request.getParameter("index") != null ? Integer.parseInt(request.getParameter("index")) : 0;
        wordSetId = Integer.parseInt(request.getParameter("wordSetId"));
        userId = SessionCache.INSTANCE.getUserIdBySessionId(sessionId);
    }
}