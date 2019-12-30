package application;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.controlsfx.control.CheckComboBox;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class SampleController implements Initializable {

    @FXML TableView<Provider> tableView;
    @FXML private TableColumn<Provider, String> firstNameColumn;
    @FXML private TableColumn<Provider, String> lastNameColumn;
    @FXML private TableColumn<Provider, String> degreeColumn;
    @FXML private TableColumn<Provider, String> insuranceColumn;
    @FXML private TableColumn<Provider, String> groupNameColumn;
    @FXML private TableColumn<Provider, String> addressColumn;
    @FXML private TableColumn<Provider, String> cityColumn;
    @FXML private TableColumn<Provider, String> zipColumn;
    @FXML private TableColumn<Provider, String> phoneNumberColumn;
    @FXML private CheckComboBox<String> providerType;
    @FXML private CheckComboBox<String> patientAge;
    @FXML private CheckComboBox<String> therapyType;
    @FXML private CheckComboBox<String> providerGrade;
    @FXML private ComboBox<String> travelRadius;
    @FXML private CheckComboBox<String> service;
    @FXML private CheckBox publicTransportation;
    @FXML private CheckBox lgbtq;
    @FXML private CheckBox eveningHours;
    @FXML private CheckBox weekendHours;
    @FXML private CheckBox mobileTherapy;
    @FXML private CheckBox faithBased;
    @FXML private TextField diagnosisCategory;
    @FXML private TextField zipCode;
    @FXML private TextField insurance;
    @FXML private TextField alternativeLanguage;
    @FXML private CheckComboBox<String> checkComboBox;
    @FXML private Button search;
    @FXML private Button refresh;
    //in order to give UI of a box because opacity of actual checkComboBox = 0
    @FXML private ComboBox<String> providerTypeUX;
    @FXML private ComboBox<String> patientAgeUX;
    @FXML private ComboBox<String> therapyTypeUX;
    @FXML private ComboBox<String> providerGradeUX;
    @FXML private ComboBox<String> travelRadiusUX;
    @FXML private ComboBox<String> serviceUX;
    
    private static ObservableList<Provider> providers = FXCollections.observableArrayList();
    private static ObservableList<Provider> filteredProviders = FXCollections.observableArrayList();
    private ObservableList<String> providerTypes = FXCollections.observableArrayList("Provider Type", "Therapist", "Psychiatrist");
    private ObservableList<String> patientAges = FXCollections.observableArrayList("Patient Age", "Child (0-12)", "Adolescent (13-18)", "Adult (19-65)", "Geriatric (65+)");
    private ObservableList<String> therapyTypes = FXCollections.observableArrayList("Therapy Type", "Trauma Focused", "EAP for PBHCS", "DBT", "CBT", "Individual Therapy", "Couples Therapy", "Group Therapy", "Family Therapy");
    private ObservableList<String> providerGrades = FXCollections.observableArrayList("Provider Grade", "Preferred", "Acceptable", "Last Resort");
    private ObservableList<String> travelRadii = FXCollections.observableArrayList("<1 mile", "<5 miles", "<10 miles", "<20 miles", "<50 miles");
    private ObservableList<String> services = FXCollections.observableArrayList("Service", "EAP", "PIC");

    
    
    public static final String file = "./Providers.xlsx";
    public static final String coordFile = "./Zip Codes.xlsx";
    private final double d2r = (Math.PI / 180.0);
    public static HashMap<Integer, Double[]> coordinates = new HashMap<Integer, Double[]>();

    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            populateCoordinates();
        } catch (InvalidFormatException | IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        
        //setup output view
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Provider, String>("lastName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Provider, String>("firstName"));
        degreeColumn.setCellValueFactory(new PropertyValueFactory<Provider, String>("degree"));
        groupNameColumn.setCellValueFactory(new PropertyValueFactory<Provider, String>("groupName"));
        insuranceColumn.setCellValueFactory(new PropertyValueFactory<Provider, String>("insurance"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Provider, String>("address"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<Provider, String>("city"));
        zipColumn.setCellValueFactory(new PropertyValueFactory<Provider, String>("zip"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<Provider, String>("phoneNumber"));

        try {
            providers.clear();
            filteredProviders.clear();
            providers = getProviders(providers);
            tableView.setItems(providers);
            getProviders(filteredProviders);
        } catch (InvalidFormatException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        


        //setup drop-down menu options
        providerType.getItems().addAll(providerTypes);

        patientAge.getItems().addAll(patientAges);

        therapyType.getItems().addAll(therapyTypes);

        providerGrade.getItems().addAll(providerGrades);

        travelRadius.getItems().addAll(travelRadii);
        
        service.getItems().addAll(services);
        


        //Implement copying from cells 
        MenuItem item = new MenuItem("Copy");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @SuppressWarnings("rawtypes")
            @Override
            public void handle(ActionEvent event) {
                ObservableList<TablePosition> posList = tableView.getSelectionModel().getSelectedCells();
                int old_r = -1;
                StringBuilder clipboardString = new StringBuilder();
                for (TablePosition<?, ?> p : posList) {
                    int r = p.getRow();
                    int c = p.getColumn();
                    Object cell = tableView.getColumns().get(c).getCellData(r);
                    if (cell == null)
                        cell = "";
                    if (old_r == r)
                        clipboardString.append('\t');
                    else if (old_r != -1)
                        clipboardString.append('\n');
                    clipboardString.append(cell);
                    old_r = r;
                }
                final ClipboardContent content = new ClipboardContent();
                content.putString(clipboardString.toString());
                Clipboard.getSystemClipboard().setContent(content);
            }
        });
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(item);
        tableView.setContextMenu(menu);
        tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        
        //initialize drop down menu items
        providerType.getCheckModel().check(0);
        therapyType.getCheckModel().check(0);
        providerGrade.getCheckModel().check(0);
        patientAge.getCheckModel().check(0);
        service.getCheckModel().check(0);
    }
    
    public ObservableList<Provider> getProviders(ObservableList<Provider> mproviders) throws InvalidFormatException, IOException {  

        Workbook workbook;

        workbook = WorkbookFactory.create(new File(file));

        Sheet sheet = workbook.getSheetAt(0);
        
        HashMap<String, Integer> map = new HashMap<String,Integer>(); //Create map
        Row roww = sheet.getRow(0); //Get first row
        short minColIx = roww.getFirstCellNum(); //get the first column index for a row
        short maxColIx = roww.getLastCellNum(); //get the last column index for a row
        for(short colIx=minColIx; colIx<maxColIx; colIx++) { //loop from first to last index
           Cell cell = roww.getCell(colIx); //get the cell
           map.put(cell.getStringCellValue().trim(),cell.getColumnIndex()); //add the cell contents (name of column) and cell index to the map
         }

        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        String lastName;
        String firstName;
        String degree;
        String groupName;
        String address1;
        String address2;
        String city;
        String zipCode;
        String phoneNumber;
        String eveningHours;
        String weekendHours;
        String publicTransportation;
        String lgbtq;
        String alternativeLanguage; 
        String insurance;
        String providerType;
        String therapyType;
        String travelRadius;
        String patientAge;
        String providerGrade;
        String diagnosisCategory;
        String service;
        String mobileTherapy;
        String faithBased;

        Iterator<Row> rowIterator = sheet.rowIterator();
        //skip the column name row
        rowIterator.next();
        //skip the text row
        rowIterator.next();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            travelRadius = "0";

            lastName = dataFormatter.formatCellValue(row.getCell(map.get("Last Name")));
            firstName = dataFormatter.formatCellValue(row.getCell(map.get("First Name")));
            degree = dataFormatter.formatCellValue(row.getCell(map.get("Title")));
            providerType = dataFormatter.formatCellValue(row.getCell(map.get("Type")));
            providerGrade = dataFormatter.formatCellValue(row.getCell(map.get("Grade")));
            groupName = dataFormatter.formatCellValue(row.getCell(map.get("Group or Practice Name")));        
            address1 = dataFormatter.formatCellValue(row.getCell(map.get("Address Line 1")));
            address2 = dataFormatter.formatCellValue(row.getCell(map.get("Address Line 2")));
            city = dataFormatter.formatCellValue(row.getCell(map.get("City")));
            zipCode = dataFormatter.formatCellValue(row.getCell(map.get("Zip")));
            phoneNumber = dataFormatter.formatCellValue(row.getCell(map.get("Telephone")));
            eveningHours = dataFormatter.formatCellValue(row.getCell(map.get("Evening Hours")));
            weekendHours = dataFormatter.formatCellValue(row.getCell(map.get("Wkend Hours")));
            publicTransportation = dataFormatter.formatCellValue(row.getCell(map.get("Public Transportation Accessible")));
            alternativeLanguage = dataFormatter.formatCellValue(row.getCell(map.get("Languages Spoken in addition to English")));
            lgbtq = dataFormatter.formatCellValue(row.getCell(map.get("LGBTQ"))) + dataFormatter.formatCellValue(row.getCell(map.get("Transgender")));
            service = dataFormatter.formatCellValue(row.getCell(map.get("Service")));
            mobileTherapy = dataFormatter.formatCellValue(row.getCell(map.get("Mobile Therapy")));
            faithBased = dataFormatter.formatCellValue(row.getCell(map.get("Faith Based/ Faith Focused")));

            patientAge = "";
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("Child          (0-12)"))))) {
                patientAge += " Child (0-12)";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("Adolescent            (13-18)"))))) {
                patientAge += " Adolescent (13-18)";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("Adult \n" + 
                    "(19-65)"))))) {
                patientAge += " Adult (19-65)";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("Geriatric         (65+)"))))) {
                patientAge += " Geriatric (65+)";
            }

            therapyType = "";
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("Traum Focused"))))) {
                therapyType += " Trauma Focused";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("EAP for PBHCS"))))) {
                therapyType += " EAP for PBHCS";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("DBT"))))) {
                therapyType += " DBT";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("CBT"))))) {
                therapyType += " CBT";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("Individual Therapy"))))) {
                therapyType += " Individual Therapy";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("Couples Therapy"))))) {
                therapyType += " Couples Therapy";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("Group Therapy"))))) {
                therapyType += " Group Therapy";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("Family Therapy"))))) {
                therapyType += " Family Therapy";
            }

            diagnosisCategory ="";
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("Substance Abuse"))))) {
                diagnosisCategory += "Substance Abuse, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("Eating Disorders"))))) {
                diagnosisCategory += "Eating Disorders, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("Psychological Testing"))))) {
                diagnosisCategory += "Psychological Testing, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("Autism Spectrum Disorders"))))) {
                diagnosisCategory += "Autism Spectrum Disorders, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("SMI Psychosis"))))) {
                diagnosisCategory += "SMI Psychosis, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("PTSD"))))) {
                diagnosisCategory += "PTSD, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("Sexual disorders"))))) {
                diagnosisCategory += "Sexual Disorders, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("Bereavement"))))) {
                diagnosisCategory += "Bereavement, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("Women Health"))))) {
                diagnosisCategory += "Women Health, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("Mood Disorders"))))) {
                diagnosisCategory += "Mood Disorders, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("Relationship Issues"))))) {
                diagnosisCategory += "Relationship Issues, ";
            }
            if (dataFormatter.formatCellValue(row.getCell(map.get("Other Specialties"))) != null && dataFormatter.formatCellValue(row.getCell(map.get("Other Specialties"))).equals("N/A")) {
                diagnosisCategory += dataFormatter.formatCellValue(row.getCell(map.get("Other Specialties")));
            }

            insurance = "";
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("AETNA"))))) {
                insurance += "AETNA, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("CIGNA"))))) {
                insurance += "CIGNA, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("KEYSTONE HEALTH PLAN EAST"))))) {
                insurance += "KEYSTONE HEALTH PLAN EAST, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("BC/BS PERSONAL CHOICE"))))) {
                insurance += "BC/BS PERSONAL CHOICE, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("UNITED BH"))))) {
                insurance += "UNITED BH, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("MEDICARE"))))) {
                insurance += "MEDICARE, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("TRI-STATE HEALTH"))))) {
                insurance += "TRI-STATE HEALTH, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("CARPENTER'S HEALTH"))))) {
                insurance += "CARPENTER'S HEALTH, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("AMERICAN BEHAVIORAL"))))) {
                insurance += "AMERICAN BEHAVIORAL, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("PREFERENTIAL CARE NETWORK"))))) {
                insurance += "PREFERENTIAL CARE NETWORK, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("TOTAL CARE NETWORK"))))) {
                insurance += "TOTAL CARE NETWORK, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("MAGELLAN"))))) {
                insurance += "MAGELLAN, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("HUMANA"))))) {
                insurance += "HUMANA, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("CAREBRIDGE"))))) {
                insurance += "CAREBRIDGE, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("ULLICARE"))))) {
                insurance += "ULLICARE, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("UBH/   AMERICHOICE"))))) {
                insurance += "UBH/AMERICHOICE, ";
            }
            if ("Y".equals(dataFormatter.formatCellValue(row.getCell(map.get("HIGHMARK"))))) {
                insurance += "HIGHMARK, ";
            }
            if (dataFormatter.formatCellValue(row.getCell(map.get("Other Insurances Accepted"))) != null && dataFormatter.formatCellValue(row.getCell(map.get("Other Insurances Accepted"))).equals("N/A")) {
                diagnosisCategory += dataFormatter.formatCellValue(row.getCell(map.get("Other Insurances Accepted")));
            }
            insurance = insurance.toLowerCase();
            if (insurance.length() > 2) {
                insurance = insurance.substring(0, insurance.length() - 2);
            }
            
            mproviders.add(new Provider(lastName, firstName, degree, groupName, address1, address2, city, zipCode, phoneNumber, eveningHours
                    ,weekendHours
                    ,publicTransportation
                    ,lgbtq
                    ,alternativeLanguage
                    ,  insurance
                    , providerType
                    , therapyType
                    , travelRadius
                    , patientAge
                    , providerGrade
                    , diagnosisCategory
                    , service
                    , mobileTherapy
                    , faithBased));
        }
        return mproviders;
    }

    public ObservableList<Provider> filterProviders (ObservableList<Provider> mproviders) {
        
        Iterator<Provider> providerIterator = mproviders.iterator();
        Provider mprovider;
        boolean matchFlag1 = true;
        boolean matchFlag2 = true;
        boolean matchFlag3 = true;
        boolean matchFlag4 = true;
        boolean matchFlag5 = true;
        while (providerIterator.hasNext()) {
            mprovider = providerIterator.next();
            
      //comboboxes (dropdown)
            ObservableList<String> provider = providerType.getCheckModel().getCheckedItems();
            ObservableList<String> age = patientAge.getCheckModel().getCheckedItems();
            ObservableList<String> therapy = therapyType.getCheckModel().getCheckedItems();
            ObservableList<String> grade = providerGrade.getCheckModel().getCheckedItems();
            String travel = (String) travelRadius.getValue();
            ObservableList<String> serve = service.getCheckModel().getCheckedItems();
            
      //buttons
            Boolean pub = publicTransportation.isSelected();
            Boolean lg = lgbtq.isSelected();
            Boolean evening = eveningHours.isSelected();
            Boolean weekend = weekendHours.isSelected();
            Boolean mobile = mobileTherapy.isSelected();
            Boolean faith = faithBased.isSelected();
            
      //text fields
            String diagnosis = (String) diagnosisCategory.getText();
            String insure = (String) insurance.getText();
            String alternative = alternativeLanguage.getText();
            String szip = (String) zipCode.getText();
            Integer zip;
            if (szip.equals("Zip Code") || szip.equals("")){
                zip = 0;
            }
            else {
                zip = Integer.valueOf(zipCode.getText());
            }
            double distance;
            
            
 
     ////filter dropdown            
           if (!(provider.get(0) == "Provider Type" && provider.size() == 1)) {
               matchFlag1 = false;
               //filter based on provider
               for (int a = 1; a < provider.size(); a++) {
                   if (mprovider.getProviderType().contains(provider.get(a))) {
                       matchFlag1 = true;
                   }
               }
           }
           if(!(age.get(0) == "Patient Age" && age.size() == 1)) {
               matchFlag2 = false;               
               //filter based on age
               for (int a = 1; a < age.size(); a++) {
                   if (mprovider.getPatientAge().contains(age.get(a))) {
                       matchFlag2 = true;
                   }
               }  
           }
           if(!(therapy.get(0) == "Therapy Type" && therapy.size() == 1)) {
               matchFlag3 = false;
               //filter based on therapy type
               for (int a = 1; a < therapy.size(); a++) {
                   if (mprovider.getTherapyType().contains(therapy.get(a))) {
                       matchFlag3 = true;
                   }
               }
           }
           if (!(grade.get(0) == "Provider Grade" && grade.size() == 1)) {
               matchFlag4 = false;
               //filter based on provider grade
               for (int a = 1; a < grade.size(); a++) {
                   if (mprovider.getProviderGrade().contains(grade.get(a))) {
                       matchFlag4 = true;
                   }
               }
           }
           if (!(serve.get(0) == "Service" && serve.size() == 1)){
               matchFlag5 = false;
               //filter based on service
               for (int a = 0; a < serve.size(); a++) {
                   if (mprovider.getService().toLowerCase().contains(serve.get(a).toLowerCase())) {
                       matchFlag5 = true;
                   }
               } 
           }


            if (matchFlag5 == false || matchFlag1 == false ||matchFlag2 == false || matchFlag3 == false || matchFlag4 == false ) {
                providerIterator.remove();
            }
////filter checkboxes and textboxes
            else if(pub && !(mprovider.getPublicTransportation().contains("Y"))) {
                providerIterator.remove();
            }
            else if(lg && !(mprovider.getLgbtq().contains("Y"))) {
                providerIterator.remove();
            }
            else if(evening && !(mprovider.getEveningHours().contains("Y"))) {
                providerIterator.remove();
            }
            else if(weekend && !(mprovider.getWeekendHours().contains("Y"))) {
                providerIterator.remove();
            }
            else if(mobile && !(mprovider.getMobileTherapy().contains("Y"))) {
                providerIterator.remove();
            }
            else if(faith && (mprovider.getFaithBased() == "")) {
                providerIterator.remove();
            }
            else if(!(insure.equals("")) && !(mprovider.getInsurance().toLowerCase().contains(insure.toLowerCase()))) {
                providerIterator.remove();
            }
            else if(!(alternative.equals("")) && !(mprovider.getAlternativeLanguage().toLowerCase().contains(alternative.toLowerCase()))) {
                providerIterator.remove();
            }
            else if(!(diagnosis.equals("")) && !(mprovider.getDiagnosisCategory().toLowerCase().contains(diagnosis.toLowerCase()))) {
                providerIterator.remove();
            }
            else if(!(szip.equals("Zip Code") && !(szip.equals("")) && !(szip == null)) && !("Travel Radius".equals(travel)) && !(zip == 0)) {
                Double d [] = coordinates.get(zip);
                distance = haversine_mi(d[0].doubleValue(),d[1].doubleValue(), mprovider.getXCoord(), mprovider.getYCoord());
                if((travel.equals("<1 mile") && !(distance < 1.0))||
                   (travel.equals("<5 miles") && !(distance < 5.0))||
                   (travel.equals("<10 miles") && !(distance < 10.0))||
                   (travel.equals("<20 miles") && !(distance < 20.0))||
                   (travel.equals("<50 miles") && !(distance < 50.0))) {
                    providerIterator.remove();
                }
            }
           




        }
        return mproviders;
        
    }
    
    public void populateCoordinates() throws InvalidFormatException, IOException {
        Workbook workbook;
        workbook = WorkbookFactory.create(new File(coordFile));
        Sheet sheet = workbook.getSheetAt(0);
        
        DataFormatter dataFormatter = new DataFormatter();
    
        Iterator<Row> rowIterator = sheet.rowIterator();
        rowIterator.next();
        
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Double[]arr = new Double[2];
            arr[0] = Double.valueOf(dataFormatter.formatCellValue(row.getCell(3)));
            arr[1] = Double.valueOf(dataFormatter.formatCellValue(row.getCell(4)));
            coordinates.put(Integer.valueOf(dataFormatter.formatCellValue(row.getCell(0))), arr); 
        }
  
    }
    
    public double haversine_mi(double lat1, double long1, double lat2, double long2) {
        double dlong = (long2 - long1) * d2r;
        double dlat = (lat2 - lat1) * d2r;
        double a = Math.pow(Math.sin(dlat/2.0), 2) + Math.cos(lat1*d2r) * Math.cos(lat2*d2r) * Math.pow(Math.sin(dlong/2.0), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = 3956 * c; 

        return d;
    }
    
    @FXML
    private void handleSearchButton(ActionEvent event) throws InvalidFormatException, IOException {
        filteredProviders.clear();
        getProviders(filteredProviders);
        filterProviders(filteredProviders);
        tableView.setItems(filteredProviders);
    }
    
    @FXML
    private void handleRefreshButton(ActionEvent event) throws InvalidFormatException, IOException {
        filteredProviders.clear();
        tableView.setItems(providers);
        eveningHours.setSelected(false);
        weekendHours.setSelected(false);
        publicTransportation.setSelected(false);
        lgbtq.setSelected(false);
        faithBased.setSelected(false);
        mobileTherapy.setSelected(false);
        
        diagnosisCategory.setText("");
        zipCode.setText("");
        alternativeLanguage.setText("");
        insurance.setText("");
        
        travelRadius.setValue("Travel Radius");
        
        
        providerType.getCheckModel().clearChecks();
        providerType.getCheckModel().check(0);
        patientAge.getCheckModel().clearChecks();
        patientAge.getCheckModel().check(0);
        therapyType.getCheckModel().clearChecks();
        therapyType.getCheckModel().check(0);
        providerGrade.getCheckModel().clearChecks();
        providerGrade.getCheckModel().check(0);
        service.getCheckModel().clearChecks();
        service.getCheckModel().check(0);

        
    }
    






}
