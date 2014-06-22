package controller;





import java.util.ArrayList;
import java.util.List;

import org.eclipse.egit.github.core.Repository;

import utils.NamedNode;
import application.Main;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.BoundingBox;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class IndexController {

	@FXML
	private Pane topBar;
	@FXML
	private Rectangle quit;
	@FXML
	private Rectangle minimize;
	@FXML
	private Rectangle maximize;
	@FXML
	private Rectangle innerMaximize;
	@FXML
	private Label quitLabel;
	@FXML
	private Label quitNotification;
	@FXML 
	private AnchorPane contentPane;
	@FXML
	private AnchorPane notificationPane;
	@FXML 
	private TextArea notificationLabel;
	@FXML
	private Label nameLabel;
	@FXML
	private ImageView userImage;
	@FXML
	private Polygon backButton;
	@FXML
	private Polygon nextButton;
	@FXML
	private ImageView resizeImage;
	
	//LEFT BAR
	@FXML
	private TextField otherRepositoryField;
	@FXML
	private Button otherOkButton;
	@FXML
	private VBox leftBox;

	@FXML
	private Label contentTitle;

	


	
	private List<NamedNode> memory;
	private int currentIdx=-1;
	
	private Main mainApp;
	private double dragDeltaX = 0;
	private double dragDeltaY = 0;
	private double pressedX = 0;
	private double pressedY = 0;
	private double draggedX = 0;
	private double draggedY = 0;
	BoundingBox savedBounds, savedFullScreenBounds;
    boolean maximized = false;
	
	public IndexController() {

	}
	
	@FXML
    private void initialize() {
		quitLabel.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {
                Platform.exit();
            }
        });
		
		userImage.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {
                IndexController.this.mainApp.loadHomeView();
            }
        });
		
		quitNotification.setOnMouseClicked(new EventHandler<MouseEvent>()
	    {
	        @Override
	        public void handle(MouseEvent t) {
	            notificationPane.setVisible(false);
	        }
	    });
		
		topBar.setOnMousePressed(new EventHandler<MouseEvent>() {
		  @Override public void handle(MouseEvent mouseEvent) {
		    Stage stage = mainApp.getPrimaryStage();
		    dragDeltaX = stage.getX() - mouseEvent.getScreenX();
		    dragDeltaY = stage.getY() - mouseEvent.getScreenY();
		  }
		});
		topBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
		  @Override public void handle(MouseEvent mouseEvent) {
		    Stage stage = mainApp.getPrimaryStage();
		    stage.setX(mouseEvent.getScreenX() + dragDeltaX);
		    stage.setY(mouseEvent.getScreenY() + dragDeltaY);
		  }
		});
		
		
		maximize.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {
                maximizeAction();
            }
        });
		
		innerMaximize.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {
                maximizeAction();
            }
        });
		
		minimize.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {
            	minimizeAction();
            }
        });
		
		backButton.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent t) {
            	previousPane();
            }
        });
		
		nextButton.setOnMouseClicked(new EventHandler<MouseEvent>()
	    {
	        @Override
	        public void handle(MouseEvent t) {
	        	nextPane();
	        }
	    });
		
		resizeImage.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e) {
                pressedX = e.getX();
                pressedY = e.getY();
            }
        });
		
		resizeImage.setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent e) {
                draggedX = e.getX();
                draggedY = e.getY();

                double differenceX = draggedX - pressedX;
                double differenceY = draggedY - pressedY;

                Stage primaryStage = mainApp.getPrimaryStage();
				primaryStage.setX(primaryStage.getX() + differenceX);
				primaryStage.setY(primaryStage.getY() + differenceY); 
            }
        });
		
		otherOkButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Repository repo = mainApp.getGitHubController().loadRepository(otherRepositoryField.getText());
				if(repo != null){
					EventHandler<ActionEvent> clicEvent = new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							// TODO Auto-generated method stub
							
						}
					};
					addBoxButtonWithImage(repo.getName(),new Image(repo.getOwner().getAvatarUrl(),50,50,true,true),leftBox,clicEvent);
				}
			}
		});
		
		notificationPane.setVisible(false);
		leftBox.setVisible(false);

		nameLabel.setText("Welcome");
		memory = new ArrayList<NamedNode>();



	}


    protected void previousPane() {
    	if(currentIdx >= 1){
        	contentPane.getChildren().clear();
        	currentIdx --;
    		contentPane.getChildren().add(memory.get(currentIdx).getNode());
    		setContentTitle(memory.get(currentIdx).getName());
    	}
    	if(currentIdx == 0){
    		leftBox.setVisible(false);
    	}
	}
    
    protected void nextPane() {
    	if(memory.size()>currentIdx+1){
        	contentPane.getChildren().clear();
        	currentIdx++;
    		contentPane.getChildren().add(memory.get(currentIdx).getNode());
    		setContentTitle(memory.get(currentIdx).getName());
    	}
    	if(currentIdx == 1){
    		leftBox.setVisible(true);
    	}
	}

	public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
    
    
    private void maximizeAction(){
	    Stage stage = mainApp.getPrimaryStage();
	    if (maximized) {
	    	stage.setX(savedBounds.getMinX());
            stage.setY(savedBounds.getMinY());
            stage.setWidth(savedBounds.getWidth());
            stage.setHeight(savedBounds.getHeight());
            savedBounds = null;
            maximized = false;
        } else {
            ObservableList<Screen> screensForRectangle = Screen.getScreensForRectangle(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());
            Screen screen = screensForRectangle.get(0);
            Rectangle2D visualBounds = screen.getVisualBounds();

            savedBounds = new BoundingBox(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight());

            stage.setX(visualBounds.getMinX());
            stage.setY(visualBounds.getMinY());
            stage.setWidth(visualBounds.getWidth());
            stage.setHeight(visualBounds.getHeight());
            maximized = true;
        }
    }
    
    public void minimizeAction() {

        if (!Platform.isFxApplicationThread()) // Ensure on correct thread else hangs X under Unbuntu
        {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    _minimize();
                }
            });
        } else {
            _minimize();
        }
    }

    private void _minimize() {
        Stage stage = mainApp.getPrimaryStage();
        stage.setIconified(true);
    }

	public void setContent(Node content,String nodeTitle) {
		
		ObservableList<Node> list = contentPane.getChildren();
		currentIdx++;
		memory.add(currentIdx,new NamedNode(content,nodeTitle));
		for(int i = memory.size()-1 ; i > currentIdx ; i--){
			memory.remove(i);
		}
		
		list.clear();
		list.add(content);
		content.autosize();
		
		content.setStyle("-fx-min-width:"+contentPane.getWidth()+";" +
							"-fx-min-height:"+contentPane.getHeight()+";" 
				);

	}

	public void writeNotification(String message) {
		notificationLabel.setText(message);
		notificationPane.setOpacity(0);
		notificationPane.setVisible(true);
		FadeTransition ft = new FadeTransition(Duration.millis(4000), notificationPane);
		ft.setFromValue(0.0);
		ft.setToValue(1.0);
		ft.setCycleCount(Timeline.INDEFINITE);
		ft.setCycleCount(2);
		ft.setAutoReverse(true);
		ft.play();
	}

	public void setName(String name) {
		nameLabel.setText(name);
	}

	public void setUserImage(Image image) {
		userImage.setImage(image);
	}
	
	
	
	private Button createLeftButton(String name,EventHandler<ActionEvent> event){
		Button butt = new Button();
		butt.setId("repositoryButton");
		butt.setPrefWidth(270);
		butt.setPrefHeight(50);
		butt.setText(name);
		
		butt.setOnAction(event);

		return butt;
	}
	
	private void addBoxButtonWithImage(String name,Image img,VBox dest,EventHandler<ActionEvent> event){
		HBox hb = new HBox();
		dest.getChildren().add(hb);
		hb.getChildren().add(new ImageView(img));
		hb.getChildren().add(createLeftButton(name,event));
	}

	public void setLeftBar(){
		leftBox.setVisible(true);
		leftBox.getChildren().clear();
		for (Repository repo : mainApp.getGitHubController().getRepositories()) {
			final String repoName = repo.getName();
			EventHandler<ActionEvent> clicEvent = new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					IndexController.this.mainApp.loadRepoWiew(repoName);
				}
			};
			addBoxButtonWithImage(repo.getName(),new Image(repo.getOwner().getAvatarUrl(),50,50,true,true),leftBox,clicEvent);
		}
	}


	public void setContentTitle(String contentTitle) {
		this.contentTitle.setText(contentTitle);
	}
}


