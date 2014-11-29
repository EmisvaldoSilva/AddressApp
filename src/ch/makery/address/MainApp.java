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
import ch.makery.address.model.PersonListWrapper;
import ch.makery.address.view.BirthdayStatisticsController;
import ch.makery.address.view.PersonEditDialogController;
import ch.makery.address.view.PersonOverviewController;
import ch.makery.address.view.RootLayoutController;
import java.io.File;
import java.util.prefs.Preferences;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.controlsfx.dialog.Dialogs;

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
        
        //Inserir imagem do programa
        this.primaryStage.getIcons().add(new javafx.scene.image.Image("file:resources/images/address_book_32.png"));
        
        initRootLayout();

        showPersonOverview();
    }
    
    /**
     * Inicializa o layout raiz.
     */
    /**
    * Inicializa o root layout e tenta carregar o último arquivo
    * de pessoa aberto.
    */
    public void initRootLayout() {
       try {
           // Carrega o root layout do arquivo fxml.
           FXMLLoader loader = new FXMLLoader();
           loader.setLocation(MainApp.class
                   .getResource("view/RootLayout.fxml"));
           rootLayout = (BorderPane) loader.load();

           // Mostra a scene (cena) contendo o root layout.
           Scene scene = new Scene(rootLayout);
           primaryStage.setScene(scene);

           // Dá ao controller o acesso ao main app.
           RootLayoutController controller = loader.getController();
           controller.setMainApp(this);

           primaryStage.show();
       } catch (IOException e) {
           e.printStackTrace();
       }

       // Tenta carregar o último arquivo de pessoa aberto.
       File file = getPersonFilePath();
       if (file != null) {
           loadPersonDataFromFile(file);
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
    
    /**
    * Retorna o arquivo de preferências da pessoa, o último arquivo que foi aberto.
    * As preferências são lidas do registro específico do SO (Sistema Operacional). 
    * Se tais prefêrencias não puderem  ser encontradas, ele retorna null.
    * 
    * @return
    */
    public File getPersonFilePath(){
        Preferences prefes = Preferences.userNodeForPackage(MainApp.class);
        String filepath = prefes.get("filepath", null);
        if (filepath != null) {
            return new File(filepath);
        }else{
            return null;
        }
    }
    
    /**
    * Define o caminho do arquivo do arquivo carregado atual. O caminho é persistido no
    * registro específico do SO (Sistema Operacional).
    * 
    * @param file O arquivo ou null para remover o caminho
    */
    public void setPersonFilePath(File file){
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            prefs.put("filepath", file.getPath());
            // Atualiza o título palco.
            primaryStage.setTitle("AddressApp - " + file.getName());
        }else{
            prefs.remove("filepath");
            // Atualiza o título palco.
            primaryStage.setTitle("AddressApp");
        }
    }
    
    /**
    * Carrega os dados da pessoa do arquivo especificado. A pessoa atual
    * será substituída.
    * 
    * @param file
    */
    public void loadPersonDataFromFile(File file) {
    try {
        JAXBContext context = JAXBContext
                .newInstance(PersonListWrapper.class);
        Unmarshaller um = context.createUnmarshaller();

        // Lendo o XML do arquivo e fazendo uma triagem .
        PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

        personData.clear();
        personData.addAll(wrapper.getPersons());

        // Salve o caminho do arquivo para o registro.
        setPersonFilePath(file);

    } catch (Exception e) { // captura qualquer exceção
        Dialogs.create()
                .title("Erro")
                .masthead("Não foi possível carregar dados do arquivo:\n" 
                          + file.getPath()).showException(e);
    }
    }

    /**
     * Salva os dados da pessoa atual no arquivo especificado.
     * 
     * @param file
     */
    public void savePersonDataToFile(File file) {
    try {
        JAXBContext context = JAXBContext
                .newInstance(PersonListWrapper.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // Envolvendo nossos dados da pessoa.
        PersonListWrapper wrapper = new PersonListWrapper();
        wrapper.setPersons(personData);

        // Enpacotando e salvando XML  no arquivo.
        m.marshal(wrapper, file);

        // Saalva o caminho do arquivo no registro.
        setPersonFilePath(file);
    } catch (Exception e) { // captura qualquer exceção
        Dialogs.create().title("Erro")
                .masthead("Não foi possível salvar os dados do arquivo:\n" 
                          + file.getPath()).showException(e);
    }
    }
    
    /**
    * Abre uma janela para mostrar as estatísticas de aniversário.
    */
    public void showBirthdayStatistics() {
    try {
        // Carrega o arquivo fxml e cria um novo palco para o popup.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("view/BirthdayStatistics.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Birthday Statistics");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Define a pessoa dentro do controller.
        BirthdayStatisticsController controller = loader.getController();
        controller.setPersonData(personData);

        dialogStage.show();

    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    public static void main(String[] args) {
        launch(args);
    }
}