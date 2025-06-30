
package com.example.carrental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.*;

@SpringBootApplication
public class CarRentalApiApp {
    public static void main(String[] args) {
        SpringApplication.run(CarRentalApiApp.class, args);
    }
}

@RestController
@RequestMapping("/api")
class CarRentalController {

    private CarRentalSystem rentalSystem = new CarRentalSystem();

    public CarRentalController() {
        rentalSystem.addCar(new Car("C001", "Toyota", "Camry", 60.0));
        rentalSystem.addCar(new Car("C002", "Honda", "Accord", 70.0));
        rentalSystem.addCar(new Car("C003", "Mahindra", "Thar", 150.0));
    }

    @GetMapping("/cars")
    public List<Car> getAllCars() {
        return rentalSystem.getCars();
    }

    @PostMapping("/rent")
    public ResponseEntity<String> rentCar(@RequestBody RentRequest request) {
        String result = rentalSystem.rentCarByRequest(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnCar(@RequestParam String carId) {
        String result = rentalSystem.returnCarById(carId);
        return ResponseEntity.ok(result);
    }
}

class RentRequest {
    public String carId;
    public String customerName;
    public int rentalDays;
}

class Car {
    private String carId;
    private String brand;
    private String model;
    private double basePricePerDay;
    private boolean isAvailable = true;

    public Car(String carId, String brand, String model, double basePricePerDay) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.basePricePerDay = basePricePerDay;
    }

    public String getCarId() { return carId; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public double getBasePricePerDay() { return basePricePerDay; }
    public boolean isAvailable() { return isAvailable; }

    public void rent() { isAvailable = false; }
    public void returnCar() { isAvailable = true; }
    public double calculatePrice(int rentalDays) {
        return basePricePerDay * rentalDays;
    }
}

class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
}

class Rental {
    private Car car;
    private Customer customer;
    private int days;

    public Rental(Car car, Customer customer, int days) {
        this.car = car;
        this.customer = customer;
        this.days = days;
    }

    public Car getCar() { return car; }
    public Customer getCustomer() { return customer; }
    public int getDays() { return days; }
}

class CarRentalSystem {
    private List<Car> cars = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Rental> rentals = new ArrayList<>();

    public List<Car> getCars() {
        return cars;
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public String rentCarByRequest(RentRequest request) {
        Car selectedCar = null;
        for (Car car : cars) {
            if (car.getCarId().equals(request.carId) && car.isAvailable()) {
                selectedCar = car;
                break;
            }
        }
        if (selectedCar == null) return "Car not available or invalid ID.";

        Customer newCustomer = new Customer("CUS" + (customers.size() + 1), request.customerName);
        addCustomer(newCustomer);
        selectedCar.rent();
        rentals.add(new Rental(selectedCar, newCustomer, request.rentalDays));

        double totalPrice = selectedCar.calculatePrice(request.rentalDays);
        return String.format("Car rented successfully to %s. Total Price: ₹%.2f", newCustomer.getName(), totalPrice);
    }

    public String returnCarById(String carId) {
        for (Car car : cars) {
            if (car.getCarId().equals(carId) && !car.isAvailable()) {
                car.returnCar();
                rentals.removeIf(rental -> rental.getCar().getCarId().equals(carId));
                return "Car returned successfully.";
            }
        }
        return "Car ID not found or car not rented.";
    }
}
