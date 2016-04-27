package softeng.model;

public class User {
    private int id;
    private String type;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String address;
    private String city;
    private String state;
    private int zipCode;
    private String date_of_birth;
    private String payment_info;

    private boolean isAdmin;
    private boolean isMember;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public void setAsAdmin(){
        if (type.equals("admin")) { isAdmin = true; } else { isAdmin = false; }
    }
    public void setAsMember(){
        if (type.equals("member")) { isMember = true; } else { isMember = false; }
    }
    public boolean isAdmin() {
        return isAdmin;
    }
    public boolean isMember() {
        return isMember;
    }
    /* The Handlebars {{#if}} expression tests for truth only. We can circumvent this
       limitation by either creating helpers or by creating these boolean functions */
//    public boolean isAdmin(){ return isAdmin; }

//    public boolean isMember(){ return isMember; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getPayment_info() {
        return payment_info;
    }

    public void setPayment_info(String payment_info) {
        this.payment_info = payment_info;
    }
}