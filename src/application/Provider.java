package application;


import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Provider {
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty degree;
    private final SimpleStringProperty groupName;
    private final SimpleStringProperty address;
    private final SimpleStringProperty city;
    private final SimpleStringProperty zip;
    private final SimpleStringProperty phoneNumber;
    
    private final SimpleStringProperty eveningHours;
    private final SimpleStringProperty weekendHours;
    private final SimpleStringProperty publicTransportation;
    private final SimpleStringProperty lgbtq;
    private final SimpleStringProperty alternativeLanguage; 
    private final SimpleStringProperty insurance;
    private final SimpleStringProperty providerType;
    private final SimpleStringProperty therapyType;
    private final SimpleStringProperty travelRadius;
    private final SimpleStringProperty patientAge;
    private final SimpleStringProperty providerGrade;
    
    private final SimpleStringProperty diagnosisCategory;
    
    private final SimpleStringProperty service;
    private final SimpleStringProperty mobileTherapy;
    private final SimpleStringProperty faithBased;
    
    private final double xCoord;
    private final double yCoord;


    Provider(String fn, String ln, String dg, String gn, String ad1, String ad2, String ct, String zp, String pn, String eh, String wh, String pt, String lg, String al, String in, String prt, String tt, String tr, String pa, String pg, String dc, String sv, String mt, String fb) {
        this.firstName = new SimpleStringProperty(fn);
        this.lastName = new SimpleStringProperty(ln);
        this.degree = new SimpleStringProperty(dg);
        this.groupName = new SimpleStringProperty(gn);
        this.address = new SimpleStringProperty(ad1+" "+ad2);
        this.city = new SimpleStringProperty(ct);
        this.zip = new SimpleStringProperty(zp);
        this.phoneNumber = new SimpleStringProperty(pn);
        this.eveningHours = new SimpleStringProperty(eh);
        this.weekendHours = new SimpleStringProperty(wh);
        this.publicTransportation = new SimpleStringProperty(pt);
        this.lgbtq = new SimpleStringProperty(lg);
        this.alternativeLanguage = new SimpleStringProperty(al);
        this.insurance = new SimpleStringProperty(in);
        this.providerType = new SimpleStringProperty(prt);
        this.therapyType = new SimpleStringProperty(tt);
        this.travelRadius = new SimpleStringProperty(tr);
        this.patientAge = new SimpleStringProperty(pa);
        this.providerGrade = new SimpleStringProperty(pg);
        this.diagnosisCategory = new SimpleStringProperty(dc);
        this.service = new SimpleStringProperty(sv);
        this.mobileTherapy = new SimpleStringProperty(mt);
        this.faithBased = new SimpleStringProperty(fb);
        
        Double [] d = {0.0, 0.0};
        if (!getZip().equals("") && !(getZip() == null)) {
            try {
                d = SampleController.coordinates.get(Integer.parseInt(getZip())); 
            } catch (Exception e){
                Alert errorAlert = new Alert(AlertType.ERROR);
                errorAlert.setHeaderText("Invalid ZipCode");
                errorAlert.setContentText("ZipCode" + zip + "is invalid");
                errorAlert.showAndWait();
            }
           
        }
        xCoord = d[0];
        yCoord = d[1];
    }   


    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getDegree() {
        return degree.get();
    }

    public String getGroupName() {
        return groupName.get();
    }

    public String getAddress() {
        return address.get();
    }

    public String getCity() {
        return city.get();
    }

    public String getZip() {
        return zip.get();
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public String getEveningHours() {
        return eveningHours.get();
    }

    public String getWeekendHours() {
        return weekendHours.get();
    }

    public String getPublicTransportation() {
        return publicTransportation.get();
    }

    public String getLgbtq() {
        return lgbtq.get();
    }

    public String getAlternativeLanguage() {
        return alternativeLanguage.get();
    }

    public String getInsurance() {
        return insurance.get();
    }

    public String getProviderType() {
        return providerType.get();
    }

    public String getTherapyType() {
        return therapyType.get();
    }

    public String getTravelRadius() {
        return travelRadius.get();
    }

    public String getPatientAge() {
        return patientAge.get();
    }

    public String getProviderGrade() {
        return providerGrade.get();
    }

    public String getDiagnosisCategory() {
        return diagnosisCategory.get();
    }
    
    public double getXCoord() {
        return xCoord;
    }
    
    public double getYCoord() {
        return yCoord;
    }
    
    public String getService() {
        return service.get();
    }
    
    public String getMobileTherapy() {
        return mobileTherapy.get();
    }

    public String getFaithBased() {
        return faithBased.get();
    }


}
