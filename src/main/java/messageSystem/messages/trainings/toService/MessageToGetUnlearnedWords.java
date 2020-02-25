package messageSystem.messages.trainings.toService;

import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.messages.trainings.toServlet.MessageToGetUnlearnedWordsServlet;
import services.db.TrainingService;

public class MessageToGetUnlearnedWords extends MessageToTrainingService{
    private final String sessionId;
    private final int trainingId;
    private final int userId;
    public MessageToGetUnlearnedWords(Address from, Address to, String sessionId, int trainingId, int userId) {
        super(from, to);
        this.sessionId = sessionId;
        this.trainingId = trainingId;
        this.userId = userId;
    }

    @Override
    protected void exec(TrainingService service) {
        MessageSystem.INSTANCE.sendMessageForServlet(new MessageToGetUnlearnedWordsServlet(getTo(), getFrom(),
                service.getUnlearnedWordsForTraining(trainingId, userId)), sessionId);
    }
}
