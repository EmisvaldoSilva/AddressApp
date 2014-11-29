package ch.makery.address.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import org.controlsfx.dialog.Dialogs;

import ch.makery.address.MainApp;
import ch.makery.address.model.Person;
import ch.makery.address.util.DateUtil;

public class PersonOverviewController {
    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label birthdayLabel;

    // Referência para a aplicação principal .
    private MainApp mainApp;

    /**
     * The constructor.
     * O construtor é chamado antes que o método initialize ().
     */
    public PersonOverviewController() {
    }

    /**
     * Inicializa a classe controlador. Este método é chamado automaticamente
     * após o arquivo fxml foi carregado.
     */
    @FXML
    private void initialize() {
    	// Inicializar a tabela a pessoa com as duas colunas.
        firstNameColumn.setCellValueFactory(
        		cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(
        		cellData -> cellData.getValue().lastNameProperty());
        
        // Limpar detalhes pessoalmente.
        showPersonDetails(null);

        // Ouvir para alterações de seleção e mostrar os detalhes pessoa quando mudou .
		personTable.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    /**
     * É chamado pelo aplicativo principal para dar uma referência de volta para si mesmo.
     * 
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Adicionar dados da lista de observáveis ​​para a mesa
        personTable.setItems(mainApp.getPersonData());
    }
    
    /**
     * Preenche todos os campos de texto para mostrar detalhes sobre a pessoa .
     * Se a pessoa especificada for nula, todos os campos de texto são apagadas.
     * 
     * @param person a pessoa ou nulo
     */
    private void showPersonDetails(Person person) {
    	if (person != null) {
    		// Preencha os rótulos com informações do objeto pessoa .
    		firstNameLabel.setText(person.getFirstName());
    		lastNameLabel.setText(person.getLastName());
    		streetLabel.setText(person.getStreet());
    		postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
    		cityLabel.setText(person.getCity());
    		birthdayLabel.setText(DateUtil.format(person.getBirthday()));
    	} else {
    		// Pessoa é nulo , remova todo o texto .
    		firstNameLabel.setText("");
    		lastNameLabel.setText("");
    		streetLabel.setText("");
    		postalCodeLabel.setText("");
    		cityLabel.setText("");
    		birthdayLabel.setText("");
    	}
    }

	/**
	 * Chamado quando o usuário clica no botão excluir.
	 */
	@FXML
	private void handleDeletePerson() {
		int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			personTable.getItems().remove(selectedIndex);
		} else {
			// Nada selecionado.
			Dialogs.create()
		        .title("No Selection")
		        .masthead("No Person Selected")
		        .message("Please select a person in the table.")
		        .showWarning();
		}
	}
	
	/**
	 * Chamado quando o usuário clica no botão novo . Abre uma janela para editar
	 * detalhes de uma nova pessoa.
	 */
	@FXML
	private void handleNewPerson() {
		Person tempPerson = new Person();
		boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
		if (okClicked) {
			mainApp.getPersonData().add(tempPerson);
		}
	}

	/**
	 * Chamado quando o usuário clica no botão de edição. Abre uma janela para editar
	 * detalhes da pessoa selecionada .
	 */
	@FXML
	private void handleEditPerson() {
		Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
		if (selectedPerson != null) {
			boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
			if (okClicked) {
				showPersonDetails(selectedPerson);
			}

		} else {
			// nada selecionado.
			Dialogs.create()
				.title("No Selection")
				.masthead("No Person Selected")
				.message("Please select a person in the table.")
				.showWarning();
		}
	}
}