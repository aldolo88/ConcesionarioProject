package com.example.demo.model.repositories;

import com.example.demo.model.Provider;
import com.example.demo.model.Vehicle;
import com.example.demo.model.VehicleId;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, VehicleId> {
    /*List<Vehicle> findByIdProvider(Provider provider);
    List<Vehicle> findByIdModel(String model);
    List<Vehicle> findByIdColour(String colour);
    List<Vehicle> findByIdHorsePower(int horsePower);
    List<Vehicle> findByIdType(int type);*/
    List<Vehicle> findByIdProviderAndIdModelAndIdColourAndIdHorsePowerAndIdType(Provider provider,String model,String colour,int horsePower,int type);
}
