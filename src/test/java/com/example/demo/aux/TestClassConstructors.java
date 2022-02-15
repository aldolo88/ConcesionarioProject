package com.example.demo.aux;

import com.example.demo.model.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TestClassConstructors {
    private Date date;
    private VehicleId.Type type;

    public TestClassConstructors() {
        this.date = new Date();
        this.type = VehicleId.Type.COCHE;
    }

    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");//Al cambiar a GMT, la zona horaria se representa con Z en lugar de +00:00, por lo que lo añado manual para que coincida con lo esperado
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.format(this.date);
    }

    public String getStringType() {
        return type.toString();
    }

    public Employee TestEmployee() {
        return new Employee("Employee1", "Rol1");
    }

    public Employee TestModEmployee() {
        return new Employee("Employee2", "Rol2");
    }

    public Client TestClient() {
        return new Client("12345678A","Client1", 999888777);
    }

    public Client TestModClient() {
        return new Client("87654321A","Client2", 777888999);
    }

    public Provider TestProvider(){
        return new Provider("Provider1");
    }

    public Provider TestModProvider(){
        return new Provider("Provider2");
    }

    public VehicleId TestVehicleId(){
        return new VehicleId(TestProvider(),"Model1","Color1",1, this.type);
    }

    public VehicleId TestModVehicleId(){
        return new VehicleId(TestModProvider(),"Model2","Color2",2, this.type);
    }

    public Vehicle TestVehicle(){
        return new Vehicle(TestVehicleId(),10000);
    }

    public Vehicle TestModVehicle(){
        return new Vehicle(TestModVehicleId(),20000);
    }

    public Product TestProduct(){
        return new Product(TestVehicle(),"123asd");
    }

    public Product TestModProduct(){
        return new Product(TestModVehicle(),"987dsa");
    }

    public Order TestOrder(){
        return new Order(TestProvider(),TestEmployee(),this.date);
    }

    public Order TestModOrder(){
        return new Order(TestModProvider(),TestModEmployee(),this.date);
    }

    public OrderDetail TestOrderDetail(){
        return new OrderDetail(TestOrder(),TestProduct(),10000.0);
    }

    public OrderDetail TestModOrderDetail(){
        return new OrderDetail(TestModOrder(),TestModProduct(),20000.0);
    }
    public Sale TestSale(){
        return new Sale(TestClient(),TestEmployee(),this.date);
    }

    public Sale TestModSale(){
        return new Sale(TestModClient(),TestModEmployee(),this.date);
    }

    public SaleDetail TestSaleDetail(){
        return new SaleDetail(TestSale(),TestProduct(),10000.0);
    }

    public SaleDetail TestModSaleDetail(){
        return new SaleDetail(TestModSale(),TestModProduct(),20000.0);
    }
}
