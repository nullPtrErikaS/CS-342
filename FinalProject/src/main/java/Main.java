import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.stage.StageStyle;
import javafx.scene.paint.Color;

public class GuiClient extends Application {
    private Client gameClient;
    private GridPane playerBoard; // The player's own board for placing ships
    private GridPane enemyBoard; // The grid for attacking the enemy
    private String username;
    private Label opponentLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.initStyle(StageStyle.UNDECORATED);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-image: url('/battle_S.jpg');" +
                "-fx-background-size: cover;");


        // Custom title bar
        HBox titleBar = new HBox();
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setStyle("-fx-background-color: #333;");
        titleBar.setPadding(new Insets(10));

        Label titleLabel = new Label("Battleship Game");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.WHITE);

        Button closeButton = new Button("X");
        closeButton.setOnAction(event -> primaryStage.close());
        closeButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);  // This spacer takes all available space

        titleBar.getChildren().addAll(titleLabel, spacer, closeButton); // Spacer pushes the close button to the right

        root.setTop(titleBar);


        gameClient = new Client("localhost", 5555, this::onGameUpdate);
        playerBoard = new GridPane();
        enemyBoard = new GridPane();
        initializeBoards();

        Label yourLabel = new Label("YOUR BOARD");
        opponentLabel = new Label("OPPONENT'S BOARD");

        yourLabel.setFont(Font.font("Your Cool Font", FontWeight.BOLD, 20));
        opponentLabel.setFont(Font.font("Your Cool Font", FontWeight.BOLD, 20));

        VBox yourBoardBox = new VBox(5, yourLabel, playerBoard);
        yourBoardBox.setStyle("-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: black;");

        VBox opponentBoardBox = new VBox(5, opponentLabel, enemyBoard);
        opponentBoardBox.setStyle("-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: black;");

        HBox gameBox = new HBox(20, yourBoardBox, opponentBoardBox);
        gameBox.setAlignment(Pos.CENTER);
        root.setCenter(gameBox);

        setupInteraction();

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.hide(); // Hide initially

        showStartupDialog(primaryStage);
    }

    private void showStartupDialog(Stage primaryStage) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);

        VBox dialogVBox = new VBox(20);
        dialogVBox.setAlignment(Pos.CENTER);
        dialogVBox.setPadding(new Insets(10));
        dialogVBox.setStyle("-fx-background-image: url('/battlei.jpg');" +
                "-fx-background-size: cover;");


        Label welcomeLabel = new Label("ENTER USERNAME");
        welcomeLabel.setFont(Font.font("Your Cool Font", FontWeight.BOLD, 14));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        ToggleGroup opponentGroup = new ToggleGroup();
        RadioButton userOption = new RadioButton("Play with another user");
        userOption.setToggleGroup(opponentGroup);
        userOption.setSelected(true);
        userOption.setFont(Font.font("Your Cool Font", FontWeight.BOLD, 14));

        RadioButton computerOption = new RadioButton("Play with computer");
        computerOption.setToggleGroup(opponentGroup);
        computerOption.setFont(Font.font("Your Cool Font", FontWeight.BOLD, 14));

        Button startButton = new Button("Start Game");
        startButton.setFont(Font.font("Your Cool Font", FontWeight.BOLD, 14));
        startButton.setOnAction(e -> {
            username = usernameField.getText().trim();
            if (computerOption.isSelected()) {
                opponentLabel.setText("COMPUTER'S BOARD");
            }
            dialogStage.close();
            primaryStage.show();
        });
        Button rulesButton = new Button("Rules");
        rulesButton.setFont(Font.font("Your Cool Font", FontWeight.BOLD, 14));
        rulesButton.setOnAction(e -> showRulesDialog(primaryStage));

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(startButton, rulesButton);

        dialogVBox.getChildren().addAll(welcomeLabel, usernameField, userOption, computerOption, buttonBox);

        Scene dialogScene = new Scene(dialogVBox, 600, 400);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }
    private void showRulesDialog(Stage ownerStage) {
        Stage rulesStage = new Stage();
        rulesStage.initModality(Modality.WINDOW_MODAL);
        rulesStage.initOwner(ownerStage);

        VBox rulesVBox = new VBox(20);
        rulesVBox.setAlignment(Pos.CENTER);
        rulesVBox.setPadding(new Insets(10));
        Label rulesLabel = new Label("GAME RULES:\n1.Each player secretly arranges their ships on a grid.\n2. Ships cannot overlap and are placed either horizontally or vertically.\n3. Players take turns guessing grid coordinates to locate and hit the opponent's ships.\n4. After each guess" +
                ", the opponent declares whether it was a HIT on a ship or a MISS.\n5. The game concludes when all ships of one player are sunk.\n\nPOINT SYSTEM: \nEarn +5 points for each HIT, lose -5 points for each MISS.\nTotal score is addition of HIT & MISS.");
        rulesLabel.setFont(Font.font("Your Cool Font", FontWeight.NORMAL, 14));

        Button closeButton = new Button("Close");
        closeButton.setFont(Font.font("Your Cool Font", FontWeight.BOLD, 14));
        closeButton.setOnAction(e -> rulesStage.close());

        rulesVBox.getChildren().addAll(rulesLabel, closeButton);

        Scene rulesScene = new Scene(rulesVBox, 600, 450);
        rulesStage.setScene(rulesScene);
        rulesStage.showAndWait();
    }

    private void initializeBoards() {
        int size = 10; // 10x10 board
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Button playerBtn = new Button();
                playerBtn.setMinWidth(30);
                playerBtn.setMinHeight(30);
                GridPane.setRowIndex(playerBtn, row);
                GridPane.setColumnIndex(playerBtn, col);
                playerBoard.getChildren().add(playerBtn);

                Button enemyBtn = new Button();
                enemyBtn.setMinWidth(30);
                enemyBtn.setMinHeight(30);
                enemyBtn.setOnAction(e -> handleEnemyAction(GridPane.getRowIndex(enemyBtn), GridPane.getColumnIndex(enemyBtn)));
                GridPane.setRowIndex(enemyBtn, row);
                GridPane.setColumnIndex(enemyBtn, col);
                enemyBoard.getChildren().add(enemyBtn);
            }
        }
    }

    private void handlePlayerAction(int row, int col) {
        // Logic for placing ships on the player's board
    }

    private void handleEnemyAction(int row, int col) {
        if (row == -1 || col == -1) {
            System.out.println("Error: Button does not have proper grid indices.");
            return;
        }
        // Proceed with sending attack to the server
        gameClient.sendAttack(row, col);
    }

    private void setupInteraction() {
        for (Node node : enemyBoard.getChildren()) {
            if (node instanceof Button) {
                Button button = (Button) node;
                button.setOnAction(event -> {
                    int row = GridPane.getRowIndex(button);
                    int col = GridPane.getColumnIndex(button);
                    handleEnemyAction(row, col);
                });
            }
        }
    }

    private void onGameUpdate(String message) {
        // Assume the message is a simple format "HIT 3 5" or "MISS 4 7"
        Platform.runLater(() -> {
            String[] parts = message.split(" ");
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[2]);
            Button btn = (Button) enemyBoard.getChildren().get(row * 10 + col);

            if (parts[0].equals("HIT")) {
                btn.setStyle("-fx-background-color: red;");
            } else if (parts[0].equals("MISS")) {
                btn.setStyle("-fx-background-color: blue;");
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
