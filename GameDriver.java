

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import com.sun.javafx.scene.control.behavior.ListCellBehavior;

//import javafx.application.*;
import javafx.application.*;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;


public class GameDriver extends Application{
	Scene mainMenuScene, singlePlayerCharacterSelectScene, multiPlayerCharacterSelectScene, multiPlayerGameScene, singlePlayerGameScene, victoryScene, replaysScene, helpScene, howToPlayScene, characterInfoScene, gameModeScene, replayViewerScene;
	Characters characterList = new Characters();
	Player playerOne = new Player();
	Player playerTwo = new Player();
	boolean p1CharacterSelected = false;
	boolean p2CharacterSelected = false;
	
	boolean p1SelectedOption = false;
	boolean p2SelectedOption = false;
	Replay replay = new Replay();
	
	ListView <File>replaysList = new ListView<>();
	File replayFolder = new File("resources/replays");
	File[] listOfReplayFiles = replayFolder.listFiles();
	
	
	public static void main(String[] args) {
		launch(args);

	}
	

	@Override
	public void start(Stage primaryStage){
		/*for (int i = 0; i < listOfReplayFiles.length; i++) {
			if (listOfReplayFiles[i].isFile()) {
				if (!(replaysList.getItems().contains(listOfReplayFiles[i]))) {
					replaysList.getItems().add(listOfReplayFiles[i]);
				}
			}
		}*/
		
		
		
		Fighter p1Fighter = new Fighter();
		Fighter p2Fighter = new Fighter();
		
		primaryStage.setTitle("Be the Mighty!");
		VBox backButtonVBox = new VBox();
		String mainThemeFile = "SuperClashJavaFX\\resources\\audio\\finale.wav";
		Media mainTheme = new Media(new File(mainThemeFile).toURI().toString());
		MediaPlayer mainThemePlayer = new MediaPlayer(mainTheme);
		mainThemePlayer.setOnEndOfMedia(new Runnable() {
			public void run() {
				mainThemePlayer.seek(Duration.ZERO);
			}
		});
		
		mainThemePlayer.play();
		
		FileInputStream input;
		
		try {
			input = new FileInputStream(new File("SuperClashJavaFX\\resources\\non_character_art\\logo.png"));
			Image image = new Image(input);
			primaryStage.getIcons().add(image);
		}
		catch (FileNotFoundException e){
			e.printStackTrace();
		}

		
		//Main Menu
		File titleFile = new File("SuperClashJavaFX\\resources\\non_character_art\\title_gif.gif");
		Image title = new Image(titleFile.toURI().toString());
		ImageView titleImageView = new ImageView(title);
		titleImageView.setFitWidth(750);
		titleImageView.setFitHeight(400); 
		Label mainMenuLabel = new Label("Main Menu");
		VBox mainMenu = new VBox(20);
		mainMenu.setPadding(new Insets(0,0,50,0));
		
		
		
		mainMenu.setAlignment(Pos.BOTTOM_CENTER);
		
		Button playButton = new Button("PLAY!");
		playButton.setPrefSize(188, 65);
		playButton.setMinSize(132, 46);
		playButton.setOnAction(e -> primaryStage.setScene(gameModeScene));
		
		Button helpButton = new Button("HELP");
		helpButton.setPrefSize(188, 65);
		helpButton.setMinSize(132, 46);
		helpButton.setOnAction(e -> primaryStage.setScene(helpScene));
		
		Button replayButton = new Button("VIEW REPLAYS");
		replayButton.setPrefSize(188, 65);
		replayButton.setMinSize(132, 46);
		replayButton.setOnAction(e -> primaryStage.setScene(replaysScene));
		
		
		mainMenu.getChildren().addAll(titleImageView, playButton, helpButton, replayButton);
		
		mainMenu.getChildren().addAll(mainMenuLabel);
		mainMenuScene = new Scene(mainMenu, 800, 700);
		String styleString1 = new File("/resources/styles/attributes.css").toURI().toString();
		mainMenuScene.getStylesheets().add(styleString1);
		
		
		//Game Mode Scene
		Label gameModeLabel = new Label("Game Mode Selection");
		BorderPane gameModeRoot = new BorderPane();
		VBox gameModeSelectScreen = new VBox(20);
		
		
		gameModeSelectScreen.setAlignment(Pos.BOTTOM_CENTER);
		
		backButtonVBox.setAlignment(Pos.BOTTOM_RIGHT);
		backButtonVBox.setPadding(new Insets(0,50,100,0));
		
		Button singlePlayer = new Button("SINGLE PLAYER");
		singlePlayer.setPrefSize(188, 65);
		singlePlayer.setMinSize(132, 46);
		singlePlayer.setOnAction(e -> primaryStage.setScene(singlePlayerCharacterSelectScene));
		
		Button multiPlayer = new Button("MULTIPLAYER");
		multiPlayer.setPrefSize(188, 65);
		multiPlayer.setMinSize(132, 46);
		multiPlayer.setOnAction(e -> primaryStage.setScene(multiPlayerCharacterSelectScene));
		
		Button backButton = new Button("BACK");
		backButton.setPrefSize(141, 46);
		backButton.setOnAction(e -> primaryStage.setScene(mainMenuScene));
		
		gameModeSelectScreen.getChildren().addAll(singlePlayer, multiPlayer);
		backButtonVBox.getChildren().add(backButton);
		
		
		gameModeSelectScreen.getChildren().addAll(gameModeLabel, backButtonVBox);
		gameModeRoot.setBottom(backButtonVBox);
		gameModeRoot.setCenter(gameModeSelectScreen);
		gameModeScene = new Scene(gameModeRoot, 800, 700);
		gameModeScene.getStylesheets().add(styleString1);
		
		
		//SinglePlayer Character Selection Scene
		VBox CSSRootRoot = new VBox();
		CSSRootRoot.setAlignment(Pos.CENTER);
		Text CSSTitle = new Text("Select Your Character!");
		Button backToMenu = new Button("BACK");
		backToMenu.setOnAction(e -> primaryStage.setScene(mainMenuScene));
		
		playerOne.setID(1);
		playerTwo.setID(0);
		
		Label label = new Label("Singleplayer Character Select Scene");
		
		HBox CSSRoot = new HBox();
		CSSRoot.setAlignment(Pos.CENTER);
		HBox titleHBox = new HBox();
		VBox player = new VBox(50);
		VBox characters = new VBox();
		Text charName = new Text();
		Text selectChar = new Text("Select your character!");
		VBox playerHolder = new VBox(30);
		
		
		Button singlePlayerReadyButton = new Button("LETS GO!");
		singlePlayerReadyButton.setDisable(true);
		singlePlayerReadyButton.setOnAction(e -> primaryStage.setScene(singlePlayerGameScene));
		
		
		
		/*ComboBox<String> gataSelect = new ComboBox<String>();
		gataSelect.setDisable(true);

    	gataSelect.getItems().add("Heleona");
    	gataSelect.getItems().add("Calientigressa");
    	
    	
    	gataSelect.setPrefSize(100, 50);*/
		
		player.setId("panel");
		
		ImageView characterIcon = new ImageView();
		characterIcon.setFitWidth(230);
		characterIcon.setFitHeight(200);
		
		
		player.setAlignment(Pos.CENTER);
		player.getChildren().add(charName);
		player.setPadding(new Insets(0, 30, 0, 30));
		
		
		titleHBox.getChildren().add(selectChar);
		titleHBox.setAlignment(Pos.TOP_CENTER);
		
		characters.setAlignment(Pos.CENTER);
		characters.setPadding(new Insets(0, 30, 0, 0));
		
		singlePlayerReadyButton.setPrefSize(150, 100);
		
		ListView<String> characterListView = new ListView<String>();
		for (int i = 0; i < characterList.characters.size(); i++) {
			if (characterList.findCharacter(i).isSelectable()) {
				characterListView.getItems().add(characterList.findCharacter(i).getName());
			}
		}
		
		//characterListView.getItems().add("Los Gatas Grandes");
		
		charName.setId("playerDisplay");
		characterListView.setId("fighters");
		
		
		characters.getChildren().addAll(characterListView);
		
		characterListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		        charName.setText(newValue);
		        
		        if (newValue != "Los Gatas Grandes") {
		        //File charImage = new File("character_icons\\" + newValue + ".png");
		        	File charImageFile = new File("resources\\character_icons\\" + newValue + ".png");
		        	Image charImageFromFile = new Image(charImageFile.toURI().toString());
		        	characterIcon.setImage(charImageFromFile);
		        	singlePlayerReadyButton.setDisable(false);
		        	for (int i = 0; i < characterList.characters.size(); i++) {
		        		if (characterList.findCharacter(i).getName().equals(newValue)) {
		        			playerOne.setCharacter(characterList.findCharacter(i));
		        			generateSinglePlayerGameScene(primaryStage);
		        		}
		        	}
		        	
		        	//gataSelect.setDisable(true);
		        }
		        
		        /*else {
		        	Image charImageFromFile = new Image("character_icons\\blank.png");
		        	characterIcon.setImage(charImageFromFile);
		        	singlePlayerReadyButton.setDisable(true);
		        	gataSelect.setDisable(false);
		        	gataSelect.valueProperty().addListener(new ChangeListener<String>() {
		                @Override public void changed(ObservableValue<? extends String> observable2, String oldValue, String newValue) {
		                	if (newValue != "Heleona" && newValue != "Calientigressa") {
		                		singlePlayerReadyButton.setDisable(true);
		                	}
		                	else {
		                		Image charImageFromFile = new Image("character_icons\\" + newValue + ".png");
		    		        	characterIcon.setImage(charImageFromFile);
		    		        	for (int i = 0; i < characterList.characters.size(); i++) {
		    		        		if (characterList.findCharacter(i).getName().equals(newValue)) {
		    		        			playerOne.setCharacter(characterList.findCharacter(i));
		    		        			generateSinglePlayerGameScene();
		    		        		}
		    		        	}
		    		        	singlePlayerReadyButton.setDisable(false);
		                	}
		                }    
		            });
		        	
		        }*/
		        
		        
		    }
		});
		
		
		
		player.getChildren().addAll(characterIcon);
		playerHolder.getChildren().addAll(player, singlePlayerReadyButton);
		playerHolder.setPadding(new Insets(0, 0, 30, 0));
		playerHolder.setAlignment(Pos.CENTER);
		
		
		CSSRootRoot.getChildren().addAll(CSSTitle, CSSRoot, backToMenu);
		CSSRoot.getChildren().addAll(characters, playerHolder);
		CSSRoot.setPadding(new Insets(100, 10, 0, 10));
		
		singlePlayerCharacterSelectScene = new Scene(CSSRootRoot, 800, 700);
		singlePlayerCharacterSelectScene.getStylesheets().add(styleString1);
		
		
		//Multiplayer Character Selection Scene
		playerOne.setID(1);
		playerTwo.setID(2);
		VBox MPCSSRootRoot = new VBox(30);
		MPCSSRootRoot.setAlignment(Pos.CENTER);
		HBox multiplayerCSSRoot = new HBox();
		Text p1 = new Text("Player One");
		Text p2 = new Text("Player Two");
		
		VBox playerOneWindow = new VBox(20);
		playerOneWindow.setAlignment(Pos.CENTER);
		ImageView playerOneCharacter = new ImageView();
		playerOneCharacter.setFitWidth(230);
		playerOneCharacter.setFitHeight(200);
		Text playerOneCharName = new Text();
		playerOneCharName.setId("multPlayerDisplay");
		
		VBox playerTwoWindow = new VBox(20);
		playerTwoWindow.setAlignment(Pos.CENTER);
		ImageView playerTwoCharacter = new ImageView();
		playerTwoCharacter.setFitWidth(230);
		playerTwoCharacter.setFitHeight(200);
		Text playerTwoCharName = new Text();
		playerTwoCharName.setId("multPlayerDisplay");
		
		Button multiPlayerReadyButton = new Button("LETS GO!");
		multiPlayerReadyButton.setPrefSize(130, 100);
		multiPlayerReadyButton.setDisable(true);
		multiPlayerReadyButton.setOnAction(e -> primaryStage.setScene(multiPlayerGameScene));
		
		/*ComboBox<String> playerOneGata = new ComboBox<String>();
		playerOneGata.setDisable(true);
		playerOneGata.getItems().add("Heleona");
		playerOneGata.getItems().add("Calientigressa");
		
		ComboBox<String> playerTwoGata = new ComboBox<String>();
		playerTwoGata.setDisable(true);
		playerTwoGata.getItems().add("Heleona");
		playerTwoGata.getItems().add("Calientigressa");
		*/
		
		ListView<String> playerOneSelection = new ListView<String>();
		playerOneSelection.setId("fighters");
		for (int i = 0; i < characterList.characters.size(); i++) {
			if (characterList.findCharacter(i).isSelectable()) {
				playerOneSelection.getItems().add(characterList.findCharacter(i).getName());
			}
		}
		
		//playerOneSelection.getItems().add("Los Gatas Grandes");
		
		ListView<String> playerTwoSelection = new ListView<String>();
		playerTwoSelection.setId("fighters");
		for (int i = 0; i < characterList.characters.size(); i++) {
			if (characterList.findCharacter(i).isSelectable()) {
				playerTwoSelection.getItems().add(characterList.findCharacter(i).getName());
			}
		}
		
		
		
		p1.setId("playerDisplay");
		playerOneWindow.getChildren().addAll(p1, playerOneCharName, playerOneCharacter);
		
		p2.setId("playerDisplay");
		playerTwoWindow.getChildren().addAll(p2, playerTwoCharName, playerTwoCharacter);
		
		playerOneSelection.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    @SuppressWarnings("unchecked")
			@Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		        playerOneCharName.setText(newValue);
		        
		        if (newValue != "Los Gatas Grandes") {
		        	p1CharacterSelected = true;
		        	multiPlayerReadyButton.setDisable(!bothPlayersSelectedChars(p1CharacterSelected, p2CharacterSelected));
		        	//File charImage = new File("character_icons\\" + newValue + ".png");
		        	File charImageFile = new File("resources\\character_icons\\" + newValue + ".png");
		        	Image charImageFromFile = new Image(charImageFile.toURI().toString());
		        	playerOneCharacter.setImage(charImageFromFile);

		        	for (int i = 0; i < characterList.characters.size(); i++) {
		        		if (characterList.findCharacter(i).getName().equals(newValue)) {
		        			playerOne.setCharacter(characterList.findCharacter(i));
		        			generateMultiplayerGameScene(primaryStage);
		      		        
		        		}
		        	}
		        	//playerOneGata.setDisable(true);
		        }
		        
		        /*else {
		        	p1CharacterSelected = false;
		        	multiPlayerReadyButton.setDisable(!bothPlayersSelectedChars(p1CharacterSelected, p2CharacterSelected));
		        	Image charImageFromFile = new Image("character_icons\\blank.png");
		        	playerOneCharacter.setImage(charImageFromFile);
		        	multiPlayerReadyButton.setDisable(true);
		        	playerOneGata.setDisable(false);
		        	playerOneGata.valueProperty().addListener(new ChangeListener<String>() {
		                @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		                	if (newValue != "Heleona" && newValue != "Calientigressa") {
		                		multiPlayerReadyButton.setDisable(true);
		                	}
		                	else {
		                		p1CharacterSelected = true;
		    		        	multiPlayerReadyButton.setDisable(!bothPlayersSelectedChars(p1CharacterSelected, p2CharacterSelected));
		                		Image charImageFromFile = new Image("character_icons\\" + newValue + ".png");
		    		        	playerOneCharacter.setImage(charImageFromFile);
		    		        	for (int i = 0; i < characterList.characters.size(); i++) {
		    		        		if (characterList.findCharacter(i).getName().equals(newValue)) {
		    		        			playerOne.setCharacter(characterList.findCharacter(i));
		    		        			generateMultiplayerGameScene();
		    		        		}
		    		        	}
		                	}
		                }    
		            });
		        	
		        }*/
		        
		    }
		});
		
		playerTwoSelection.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
		    @SuppressWarnings("unchecked")
			@Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		        playerTwoCharName.setText(newValue);
		        
		        if (newValue != "Los Gatas Grandes") {
		        	p2CharacterSelected = true;
		        	multiPlayerReadyButton.setDisable(!bothPlayersSelectedChars(p1CharacterSelected, p2CharacterSelected));
		        //File charImage = new File("character_icons\\" + newValue + ".png");
		        	File charImageFile = new File("resources\\character_icons\\" + newValue + ".png");
		        	Image charImageFromFileP2 = new Image(charImageFile.toURI().toString());
		        	playerTwoCharacter.setImage(charImageFromFileP2);
		        	for (int i = 0; i < characterList.characters.size(); i++) {
		        		if (characterList.findCharacter(i).getName().equals(newValue)) {
		        			playerTwo.setCharacter(characterList.findCharacter(i));
		        			generateMultiplayerGameScene(primaryStage);

		        		}
		        	}
		        	//playerTwoGata.setDisable(true);
		        }
		        
		        /*else {
		        	p2CharacterSelected = false;
		        	multiPlayerReadyButton.setDisable(!bothPlayersSelectedChars(p1CharacterSelected, p2CharacterSelected));
		        	Image charImageFromFileP2 = new Image("character_icons\\blank.png");
		        	playerTwoCharacter.setImage(charImageFromFileP2);
		        	
		        	playerTwoGata.setDisable(false);
		        	playerTwoGata.valueProperty().addListener(new ChangeListener<String>() {
		                @Override 
		                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		                	if (newValue != "Heleona" && newValue != "Calientigressa") {

		                	}
		                	else {
		                		p2CharacterSelected = true;
		    		        	multiPlayerReadyButton.setDisable(!bothPlayersSelectedChars(p1CharacterSelected, p2CharacterSelected));
		                		Image charImageFromFileP2 = new Image("character_icons\\" + newValue + ".png");
		    		        	playerTwoCharacter.setImage(charImageFromFileP2);
		    		        	for (int i = 0; i < characterList.characters.size(); i++) {
		    		        		if (characterList.findCharacter(i).getName().equals(newValue)) {
		    		        			playerTwo.setCharacter(characterList.findCharacter(i));
		    		        			generateMultiplayerGameScene();
		    		        		}
		    		        	}
		    		        	

		                	}
		                }    
		            });
		        	
		        }*/
		        
		    }
		});
		
		
		Button backToMenuFromMPCSS = new Button("BACK");
		backToMenuFromMPCSS.setOnAction(e -> primaryStage.setScene(mainMenuScene));
		
		
		
		multiplayerCSSRoot.getChildren().addAll(playerOneWindow, playerOneSelection, playerTwoSelection, playerTwoWindow);
		MPCSSRootRoot.getChildren().addAll(multiplayerCSSRoot, multiPlayerReadyButton, backToMenuFromMPCSS);
		
		
		multiPlayerCharacterSelectScene = new Scene(MPCSSRootRoot, 800, 700);
		multiPlayerCharacterSelectScene.getStylesheets().add(styleString1);
		
		//Help Scene
		Text help = new Text("HELP SCREEN");
		help.setId("title");
		VBox megaHelpRoot = new VBox(20);
		
		Text charInfo = new Text();
		
		ArrayList <Button> characterButtons = new ArrayList<>();
		HBox helpSceneRoot = new HBox(10);
		helpSceneRoot.setAlignment(Pos.CENTER);
		VBox buttons = new VBox(50);
		Button characterInfo = new Button("Character Info");
		characterInfo.setOnAction((ActionEvent) -> {
			for (Button button : characterButtons) {
				button.setDisable(false);
			}
		});
		Button howTo = new Button("How to Play");
		howTo.setOnAction((ActionEvent) -> {
			for (Button button : characterButtons) {
				button.setDisable(true);
			}
			charInfo.setId("explain");
			charInfo.setText("It's time to Be the Mighty!\nSelect a character and prepare for some turn based combat!\nEvery round, you can select an option:\nAttack\nGrab\nCounter\n\nin order to damage your opponent!\n\nATTACKS are direct blows that utilize your character's ATTACK power!\nATTACKS beat GRABS, but lose to COUNTERS!\nIf two ATTACKS collide, both players take damage!\n\nUse a GRAB to grab and throw your foe and deal damage based on your GRAB power!\nGRABS beat COUNTERS but lose to ATTACKS!\nIf both players GRAB at the same time, nothing will happen...\n\nCOUNTERING deals damage based on your OPPONENT'S ATTACK power!\nCOUNTERS only activate against ATTACKS and lose to GRABS!\nIf two players COUNTER at the same time, nothing will happen...\n\nNow that you know how to play, get out there and BE THE MIGHTY!!");
		});
		
		Button back = new Button("BACK TO MENU");
		back.setOnAction((ActionEvent) -> {
			primaryStage.setScene(mainMenuScene);
			charInfo.setText("");
		});
		
		buttons.getChildren().addAll(characterInfo, howTo, back);
		
		VBox infoButtonsSide = new VBox(20);
		VBox infoSide = new VBox();
		
		infoSide.getChildren().add(charInfo);
		
		for (int i = 0; i < characterList.characters.size(); i++) {
			int index = i;
			if (characterList.findCharacter(i).isSelectable() && characterList.findCharacter(i).getName() != "Calientigressa") {
				Button charInfoButton = new Button(characterList.findCharacter(i).getName());
				charInfoButton.setOnAction((ActionEvent) ->{
					displayCharInfo(index, charInfo);
				});
			charInfoButton.setDisable(true);
			characterButtons.add(charInfoButton);
			infoButtonsSide.getChildren().add(charInfoButton);
			}
		}
		
		
		
		
		helpSceneRoot.getChildren().addAll(buttons, infoButtonsSide, infoSide);
		megaHelpRoot.getChildren().addAll(help, helpSceneRoot);
		megaHelpRoot.setAlignment(Pos.CENTER);
		helpScene = new Scene(megaHelpRoot, 800, 700);
		helpScene.getStylesheets().add(styleString1);
		
		
		//Replay Scene
		VBox replayRootRoot = new VBox(20);
		replayRootRoot.setAlignment(Pos.CENTER);
		HBox replaySceneRoot= new HBox(30);
		replaySceneRoot.setAlignment(Pos.CENTER);
		Text selectReplay = new Text("Select a Replay!");
		selectReplay.setId("title");
	
		
		Button viewReplay = new Button("Watch the Replay!");
		viewReplay.setDisable(true);
		viewReplay.setOnAction((ActionEvent) -> {
			generateReplayViewScene(primaryStage, replay);
			primaryStage.setScene(replayViewerScene);
		});
		
		Button replayBackButton = new Button("BACK TO MENU");
		replayBackButton.setOnAction((ActionEvent) ->{
			primaryStage.setScene(mainMenuScene);
		});
		
		
		replaysList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<File>() {
		    @SuppressWarnings("unchecked")
			@Override
		    public void changed(ObservableValue<? extends File> observable, File oldValue, File newValue) {
		    	try {
					BufferedReader reader = new BufferedReader(new FileReader(newValue));
					String details;
					try {
						details = reader.readLine();
						
						while (details != null) {
							
							Replay dummyReplay = new Replay();
							Fighter dummy1 = new Fighter();
							Fighter dummy2 = new Fighter();
							
							ArrayList <Integer> playerOneActions = new ArrayList<>();
							ArrayList <Integer> playerTwoActions = new ArrayList<>();
							
							String [] detailsArray = details.split(",");
							
							String playerOneMoveString = detailsArray[0];
							String [] playerOneMovesArray = playerOneMoveString.split(" ");
							for (String move: playerOneMovesArray) {
								playerOneActions.add(Integer.parseInt(move));
							}
							
							
							String playerTwoMoveString = detailsArray[1];
							String [] playerTwoMovesArray = playerTwoMoveString.split(" ");
							for (String move: playerTwoMovesArray) {
								playerTwoActions.add(Integer.parseInt(move));
							}
							
							dummy1 = characterList.getCharacterByName(detailsArray[2]);
							dummy2 = characterList.getCharacterByName(detailsArray[3]);
							
							dummyReplay.setPlayerOneActions(playerOneActions);
							dummyReplay.setPlayerTwoActions(playerTwoActions);
							dummyReplay.setPlayerOneCharacter(dummy1);
							dummyReplay.setPlayerTwoCharacter(dummy2);
							
							replay.setPlayerOneActions(dummyReplay.getPlayerOneActions());
							replay.setPlayerTwoActions(dummyReplay.getPlayerTwoActions());
							replay.setPlayerOneCharacter(dummyReplay.getPlayerOneCharacter());
							replay.setPlayerTwoCharacter(dummyReplay.getPlayerTwoCharacter());
							
							
							details = reader.readLine();
						}
						
						viewReplay.setDisable(false);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		    }
		}
		);
		
		
		replaySceneRoot.getChildren().addAll(viewReplay, replaysList);
		replayRootRoot.getChildren().addAll(selectReplay, replaySceneRoot, replayBackButton);
		replaysScene = new Scene(replayRootRoot, 800, 700);
		replaysScene.getStylesheets().add(styleString1);
		
		
		
		
		
		primaryStage.setScene(mainMenuScene);
		
		
		
		primaryStage.show();
	}
	
	public void displayCharInfo(int index, Text textBox) {
		String charInfo = "";
		
		charInfo = characterList.findCharacter(index).getName() + "\nHP: " + characterList.findCharacter(index).getHp()+ "\nAttack: " + characterList.findCharacter(index).getAttack() + "\nGrab: " + characterList.findCharacter(index).getGrab();
		
		
		textBox.setText(charInfo);
		textBox.setId("characterInfoDisplay");
	}
	
	public boolean bothPlayersSelectedChars(boolean p1CharacterSelected, boolean p2CharacterSelected) {
		if (p1CharacterSelected && p2CharacterSelected) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void generateReplayViewScene(Stage primaryStage, Replay replay) {
		String styleString1 = new File("resources/styles/attributes.css").toURI().toString();
		
		ArrayList<Integer> playerOneOptions = new ArrayList<Integer>();
		playerOneOptions = replay.getPlayerOneActions();
		ArrayList<Integer> playerTwoOptions = new ArrayList<Integer>();
		playerTwoOptions = replay.getPlayerTwoActions();
		
		playerOne.setCharacter(replay.getPlayerOneCharacter());
		playerTwo.setCharacter(replay.getPlayerTwoCharacter());
		
		HBox controlButtons = new HBox(30);
		Button backToMenuButton = new Button("BACK TO MENU");
		backToMenuButton.setOnAction((ActionEvent) -> {
			primaryStage.setScene(mainMenuScene);
		});
		
		controlButtons.getChildren().addAll(backToMenuButton);
		
		try {
			playerOne.setHealth();
			playerTwo.setHealth(); 
		}
		catch (NullPointerException e) {
			
		}
		
		VBox gameStuff = new VBox(20);
		gameStuff.setAlignment(Pos.CENTER);
		Text waitBox = new Text();
		waitBox.setId("waitBox");
		ListView<String> displayActions = new ListView<String>();
		displayActions.setId("multPlayerDisplay");
		
		gameStuff.getChildren().addAll(waitBox, displayActions);
		
		Text p1Health = new Text();
		p1Health.setId("hpText");
		p1Health.setText("HP: " + playerOne.getHealth());
		p1Health.setId("actionDisplay");
		
		Text p2Health = new Text();
		p2Health.setId("hpText");
		p2Health.setText("HP: " + playerTwo.getHealth());
		p2Health.setId("actionDisplay");
		
		HBox multiPlayerGameRoot = new HBox(20);
		multiPlayerGameRoot.setPadding(new Insets(0, 50, 0, 50));
		
		VBox playerOneBox = new VBox(20);
		playerOneBox.setAlignment(Pos.CENTER);
		Text p1Char = new Text();
		p1Char.setId("multPlayerDisplay");
		
		try {
			p1Char.setText(playerOne.getCharacter().getName());
		}
		
		catch (NullPointerException e) {
			
		}
		
		ImageView p1CharIcon = new ImageView();
		p1CharIcon.setFitHeight(210);
		p1CharIcon.setFitWidth(230);
		try {
			File playerOneIconFile = new File("resources\\character_icons\\" + playerOne.getCharacter().getName() + ".png");
			Image playerOneIcon = new Image(playerOneIconFile.toURI().toString());
			p1CharIcon.setImage(playerOneIcon);
		}
		catch (NullPointerException e) {
			
		}
		playerOneBox.getChildren().addAll(p1Char, p1Health, p1CharIcon);
		
		
		VBox playerTwoBox = new VBox(20);
		playerTwoBox.setAlignment(Pos.CENTER);
		Text p2Char = new Text();
		p2Char.setId("multPlayerDisplay");
		
		try {
			p2Char.setText(playerTwo.getCharacter().getName());
		}
			
		catch (NullPointerException e) {
				
		}
		
		ImageView p2CharIcon = new ImageView();
		p2CharIcon.setFitHeight(210);
		p2CharIcon.setFitWidth(230);
		p2CharIcon.setRotationAxis(Rotate.Y_AXIS);
		p2CharIcon.setRotate(180);
		try {
			File playerTwoIconFile = new File("resources\\character_icons\\" + playerOne.getCharacter().getName() + ".png");
			Image playerTwoIcon = new Image(playerTwoIconFile.toURI().toString());
			p2CharIcon.setImage(playerTwoIcon);
		}
		catch (NullPointerException e) {
			
		}
		playerTwoBox.getChildren().addAll(p2Char, p2Health, p2CharIcon);
		
		for (int i = 0; i < playerOneOptions.size(); i ++) {			
			
			switch(playerOneOptions.get(i)) {
			case 1:
				playerOne.attack();
				break;
			case 2:
				playerOne.grab();
				break;
			case 3:
				playerOne.counter();
				break;
			}
			
			switch(playerTwoOptions.get(i)) {
			case 1:
				playerTwo.attack();
				break;
			case 2:
				playerTwo.grab();
				break;
			case 3:
				playerTwo.counter();
				break;
			}
			
	
				Timer timer = new Timer();
				
				timer.schedule(new Countdown(waitBox, "3"), 1000 +(4000 * i));
				timer.schedule(new Countdown(waitBox, "2"), 2000 + (4000 * i));
				timer.schedule(new Countdown(waitBox, "1"), 3000+ (4000 * i));
				timer.schedule(new Countdown(waitBox, "OK!"), 4000+ (4000 * i));

				String flavorText = compareOptions(playerOne, playerTwo);
				
				int p1HP = playerOne.getHealth();
				if (p1HP < 0) {
					p1HP = 0;
				}
				int p2HP = playerTwo.getHealth();
				if (p2HP < 0) {
					p2HP = 0;
				}
				
				timer.schedule(new Countdown(p1Health, "HP: " + p1HP), 4000+ (4000 * i));
				timer.schedule(new Countdown(p2Health, "HP: " + p2HP), 4000+ (4000 * i));
				
				if (!(playerOne.getHealth() <= 0 || playerTwo.getHealth() <= 0)) {
					timer.schedule(new DisplayCountdown(displayActions, flavorText), 4000+ (4000 * i));
					
					
				}
				
				else {
					timer.schedule(new DisplayCountdown(displayActions, flavorText), 4000+ (4000 * i));
					if (playerOne.getHealth() <= 0 && !(playerTwo.getHealth() <= 0)) {
						timer.schedule(new DisplayCountdown(displayActions, "GAME! The winner is..." + playerTwo.getCharacter().getName() + "!"), 4100+ (3000 * i));
						replay.setPlayerOneActions(playerOneOptions);
						replay.setPlayerTwoActions(playerTwoOptions);
						replay.setPlayerTwoCharacter(playerTwo.getCharacter());
						replay.setPlayerOneCharacter(playerOne.getCharacter());
						gameStuff.getChildren().add(controlButtons);
					}
					
					else if (!(playerOne.getHealth() <= 0) && playerTwo.getHealth() <= 0) {
						timer.schedule(new DisplayCountdown(displayActions, "GAME! The winner is..." + playerOne.getCharacter().getName() + "!"), 4100+ (4000 * i));
						replay = new Replay(playerOneOptions, playerTwoOptions, playerOne.getCharacter(), playerTwo.getCharacter());
						gameStuff.getChildren().add(controlButtons);
					}
					
					else {
						timer.schedule(new DisplayCountdown(displayActions, "GAME! No contest.") , 4100+ (4000 * i));
						replay = new Replay(playerOneOptions, playerTwoOptions, playerOne.getCharacter(), playerTwo.getCharacter());
						gameStuff.getChildren().add(controlButtons);
					}

				}
		}
		
		multiPlayerGameRoot.getChildren().addAll(playerOneBox, gameStuff, playerTwoBox);
		replayViewerScene = new Scene(multiPlayerGameRoot, 800, 700);
		replayViewerScene.getStylesheets().add(styleString1);

		
	}
	
	
	
	public void generateMultiplayerGameScene(Stage primaryStage) {
		String styleString1 = new File("resources/styles/attributes.css").toURI().toString();
		
		ArrayList<Integer> playerOneOptions = new ArrayList<Integer>();
		ArrayList<Integer> playerTwoOptions = new ArrayList<Integer>();
		
		
		HBox controlButtons = new HBox(30);
		Button backToMenuButton = new Button("BACK TO MENU");
		backToMenuButton.setOnAction((ActionEvent) -> {
			primaryStage.setScene(mainMenuScene);
		});
		Button saveReplayButton = new Button("SAVE REPLAY");
		saveReplayButton.setDisable(false);
		saveReplayButton.setOnAction((ActionEvent) -> {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyMMddHHmmss");
			LocalDateTime now = LocalDateTime.now();
			File replayFile = new File("resources" + File.separator + "replays" + File.separator + "replay" + playerOne.getCharacter().getName() + playerTwo.getCharacter().getName() + dtf.format(now) + ".csv");
			try {
				FileWriter output = new FileWriter(replayFile);
				replaysList.getItems().add(replayFile);
				output.append(replay.toString());
				output.flush();
				output.close();
				saveReplayButton.setDisable(true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		controlButtons.getChildren().addAll(backToMenuButton, saveReplayButton);
		
		try {
			playerOne.setHealth();
			playerTwo.setHealth(); 
		}
		catch (NullPointerException e) {
			
		}
		
		
		VBox gameStuff = new VBox(20);
		gameStuff.setAlignment(Pos.CENTER);
		Button roundGo = new Button ("GO");
		Text waitBox = new Text();
		waitBox.setId("waitBox");
		
		roundGo.setDisable(true);
		ListView<String> displayActions = new ListView<String>();
		displayActions.setId("multPlayerDisplay");
		
		Button p1AttackButton = new Button("ATTACK");
		Button p1GrabButton = new Button("GRAB");
		Button p1CounterButton = new Button("COUNTER");
		Text p1Health = new Text();
		p1Health.setId("hpText");
		p1Health.setText("HP: " + playerOne.getHealth());
		p1Health.setId("actionDisplay");
		
		Button p2AttackButton = new Button("ATTACK");
		Button p2GrabButton = new Button("GRAB");
		Button p2CounterButton = new Button("COUNTER");
		Text p2Health = new Text();
		p2Health.setId("hpText");
		p2Health.setText("HP: " + playerTwo.getHealth());
		p2Health.setId("actionDisplay");
		
		
		
		
		
		gameStuff.getChildren().addAll(waitBox, displayActions, roundGo);
		
		roundGo.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				Timer timer = new Timer();
				
				waitBox.setText("3");
				timer.schedule(new Countdown(waitBox, "2"), 1000);
				timer.schedule(new Countdown(waitBox, "1"), 2000);
				timer.schedule(new Countdown(waitBox, "OK!"), 3000);

				String flavorText = compareOptions(playerOne, playerTwo);
				
				int p1HP = playerOne.getHealth();
				if (p1HP < 0) {
					p1HP = 0;
				}
				int p2HP = playerTwo.getHealth();
				if (p2HP < 0) {
					p2HP = 0;
				}
				
				timer.schedule(new Countdown(p1Health, "HP: " + p1HP), 3000);
				timer.schedule(new Countdown(p2Health, "HP: " + p2HP), 3000);
				
				if (!(playerOne.getHealth() <= 0 || playerTwo.getHealth() <= 0)) {
					timer.schedule(new DisplayCountdown(displayActions, flavorText), 3000);
					
					roundGo.setDisable(true);
					
					timer.schedule(new ButtonCountdown(p1AttackButton), 3000);
					timer.schedule(new ButtonCountdown(p1GrabButton), 3000);
					timer.schedule(new ButtonCountdown(p1CounterButton), 3000);
					
					timer.schedule(new ButtonCountdown(p2AttackButton), 3000);
					timer.schedule(new ButtonCountdown(p2GrabButton), 3000);
					timer.schedule(new ButtonCountdown(p2CounterButton), 3000);
					
				}
				
				else {
					timer.schedule(new DisplayCountdown(displayActions, flavorText), 3000);
					if (playerOne.getHealth() <= 0 && !(playerTwo.getHealth() <= 0)) {
						timer.schedule(new DisplayCountdown(displayActions, "GAME! The winner is..." + playerTwo.getCharacter().getName() + "!"), 3100);
						replay.setPlayerOneActions(playerOneOptions);
						replay.setPlayerTwoActions(playerTwoOptions);
						replay.setPlayerTwoCharacter(playerTwo.getCharacter());
						replay.setPlayerOneCharacter(playerOne.getCharacter());
						gameStuff.getChildren().add(controlButtons);
					}
					
					else if (!(playerOne.getHealth() <= 0) && playerTwo.getHealth() <= 0) {
						timer.schedule(new DisplayCountdown(displayActions, "GAME! The winner is..." + playerOne.getCharacter().getName() + "!"), 3100);
						replay = new Replay(playerOneOptions, playerTwoOptions, playerOne.getCharacter(), playerTwo.getCharacter());
						gameStuff.getChildren().add(controlButtons);
					}
					
					else {
						timer.schedule(new DisplayCountdown(displayActions, "GAME! No contest.") , 3100);
						replay = new Replay(playerOneOptions, playerTwoOptions, playerOne.getCharacter(), playerTwo.getCharacter());
						gameStuff.getChildren().add(controlButtons);
					}
					
					roundGo.setDisable(true);
				}
					
			}
			
		});
		
		
		HBox multiPlayerGameRoot = new HBox(20);
		multiPlayerGameRoot.setPadding(new Insets(0, 50, 0, 50));
		
		VBox playerOneBox = new VBox(20);
		playerOneBox.setAlignment(Pos.CENTER);
		Text p1Char = new Text();
		p1Char.setId("multPlayerDisplay");
		
		try {
			p1Char.setText(playerOne.getCharacter().getName());
		}
		
		catch (NullPointerException e) {
			
		}

		
		ImageView p1CharIcon = new ImageView();
		p1CharIcon.setFitHeight(210);
		p1CharIcon.setFitWidth(230);
		try {
			File playerOneIconFile = new File("resources\\character_icons\\" + playerOne.getCharacter().getName() + ".png");
			Image playerOneIcon = new Image(playerOneIconFile.toURI().toString());
			p1CharIcon.setImage(playerOneIcon);
		}
		catch (NullPointerException e) {
			
		}
		
		
		p1AttackButton.setOnAction(new EventHandler <ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				playerOne.attack();
				playerOneOptions.add(1);
				p1SelectedOption = true;
				
				if (p1SelectedOption && p2SelectedOption) {
					roundGo.setDisable(false);
				}
				
				p1AttackButton.setDisable(true);
				p1GrabButton.setDisable(true);
				p1CounterButton.setDisable(true);
			}
			
		});
		
		p1GrabButton.setOnAction(new EventHandler <ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				playerOne.grab();
				playerOneOptions.add(2);
				p1SelectedOption = true;
				
				if (p1SelectedOption && p2SelectedOption) {
					roundGo.setDisable(false);
				}
				
				p1AttackButton.setDisable(true);
				p1GrabButton.setDisable(true);
				p1CounterButton.setDisable(true);
				
			}
			
		});
		
		p1CounterButton.setOnAction(new EventHandler <ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				playerOne.counter();
				playerOneOptions.add(3);
				p1SelectedOption = true;
				
				if (p1SelectedOption && p2SelectedOption) {
					roundGo.setDisable(false);
				}
				
				p1AttackButton.setDisable(true);
				p1GrabButton.setDisable(true);
				p1CounterButton.setDisable(true);
				
			}
			
		});
		//Button p1SpecialButton = new Button("SPECIAL");
		playerOneBox.getChildren().addAll(p1Char, p1Health, p1CharIcon, p1AttackButton, p1GrabButton, p1CounterButton);
		
		VBox playerTwoBox = new VBox(20);
		playerTwoBox.setAlignment(Pos.CENTER);
		Text p2Char = new Text();
		p2Char.setId("multPlayerDisplay");
		
		try {
			p2Char.setText(playerTwo.getCharacter().getName());
		}
			
		catch (NullPointerException e) {
				
		}
		
		
		ImageView p2CharIcon = new ImageView();
		p2CharIcon.setFitHeight(210);
		p2CharIcon.setFitWidth(230);
		p2CharIcon.setRotationAxis(Rotate.Y_AXIS);
		p2CharIcon.setRotate(180);
		try {
			File playerTwoIconFile = new File("resources\\character_icons\\" + playerTwo.getCharacter().getName() + ".png");
			Image playerTwoIcon = new Image(playerTwoIconFile.toURI().toString());
			p2CharIcon.setImage(playerTwoIcon);
		}
		catch (NullPointerException e) {
			
		}
		
		
		p2AttackButton.setOnAction(new EventHandler <ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				playerTwo.attack();
				playerTwoOptions.add(1);
				p2SelectedOption = true;
				
				if (p1SelectedOption && p2SelectedOption) {
					roundGo.setDisable(false);
				}
				
				p2AttackButton.setDisable(true);
				p2GrabButton.setDisable(true);
				p2CounterButton.setDisable(true);
			}
			
		});
		
		p2GrabButton.setOnAction(new EventHandler <ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				playerTwo.grab();
				playerTwoOptions.add(2);
				p2SelectedOption = true;
				
				if (p1SelectedOption && p2SelectedOption) {
					roundGo.setDisable(false);
				}
				
				p2AttackButton.setDisable(true);
				p2GrabButton.setDisable(true);
				p2CounterButton.setDisable(true);
				
			}
			
		});
		
		p2CounterButton.setOnAction(new EventHandler <ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				playerTwo.counter();
				playerTwoOptions.add(3);
				p2SelectedOption = true;
				
				if (p1SelectedOption && p2SelectedOption) {
					roundGo.setDisable(false);
				}
				
				p2AttackButton.setDisable(true);
				p2GrabButton.setDisable(true);
				p2CounterButton.setDisable(true);
			}
			
		});
		//Button p2SpecialButton = new Button("SPECIAL");
		playerTwoBox.getChildren().addAll(p2Char, p2Health, p2CharIcon, p2AttackButton, p2GrabButton, p2CounterButton);
		
		
		
		
		multiPlayerGameRoot.getChildren().addAll(playerOneBox, gameStuff, playerTwoBox);
		multiPlayerGameScene = new Scene(multiPlayerGameRoot, 800, 700);
		multiPlayerGameScene.getStylesheets().add(styleString1);
	}
	
	
	public class Countdown extends TimerTask {
		Text textbox;
		String text;
		
		public Countdown(Text textbox, String text) {
			this.textbox = textbox;
			this.text = text;
		}
		@Override
		public void run() {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					textbox.setText(text);
				}
			});
		}
	}
	
	public class DisplayCountdown extends TimerTask {
		ListView<String> list;
		String text;
		
		public DisplayCountdown(ListView<String> list, String text) {
			this.list = list;
			this.text = text;
		}
		@Override
		public void run() {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					list.getItems().add(text);

				}
			});
		}
	}
	
	public class ButtonCountdown extends TimerTask {
		Button button;
		
		public ButtonCountdown(Button button) {
			this.button = button;
		}

		@Override
		public void run() {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					if (button.isDisabled() == false) {
						button.setDisable(true);
					}
					else {
						button.setDisable(false);
					}
				}
			});
		}
		
	}
	
	public class ImageCountdown extends TimerTask{
		ImageView imageView;
		Image image;
		
		ImageCountdown(ImageView imageView, Image image){
			this.imageView = imageView;
			this.image = image;
		}

		@Override
		public void run() {
			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					imageView.setImage(image);
				}
			});
		}
	}
	
	
	
	public void generateSinglePlayerGameScene(Stage primaryStage) {
		String styleString1 = new File("resources/styles/attributes.css").toURI().toString();
		
		playerOne.setHealth();
		
		
		Button backToMenuButton = new Button("BACK TO MENU");
		backToMenuButton.setOnAction((ActionEvent) -> {
			primaryStage.setScene(mainMenuScene);
		});

		
		ArrayList<Integer> playerChoices = new ArrayList<>();
		ArrayList<Integer> CPUChoices = new ArrayList<>();
		
		HBox singlePlayerGameRoot = new HBox(20);
		singlePlayerGameRoot.setPadding(new Insets(0, 50, 0, 50));
		
		VBox playerBox = new VBox(20);
		playerBox.setAlignment(Pos.CENTER);
		Text playerChar = new Text();
		playerChar.setId("playerDisplay");
		
		try {
			playerChar.setText(playerOne.getCharacter().getName());
		}
		
		catch (NullPointerException e) {
			
		}

		Text playerHealth = new Text("HP: " + playerOne.getHealth());
		playerHealth.setId("multPlayerDisplay");
		File playerIconFile = new File("resources\\character_icons\\" + playerOne.getCharacter().getName() + ".png");
		Image playerIcon = new Image(playerIconFile.toURI().toString());
		
		ImageView playerCharIcon = new ImageView(playerIcon);
		playerCharIcon.setFitHeight(210);
		playerCharIcon.setFitWidth(230);
		
		Button playerAttackButton = new Button("ATTACK"); 
		Button playerGrabButton = new Button("GRAB");
		Button playerCounterButton = new Button("COUNTER");
		//Button playerSpecialButton = new Button("SPECIAL");
		
		VBox gameStuff = new VBox(30);
		gameStuff.setAlignment(Pos.CENTER);
		Text waitBox = new Text();
		waitBox.setId("waitBox");
		ListView display = new ListView();
		display.setId("actionDisplay");
		
		gameStuff.getChildren().addAll(waitBox, display);
		
		
		
		playerTwo.setCharacter(characterList.findCharacter(0));
		VBox CPUBox = new VBox(20);
		CPUBox.setAlignment(Pos.CENTER);
		File CPUIconFile = new File("resources\\character_icons\\" + playerTwo.getCharacter().getName() + ".png");
		Image CPUIcon = new Image(CPUIconFile.toURI().toString());
		playerTwo.setHealth();
		Text CPUHP = new Text("HP: " + playerTwo.getCharacter().getHp());
		CPUHP.setId("multPlayerDisplay");
		Text CPUCharName = new Text(playerTwo.getCharacter().getName());
		CPUCharName.setId("playerDisplay");
		ImageView CPUIconView = new ImageView(CPUIcon);
		CPUIconView.setFitHeight(210);
		CPUIconView.setFitWidth(230);
		CPUIconView.setRotationAxis(Rotate.Y_AXIS);
		CPUIconView.setRotate(180);
		
		CPUBox.getChildren().addAll(CPUCharName, CPUHP, CPUIconView);
		
		playerAttackButton.setOnAction(new EventHandler <ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				int AICharID = playerTwo.getCharacter().getId();
				
				playerOne.attack();
				playerChoices.add(1);
				playerTwo.AI(playerChoices);
				CPUChoices.add(CPUChoice(playerTwo));
				
				playerAttackButton.setDisable(true);
				playerGrabButton.setDisable(true);
				playerCounterButton.setDisable(true);
				
				Timer timer = new Timer();
				
				waitBox.setText("3");
				timer.schedule(new Countdown(waitBox, "2"), 1000);
				timer.schedule(new Countdown(waitBox, "1"), 2000);
				timer.schedule(new Countdown(waitBox, "OK!"), 3000);

				String flavorText = compareOptions(playerOne, playerTwo);
				
				int p1HP = playerOne.getHealth();
				if (p1HP < 0) {
					p1HP = 0;
				}
				int p2HP = playerTwo.getHealth();
				if (p2HP < 0) {
					p2HP = 0;
				}
				
				timer.schedule(new Countdown(playerHealth, "HP: " + p1HP), 3000);
				timer.schedule(new Countdown(CPUHP, "HP: " + p2HP), 3000);
				
				if (!(playerOne.getHealth() <= 0 || playerTwo.getHealth() <= 0)) {
					
					timer.schedule(new DisplayCountdown(display, flavorText), 3000);
					
					timer.schedule(new ButtonCountdown(playerAttackButton), 3000);
					timer.schedule(new ButtonCountdown(playerGrabButton), 3000);
					timer.schedule(new ButtonCountdown(playerCounterButton), 3000);
					
					//timer.schedule(new Countdown(CPUHP, "HP: " + playerTwo.getHealth()), 3100);

					
				}
				
				else {
					
					timer.schedule(new DisplayCountdown(display, flavorText), 3000);
					
					if (playerTwo.getHealth() <= 0) {
						if (AICharID + 1 == characterList.characters.size()) {
							gameStuff.getChildren().add(backToMenuButton); 
							timer.schedule(new DisplayCountdown(display, "You've beat all the opponents in the CPU gauntlet! GOOD JOB, GAMER!"), 3001);
						}
						else {
							AICharID++;
							while (!(characterList.findCharacter(AICharID).isSelectable())) {
								AICharID++;
							}
							timer.schedule(new Countdown(CPUCharName, playerTwo.getCharacter().getName()), 3001);
							timer.schedule(new DisplayCountdown(display, playerTwo.getCharacter().getName() + " defeated! " + characterList.findCharacter(AICharID) + " steps into the ring!"), 3001);
							playerTwo.setCharacter(characterList.findCharacter(AICharID));
							
							File newCharIconFile = new File("resources\\character_icons\\" + characterList.findCharacter(AICharID).getName() + ".png");
							Image newCharIcon = new Image(newCharIconFile.toURI().toString());
							timer.schedule(new ImageCountdown(CPUIconView, newCharIcon), 3000);
							
							//CPUIconView.setImage(newCharIcon);
							
							timer.schedule(new Countdown(CPUHP, "HP: " + characterList.findCharacter(AICharID).getHp()), 3001);
							timer.schedule(new Countdown(CPUCharName, characterList.findCharacter(AICharID).getName()), 3001);
							timer.schedule(new UpdateChar(playerTwo, characterList.findCharacter(AICharID)), 3000);
						}
						
						timer.schedule(new ButtonCountdown(playerAttackButton), 3000);
						timer.schedule(new ButtonCountdown(playerGrabButton), 3000);
						timer.schedule(new ButtonCountdown(playerCounterButton), 3000);

					}
					
					else {
						gameStuff.getChildren().add(backToMenuButton); 
						if (playerOne.getHealth() <= 0) {
							timer.schedule(new DisplayCountdown(display, "FAILURE!"), 3000);
						}
					}
				}
			}
			
		});
		
		playerGrabButton.setOnAction(new EventHandler <ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				int AICharID = playerTwo.getCharacter().getId();
				playerOne.grab();
				playerChoices.add(2);
				playerTwo.AI(playerChoices);
				CPUChoices.add(CPUChoice(playerTwo));
				
				playerAttackButton.setDisable(true);
				playerGrabButton.setDisable(true);
				playerCounterButton.setDisable(true);
				
				Timer timer = new Timer();
				
				waitBox.setText("3");
				timer.schedule(new Countdown(waitBox, "2"), 1000);
				timer.schedule(new Countdown(waitBox, "1"), 2000);
				timer.schedule(new Countdown(waitBox, "OK!"), 3000);

				String flavorText = compareOptions(playerOne, playerTwo);
				
				timer.schedule(new Countdown(playerHealth, "HP: " + playerOne.getHealth()), 3000);
				timer.schedule(new Countdown(CPUHP, "HP: " + playerTwo.getHealth()), 3000);
				
				if (!(playerOne.getHealth() <= 0 || playerTwo.getHealth() <= 0)) {
					
					timer.schedule(new DisplayCountdown(display, flavorText), 3000);
					
					timer.schedule(new ButtonCountdown(playerAttackButton), 3000);
					timer.schedule(new ButtonCountdown(playerGrabButton), 3000);
					timer.schedule(new ButtonCountdown(playerCounterButton), 3000);

					//timer.schedule(new Countdown(CPUHP, "HP: " + playerTwo.getHealth()), 3100);
				}
				
				else {
					
					if (playerTwo.getHealth() <= 0) {
						if (AICharID + 1 == characterList.characters.size()) {
							gameStuff.getChildren().add(backToMenuButton); 
							timer.schedule(new DisplayCountdown(display, "You've beat all the opponents in the CPU gauntlet! GOOD JOB, GAMER!"), 3000);
						}
						else {
							AICharID++;
							while (!(characterList.findCharacter(AICharID).isSelectable())) {
								AICharID++;
							}
							
							timer.schedule(new DisplayCountdown(display, playerTwo.getCharacter().getName() + " defeated! " + characterList.findCharacter(AICharID) + " steps into the ring!"), 3000);
							playerTwo.setCharacter(characterList.findCharacter(AICharID));
							File newCharIconFile = new File("resources\\character_icons\\" + characterList.findCharacter(AICharID).getName() + ".png");
							Image newCharIcon = new Image(newCharIconFile.toURI().toString());
							timer.schedule(new ImageCountdown(CPUIconView, newCharIcon), 3000);
							//CPUIconView.setImage(newCharIcon);
							
							timer.schedule(new UpdateChar(playerTwo, characterList.findCharacter(AICharID)), 3000);
							timer.schedule(new Countdown(CPUHP, "HP: " + characterList.findCharacter(AICharID).getHp()), 3001);
							timer.schedule(new Countdown(CPUCharName, characterList.findCharacter(AICharID).getName()), 3001);
						}
						
						timer.schedule(new ButtonCountdown(playerAttackButton), 3000);
						timer.schedule(new ButtonCountdown(playerGrabButton), 3000);
						timer.schedule(new ButtonCountdown(playerCounterButton), 3000);

					}
					
					else {
						gameStuff.getChildren().add(backToMenuButton); 
						if (playerOne.getHealth() <= 0) {
							timer.schedule(new DisplayCountdown(display, "FAILURE!"), 3000);
						}
					}
				}
			}
			
		});
		
		playerCounterButton.setOnAction(new EventHandler <ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				int AICharID = playerTwo.getCharacter().getId();
				playerOne.counter();
				playerChoices.add(3);
				playerTwo.AI(playerChoices);
				CPUChoices.add(CPUChoice(playerTwo));
				
				playerAttackButton.setDisable(true);
				playerGrabButton.setDisable(true);
				playerCounterButton.setDisable(true);
				
				Timer timer = new Timer();
				
				waitBox.setText("3");
				timer.schedule(new Countdown(waitBox, "2"), 1000);
				timer.schedule(new Countdown(waitBox, "1"), 2000);
				timer.schedule(new Countdown(waitBox, "OK!"), 3000);

				String flavorText = compareOptions(playerOne, playerTwo);
				
				
				timer.schedule(new Countdown(playerHealth, "HP: " + playerOne.getHealth()), 3000);
				timer.schedule(new Countdown(CPUHP, "HP: " + playerTwo.getHealth()), 3000);
				
				if (!(playerOne.getHealth() <= 0 || playerTwo.getHealth() <= 0)) {
					
					timer.schedule(new DisplayCountdown(display, flavorText), 3000);
					
					timer.schedule(new ButtonCountdown(playerAttackButton), 3000);
					timer.schedule(new ButtonCountdown(playerGrabButton), 3000);
					timer.schedule(new ButtonCountdown(playerCounterButton), 3000);

					//timer.schedule(new Countdown(CPUHP, "HP: " + playerTwo.getHealth()), 3100);
				}
				
				else {
					
					if (playerTwo.getHealth() <= 0) {
						if (AICharID + 1 == characterList.characters.size()) {
							gameStuff.getChildren().add(backToMenuButton); 
							timer.schedule(new DisplayCountdown(display, "You've beat all the opponents in the CPU gauntlet! GOOD JOB, GAMER!"), 3000);
						}
						else {
							AICharID++;
							while (!(characterList.findCharacter(AICharID).isSelectable())) {
								AICharID++;
							}
							
							
							timer.schedule(new DisplayCountdown(display, playerTwo.getCharacter().getName() + " defeated! " + characterList.findCharacter(AICharID) + " steps into the ring!"), 3000);
							playerTwo.setCharacter(characterList.findCharacter(AICharID));
							File newCharIconFile = new File("resources\\character_icons\\" + characterList.findCharacter(AICharID).getName() + ".png");
							Image newCharIcon = new Image(newCharIconFile.toURI().toString());
							timer.schedule(new ImageCountdown(CPUIconView, newCharIcon), 3000);
							//CPUIconView.setImage(newCharIcon);
							
							timer.schedule(new UpdateChar(playerTwo, characterList.findCharacter(AICharID)), 3000);
							timer.schedule(new Countdown(CPUHP, "HP: " + playerTwo.getHealth()), 3001);
							timer.schedule(new Countdown(CPUCharName, playerTwo.getCharacter().getName()), 3001);
						}
						
						timer.schedule(new ButtonCountdown(playerAttackButton), 3000);
						timer.schedule(new ButtonCountdown(playerGrabButton), 3000);
						timer.schedule(new ButtonCountdown(playerCounterButton), 3000);

					}
					
					else {
						gameStuff.getChildren().add(backToMenuButton); 
						if (playerOne.getHealth() <= 0) {
							timer.schedule(new DisplayCountdown(display, "FAILURE!"), 3000);
						}
					}
				}
				
			}
			
		});
		
		playerBox.getChildren().addAll(playerChar, playerHealth, playerCharIcon, playerAttackButton, playerGrabButton, playerCounterButton);
		
		
		
		singlePlayerGameRoot.getChildren().addAll(playerBox, gameStuff, CPUBox);
		singlePlayerGameScene = new Scene(singlePlayerGameRoot, 800, 700);
		singlePlayerGameScene.getStylesheets().add(styleString1);
	}
	
	class UpdateChar extends TimerTask{
		Player player;
		Fighter fighter;
		
		UpdateChar(Player player, Fighter fighter){
			this.player = player;
			this.fighter = fighter;
		}

		@Override
		public void run() { 
			player.setCharacter(fighter);
			
			player.setHealth();
		}
		
		
	}
	
	public String compareOptions(Player playerOne, Player playerTwo) {
		String p1Char = playerOne.getCharacter().getName();
		int p1Attack = playerOne.getCharacter().getAttack();
		int p1Grab = playerOne.getCharacter().getGrab();
		String p2Char = playerTwo.getCharacter().getName();
		int p2Attack = playerTwo.getCharacter().getAttack();
		int p2Grab = playerTwo.getCharacter().getGrab();
		
		String result = "";
		if (playerOne.isAttacking) {
			if (playerTwo.isAttacking) {
				result += p1Char + " and " + p2Char + " traded attaks! " + p1Char + " took " + p2Attack + " damage and " + p2Char + " took " + p1Attack + " damage!";
				playerOne.loseHealth(p2Attack);
				playerTwo.loseHealth(p1Attack);
			}
			
			else if (playerTwo.isGrabbing) {
				result += p1Char + "'s attack beat out " + p2Char + "'s grab! " + p2Char + " took " + p1Attack + " damage!";
				playerTwo.loseHealth(p1Attack);
			}
			
			else if (playerTwo.isCountering) { 
				result += p2Char + " countered " + p1Char + "'s attack! " + p1Char + " took " + p2Attack + " damage!";
				playerOne.loseHealth(p2Attack);
			}
		}
		
		else if (playerOne.isGrabbing) {
			if (playerTwo.isAttacking) {
				result += p2Char + "'s attack beat out " + p1Char + "'s grab! " + p1Char + " took " + p2Attack + " damage!";
				playerOne.loseHealth(p2Attack);
			}
			
			else if (playerTwo.isGrabbing) {
				result += p1Char + " grabbed and " + p2Char + " grabbed. " + randomizeNothingResult();
			}
			
			else if (playerTwo.isCountering) {
				result += p1Char + " grabbed " + p2Char + "! " + p2Char + " took " + p1Grab + " damage!";
				playerTwo.loseHealth(p1Grab);
			}
		}
		
		else if (playerOne.isCountering) {
			
			if (playerTwo.isAttacking) {
				result += p1Char + " countered " + p2Char + "'s attack! " + p2Char + " took " + p1Attack + " damage";
				playerTwo.loseHealth(p1Attack);
			}
			
			else if (playerTwo.isGrabbing) {
				result += p2Char + " grabbed " + p1Char + "! " + p1Char + " took " + p2Grab + " damage!";
				playerOne.loseHealth(p2Grab);
			}
			
			else {
				result += p1Char + " and " + p2Char + " both countered. " + randomizeNothingResult();
			}
			
			
		}
		
		return result;
	}
	
	public String randomizeNothingResult() {
		String returnString = "";
		int random = (int) ((Math.random() * 100) + 1);
		
		if (random <= 10) {
			returnString = "But nothing happened!";
		}
		
		else {
			if (random <= 20) {
				returnString = "It wasn't very effective...";
			}
			
			else {
				if (random <= 30) {
					returnString = "Nice work, guys.";
				}
				
				else {
					if (random <= 40) {
						returnString = "So you're not even gonna try, are you?";
					}
					
					else {
						if (random <= 50) {
							returnString = "You guys suck.";
						}
						
						else {
							if (random <= 60) {
								returnString = "Why are you like this?";
							}
							
							else {
								if (random <= 70 ) {
									returnString = "The only loser here is me for bearing witness to this nonsense.";
								}
								
								else {
									if (random <= 80) {
										returnString = "I award you no points, and may God have mercy on your soul.";
									}
									
									else {
										if (random <= 90) {
											returnString = "Ok boomers, lets move this along.";
										}
										
										else {
											if (random <= 100) {
												returnString = "Whatever.";
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return returnString;
	}
	
	public int CPUChoice(Player CPU) {
		if (playerTwo.isAttacking) {
			return 1;
		}
		else if (playerTwo.isGrabbing) {
			return 2;
		}
		else if(playerTwo.isCountering) {
			return 3;
		}
		else {
			return 3;
		}
	}

}
