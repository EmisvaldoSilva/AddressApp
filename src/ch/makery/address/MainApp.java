package ch.makery.address;

import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ch.makery.address.model.Person;
import ch.makery.address.view.PersonEditDialogController;
import ch.makery.address.view.PersonOverviewController;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    
    /**
     * Os dados como uma lista observável de Pessoas.
     */
    private ObservableList<Person> personData = FXCollections.observableArrayList();

    /**
     * Constructor
     */
    public MainApp() {
        // Adicionar alguns dados da amostra
        personData.add(new Person("Emisvaldo", "Silva"));
        personData.add(new Person("Celia", "Silva"));
        personData.add(new Person("Mariana", "Silva"));
        personData.add(new Person("Ana", "Silva"));
        personData.add(new Person("Vanessa", "Silva"));
        personData.add(new Person("Samuel", "Silva"));
        personData.add(new Person("Francisca", "Silva"));
        personData.add(new Person("Natalia", "Silva"));
        personData.add(new Person("Antonio", "Rodriguez"));
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");

        initRootLayout();

        showPersonOverview();
    }
    
    /**
     * Inicializa o layout raiz.
     */
    public void initRootLayout() {
        try {
            // Layout de raiz Carregar do arquivo fxml.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            
            // Mostrar a cena que contém o layout raiz.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mostra o panorama pessoa dentro do esquema de raiz.
     */
    public void showPersonOverview() {
        try {
            // Pessoa visão geral Load.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));
            AnchorPane personOverview = (AnchorPane) loader.load();
            
            // Definir panorama pessoa para o centro da disposição raiz.
            rootLayout.setCenter(personOverview);
            
            // Dê o acesso controlador para o app principal.
            PersonOverviewController controller = loader.getController();
            controller.setMainApp(this);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
	/**
	 * Abre uma janela para editar os detalhes para a pessoa especificada . Se o utilizador
	 * clicar em OK, as alterações são salvas na pessoa objeto fornecido e verdadeiro
	 * é devolvido.
	 * 
	 * @param person o objecto pessoa a ser editada
	 * @return verdadeiro se o usuário clicar em OK , falso caso contrário .
	 */
	public boolean showPersonEditDialog(Person person) {
		try {
			// Carregar o arquivo fxml e criar um novo palco para o diálogo pop-up 
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/PersonEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Criar o Palco diálogo.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Person");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Defina a pessoa no controlador.
			PersonEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setPerson(person);

			// Exibir o diálogo e esperar até que o usuário fecha
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
    
    /**
     * Retorna o palco principal.
     * @return
     */
    public Stage getPrimaryStage() {
    	return primaryStage;
    }
    
    /**
     * Retorna os dados como uma lista observável de Pessoas. 
     * @return
     */
    public ObservableList<Person> getPersonData() {
    	return personData;
    }

    public static void main(String[] args) {
        launch(args);
    }
}