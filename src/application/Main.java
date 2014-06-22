package application;
	
import java.io.IOException;
















import model.GitHubModel;
import controller.HomeController;
import controller.IndexController;
import controller.LoginController;
import controller.RepositoryController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Main extends Application {
	
	private BorderPane root;
	private Stage primaryStage;
	private AnchorPane indexViewPage;
	private IndexController idxC;
	
	private GitHubModel gitHubModel = new GitHubModel(this);
		
	@Override
	public void start(Stage primaryStage) {
		
		try {
			this.primaryStage = primaryStage;
	        primaryStage.initStyle(StageStyle.UNDECORATED);
			root = new BorderPane();
			Scene scene = new Scene(root,1200,800);
			scene.getStylesheets().add(getClass().getResource("JMetroDarkTheme.css").toExternalForm());
			
		
			loadIndex();
            loadLogin();

	        
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void loadIndex() {
		try {
            // Load the fxml file and set into the center of the main layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/IndexView.fxml"));
            indexViewPage = (AnchorPane) loader.load();
            
            idxC = loader.getController();
            idxC.setMainApp(this);
           
            root.setCenter(indexViewPage);
            

        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }
	}
	
	private void loadLogin(){
		try {
            // Load the fxml file and set into the center of the main layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            AnchorPane loginViewPage = (AnchorPane) loader.load();
            
            LoginController loginC = loader.getController();
            loginC.setMainApp(this);
           
            String nodeTitle = "Login";
			idxC.setContent(loginViewPage,nodeTitle);
            idxC.setContentTitle(nodeTitle);

        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }
	}

	public static void main(String[] args) {
		launch(args);
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}
	

	public void setGitHubController(GitHubModel gitHubModel){
		this.gitHubModel = gitHubModel;
	}
	
	public GitHubModel getGitHubController(){
		return this.gitHubModel;
	}
	
	public void writeNotification(String message){
		idxC.writeNotification(message);
	}
	


	public void loadHomeView() {
		
		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HomeView.fxml"));
            AnchorPane homeView = (AnchorPane) loader.load();
            
            HomeController homeC = loader.getController();
            homeC.setMainApp(this);
           
            String nodeTitle = "Home";
			idxC.setContent(homeView,nodeTitle);
            idxC.setLeftBar();
            idxC.setContentTitle(nodeTitle);

        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public void setName(String name) {
		idxC.setName(name);
	}
	
	public void setUserImage(Image image){
		idxC.setUserImage(image);
	}

	public void loadWebPage(String htmlUrl) {
		WebView browser = new WebView();
		WebEngine webEngine = browser.getEngine();
		webEngine.load(htmlUrl);
		String nodeTitle = "Browser";
		idxC.setContent(browser,nodeTitle);
        idxC.setContentTitle(nodeTitle);

	}
	
	public void loadRepoWiew(String repoName){
		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RepositoryView.fxml"));
            AnchorPane repoView = (AnchorPane) loader.load();
            
            RepositoryController repoC = loader.getController();
            repoC.setMainApp(this);

            idxC.setContent(repoView,repoName);
            idxC.setContentTitle(repoName);


        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}