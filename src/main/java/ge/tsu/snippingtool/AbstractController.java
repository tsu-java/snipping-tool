package ge.tsu.snippingtool;

import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractController {
    protected static final Logger log = LoggerFactory.getLogger(AbstractController.class);

    private Optional<Consumer<Stage>> afterStageInit;

    protected Stage stage;

    public AbstractController() {
    }

    public AbstractController(Consumer<Stage> afterStageInit) {
        this.afterStageInit = Optional.of(afterStageInit);
    }

    public void setAfterStageInit(Consumer<Stage> afterStageInit) {
        this.afterStageInit = Optional.of(afterStageInit);
    }

    public void initStage(Stage stage) {
        this.stage = stage;
        this.afterStageInit.ifPresent((i) -> i.accept(stage));
        log.debug("Initialized stage object with: {}", stage);
    }
}
