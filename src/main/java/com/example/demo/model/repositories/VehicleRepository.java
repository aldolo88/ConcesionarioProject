package com.example.demo.model.repositories;

import com.example.demo.model.Provider;
import com.example.demo.model.Vehicle;
import com.example.demo.model.VehicleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, VehicleId> {
    /*List<Vehicle> findByIdProvider(Provider provider);
    List<Vehicle> findByIdModel(String model);
    List<Vehicle> findByIdColour(String colour);
    List<Vehicle> findByIdHorsePower(int horsePower);
    List<Vehicle> findByIdType(int type);*/
//    List<Vehicle> findByIdProviderAndModelAndColourAndHorsePowerAndType(Provider provider, String model, String colour,
//                                                                        int horsePower, VehicleId.Type type);


    @Query(value = "select * from vehicles u where u.provider_id = :#{#provider_id} and u.model = :#{#model}" +
            " and u.colour = :#{#colour} and u.horse_power = :#{#horsePower}" +
            " and u.type = :#{#type}", nativeQuery = true)
    List<Vehicle> findByVehicleId(@Param("provider_id") Long providerId,
                                      @Param("model") String model,
                                      @Param("colour") String colour,
                                      @Param("horsePower") Integer horsePower,
                                      @Param("type") String type);

    @Transactional
    @Modifying
    @Query(value = "delete from vehicles u where u.provider_id = :#{#provider_id} and u.model = :#{#model}" +
            " and u.colour = :#{#colour} and u.horse_power = :#{#horsePower}" +
            " and u.type = :#{#type}", nativeQuery = true)
    void deleteByVehicleId(@Param("provider_id") Long providerId,
                                  @Param("model") String model,
                                  @Param("colour") String colour,
                                  @Param("horsePower") Integer horsePower,
                                  @Param("type") String type);
}
